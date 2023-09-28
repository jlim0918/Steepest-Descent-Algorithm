import java.util.*;

public class SteepestDescent {
	private double eps ; // tolerance
	private int maxIter ; // maximum number of iterations
	private double stepSize ; // step size alpha
	private double x0 ; // starting point
	private ArrayList < double [] > bestPoint ; // best point found for all polynomials
	private double [] bestObjVal ; // best obj fn value found for all polynomials
	private double [] bestGradNorm ; // best gradient norm found for all polynomials
	private long [] compTime ; // computation time needed for all polynomials
	private int [] nIter ; // no. of iterations needed for all polynomials
	private boolean resultsExist; // whether or not results exist

	// constructors
	public SteepestDescent () {}
	public SteepestDescent ( double eps , int maxIter , double stepSize , double x0 ) {
		this.eps = eps;
		this.maxIter = maxIter;
		this.stepSize = stepSize;
		this.x0 = x0;
		this.bestPoint = new ArrayList<>();
		this.resultsExist = false;
	}
	
	// getters
	public double getEps () {
		return this.eps;
	}
	public int getMaxIter () {
		return this.maxIter;
	}
	public double getStepSize () {
		return this.stepSize;
	}
	public double getX0 () {
		return this.x0;
	}
	public double [] getBestObjVal () {
		return this.bestObjVal;
	}
	public double [] getBestGradNorm () {
		return this.bestGradNorm;
	}
	public double [] getBestPoint (int i) {
		return this.bestPoint.get(i);
	}
	public int [] getNIter () {
		return this.nIter;
	}
	public long [] getCompTime () {
		return this.compTime;
	}

	public boolean hasResults () {
		return this.resultsExist;
	}

	// setters
	public void setEps ( double a ) {
		this.eps = a;
	}
	public void setMaxIter ( int a ) {
		this.maxIter = a;
	}
	public void setStepSize ( double a ) {
		this.stepSize = a;
	}
	public void setX0 ( double a ) {
		this.x0 = a;
	}
	public void setBestObjVal ( int i , double a ) {
		this.bestObjVal[i] = a;
	}
	public void setBestGradNorm ( int i , double a ) {
		this.bestGradNorm[i] = a;
	}
	public void setBestPoint ( int i , double [] a ) {
		this.bestPoint.add(i, a);
	}
	public void setCompTime ( int i , long a ) {
		this.compTime[i] = a;
	}
	public void setNIter ( int i , int a ) {
		this.nIter[i] = a;
	}
	public void setHasResults ( boolean a ) {
		this.resultsExist = a;
	}

