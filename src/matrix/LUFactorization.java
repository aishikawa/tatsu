package matrix;


public class LUFactorization {
	private static final double EPS = 1e-10;
	
	private int n;

	private Matrix LU;
	private int[] P;
	
	/**
	 * factorize A as P*L*U
	 * @param A
	 */
	public LUFactorization(Matrix A) {
		if (A.numRow != A.numCol) {
			throw new IllegalArgumentException("matrix is not square");
		}
		n = A.numRow;
		LU = new Matrix(A);
		P = new int[n];
		for (int i=0; i<n; i++) {
			P[i] = i;
		}
		
		factorize();
	}
	
	private void factorize() {
		for (int d=0; d<n; d++) {
			pivot(d);
			if (LU.value[d][d] < EPS) {
				throw new IllegalArgumentException("The matrix is singular");
			}
			for (int r=d+1; r<n; r++) {
				LU.value[r][d] /= LU.value[d][d];
				for (int c=d+1; c<n; c++) {
					LU.value[r][c] -= LU.value[r][d]*LU.value[d][c];
				}
			}
		}
	}
	
	private void pivot(int d) {
		int pivot = d;
		for (int r=d; r<n; r++) {
			if (Math.abs(LU.value[pivot][d]) < Math.abs(LU.value[r][d])) {
				pivot = r;
			}
		}
		double[] temp = LU.value[d];
		LU.value[d] = LU.value[pivot];
		LU.value[pivot] = temp;
		
		int t = P[d];
		P[d] = P[pivot];
		P[pivot] = t;
	}
	
	
	/**
	 * return a unit lower triangular matrix L
	 * @return a unit lower triangular matrix L
	 */
	public Matrix getL() {
		Matrix ret = new Matrix(n, n);
		for (int i=0; i<n; i++) {
			ret.value[i][i] = 1;
			for (int j=0; j<i; j++) {
				ret.value[i][j] = LU.value[i][j];
			}
		}
		return ret;
	}
	
	/**
	 * return a upper triangular matrix U
	 * @return a upper triangular matrix U
	 */
	public Matrix getU() {
		Matrix ret = new Matrix(n, n);
		for (int i=0; i<n; i++) {
			for (int j=i; j<n; j++) {
				ret.value[i][j] = LU.value[i][j];
			}
		}
		return ret;
	}
	
	public Matrix getP() {
		Matrix ret = new Matrix(n, n);
		for (int i=0; i<n; i++) {
			ret.value[P[i]][i] = 1;
		}
		return ret;
	}
	
	/**
	 * solve a linear system A*x = b
	 * @param b 
	 * @return solution x
	 */
	public double[] solve(double[] b) {
		if (b.length != n) {
			throw new IllegalArgumentException("size mismatch");
		}
		
		double[] z = new double[n];
		for (int i=0; i<n; i++) {
			z[i] = b[P[i]];
			for (int k=0; k<i; k++) {
				z[i] -= LU.value[i][k] * z[k];
			}
		}
		
		double[] x = new double[n];
		for (int i=n-1; i>=0; i--) {
			x[i] = z[i];
			for (int k=i+1; k<n; k++) {
				x[i] -= LU.value[i][k] * x[k];
			}
			x[i] /= LU.value[i][i];
		}
		return x;
	}
	
	/**
	 * solve a linear system A^t*x = b;
	 * @param b
	 * @return solution x
	 */
	public double[] solveTranspose(double[] b) {
		if (b.length != n) {
			throw new IllegalArgumentException("size mismatch");
		}
		
		double[] z = new double[n];
		for (int i=0; i<n; i++) {
			z[i] = b[i];
			for (int k=0; k<i; k++) {
				z[i] -= LU.value[k][i] * z[k];
			}
			z[i] /= LU.value[i][i];
		}
		
		double[] x = new double[n];
		double[] ret = new double[n];
		for (int i=n-1; i>=0; i--) {
			x[i] = z[i];
			for (int k=i+1; k<n; k++) {
				x[i] -= LU.value[k][i] * x[k];
			}
			ret[P[i]] = x[i];
		}
		
		return ret;
	}
}
