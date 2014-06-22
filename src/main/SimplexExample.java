package main;

import java.util.Arrays;

import linear_programming.Simplex;

public class SimplexExample {

	public static void main(String[] args) {
		
		double[] c = {-2.0, -1.0};
		double[][] A = {
				{3.0, 1.0},
				{1.0, 2.0}
		};
		double[] b = {12, 8};
		
		Simplex lp = new Simplex(c, A, b);
		double[] solution = new double[c.length];
		try {
			double value = lp.solve(solution);
			System.out.println(Arrays.toString(solution)+" "+value);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
