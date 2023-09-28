import java.io.*;
import java.util.*;

public class Pro4_limjoaqu {

	public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws NumberFormatException,IOException {
		String choice = "";
		//cancel checks for if user inputted 0 to cancel input process
		boolean cancel;
		ArrayList <Polynomial> P = new ArrayList<>();
		
		//sets default values for steepest descent
		SteepestDescent SD = new SteepestDescent(0.001,100,0.05,1.0);
		
		//displays menu and collects user input until user quits
		while (choice != "Q") {
			displayMenu();
			cancel = false;
			
			System.out.print("\nEnter choice: ");
			choice = cin.readLine();
			System.out.println();
			
			if (choice.equalsIgnoreCase("L")) { //View polynomial functions
				
				cancel = loadPolynomialFile(P);
				
				if (cancel == true) {
					System.out.println("File loading process canceled."); //displays cancelled text if user typed 0 during input
				} else {
					SD.setHasResults(false); //if new file successfully loaded, results are non existent
				}
				
				System.out.println();
				
			} else if (choice.equalsIgnoreCase("F")) { //View polynomial functions
				
				//prints polynomials if size of Polynomial ArrayList has at least 1 element
				if (P.size() > 0) {
					printPolynomials(P); 
				} else {
					System.out.println("ERROR: No polynomial functions are loaded!");
				}
				
				System.out.println();
				
			} else if (choice.equalsIgnoreCase("C")) { //Clear polynomial functions
			
				P.clear(); //empties Polynomial array list
				SD.setHasResults(false); //sets results to non-existent
				
				System.out.println("All polynomials cleared.\n");
				
			} else if (choice.equalsIgnoreCase("S")) { //Set steepest descent parameters

				cancel = getAlgorithmParams(SD, P.size());
				
				//displays cancelled text if user typed 0 during input
				if (cancel == true) {
					System.out.println("\nProcess canceled. No changes made to algorithm parameters.\n");
				} else {
					SD.setHasResults(false); //if parameters change, results with new parameters are non=existent
				}
				
			} else if (choice.equalsIgnoreCase("P")) { //View steepest descent parameters
					
				SD.print();
				
			} else if (choice.equalsIgnoreCase("R")) { //Run steepest descent algorithm
				
				//runs steepest descent function if at least 1 polynomial is array list
				if (P.size() > 0) {
					SD.init(P);
					//loops to run steepest descent for all polynomials in array list
					for (int i = 0; i < P.size(); i++) {
						SD.run(i, P.get(i));
					}
					System.out.println("\nAll polynomials done.");
				} else {
					System.out.println("ERROR: No polynomial functions are loaded!");
				}
				System.out.println();

			} else if (choice.equalsIgnoreCase("D")) { //Display algorithm performance
				
				//prints performance and stats if run function has occurred and stats are set 
				if (SD.hasResults()) {
					SD.printAll();
					SD.printStats();
				} else {
					System.out.println("ERROR: No results exist!");
				}
				System.out.println();
				
			} else if (choice.equalsIgnoreCase("Q")) { //exits program
				System.out.println("Fin.");
				System.exit(0);
				
			} else { //notifies if invalid input was entered
				System.out.println("ERROR: Invalid menu choice!\n");
			}
		}

	}
	public static void displayMenu() {// Display the menu. 
		
		System.out.println("   JAVA POLYNOMIAL MINIMIZER (STEEPEST DESCENT)\nL - Load polynomials from file\n"
				+ "F - View polynomial functions\n"
				+ "C - Clear polynomial functions\n"
				+ "S - Set steepest descent parameters\n"
				+ "P - View steepest descent parameters\n"
				+ "R - Run steepest descent algorithm\n"
				+ "D - Display algorithm performance\n"
				+ "Q - Quit");
	}

	public static boolean loadPolynomialFile(ArrayList<Polynomial> P) throws NumberFormatException,IOException { //Load the polynomial function details from a user-specified file.
		String line = "", filename = "";
		String [] strCoefs;
		ArrayList<double[]> polyCoefs = new ArrayList<>();
		double [] singleVarCoefs = null;
		int correctPolyNum = 0, totalPolyNum = 0;
		BufferedReader fin;
		
		//takes user input for filename
		System.out.print("Enter file name (0 to cancel): ");
		filename = cin. readLine();
		System.out.println();
		
		//try-catch to check if inputted filename is within directory
		try {
			fin = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			//checks for if manually cancelled with 0 or non-existent filename inputted then returns to menu
			if (!filename.equals("0")) {
				System.out.println("ERROR: File not found!");
				return false;
			} else {
				return true;
			}
		}
		
		do {
			line = fin.readLine(); //reads single line of text file
			if (line != null && !line.equals("*")) { //checks for end of text file or * to indicate next polynomial
				strCoefs = line.split(","); //separates coefficients based on commas and puts into string array
				singleVarCoefs = new double[strCoefs.length];
				
				//converts string array to double array
				for (int a = 0; a < strCoefs.length; a++) {
					double d = Double.parseDouble(strCoefs[a]);
					singleVarCoefs[a] = d;
				}
	
				polyCoefs.add(singleVarCoefs); //adds coefficients for single variable to temporary storage array for all coefficients for each variable in polynomial
				
			}else { //sets new polynomial when end of text file or * occurs
				
				
				
				outloop: //label to break out of nested loop if inconsistent dimensions for polynomial
				if (polyCoefs.size() > 0) {//checks if at least 1 variable in polynomial
					
					totalPolyNum++; //incrementss number of tested polynomials
					
					//compares all arrays of coefficients to check if all same size
					for (int x = 1; x < polyCoefs.size(); x++) {
						if (polyCoefs.get(0).length != polyCoefs.get(x).length) {
							//removes polynomial if inconsistent dimensions between variables
							System.out.println("ERROR: Inconsistent dimensions in polynomial " + totalPolyNum + "!\n");
							polyCoefs.clear(); //resets coefficient array
							break outloop; //breaks out of nested loop
						}
					}
					
					Polynomial singleP = new Polynomial();
					
					//sets and initializes new polynomial to be added to polynomial array list
					singleP.setN(polyCoefs.size());
					singleP.setDegree(singleVarCoefs.length-1);
					singleP.init();
					
					for (int i = 0; i < singleP.getN(); i++) {
						for (int j = 0; j < (singleP.getDegree()+1); j++) {
							singleP.setCoef(i,singleP.getDegree()-j,polyCoefs.get(i)[j]);
						}
					}
					P.add(singleP); //adds polynomial to array list
					polyCoefs.clear(); 
					correctPolyNum++; //increments number of polynomials successfully added to array list
				}
			}	
		} while (line != null); //ends loop at end of text file
		fin.close();
		
		System.out.println(correctPolyNum +" polynomials loaded!");
		
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
		SD.getParamsUser();
		
		return false;
	}
	
	public static void printPolynomials(ArrayList<Polynomial> P) { //Print all the polynomial functions currently loaded.
		
		System.out.println("---------------------------------------------------------\n"
				+ "Poly No.  Degree   # vars   Function\n"
				+ "---------------------------------------------------------");
		for (int i = 0; i < P.size(); i++) {
			System.out.printf("%8d%8d%9d   ", i+1, P.get(i).getDegree(), P.get(i).getN());
			P.get(i).print();
		}
		
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
