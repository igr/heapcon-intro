package com.oblac.intro;

public class SPoint {
	private float x;
	private float y;

	public SPoint(float x, float y) {
		this((int)x, (int)y);
	}
	public SPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public SPoint(SPoint point) {
		this.x = point.getX();
		this.y = point.getY();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void moveBy(float dx, float dy) {
		x += dx;
		y += dy;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns distance to other point.
	 */
	public float distanceTo(SPoint other) {
		float dx = other.x - this.x;
		float dy = other.y - this.y;

		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	@Override
	public String toString() {
		return "SPoint{" +
			"x=" + x +
			", y=" + y +
			'}';
	}

	public boolean isZero() {
		return x == 0 && y == 0;
	}
}