	// other methods
	public void init ( ArrayList < Polynomial > P ) { // initialize member arrays to correct size
		int n = P.size(); //member arrays must be equal to number of polynomials within input file
		this.bestObjVal = new double [n];
		this.bestGradNorm = new double [n];
		this.compTime = new long [n];
		this.nIter = new int [n];
	}
	public void run ( int i , Polynomial P ) { // run the steepest descent algorithm
		long startTime = System.currentTimeMillis(); //marks start time of when run function occurs
		double x[];
		
		x = new double [P.getN()];
		Arrays.fill(x, this.x0); //x0 values are all set to single value defined within algorithm parameters for convenience
		do {
				
			//calculates for values to be displayed
			setBestObjVal(i,P.f(x));
			setBestGradNorm(i,P.gradientNorm(x));
			setBestPoint(i,x);
			
			//x^(k+1) = x^k + alpha_k * d^k
			if (this.nIter[i] < this.maxIter) {
				for (int j = 0; j < x.length; j++) {
					x[j] += this.lineSearch() * this.direction(P,x)[j];
				} 	
			}		
			this.nIter[i]++;
		} while (this.nIter[i] < (this.maxIter+1) && this.bestGradNorm[i] > this.eps); //stops running when max number of iterations reached or gradient Norm is greater than epsilon
		this.nIter[i]--; //accounts for increment of nIter at end of final loop
		
		this.setHasResults(true); //results exist after run function is completed
		
		//measures length of time for calculation
		this.setCompTime(i,System.currentTimeMillis() - startTime);
		
		System.out.println("Polynomial " + (i+1) + " done in " + this.getCompTime()[i] +"ms.");
	}
	public double lineSearch () { // find the next step size
		//step size is assumed constant
		return this.stepSize;
	}
	public double [] direction ( Polynomial P , double [] x ) { // find the next direction
		double negativeGrad[] = new double[x.length];
		
		//direction is equal to gradient * -1
		for (int i = 0; i < x.length; i++) {
			negativeGrad[i] = P.gradient(x)[i] * (-1.0);
		}
		return negativeGrad;
	}
	public void getParamsUser () { // get algorithm params from user 
		
		//collects user input for x0 value
		this.setX0(Pro4_limjoaqu.getDouble("Enter value for starting point (0 to cancel): ",Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY));
		System.out.println();
		System.out.println("Algorithm parameters set!\n");
	}
	public void print () { // print algorithm parameters
		System.out.println("Tolerance (epsilon): " + this.eps + "\nMaximum iterations: " + this.maxIter + "\nStep size (alpha): " + this.stepSize + "\nStarting point (x0): " + this.x0);
		System.out.println();
	}
	public void printStats () {// print statistical summary of results
		System.out.println("\nStatistical summary:\n"
				+ "---------------------------------------------------\n"
				+ "          norm(grad)       # iter    Comp time (ms)\n"
				+ "---------------------------------------------------");
		
		double avgGradNorm, avgNIter, avgCompTime;
		
		//duplicates nIter and compTime arrays as doubles for calculations since they are int[] and long[] respectively
		double [] nIterDouble = new double [this.nIter.length];
		for (int i = 0; i < this.nIter.length; i++) {
			nIterDouble[i] = this.nIter[i];
		}
		
		double [] compTimeDouble = new double [this.compTime.length];
		for (int i = 0; i < this.nIter.length; i++) {
			compTimeDouble[i] = this.compTime[i];
		}
		
		//calculates statistics for polynomial data using the created stats class
		avgGradNorm = Stats.Average(this.bestGradNorm);
		avgNIter = Stats.Average(nIterDouble);
		avgCompTime = Stats.Average(compTimeDouble);
		
		System.out.printf("Average%13.3f%13.3f%18.3f", avgGradNorm, avgNIter, avgCompTime);
		System.out.printf("\nSt Dev%14.3f%13.3f%18.3f", Stats.standardDeviation(avgGradNorm,this.bestGradNorm,this.bestGradNorm.length),Stats.standardDeviation(avgNIter,nIterDouble,nIterDouble.length),Stats.standardDeviation(avgGradNorm,compTimeDouble,compTimeDouble.length));
		System.out.printf("\nMin%17.3f%13.0f%18.0f", Stats.Minimum(this.bestGradNorm),Stats.Minimum(nIterDouble),Stats.Minimum(compTimeDouble));
		System.out.printf("\nMax%17.3f%13.0f%18.0f", Stats.Maximum(this.bestGradNorm),Stats.Maximum(nIterDouble),Stats.Maximum(compTimeDouble));
		System.out.println();
	
	}
	public void printAll () {// print final results for all polynomials 
		
		//prints header only on first iteration of loop
		for (int i = 0; i < this.bestObjVal.length; i++) {
			if (i == 0) {
				printSingleResult(i, false);
			} else {
				printSingleResult(i, true);
			}
		}
	}
	public void printSingleResult ( int i , boolean rowOnly ) {// print final result for one polynomial , column header optional
		if (rowOnly == false) {
			System.out.println("Detailed results:\n"
					+ "-------------------------------------------------------------------------\n"
					+ "Poly no.         f(x)   norm(grad)   # iter   Comp time (ms)   Best point   \n"
					+ "-------------------------------------------------------------------------");
		}
		System.out.printf("%8d%13.6f%13.6f%9d%17d   ",(i+1),this.getBestObjVal()[i],this.getBestGradNorm()[i],this.getNIter()[i],this.getCompTime()[i]);
		for (int j = 0; j < this.getBestPoint(i).length; j++) {
			
			if (j == 0) {
				System.out.format("%4.4f", this.getBestPoint(i)[j]);
			} else {
				System.out.format("%.4f", this.getBestPoint(i)[j]);
			}
			
			if (j < (this.getBestPoint(i).length-1)) {
				System.out.print(", ");
			}
			
		}
		System.out.println();
	}
}