package nonlinear_programming.function;

public interface Function {
	/**
	 * @return dimension of argument vector
	 */
	abstract int dimension();
	abstract double f(double[] x);
}
