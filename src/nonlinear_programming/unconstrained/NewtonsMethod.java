package nonlinear_programming.unconstrained;

import java.util.Arrays;

import matrix.LUFactorization;
import matrix.Matrix;
import nonlinear_programming.function.TwiceDifferentiableFunction;
import nonlinear_programming.line_search.Backtracking;

public class NewtonsMethod {
	private static final double EPS = 1e-10;
	private int n;
	private TwiceDifferentiableFunction F;
	
	public NewtonsMethod(TwiceDifferentiableFunction f) {
		F = f;
		n = f.dimension();
	}
	
	public double solve(double[] solution, double[] initial) {
		double[] x = Arrays.copyOf(initial, initial.length);
		while (true) {
			double[] gradient = F.gradient(x);
			
			boolean isZero = true;
			for (int i=0; i<n; i++) {
				if (Math.abs(gradient[i]) > EPS) {
					isZero = false;
				}
				gradient[i] *= -1; 
			}
			if (isZero) {
				break;
			}
			
			double[][] hessian = F.hessian(x);
			
			LUFactorization lu = new LUFactorization(new Matrix(hessian));
			double[] d = lu.solve(gradient);
			
			Backtracking bt = new Backtracking(F, x, d);
			x = bt.getBest();
			
		}
		for (int i=0; i<n; i++) {
			solution[i] = x[i];
		}
		return F.f(x);
	}
}
