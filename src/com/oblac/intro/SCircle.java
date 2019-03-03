package com.oblac.intro;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class SCircle {

	private SPoint center;
	private float r;
	private Color color;
	private float delta = 0;

	public SCircle(SPoint center, float r, Color color) {
		this.center = center;
		this.r = r;
		this.color = color;
	}

	public void draw(Graphics2D g) {
		float rr = r + delta;
		float rr2 = 2 * rr;
		Ellipse2D.Double circle = new Ellipse2D.Double(center.getX() - rr, center.getY() - rr, rr2, rr2);
		g.setColor(color);
		g.fill(circle);
	}

	public SPoint getCenter() {
		return center;
	}

	public void exsize(float delta) {
		this.delta = delta;
	}
}
