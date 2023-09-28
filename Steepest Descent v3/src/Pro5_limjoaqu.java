import java.io.*;
import java.util.*;

public class Pro5_limjoaqu {

	public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws NumberFormatException,IOException {
		String choice = "";
		//cancel checks for if user inputted 0 to cancel input process
		boolean cancel;
		ArrayList <Polynomial> P = new ArrayList<>();
		
		//sets default values for steepest descent variations
		SDFixed SDF = new SDFixed();
		SDArmijo SDA = new SDArmijo();
		SDGSS SDG = new SDGSS();
		
		//displays menu and collects user input until user quits
		while (choice != "Q") {
			displayMenu();
			cancel = false;
			
			System.out.print("\nEnter choice: ");
			choice = cin.readLine();
			System.out.println();
			
			if (choice.equalsIgnoreCase("L")) { //loads polynomial from txt file
				
				cancel = loadPolynomialFile(P);
				
				if (cancel == true) {
					System.out.println("File loading process canceled."); //displays cancelled text if user typed 0 during input
				} else {
					noResults(SDF,SDA,SDG); //sets results to non-existent
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
				noResults(SDF,SDA,SDG); //sets results to non-existent
				
				System.out.println("All polynomials cleared.\n");
				
			} else if (choice.equalsIgnoreCase("S")) { //Set steepest descent parameters

				getAllParams(SDF,SDA,SDG);
				
			} else if (choice.equalsIgnoreCase("P")) { //View steepest descent parameters
					
				printAllParams(SDF, SDA, SDG);
				
			} else if (choice.equalsIgnoreCase("R")) { //Run steepest descent algorithms
				
				//runs steepest descent function if at least 1 polynomial in array list
				if (P.size() > 0) {
					runAll(SDF, SDA, SDG, P);
					
				} else {
					System.out.println("ERROR: No polynomial functions are loaded!");
				}
				
				System.out.println();

			} else if (choice.equalsIgnoreCase("D")) { //Display algorithm performance
				
				//prints performance and stats if results exist 
				if (SDA.hasResults() && SDF.hasResults() && SDG.hasResults()) {
					printAllResults(SDF,SDA,SDG,P);
				} else {
					System.out.println("ERROR: Results do not exist for all line searches!\n");
					}
				
			} else if (choice.equalsIgnoreCase("X")){ //Display comparisons and winners between line searches
				
				//prints comparisons if results exist
				if (SDA.hasResults() && SDF.hasResults() && SDG.hasResults()) {
					compare(SDF,SDA,SDG);
				} else {
					System.out.println("ERROR: Results do not exist for all line searches!\n");
				}
				
			} else if (choice.equalsIgnoreCase("Q")) { //exits program
				System.out.println("Arrivederci.");
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
				+ "R - Run steepest descent algorithms\n"
				+ "D - Display algorithm performance\n"
				+ "X - Compare average algorithm performance\n"
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
					
					totalPolyNum++; //increments number of tested polynomials
					
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
	
	public static void getAllParams(SDFixed SDF, SDArmijo SDA, SDGSS SDG) {//Get algorithm parameters from the user for each algorithm variation.
		boolean success;
		//success notifies user if paramters for a line search are set or cancelled
		
		success = SDF.getParamsUser();
		if (success == false) {
			System.out.println("\nProcess canceled. No changes made to algorithm parameters.\n");
		} else {
			System.out.println("\nAlgorithm parameters set!\n");
			noResults(SDF,SDA,SDG); //sets results to non-existent
		}
		
		success = SDA.getParamsUser();
		if (success == false) {
			System.out.println("\nProcess canceled. No changes made to algorithm parameters.\n");
		} else {
			System.out.println("\nAlgorithm parameters set!\n");
			noResults(SDF,SDA,SDG); //sets results to non-existent
		}
		
		success = SDG.getParamsUser();
		if (success == false) {
			System.out.println("\nProcess canceled. No changes made to algorithm parameters.\n");
		} else {
			System.out.println("\nAlgorithm parameters set!\n");
			noResults(SDF,SDA,SDG); //sets results to non-existent
		}
	}
	public static void printAllParams(SDFixed SDF, SDArmijo SDA, SDGSS SDG) { //Print the parameters for all the algorithm variations.
		System.out.println("SD with a fixed line search:");
		SDF.print();
		System.out.println("SD with an Armijo line search:");
		SDA.print();
		System.out.println("SD with a golden section line search:");
		SDG.print();
	}
	public static void runAll(SDFixed SDF, SDArmijo SDA,SDGSS SDG,ArrayList<Polynomial> P) { //Run all algorithm variations on all loaded polynomials
		//initializes all algorithm variations
		SDF.init(P);
		SDA.init(P);
		SDG.init(P);
		
		//runs SD algorithm for all polynomials and each variation
		
		System.out.println("Running SD with a fixed line search:");
		for(int i = 0; i < P.size(); i++) {
			SDF.run(i, P.get(i));
		}
		System.out.println();
		System.out.println("Running SD with an Armijo line search:");
		for(int i = 0; i < P.size(); i++) {
			SDA.run(i, P.get(i));
		}
		System.out.println();
		System.out.println("Running SD with a golden section line search:");
		for(int i = 0; i < P.size(); i++) {
			SDG.run(i, P.get(i));
		}
		
		System.out.println("\nAll polynomials done.");
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
	public static void printAllResults(SDFixed SDF, SDArmijo SDA, SDGSS SDG, ArrayList<Polynomial> P) { //Print the detailed results and statistics summaries for all algorithm variations
		System.out.println("Detailed results for SD with a fixed line search:");
		SDF.printAll();
		System.out.println("Statistical summary for SD with a fixed line search:");
		SDF.printStats();
		
		System.out.println("\n");
		
		System.out.println("Detailed results for SD with an Armijo line search:");
		SDA.printAll();
		System.out.println("Statistical summary for SD with an Armijo line search:");
		SDA.printStats();
		
		System.out.println("\n");
		
		System.out.println("Detailed results for SD with a golden section line search:");
		SDG.printAll();
		System.out.println("Statistical summary for SD with a golden section line search:");
		SDG.printStats();
		
		System.out.println();
	}
	public static void compare(SDFixed SDF, SDArmijo SDA, SDGSS SDG) { //Compare the performance of the algorithms and pick winners
		double[] avgSDF,avgSDA, avgSDG;
		String[] winner = new String[3];
		String winnerOverall;

		System.out.println("---------------------------------------------------\n"
+ "          norm(grad)       # iter    Comp time (ms)\n"
+ "---------------------------------------------------");
	
		//displays average stats for all algorithm variations
		avgSDF = SDF.printAverageStat("Fixed ");
		System.out.println();
		avgSDA = SDA.printAverageStat("Armijo");
		System.out.println();
		avgSDG = SDG.printAverageStat("GSS   ");
		System.out.println();
		
		//compares average stats of each variation and selects lowest as winner
		for (int i = 0; i < avgSDF.length; i++) {
			if (avgSDF[i] < avgSDA[i] && avgSDF[i] < avgSDG[i]) {
				winner[i] = "Fixed";
			} else if (avgSDA[i] < avgSDF[i] && avgSDA[i] < avgSDG[i]) {
				winner[i] = "Armijo";
			} else if (avgSDG[i] < avgSDF[i] && avgSDG[i] < avgSDA[i]) {
				winner[i] = "GSS";
			} else if (avgSDF[i] == avgSDA[i] || avgSDF[i] == avgSDG[i]) {
				winner[i] = "Fixed";
			} else if (avgSDA[i] == avgSDG[i]) {
				winner[i] = "Armijo";
			}
		}
		
		//overall winner must win all categories, unclear otherwise
		if (winner[0] == winner[1] && winner[0] == winner[2]) {
			winnerOverall = winner[0];
		} else {
			winnerOverall = "Unclear";
		}
		
		System.out.printf("---------------------------------------------------\n"
				+ "Winner%14s%13s%18s\n"
				+ "---------------------------------------------------\n"
				+ "Overall winner: %s\n\n",winner[0],winner[1],winner[2],winnerOverall);
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
	
	public static void noResults(SDFixed SDF, SDArmijo SDA, SDGSS SDG) { //sets results to non-existent for all line searches
		SDA.setHasResults(false); 
		SDF.setHasResults(false);
		SDG.setHasResults(false);
	}
}

