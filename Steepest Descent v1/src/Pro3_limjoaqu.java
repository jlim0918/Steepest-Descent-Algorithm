import java.io.*;

public class Pro3_limjoaqu {

	public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws NumberFormatException,IOException {
		String choice = "";
		//cancel checks for if user inputted 0 to cancel input process; set checks for if polynomial is set based on isSet() in Polynomial
		boolean cancel = false, set = false;
		Polynomial P = new Polynomial();
		double[] x0 = new double[1];
		x0[0] = 1.00;
		
		//sets default values for steepest descent
		SteepestDescent SD = new SteepestDescent(0.001,100,0.05,x0);
		
		//displays menu and collects user input until user quits
		while (choice != "Q") {
			displayMenu();
	
			System.out.print("\nEnter choice: ");
			choice = cin.readLine();
			System.out.println();
			
			if (choice.equalsIgnoreCase("E")) { //collects values for polynomial and notifies user if process was cancelled
				cancel = getPolynomialDetails(P);
				
				if (cancel == true) {
					System.out.println("\nProcess canceled. No changes made to polynomial function.\n");
				}
				
			} else if (choice.equalsIgnoreCase("F")) { //displays polynomial unless it has not yet been set
				set = P.isSet();
				
				if (set == false) {
					System.out.println("ERROR: Polynomial function has not been entered!\n");
				} else {
					P.print();
				}
				
			} else if (choice.equalsIgnoreCase("S")) { //collects steepest descent parameters unless polynomial not yet set; also notifies if process cancelled
				set = P.isSet();
				
				if (set == true) {
					cancel = getAlgorithmParams(SD, P.getN());
				
					if (cancel == true) {
						System.out.println("Process canceled. No changes made to algorithm parameters.\n");
					}
				} else {
					System.out.println("ERROR: Polynomial function has not been entered!\n");
				}
				
				
			} else if (choice.equalsIgnoreCase("P")) { //displays algorithm parameters
				SD.print();
				
			} else if (choice.equalsIgnoreCase("R")) { //runs steepest descent algorithm unless polynomial not yet set
				set = P.isSet();
				
				if (set == false) {
					System.out.println("ERROR: Polynomial function has not been entered!\n");
				} else {
					SD.run(P);
				}
				
			}  else if (choice.equalsIgnoreCase("D")) { //displays final best point values unless steepest descent algorithm has not been run yet
				if (SD.getNIter() == -1) {
					System.out.println("ERROR: No results exist!\n");
				} else {
					SD.printResults(false); 
					System.out.println();
				}
				
			} else if (choice.equalsIgnoreCase("Q")) { //exits program
				System.out.println("The end.");
				System.exit(0);
				
			} else { //notifies if invalid input was entered
				System.out.println("ERROR: Invalid menu choice!\n");
			}
		}

	}
	public static void displayMenu() {// Display the menu. 
		
		System.out.println("   JAVA POLYNOMIAL MINIMIZER (STEEPEST DESCENT)\nE - Enter polynomial function\n"
				+ "F - View polynomial function\n"
				+ "S - Set steepest descent parameters\n"
				+ "P - View steepest descent parameters\n"
				+ "R - Run steepest descent algorithm\n"
				+ "D - Display algorithm performance\n"
				+ "Q - Quit");
	}
	public static boolean getPolynomialDetails(Polynomial P) { //Get polynomial function details from the user.
		int n, degree;
		
		//stores inputted values in temporary variables to prevent changes if process cancelled; exits method when process is cancelled
		n = getInteger("Enter number of variables (0 to cancel): ",0,Integer.MAX_VALUE);
		if (n == 0) { return true; }
		degree = getInteger("Enter polynomial degree (0 to cancel): ",0,Integer.MAX_VALUE);
		if (degree == 0) { return true; }
		
		//inputs values and initializes array when process can no longer be cancelled
		P.setN(n);
		P.setDegree(degree);
		P.init();
		
		//collects coefficient inputs
		for (int i = 0; i < P.getN(); i++) {
			System.out.format("Enter coefficients for variable x%d: ", i+1);
			System.out.println();
			for (int j = 0; (j < P.getDegree()+1); j++) {
				P.setCoef(i, (P.getDegree()-j), getDouble("   Coefficient " + (j+1) + ": ",(Double.NEGATIVE_INFINITY),Double.POSITIVE_INFINITY));
			}
		}
		System.out.println("\nPolynomial complete!\n");
		return false;
	}
	public static boolean getAlgorithmParams(SteepestDescent SD, int n) { // Get algorithm parameters from the user.
		double eps, stepSize;
		int maxIter;
		
		//temporarily collects parameter values until process can no longer be cancelled; remaining parameters are set in getParamsUser in SD
		eps = getDouble("Enter tolerance epsilon (0 to cancel): ",0,Double.POSITIVE_INFINITY);
		if (eps == 0) { return true; }
		maxIter = getInteger("Enter maximum number of iterations (0 to cancel): ",0,10000);
		if (maxIter == 0) { return true; }
		stepSize = getDouble("Enter step size alpha (0 to cancel): ",0,Double.POSITIVE_INFINITY);
		if (stepSize == 0) { return true; }
		
		//inputs values when process can no longer be cancelled
		SD.setEps(eps);
		SD.setMaxIter(maxIter);
		SD.setStepSize(stepSize);
		
		//runs getParamsUser for x0 inputs
		SD.getParamsUser(n);
		
		return false;
	}
	public static int getInteger(String prompt, int LB, int UB) { //Get an integer in the range [LB, UB] from the user. Prompt the user repeatedly until a valid value is entered.
		int I = 0;
    	boolean valid;
    	
    	String strLB = Integer.toString(LB), strUB = Integer.toString(UB);
		
    	//converts infinite bounds to text for when error message occurs due to range
		if (LB == (-1 * Integer.MAX_VALUE)) {
			strLB = "-infinity";
		} 
		
		if (UB ==  Integer.MAX_VALUE) {
			strUB = "infinity";
		}
		
    	//loop until input is valid
    	do {
    		valid = true;
    		System.out.print(prompt);
    		try {
    			
    			I = Integer.parseInt(cin.readLine());
        	
    			
    		} catch (NumberFormatException e) {
    			System.out.println("ERROR: Input must be an integer in [" + strLB + ", " + strUB + "]!\n");
    			valid = false;
    			continue;
    			
    		} catch (IOException e) {
    			System.out.println("ERROR: Input must be an integer in [" + strLB + ", " + strUB + "]!\n");
    			valid = false;
    			continue;
    			
    		} 
    		
    		if (I < LB || I > UB) {
    			System.out.println("ERROR: Input must be an integer in [" + strLB + ", " + strUB + "]!\n");
    			valid = false;
    			continue;
    		} 
    	} while (!valid);
    	
    	return I;
	}
	public static double getDouble(String prompt, double LB, double UB) { //Get a real number in the range [LB, UB] from the user. Prompt the user repeatedly until a valid value is entered.
		double D = 0;
    	boolean valid;
    	
    	//converts bounds to strings w/ 2 decimal places
    	String strLB = String.format("%.2f", LB), strUB = String.format("%.2f", UB);
		
    	//converts infinite bounds to text for when error message occurs due to range;
		if (LB == Double.NEGATIVE_INFINITY) { //NEGATIVE_INFINITY results in "-Infinity", output must be "-infinity"
			strLB = "-infinity";
		} 
		
		if (UB == Double.POSITIVE_INFINITY) { //POSITIVE_INFINITY results in "Infinity", output must be "infinity"
			strUB = "infinity";
		}
    	//loop until input is valid
    	do {
    		valid = true;
    		System.out.print(prompt);
    		
    		try {
    			
    			D = Double.parseDouble(cin.readLine());
        		
    			
    		} catch (NumberFormatException e) {
    			System.out.println("ERROR: Input must be a real number in [" + strLB + ", " + strUB + "]!\n");
    			valid = false;
    			continue;
    			
    		} catch (IOException e) {
    			System.out.println("ERROR: Input must be a real number in [" + strLB + ", " + strUB + "]!\n");
    			valid = false;
    			continue;
    		} catch (NullPointerException e) {
    			System.out.println("ERROR: Input must be a real number in [" + strLB + ", " + strUB + "]!\n");
    			valid = false;
    			continue;
    		}
    		
    		if (D < LB || D > UB) {
    			System.out.println("ERROR: Input must be a real number in [" + strLB + ", " + strUB + "]!\n");
    			valid = false;
    			continue;
    		} 
    	} while (!valid);
    	
    	return D;
	}


}
