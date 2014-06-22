package nonlinear_programming.line_search;

import nonlinear_programming.function.DifferentiableFunction;

public class Backtracking {
	private static double ALPHA = 0.3;
	private static double BETA = 0.8;
	
	private DifferentiableFunction F;
	private double[] best;
	
	public Backtracking(DifferentiableFunction f, double[] x, double[] direction) {
		F = f;
		search(x, direction);
	}
	
	private void search(double[] x0, double[] deltaX) {
		int n = x0.length;
		double[] gradient = F.gradient(x0);
		double katamuki = 0.0;
		for (int i=0; i<n; i++) {
			katamuki += gradient[i] * deltaX[i];
		}
		katamuki *= ALPHA;
		double fx0 = F.f(x0);
		
		double t = 1.0;
		best = new double[n];
		while(true) {
			for (int i=0; i<n; i++) {
				best[i] = x0[i] + t*deltaX[i];
			}
			if (F.f(best) <= fx0 + t*katamuki) {
				break;
			}
			t *= BETA;
		}		
	}
	
	public double[] getBest() {
		return best;
	}
}
