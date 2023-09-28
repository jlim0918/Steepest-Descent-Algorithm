package LuckySevensOOP.copy;
import java.io.*;
 
public class LuckySevens_OOP {
	
    public static void main(String[] args) {
		int startingCash = getUserStartingCash();
		LuckySevens game = new LuckySevens(startingCash);
		game.play();
		game.summarize();
		game.checkFairness();
    } // end main

	// get a positive integer as starting cash
	public static int getUserStartingCash() {
		int x = 0;
		boolean valid;
		String input = null;
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		do {
			valid = true;
			System.out.print("Enter the amount of cash to start with (or \"quit\" to quit): ");
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
				System.out.println("\nERROR: Starting cash value must be a positive integer!\n");
		} while (!valid);
		return x;
	} // end of getStartingCash()
}
