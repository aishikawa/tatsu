package nonlinear_programming.function;

public interface TwiceDifferentiableFunction extends DifferentiableFunction{
	abstract double[][] hessian(double[] x);
}
