public class Polynomial {
	
	private int n; // no. of variables
	private int degree; // degree of polynomial
	private double[][] coefs;  // coefficients

	// constructors
	public Polynomial () {
		
	}
	public Polynomial ( int n , int degree , double [][] coefs ) {
			
		this.n = n;
		this.degree = degree;
		this.coefs = coefs;
		
	}

	// getters
	public int getN () {
		return this.n;
	}
	public int getDegree () {
		return this.degree;
	}
	public double [][] getCoefs () {
		return this.coefs;
	}

	// setters
	public void setN ( int a ) {
		this.n = a;
	}
	public void setDegree ( int a ) {
		this.degree = a;
	}
	public void setCoef ( int j , int d , double a ) {
		this.coefs[j][d] = a;
	}

	// other methods
	public void init () { // init member arrays to correct size
		coefs = new double[this.n][this.degree+1];
	}
	public double f ( double [] x ) { // calculate function value at point x
		double fSum = 0;
		
		//f(x) = c_(j,d)x^d_j + c_(j,d−1)x^d−1_j + . . . + c_(j,2)x^2_j + c_(j,1)x^j + c_(j,0)
		for (int j = 0;  j < this.n; j++) {
			for (int d = this.degree; d > -1; d--) {
				if (d > 0) {
					fSum += this.coefs[j][d]* Math.pow(x[j],d);
				} else if (d == 0) {
					fSum += this.coefs[j][d];
				}
			}
		}
		return fSum;
	}
	public double [] gradient ( double [] x ) { // calculate gradient at point x
		double fGrad[] = new double[x.length];
		
		//adds up terms of f'(x)
		for (int j = 0;  j < this.n; j++) {
			for (int d = this.degree; d > 0; d--) {
				if ((d-1) > 0) {
					fGrad[j] += this.coefs[j][d]* d * Math.pow(x[j],(d-1));
				} else if ((d-1) == 0) {
					fGrad[j] += this.coefs[j][d] * d;
				}
			}
		}
		return fGrad;
	}
	public double gradientNorm ( double [] x ) { // calculate norm of gradient at point x
		double gradTotal = 0;
		//gradient norm = sqrt(g1^2 + g2^2 +g3^2 + ...), where g1,g2,g3... are gradients of the corresponding x-values 
		for (int j = 0; j <this.n; j++) {
			gradTotal += Math.pow(gradient(x)[j],2);
		}
		return Math.sqrt(gradTotal);
	}
	public boolean isSet () { // indicate whether polynomial is set
		
		//polynomial is not set when there are 0 variables and maximum degree is 0
		if (this.n == 0 && this.degree == 0) {
			return false;
		} 
		return true;
		
	}
	public void print () { // print out the polynomial
		
		System.out.print("f(x) =");
		for (int j = 0;  j < this.n; j++) {
			
			if (j > 0) {
				System.out.print(" +");
			}
			System.out.print(" ( ");
			for (int d = this.degree; d > -1; d--) {
				if (d > 0) {
					System.out.printf("%.2fx%d^%d + ",this.coefs[j][d],(j+1),d );
				} else if (d == 0) {
					System.out.printf("%.2f",this.coefs[j][d]);
				}
			}
			System.out.print(" )");
		}
		System.out.println("\n");
	}

}
