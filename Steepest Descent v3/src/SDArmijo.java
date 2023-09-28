public class SDArmijo extends SteepestDescent {
	private double maxStep ; // Armijo max step size
	private double beta ; // Armijo beta parameter
	private double tau ; // Armijo tau parameter
	private int K; // Armijo max no. of iterations
	
	// constructors
	public SDArmijo () {
		super();
		this.setMaxStep(1.0);
		this.setK(10);
		this.setBeta(0.0001);
		this.setTau(0.5);
	}
	public SDArmijo ( double maxStep , double beta , double tau , int K ) {
		this.setMaxStep(maxStep);
		this.setK(K);
		this.setBeta(beta);
		this.setTau(tau);
	}
	
	// getters
	public double getMaxStep () {
		return maxStep;
		}
	public double getBeta () {
		return beta; 
		}
	public double getTau () {
		return tau;
		}
	public int getK () { 
		return K; 
		}
	
	// setters
	public void setMaxStep ( double a ) {
		this.maxStep = a;
	}
	public void setBeta ( double a ) {
		this.beta = a;
	}
	public void setTau ( double a ) {
		this.tau = a;
	}
	public void setK ( int a ) {
		this.K = a;
	}
	
	// other methods
	public double lineSearch ( Polynomial P , double [] x ) { // Armijo line search
		double alpha = this.maxStep;
		double x2[] = new double[x.length];
		boolean ArmijoDecrease;
		int count = 0;
		
		//loops until maximum #iterations reached
		do {
			for (int i = 0; i < x.length; i++) {
				x2[i] = x[i] + alpha * this.direction(P, x)[i];
			}
			//sets ArmijoDecrease condition
			ArmijoDecrease = (P.f(x2) <= P.f(x)- alpha*this.beta*P.gradientNorm(x)*P.gradientNorm(x));
			
			if (!ArmijoDecrease) {
				alpha *= this.tau; //decreases alpha if ArmijoDecrease not fulfilled
			} else {
				return alpha; //returns step size if ArmijoDecrease fulfilled
			}
			
			count ++; //increments #iterations
		} while (count < K);
		return -1; //sets step size to impossible value to indicate armijo did not converge
	}
	public boolean getParamsUser () { // get algorithm parameters from user
		double maxStep,beta,tau;
		int K;
		boolean success;
		
		//collects Armijo parameters
		System.out.println("Set parameters for SD with an Armijo line search:");
		maxStep = Pro5_limjoaqu.getDouble("Enter Armijo max step size (0 to cancel): ",0,Double.POSITIVE_INFINITY);
		if (maxStep == 0) { return false; }
		beta = Pro5_limjoaqu.getDouble("Enter Armijo beta (0 to cancel): ",0,1);
		if (beta == 0) { return false; }
		tau =Pro5_limjoaqu.getDouble("Enter Armijo tau (0 to cancel): ",0,1);
		if (tau == 0) { return false; }
		K = Pro5_limjoaqu.getInteger("Enter Armijo K (0 to cancel): ",0,Integer.MAX_VALUE);
		if (K == 0) { return false; }
		
		success = super.getParamsUser(); //sets common parameters
		
		//sets Armijo parameters if not cancelled
		if (success == true) {
			this.setMaxStep(maxStep);
			this.setBeta(beta);
			this.setTau(tau);
			this.setK(K);
			return true;
		} else {
			return false;
		}
	}
	public void print () { // print parameters
		super.print();
		System.out.println("Armijo maximum step size: " + maxStep);
		System.out.println("Armijo beta: " + beta);
		System.out.println("Armijo tau: " + tau);
		System.out.println("Armijo maximum iterations: " + K);
		System.out.println();
	}
}
