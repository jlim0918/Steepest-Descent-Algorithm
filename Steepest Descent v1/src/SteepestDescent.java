import java.util.Arrays;

public class SteepestDescent {
	private double eps ; // tolerance
	private int maxIter ; // maximum number of iterations
	private double stepSize ; // step size alpha
	private double [] x0 ; // starting point
	private double [] bestPoint ; // best point found
	private double bestObjVal ; // best obj fn value found
	private double bestGradNorm ; // best gradient norm found
	private long compTime ; // computation time needed
	private int nIter ; // no. of iterations needed
	private boolean resultsExist; // whether or not results exist

	// constructors
	public SteepestDescent () {  }
	public SteepestDescent ( double eps , int maxIter , double stepSize , double [] x0 ) {
		this.eps = eps;
		this.maxIter = maxIter;
		this.stepSize = stepSize;
		this.x0 = x0;
		this.nIter = -1;
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
	public double [] getX0 () {
		return this.x0;
	}
	public double getBestObjVal () {
		return this.bestObjVal;
	}
	public double getBestGradNorm () {
		return this.bestGradNorm;
	}
	public double [] getBestPoint () {
		return this.bestPoint;
	}
	public int getNIter () {
		return this.nIter;
	}
	public long getCompTime () {
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
	public void setX0 ( int j , double a ) {
		this.x0[j] = a;
	}
	public void setBestObjVal ( double a ) {
		this.bestObjVal = a;
	}
	public void setBestGradNorm ( double a ) {
		this.bestGradNorm = a;
	}
	public void setBestPoint ( double [] a ) {
		for (int i = 0; i < a.length; i++) {
			this.bestPoint[i] = a[i];
		}
	}
	public void setCompTime ( long a ) {
		this.compTime = a;
	}
	public void setNIter ( int a ) {
		this.nIter = a;
	}
	public void setHasResults ( boolean a ) {
		this.resultsExist = a;
	}

	// other methods
	public void init ( int n ) { // initialize member arrays to correct size
		this.x0 = new double[n];
		this.bestPoint = new double[n];
	}
	public void run ( Polynomial P ) { // run the steepest descent algorithm
		long startTime = 0;
		double x[];
		
		//creates default array of 1's when length of x0 is not equal to # of variables in polynomial
		if (this.x0.length != P.getN() || this.bestPoint == null) {
			if (this.x0.length != P.getN()) {
				System.out.println("WARNING: Dimensions of polynomial and x0 do not match! Using x0 = 1-vector of appropriate dimension.\n");
			}
				this.init(P.getN());
			Arrays.fill(this.x0, 1.00);
		}	
		
		x = Arrays.copyOf(this.x0, P.getN());
		this.nIter = 0;
		
		do {
			if (this.nIter == 0) {
				startTime = System.currentTimeMillis();
			}
			//calculates for values to be displayed
			setBestObjVal(P.f(x));
			setBestGradNorm(P.gradientNorm(x));
			setBestPoint(x);
			
			//x^(k+1) = x^k + alpha_k * d^k
			for (int j = 0; j < x.length; j++) {
				x[j] += this.lineSearch() * this.direction(P,x)[j];
			} 
			
			//measures length of time for calculations
			setCompTime(System.currentTimeMillis() - startTime);
			
			//displays header only on 1st iteration
			if (this.nIter == 0) {
				this.printResults(false);
			} else {
				this.printResults(true);
			}		

			this.nIter++;
			
		} while (this.nIter < (this.maxIter + 1) && this.bestGradNorm > this.eps);
		this.nIter--;
		System.out.println();
	}
	public double lineSearch () { // find the next step size
		//step size is assumed constant
		return this.stepSize;
	}
	public double [] direction ( Polynomial P , double [] x ) { // find the next direction
		double negativeGrad[] = new double[x.length];
		
		//direction is equal to gradient * -1
		for (int i = 0; i < x.length; i++) {
			negativeGrad[i] = -1 * P.gradient(x)[i];
		}
		return negativeGrad;
	}
	public void getParamsUser ( int n ) { // get params from user for n- dim polynomial
		this.init(n);
		
		//collects user input for x0 values
		System.out.println("Enter values for starting point: ");
		for (int i = 0; i < this.x0.length; i++) {
			this.setX0(i,Pro3_limjoaqu.getDouble("   x" + (i+1) + ": ",Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY));
		}
		System.out.println("\nAlgorithm parameters set!\n");
	}
	public void print () { // print algorithm parameters
		System.out.print("Tolerance (epsilon): " + this.eps + "\nMaximum iterations: " + this.maxIter + "\nStep size (alpha): " + this.stepSize + "\nStarting point (x0): (");
		
		for (int i = 0; i < this.x0.length; i++) {
			System.out.printf(" %.2f",this.x0[i]);
			if (i < (x0.length - 1)) {
				System.out.print(",");
			}
		}
			
		System.out.println(" )\n");
	}
	public void printResults ( boolean rowOnly ) { // print iteration results , column header optional 
		if (rowOnly == false) {
			System.out.println("--------------------------------------------------------------\n"
					+ "      f(x)   norm(grad)   # iter   Comp time (ms)   Best point   \n"
					+ "--------------------------------------------------------------");
		}
	         
		System.out.printf("%10.6f%13.6f%9d%17d   ",this.bestObjVal,this.bestGradNorm,this.nIter,this.compTime);
		for (int i = 0; i < this.getBestPoint().length; i ++) {
			
			if (i == 0) {
				System.out.format("%4.4f", this.getBestPoint()[i]);
			} else {
				System.out.format("%.4f", this.getBestPoint()[i]);
			}
			
			if (i < (this.getBestPoint().length-1)) {
				System.out.print(", ");
			}
			
		}
		
		System.out.println();
	}
}