package main;

import java.util.Arrays;

import nonlinear_programming.function.TwiceDifferentiableFunction;
import nonlinear_programming.unconstrained.NewtonsMethod;

public class NewtonsMethodExample {

	public static void main(String[] args) {
		final int dimension = 2;
		NewtonsMethod newton = new NewtonsMethod(new TwiceDifferentiableFunction() {
			@Override
			public int dimension() {
				return dimension;
			}

			@Override
			public double f(double[] x) {
				double a = x[0] - 1;
				double b = x[1] - 3;
				return a*a + 3*b*b;
			}

			@Override
			public double[] gradient(double[] x) {
				double[] ret = new double[dimension];
				ret[0] = 2*(x[0]-1); 
				ret[1] = 6*(x[1]-3);
				return ret;
			}

			@Override
			public double[][] hessian(double[] x) {
				double[][] ret = new double[dimension][dimension];
				ret[0][0] = 2;
				ret[1][0] = ret[0][1] = 0;
				ret[1][1] = 6;
				return ret;
			}			
		});
		
		double[] initial = new double[dimension];
		initial[0] = 0.0;
		initial[1] = 0.0;
		
		double[] solution = new double[dimension];
		double minimumValue = newton.solve(solution, initial);
		System.out.println(minimumValue);
		System.out.println(Arrays.toString(solution));
	}

}
