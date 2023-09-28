public class SDFixed extends SteepestDescent {
	private double alpha; // fixed step size
	
	// constructors
	public SDFixed () {
		super();
		this.setAlpha(0.01);
	}
	public SDFixed ( double alpha ) {
		super();
		this.setAlpha(alpha);
	}
		
	// getters
	public double getAlpha () {
		return this.alpha;
	}
		
	// setters
	public void setAlpha ( double a ) {
		this.alpha = a;
	}
		
	// other methods
	public double lineSearch ( Polynomial P , double [] x ) { // fixed step size
		return alpha;
	}
	public boolean getParamsUser () { // get algorithm parameters from user
		double stepSize;
		boolean success;
		
		//collects fixed parameters
		System.out.println("Set parameters for SD with a fixed line search:");
		stepSize = Pro5_limjoaqu.getDouble("Enter fixed step size (0 to cancel): ",0,Double.POSITIVE_INFINITY);
		if (stepSize == 0) { return false; }
		success = super.getParamsUser(); //sets common parameters
		
		//sets Fixed parameters if not cancelled
		if (success == true) {
			this.setAlpha(stepSize);
			return true;
		} else {
			return false;
		}
	}
	public void print () { // print parameters
		super.print();
		System.out.println("Fixed step size (alpha): " + alpha);
		System.out.println();
	}

}
