package nonlinear_programming.function;

public interface DifferentiableFunction extends Function {
	abstract double[] gradient(double[] x);
}
