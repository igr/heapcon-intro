package com.oblac.intro;

import javax.swing.*;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SLetter {

	private final String letter;
	private final Font hsFont;
	private final boolean isE;
	private final boolean isA;
	private final boolean isP;
	private final boolean isO;
	private final List<SBulb> points;
	private float width = 0;
	private float height = 0;
	private float x = 0;
	private float minY;
	private float maxY;

	public SLetter(String letter, Font hsFont) {
		this.letter = letter;
		this.hsFont = hsFont;
		this.isE = letter.equalsIgnoreCase("e");
		this.isA = letter.equalsIgnoreCase("a");
		this.isP = letter.equalsIgnoreCase("p");
		this.isO = letter.equalsIgnoreCase("o");
		this.points = new ArrayList<>();
	}

	public void sample(JFrame jframe) {
		GlyphVector gv = hsFont.createGlyphVector(jframe.getFontMetrics(hsFont).getFontRenderContext(), letter);
		Shape hsShape = gv.getOutline();

		// font points
		PathIterator pi = new FlatteningPathIterator(hsShape.getPathIterator(null), 1);

		// sample points
		while (!pi.isDone()) {
			double[] coordinates = new double[6];
			pi.currentSegment(coordinates);
			SBulb sbulb = new SBulb(new SPoint((float) coordinates[0], (float) coordinates[1]), HsColors.BEIGE);

			if (!sbulb.getCenter().isZero()) {
				points.add(sbulb);
			}

			pi.next();
		}

		// sample lines
		points.add(new SBulb(points.get(0)));

		final List<SBulb> newArray = new ArrayList<>();
		for (int j = 0; j < points.size() - 1; j++) {
			SBulb hsPoint1 = points.get(j);
			SBulb hsPoint2 = points.get(j + 1);

			List<SPoint> linePoints = sampleLine(hsPoint1.getCenter(), hsPoint2.getCenter());

			if (isE) {
				System.out.println(j + " " + hsPoint1.getCenter().distanceTo(hsPoint2.getCenter()));
				if (j == 49 || j == 67) {
				//if (j == 50 || j == 68) {     // ORIGINAL, BIG resolution
					continue;
				}
			}
			if (isA) {
//				System.out.println(j + " " + hsPoint1.getCenter().distanceTo(hsPoint2.getCenter()));
				if (j == 54 || j == 73) {
					continue;
				}
			}
			if (isP) {
//				System.out.println(j + " " + hsPoint1.getCenter().distanceTo(hsPoint2.getCenter()));
				if (j == 29 || j == 62) {
					continue;
				}
			}
			if (isO) {
				if (j == 32 || j == 65) {
					continue;
				}
			}
			for (SPoint linePoint : linePoints) {
				newArray.add(new SBulb(linePoint, HsColors.BEIGE));
			}
		}

		points.addAll(newArray);

		// find max points

		minY = 0;
		maxY = 0;

		for (SBulb sBulb : points) {
			if (sBulb.getCenter().getX() > width) {
				width = sBulb.getCenter().getX();
			}
			if (sBulb.getCenter().getY() > maxY) {
				maxY = sBulb.getCenter().getY();
			}
			if (sBulb.getCenter().getY() < minY) {
				minY = sBulb.getCenter().getY();
			}
		}
		height = maxY - minY;
	}

	public float getWidth() {
		return width;
	}

	private static List<SPoint> sampleLine(SPoint start, SPoint end) {
		double distanceX = end.getX() - start.getX();
		double distanceY = end.getY() - start.getY();

		int numberOfSamples = (int) Math.sqrt(distanceX*distanceX + distanceY*distanceY) / 10;

		double deltaX = distanceX / numberOfSamples;
		double deltaY = distanceY / numberOfSamples;

		ArrayList<SPoint> samples = new ArrayList<>();
		samples.add(start);

		double x = start.getX();
		double y = start.getY();

		for (int i = 1; i <= numberOfSamples; i++) {
			x += deltaX;
			y += deltaY;
			samples.add(new SPoint((float)x, (float)y));
		}

		return samples;
	}

	public void draw(Graphics2D g) {
		for (SBulb sBulb : points) {
			sBulb.draw(g);
		}
	}

	public void translateTo(float x, float y) {
		for (SBulb sBulb : points) {
			sBulb.moveCenterBy(x, y);
		}
		this.x += x;
		this.minY += y;
		this.maxY += y;
	}

	public void forEachPoint(final Consumer<SBulb> pointConsumer) {
		points.forEach(pointConsumer);
	}

	public void hide() {
		for (SBulb sBulb : points) {
			sBulb.hide();
		}
	}

	public SLetter show() {
		for (SBulb sBulb : points) {
			sBulb.show(5);
		}
		return this;
	}

	public boolean containsVector(SVector vector) {
		SPoint p = vector.to;

		if (p.getX() < this.x) {
			return false;
		}
		if (p.getX() > this.x + width) {
			return false;
		}
		if (p.getY() < this.minY) {
			return false;

		}
		if (p.getY() > this.maxY) {
			return false;
		}

		return true;
	}
}
