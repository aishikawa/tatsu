package nonlinear_programming.unconstrained;

import java.util.Arrays;

import matrix.LUFactorization;
import matrix.Matrix;
import nonlinear_programming.function.DifferentiableFunction;
import nonlinear_programming.line_search.Backtracking;

public class QuasiNewtonMethod {
	private static final double EPS = 1e-10;
	private int n;
	private DifferentiableFunction F;
	
	public QuasiNewtonMethod(DifferentiableFunction f) {
		F = f;
		n = f.dimension();
	}
	
	public double solve(double[] solution, double[] initial) {
		double[] x = Arrays.copyOf(initial, n);
		double[][] b = new double[n][n];
		for (int i=0; i<n; i++) {
			b[i][i] = 1; 
		}
		Matrix B = new Matrix(b);
		while (true) {
			double[] gradient = F.gradient(x);
			double[] minusGradient = new double[n];
			
			boolean isZero = true;
			for (int i=0; i<n; i++) {
				if (Math.abs(gradient[i]) > EPS) {
					isZero = false;
				}
				minusGradient[i] = -1 * gradient[i]; 
			}
			if (isZero) {
				break;
			}
			
			
			LUFactorization lu = new LUFactorization(B);
			double[] d = lu.solve(minusGradient);
			
			Backtracking bt = new Backtracking(F, x, d);
			double[] nx = bt.getBest();

			// update B by BFGS
			double[] ngradient = F.gradient(nx);
			double[] s = new double[n];
			double[] y = new double[n];
			for (int i=0; i<n; i++) {
				s[i] = nx[i] - x[i]; 
				y[i] = ngradient[i] - gradient[i];
			}
			Matrix S = new Matrix(s);
			Matrix Y = new Matrix(y);
			
			
			double beta = Y.transpose().multiply(S).get(0, 0);
			double gamma = S.transpose().multiply(B).multiply(S).get(0, 0);
			
			B = B.add(Y.multiply(Y.transpose()).scale(1/beta)).add(B.multiply(S).multiply(S.transpose()).multiply(B).scale(-1/gamma));
			
			x = nx;
			
		}
		for (int i=0; i<n; i++) {
			solution[i] = x[i];
		}
		return F.f(x);
	}
}
