import java.io.*;
 
public class LuckySevensSimulation_OOP {
	
    public static void main(String[] args) {
		int nSims = getPositiveInt("Enter the number of simulations to run (or \"quit\" to quit): ");
		int startingCash = getPositiveInt("Enter the amount of cash to start with (or \"quit\" to quit): ");
		LuckySevens game = new LuckySevens(startingCash);
		
		for (int i = 0; i < nSims; i++) {
			game.reset();
			game.play();
			game.summarize();
			game.checkFairness();
		}
    } // end main

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
	} // end of getStartingCash()
}
