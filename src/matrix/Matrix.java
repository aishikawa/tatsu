package matrix;

import java.util.Arrays;
import java.util.List;

public class Matrix {
	int numRow;
	int numCol;
	double[][] value;

	/**
	 * m x n zero matrix
	 * @param m row
	 * @param n column
	 */
	public Matrix(int m, int n) {
		numRow = m;
		numCol = n;
		value = new double[numRow][numCol];
	}
	
	/**
	 * column vector
	 * @param v
	 */
	public Matrix(double[] v) {
		numRow = v.length;
		numCol = 1;
		value = new double[numRow][numCol];
		
		for (int i=0; i<numRow; i++) {
			value[i][0] = v[i];
		}
	}
	
	public Matrix(double[][] v) {
		numRow = v.length;
		numCol = v[0].length;
		value = new double[numRow][numCol];
		
		for (int r=0; r<numRow; r++) {
			value[r] = Arrays.copyOf(v[r], numCol);
		}
	}
	

	/**
	 * copy constructor
	 * @param A
	 */
	public Matrix(Matrix A) {
		this(A.numRow, A.numCol);
		for (int r=0; r<numRow; r++) {
			for (int c=0; c<numCol; c++) {
				value[r][c] = A.value[r][c];
			}
		}
	}
	
	public static Matrix getSubMatrix(double[][] A, List<Integer> column) {
		Matrix ret = new Matrix(A.length, column.size());
		int k=0;
		for (int c : column) {
			for (int r=0; r<ret.numRow; r++) {
				ret.value[r][k] = A[r][c];
			}
			k++;
		}
		return ret;
	}
	
	/**
	 * A.add(B) returns A+B (not update A)
	 * @param B matrix
	 * @return A+B
	 * @throws IllegalArgumentException then size of A and B are not equal
	 */
	public Matrix add(Matrix B) throws IllegalArgumentException{
		if (numRow != B.numRow || numCol != B.numCol) {
			throw new IllegalArgumentException("size mismatch");
		}
		Matrix ret = new Matrix(numRow, numCol);
		for (int i=0; i<numRow; i++) {
			for (int j=0; j<numCol; j++) {
				ret.value[i][j] = value[i][j] + B.value[i][j];
			}
		}
		return ret;
	}

	/**
	 * A.scale(a) returns a*A
	 * @param a scaler
	 * @return a*A
	 */
	public Matrix scale(double a) {
		Matrix ret = new Matrix(numRow, numCol);
		for (int i=0; i<numRow; i++) {
			for (int j=0; j<numCol; j++) {
				ret.value[i][j] = value[i][j] * a;
			}
		}
		return ret;
	}
	
	/**
	 * A.multiply(B) returns A*B (not update A)
	 * @param B matrix
	 * @return A*B
	 */
	public Matrix multiply(Matrix B) {
		if (this.numCol != B.numRow) {
			throw new IllegalArgumentException("size mismatch");
		}
		int newRow = this.numRow;
		int newCol = B.numCol;
		int K = this.numCol;
		Matrix ret = new Matrix(newRow, newCol);
		for (int i=0; i<newRow; i++) {
			for (int j=0; j<newCol; j++) {
				for (int k=0; k<K; k++) {
					ret.value[i][j] += this.value[i][k] * B.value[k][j];
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * A.transpose() returns a transposed matrix (not update A)
	 * @return transposed matrix
	 */
	public Matrix transpose() {
		Matrix ret = new Matrix(numCol, numRow);
		for (int i=0; i<numCol; i++) {
			for (int j=0; j<numRow; j++) {
				ret.value[i][j] = value[j][i];
			}
		}
		
		return ret;
	}
	
	/**
	 * return (r,c) component
	 * @param r row index
	 * @param c column index
	 * @return (r,c) component
	 */
	public double get(int r, int c) {
		return value[r][c];
	}
	
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<numRow; i++) {
			for (int j=0; j<numCol; j++) {
				buff.append(value[i][j]+" ");
			}
			buff.append("\n");
		}
		return buff.toString();
	}
}
