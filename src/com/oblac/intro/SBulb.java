package com.oblac.intro;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class SBulb {
	private static final SquareRoot SQUARE_ROOT = new SquareRoot((int) Chaos.CLOSE_DISTANCE + 1);

	private SPoint center;
	private float r;
	private Color color;

	public SBulb(SPoint center, Color color) {
		this.center = new SPoint(center);
		this.r = 0;
		this.color = color;
	}

	public SBulb(SBulb sBulb) {
		this.center = new SPoint(sBulb.getCenter());
		this.color = sBulb.color;
	}

	public void draw(Graphics2D g) {
		if (r < 1) {
			return;
		}

		Ellipse2D.Double circle = new Ellipse2D.Double(center.getX() - r, center.getY() - r, 2 * r, 2 * r);
		g.setColor(color);
		g.fill(circle);
	}

	public SPoint getCenter() {
		return center;
	}

	public void moveCenterBy(float dx, float dy) {
		this.center.moveBy(dx, dy);
	}

	public void show(float d) {
		r = d;
	}

	public void hide() {
		r = 0;
	}

	public float closeDistanceTo(SVector vector, final float maxDistance) {
		SPoint p = vector.to;
		float dx = Math.abs(p.getX() - center.getX());
		float dy = Math.abs(p.getY() - center.getY());

		if (dx > maxDistance) {
			return 0;
		}
		if (dy > maxDistance) {
			return 0;
		}

		return maxDistance - SQUARE_ROOT.distance(dx, dy);
	}

	public boolean isVisible() {
		return r > 0;
	}
}