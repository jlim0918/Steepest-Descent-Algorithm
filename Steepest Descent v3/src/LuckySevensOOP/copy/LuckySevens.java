package LuckySevensOOP.copy;
/* -----------------
 
 The LuckySevens class
 
 ------------------- */
 
public class LuckySevens {
	
	// member fields (variables): everything we want to know about a game
	private int winValue;					// amount won on roll of 7
	private int loseValue;					// amount lost on any roll other than 7
	private int startingCash;				// starting cash value
	private int nRolls;						// number of rolls in the game until bankruptcy
	private int maxCash;					// peak winnings in the game
	private int[] rollFreq = new int[13];	// frequency of roll values
	private boolean doSuppressOutput;		// whether or not to suppress output
	
	
	// void constructor
	public LuckySevens() { 
		this.setBasicGameValues();
		this.reset();
		this.startingCash = 0;
	}
	
	
	// constructor to initialize starting cash value
	public LuckySevens(int startingCash) {
		this.setBasicGameValues();
		this.reset();
		this.startingCash = startingCash;
	}
	
	
	// basic getters
	public int getWinValue() { return this.winValue; }
	public int getLoseValue() { return this.loseValue; }
	public int getStartingCash() { return this.startingCash; }
	public int getNRolls() { return this.nRolls; }
	public int getMaxCash() { return this.maxCash; }
	public int[] getRollFreq() { return this.rollFreq; }
	public boolean outputSuppressed() { return this.doSuppressOutput; }
	
	
	// basic setters
	public void setWinValue(int n) { this.winValue = n; }
	public void setLoseValue(int n) { this.loseValue = n; }
	public void setStartingCash(int n) { this.startingCash = n; }
	public void setNRolls(int n) { this.nRolls = n; }
	public void setMaxCash(int n) { this.maxCash = n; }
	public void setRollFreq(int i, int n) { this.rollFreq[i] = n; }
	public void suppressOutput(boolean b) { this.doSuppressOutput = b; }
	
	
	// set the basic game values
	private void setBasicGameValues() {
		this.winValue = 4;
		this.loseValue = -1;
		this.doSuppressOutput = false;
	} // end setBasicGameValues()
	
	
	// reset the basics
	public void reset() {
		this.nRolls = 0;
		this.maxCash = 0;
		for (int i = 0; i < this.rollFreq.length; i++) 
			this.rollFreq[i] = 0;
	} // end reset()
	
	
	// play Lucky Sevens with a starting amount of money
	public void play() {
		int cash = this.startingCash;	// current amount of money
		this.maxCash = cash;			// peak winnings
		this.nRolls = 0;				// number of rolls before bankruptcy

		// the actual game
		while (cash > 0) {
			int rollValue = this.rollDice();
			this.rollFreq[rollValue]++;
			
			cash += (rollValue == 7) ? this.winValue : this.loseValue;
			String outcome = (rollValue == 7) ? "WIN" : "lose";
			
			if (cash > this.maxCash)
				this.maxCash = cash;
		
			this.nRolls++;
			
			if (!this.outputSuppressed())
				System.out.format("\tRoll %3d: %2d  %4s -> $%d\n", nRolls, rollValue, outcome, cash);
		}
		
	} // end play()
	
	
	// get the outcome of rolling the dice
	public int rollDice() {
		return this.randUnif(1,6) + this.randUnif(1,6);
	}
	
	
	// summarize outcome
	public void summarize() {
		System.out.println("\nYou start with $" + this.startingCash + ".\n"
						   + "You peak at $" + this.maxCash + ".\n"
						   + "After " + this.nRolls + " rolls, you run out of cash.\n");
	} // end summarize()
	
	
	// check the fairness of the dice roll distribution
	public boolean checkFairness() {

		boolean fair = this.chiSquare();
		
		if (!this.doSuppressOutput) {
			System.out.println("Was it fair? Roll distribution:\n");
			this.printHistogram();
			if (fair) {
				System.out.println("\nFair at 95% significance level!\n");
			}
			else {
				System.out.println("\nUnfair at 95% significance level!\n");
			}
		}
		
		return fair;
	} // end checkFairness()
	
	
	// print histogram (vertical bars)
	public void printHistogram() {
		int maxHeight = 10;
		double maxVal = (double)this.arrayMax(this.rollFreq);
		double step = (maxVal > maxHeight) ? maxVal/maxHeight : 1;
		
		// print vertical bars
		for (double cur = maxVal; cur > 0; cur-=step) {
			for (int i = 0; i < this.rollFreq.length; i++) {
				String marker = (this.rollFreq[i] >= cur) ? "X" : "";
				System.out.format("%3s", marker);
			}
			System.out.println();
		}
		
		// print axis
		for (int i = 0; i < 13; i++)
			System.out.print("---");
		System.out.println();
		
		// print x tick marks
		for (int i = 0; i < 13; i++)
			System.out.format("%3d", i);
		System.out.println();
	} // end printHistogram()
	
	
	// Chi-Square test of two dice roll distribution; True if fair, false o/w
	public boolean chiSquare() {
		int nSamples = this.nRolls;
		double [] expDist = {0, 0, 1/36., 2/36., 3/36., 4/36., 5/36., 6/36., 5/36., 4/36., 3/36., 2/36., 1/36.};
		
		// degrees of freedom is 11 (bins) - 4 (triangle dist df + 1) = 7
		// sig level = 95%, p = 0.05
		double critVal = 14.067;	
		
		// turn sample counts into percentage frequencies
		int [] expFreq = new int[13];
		for (int i = 2; i <= 12; i++) 
			expFreq[i] = (int)Math.round(expDist[i]*nSamples);
		
		// Chi-Square statistic
		double testStat = 0.;
		for (int i = 2; i <= 12; i++) 
			testStat += Math.pow(this.rollFreq[i]-expFreq[i], 2)/expFreq[i];
		
		// reject H0 if testStat > critVal
		return (testStat <= critVal);
	} // end chiSquare()
	
	
	// get max value in array
	public int arrayMax(int [] x) {
		int maxVal = x[0];
		for (int val : x) {
			if (val > maxVal)
				maxVal = val;
		}
		return maxVal;
	} // end max()
	
	
	// get random integer in [LB, UB]
	public int randUnif(int LB, int UB) {
		return (int)((UB - LB + 1)*Math.random()) + LB;
	} // end randUnif()
				
}
