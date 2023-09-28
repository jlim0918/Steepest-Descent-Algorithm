import java.io.*;
 
public class LuckySevensSimulation_OOP_V2 {
	
    public static void main(String[] args) {
		int nSims = getPositiveInt("Enter the number of simulations to run (or \"quit\" to quit): ");
		int startingCash = getPositiveInt("Enter the amount of cash to start with (or \"quit\" to quit): ");
		
		LuckySevens game = new LuckySevens(startingCash);
		game.suppressOutput(true);
		
		int nFair = 0;
		int nRollsTotal = 0, maxRolls = 0, minRolls = Integer.MAX_VALUE;
		int peakTotal = 0, maxPeak = 0, minPeak = Integer.MAX_VALUE;
		int[] nRolls = new int[nSims];
		int[] peaks = new int[nSims];
		
		for (int i = 0; i < nSims; i++) {
			game.reset();
			game.play();
			
			nRolls[i] = game.getNRolls();
			peaks[i] = game.getMaxCash();
			
			nRollsTotal += nRolls[i];
			if (nRolls[i] > maxRolls)
				maxRolls = nRolls[i];
			else if (nRolls[i] < minRolls)
				minRolls = nRolls[i];
			
			peakTotal += peaks[i];
			if (peaks[i] > maxPeak)
				maxPeak = peaks[i];
			else if (peaks[i] < minPeak)
				minPeak = peaks[i];
			
			if (game.checkFairness())
				nFair++;
		}
		
		double avgRolls = nRollsTotal/(double)nSims;
		double avgPeak = peakTotal/(double)nSims;
		
		double stdRolls = 0.;
		double stdPeak = 0.;
		for (int i = 0; i < nSims; i++) {
			stdRolls += Math.pow(nRolls[i] - avgRolls, 2);
			stdPeak += Math.pow(peaks[i] - avgPeak, 2);
		}
		stdRolls = Math.sqrt(stdRolls/(nSims - 1));
		stdPeak = Math.sqrt(stdPeak/(nSims - 1));
		
		System.out.format("\nAfter %d simulations starting with $%d:\n\n", nSims, startingCash);
		printFairness(nFair, nSims);
		printStats("Rolls", avgRolls, stdRolls, minRolls, maxRolls);
		printStats("Peak cash", avgPeak, stdPeak, minPeak, maxPeak);
		
    } // end main
	

	
	// print fairness stats
	public static void printFairness(int nFair, int nSims) {
		System.out.format("    %d of %d (%.0f%%) games were fair\n\n", nFair, nSims, 100*nFair/(double)nSims);
	}
	
	
	// print basic stats
	public static void printStats(String item, double avg, double std, int min, int max) {
		System.out.format("    %s:\n" +
						  "\tAverage : %.2f\n" +
						  "\tSt. dev.: %.2f\n" +
						  "\tRange   : [%d, %d]\n\n", item, avg, std, min, max);
	}
	

	// get a positive integer from the user
	public static int getPositiveInt(String prompt) {
		int x = 0;
		boolean valid;
		String input = null;
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		
		do {
			valid = true;
			System.out.print(prompt);
			try {
				input = cin.readLine();
				if (input.equals("quit")) {
					System.out.println("\nQuitters never win.\n");
					System.exit(1);
				}
				x = Integer.parseInt(input);
				if (x <= 0) 
					valid = false;
			} 
			catch (IOException e) {
				valid = false;
			}
			catch (NumberFormatException e) {
				valid = false;
			} 
			if (!valid)
				System.out.println("\nERROR: Value must be a positive integer!\n");
		} while (!valid);
		return x;
	} // end of getPositiveInt()
}
