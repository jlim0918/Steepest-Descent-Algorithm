public class SDGSS extends SteepestDescent {
	private final double _PHI_ = (1. + Math . sqrt (5) ) /2.;
	private double maxStep ; // GSS max step size
	private double minStep ; // GSS min step parameter
	private double delta ; // GSS delta parameter
	
	// constructors
	public SDGSS () {
		super();
		this.maxStep = 1.0;
		this.minStep = 0.001;
		this.delta = 0.001;
	}
	public SDGSS ( double maxStep , double minStep , double delta ) {
		super();
		this.maxStep = maxStep;
		this.minStep = minStep;
		this.delta = delta;
	}
	
	// getters
	public double getMaxStep () {
		return this.maxStep;
		}
	public double getMinStep () {
		return this.minStep; 
	}
	public double getDelta () {
		return this.delta;
	}
	
	// setters
	public void setMaxStep ( double a ) {
		this.maxStep = a;
	}
	public void setMinStep ( double a ) {
		this.minStep = a;
	}
	public void setDelta ( double a ) {
		this.delta = a;
	}
	
	// other methods
	public double lineSearch ( Polynomial P , double [] x ) { // step size from GSS
		double alpha;
		
		//sets initial endpoints
		double a = minStep;
		double b = maxStep;
		//calculates c
		double c = a + (b-a)/this._PHI_;
		//runs GSS for step size, alpha
		alpha = GSS(a, b, c, x, this.direction(P, x), P);
		return alpha;
	}
	public boolean getParamsUser () { // get algorithm parameters from user
		double maxStep,minStep,delta;
		boolean success;
		
		//collects GSS parameters
		System.out.println("Set parameters for SD with a golden section line search:");
		maxStep = Pro5_limjoaqu.getDouble("Enter GSS maximum step size (0 to cancel): ",0,Double.POSITIVE_INFINITY);
		if (maxStep == 0) { return false; }
		minStep = Pro5_limjoaqu.getDouble("Enter GSS minimum step size (0 to cancel): ",0,maxStep);
		if (minStep == 0) { return false; }
		delta = Pro5_limjoaqu.getDouble("Enter GSS delta (0 to cancel): ",0,Double.POSITIVE_INFINITY);
		if (delta == 0) { return false; }
		
		success = super.getParamsUser(); //sets common parameters
		
		//sets GSS parameters if not cancelled
		if (success == true) {
			this.setMaxStep(maxStep);
			this.setMinStep(minStep);
			this.setDelta(delta);
			return true;
		} else {
			return false;
		}
	}
	public void print () { // print parameters
		super.print();
		System.out.println("GSS maximum step size: " + maxStep);
		System.out.println("GSS minimum step size: " + minStep);
		System.out.println("GSS delta: " + delta);
		System.out.println();
	}
	private double GSS ( double a , double b , double c , double [] x , double [] dir , Polynomial P ) { //golden section line search
		double y1,y2;
		double[] y1Double = new double [x.length];
		double[] y2Double = new double [x.length];
		double[] aDouble = new double [x.length];
		double[] bDouble = new double [x.length];
		double[] cDouble = new double [x.length];

		y1 = a + (c-a)/this._PHI_; //calculates y1, used if left side larger
		y2 = b-(b-c)/this._PHI_; //calculates y2, used if right side larger
		
		//applies points to array as stepsizes
		for (int i = 0; i < x.length; i++) {
			aDouble[i] = x[i] + a*dir[i];
			bDouble[i] = x[i] + b*dir[i];
			cDouble[i] = x[i] + c*dir[i];
			y1Double[i] = x[i] + y1*dir[i];
			y2Double[i] = x[i] + y2*dir[i];
		}
		
		//f(c)>=f(a) or f(c)>=f(b), indicates no minimum or multiple minima (special considerations)
		if (P.f(cDouble) >= P.f(aDouble) || P.f(cDouble) >= P.f(bDouble)) {
			if (P.f(aDouble) >= P.f(bDouble)) {
				return maxStep; //f improves w/ largest step size
			} else {
				return minStep; //f worsens w/ largest step size
			}
		}
		
		//loops until diff. b/w endpoints <= delta
		while ((b-a) > delta) {
			//if left side larger than right
			if ((c-a) > (b-c)) {
				//f(y) < f(a) and f(y) < f(b)
				if (P.f(y1Double) < P.f(aDouble) && P.f(y1Double) < P.f(bDouble)) {
					GSS(a,c,y1,x,dir,P); //recursion with (a,y,c) triplet
				} else if (P.f(y1Double) > P.f(cDouble)) { //f(y) > f(c)
					GSS(y1,b,c,x,dir,P); //recursion with (y,c,b) triplet
				}
			//right side larger than left
			} else if ((c-a) < (b-c)) {
				//f(y) < f(a) and f(y) < f(b)
				if (P.f(y2Double) < P.f(aDouble) && P.f(y2Double) < P.f(bDouble)) {
					GSS(a,c,y2,x,dir,P); //recursion with (a,y,c) triplet
				} else if (P.f(y2Double) > P.f(cDouble)) { //f(y) > f(c)
					GSS(y2,b,c,x,dir,P); //recursion with (y,c,b) triplet
				}
			}
		}
		return (b-a)/2; //when endpoints diff. smaller than delta, return midpoint
	}

}
