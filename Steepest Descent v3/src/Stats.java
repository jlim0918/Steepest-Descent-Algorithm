
public class Stats {

	public Stats () {}

	public static double Average (double[] x) { //calculates average of all elements in array of doubles
		double sum = 0;
		int count = x.length; //counts number of elements in array
		
		//adds up all elements of array
		for (int i = 0; i < x.length; i++) {
			sum += x[i]; 
		}
		
		return (sum/count); //average = sum of elements/# elements
	}
	
	public static double standardDeviation (double average, double[] x, int n) { //calculates standard deviation of elements in array
		double sqDevSum = 0;
		
		//adds up (x_i - x_avg)^2 for all elements of array
		for (int i = 0; i < x.length; i++) {
			sqDevSum += Math.pow((x[i] - average),2);
		}
		return Math.sqrt(sqDevSum/(n-1)); //st dev = sqrt(sigma((x_i-x_avg)^2)/(n-1))
	}
	public static double Minimum (double [] x) { //finds minimum value in array
		double min = Double.POSITIVE_INFINITY; //sets maximum possible value as initial
	
		//loops through all elements array and replaces min when next value is lower
		for (int i = 0; i < x.length; i++) {
			if (x[i] < min) {
				min = x[i];
			}
		}
		return min;
	}
	public static double Maximum (double [] x) { //finds maximum value in array
		double max = Double.NEGATIVE_INFINITY; //sets minimum possible value as initial
		
		//loops through all elements in array and replaces max when next value is higher
		for (int i = 0; i < x.length; i++) {
			if (x[i] > max) {
				max = x[i];
			}
		}
		return max;
	}
}
