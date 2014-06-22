package linear_programming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import linear_programming.exception.InfeasibleException;
import linear_programming.exception.UnboundException;
import matrix.LUFactorization;
import matrix.Matrix;

public class Simplex {
	int num = 0;
	private static final double EPS = 1e-10;
	
	private int n;
	private int m;
	
	private double[] c;
	private double[][] A;
	private double[] b;
	
	/**
	 * Linear Programming
	 * minimize c'x
	 * s.t. Ax = b, x>=0
	 */
	public Simplex(double[] c, double[][] A, double[] b) {
		n = A[0].length;
		m = A.length;
		
		this.c = Arrays.copyOf(c, n);
		this.A = new double[m][];
		for (int i=0; i<m; i++) {
			this.A[i] = Arrays.copyOf(A[i], n);
		}
		this.b = Arrays.copyOf(b, m);
	}
	
	/**
	 * Solve the linear programming
	 * @param solution output optimal solution
	 * @return optimal value
	 * @throws Exception 
	 */
	public double solve(double[] solution) throws UnboundException, InfeasibleException {		
		Solution initial = feasibleBasicSolution();
		Solution sol = solveFrom(initial);
		
		for (int i=0; i<n; i++) {
			solution[i] = sol.variable[i];
		}
		double ret = 0;
		for (int i=0; i<n; i++) {
			ret += c[i] * solution[i];
		}
		return ret;
	}
	
	private Solution feasibleBasicSolution() throws UnboundException, InfeasibleException {
		double[] cc = new double[n+m];
		for (int i=0; i<m; i++) {
			cc[n+i] = 1;
		}
		double[] bb = Arrays.copyOf(b, m);
		double[][] AA = new double[m][n+m];
		for (int i=0; i<m; i++) {
			int r = 1;
			if (bb[i] < 0) {				
				r = -1;
				bb[i] = -bb[i];
			}
			for (int j=0; j<n+m; j++) {
				if (j<n) {
					AA[i][j] = A[i][j] * r;
				} else if (j-n == i) {
					AA[i][j] = 1;
				}
			}
		}
		
		Simplex ln = new Simplex(cc, AA, bb);
		
		Solution initial = new Solution(n+m, m);
		for (int i=n; i<n+m; i++) {
			initial.basis.add(i);
		}
		initial.B = Matrix.getSubMatrix(AA, initial.basis);
		for (int i=0; i<m; i++) {
			initial.variable[i+n] = bb[i];
		}
		
		Solution sol = ln.solveFrom(initial);
		
		double value = 0.0;
		for (int i=0; i<n+m; i++) {
			value += sol.variable[i] * cc[i];
		}
		if (Math.abs(value) > EPS) {
			throw new InfeasibleException();
		}
		return sol;
	}
	
	private Solution solveFrom(Solution initial) throws UnboundException {
		
		LUFactorization lu = new LUFactorization(initial.B);
		double[] c_b = new double[m];
		int k=0;
		for (int i : initial.basis) {
			c_b[k++] = c[i];
		}
		double[] pi = lu.solveTranspose(c_b);		
		
		k = -1;
		double[] a = new double[m];
		for (int column=0; column<n; column++) {
			if (initial.basis.contains(column)) {
				continue;
			}
			double rc = c[column];
			for (int row=0; row<m; row++) {
				a[row] = A[row][column];
				rc -= pi[row] * a[row];
			}
			if (rc < -EPS) {
				k = column;
				break;
			}
		}

		if (k == -1) {
			return initial;
		}
				
		double[] y = lu.solve(a);
		double delta = Double.MAX_VALUE; 
		int nb = -1;
		for (int i=0; i<m; i++) {
			if (y[i] > EPS) {
				int index = initial.basis.get(i);
				double t = initial.variable[index]/y[i];
				if (t < delta) {
					delta = t;
					nb = index;
				}
			}
		}
		if (nb == -1) {
			throw new UnboundException("unbound");
		}
		
		initial.variable[k] = delta;
		for (int i=0; i<m; i++) {
			int index = initial.basis.get(i);
			initial.variable[index] -= delta*y[i];
		}
		
		initial.basis.remove(Integer.valueOf(nb));
		initial.basis.add(k);
		Collections.sort(initial.basis);
		initial.B = Matrix.getSubMatrix(A, initial.basis);
		
		return solveFrom(initial);
	}
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(n+" "+m+"\n");
		buff.append("c="+Arrays.toString(c)+"\n");
		buff.append("A=\n");
		for (int i=0; i<m; i++) {
			buff.append(Arrays.toString(A[i])+"\n");
		}
		buff.append("b="+Arrays.toString(b));
		return buff.toString();
	}
	
	private class Solution {
		List<Integer> basis;
		Matrix B;
		double[] variable;
		
		private Solution(int n, int m) {
			basis = new ArrayList<Integer>(m);
			B = new Matrix(m,m);
			variable = new double[n];
		}
		
		@Override
		public String toString() {
			StringBuffer buff = new StringBuffer();
			buff.append("basis="+basis+"\n");
			buff.append("B=\n");
			buff.append(B);
			buff.append("variable="+Arrays.toString(variable)+"\n");
			return buff.toString();
		}
		
	}
}
