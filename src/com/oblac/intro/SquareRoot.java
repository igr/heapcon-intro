package com.oblac.intro;

public class SquareRoot {
	private int dimension;

	private float[][] table;

	public SquareRoot(int dimension) {
		this.dimension = dimension;

		table = new float[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				table[i][j] = (float) Math.sqrt(i * i + j * j);
			}
		}
	}

	public float distance(float dx, float dy) {
		if ((int) dx >= dimension || (int) dy >= dimension) {
			throw new IllegalArgumentException();
		}
		return table[(int) dx][(int) dy];
	}
}
