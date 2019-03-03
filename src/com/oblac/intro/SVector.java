package com.oblac.intro;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class SVector {

	private final int requiredLen;
	private final SCircle circle;
	private float angle;
	private final Color color;
	SPoint to;
	private SPoint from;
	private Stroke stroke;

	private boolean initDrawing;
	private boolean hidden;

	public SVector(SPoint startingPoint, int requiredLen, float angle, Color color) {
		this.from = startingPoint;
		this.to = new SPoint(startingPoint);
		this.requiredLen = requiredLen;
		this.angle = (float) Math.toRadians(angle);
		this.color = color;
		this.stroke = new BasicStroke(2);
		this.circle = new SCircle(this.to, Rnd.nextInt(6, 10), color);
		this.initDrawing = true;
		this.hidden = false;
	}

	public void move(int width, int height) {
		// calculate movement
		float speed = 4f;

		float dx = (float) (speed * Math.sin(this.angle));
		float dy = (float) (speed * Math.cos(this.angle));

		to.moveBy(dx, dy);

		if (initDrawing) {
			if (from.distanceTo(to) >= requiredLen) {
				initDrawing = false;
			}
		} else {
			from.moveBy(dx, dy);
		}

		if (to.getX() < -Chaos.MAX_LEN) {
			float deltaX = from.getX() - to.getX();
			to.setX(width);
			from.setX(width + deltaX);
		}
		else if (to.getX() > width + Chaos.MAX_LEN) {
			float deltaX = to.getX() - from.getX();
			to.setX(0);
			from.setX(-deltaX);
		}
		else if (to.getY() < -Chaos.MAX_LEN) {
			float deltaY = from.getY() - to.getY();
			to.setY(height);
			from.setY(height + deltaY);
		}
		else if (to.getY() > height + Chaos.MAX_LEN) {
			float deltaY = to.getY() - from.getY();
			to.setY(0);
			from.setY(-deltaY);
		}
	}

	public void draw(Graphics2D g) {
		if (hidden) {
			return;
		}
		g.setColor(color);
		g.setStroke(stroke);

		g.drawLine((int)from.getX(), (int)from.getY(), (int)to.getX(), (int)to.getY());

		circle.draw(g);
	}

	public void normal() {
		circle.exsize(0);
	}

	public void hit(float k) {
		circle.exsize(50 - k);
	}

	public void hide() {
		this.hidden = true;
	}
}
