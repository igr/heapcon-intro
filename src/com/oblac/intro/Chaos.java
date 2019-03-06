package com.oblac.intro;

import javax.swing.*;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.InputStream;
import java.util.ArrayList;

public class Chaos {

	public static final String HEAPCON = "Heapcon";

// ORIGINAL VALUES USED FOR HEAPCON 2019
//	public static final int SLEEP_TIME = 20;
//	public static final int HALF_TIME = 400;
//	public static final int MAX_TIME = 420;
//	public static final int CHAOS_TIME = 30;
//	public static final int HALF_VECTORS = 800;
//	public static final int MAX_VECTORS = 15000;

	public static final int SLEEP_TIME = 3;
	public static final int HALF_TIME = 10;
	public static final int MAX_TIME = 20;
	public static final int CHAOS_TIME = 5;
	public static final int HALF_VECTORS = 800;
	public static final int MAX_VECTORS = 15000;

	public static final int MAX_LEN = 200;
	public static final float CLOSE_DISTANCE = 20;
	public static final float BULB_SIZE = 8;

	private int totalVectors = 0;

	private final JFrame panel;
	private final int width;
	private final int height;
	private final SVectorFactory svectorFactory;

	private Font hsFont;
	private SLetter[] letters;

	public Chaos(JFrame panel, int width, int height) {
		this.panel = panel;
		this.width = width;
		this.height = height;
		this.svectorFactory = new SVectorFactory(width, height);
	}


	/**
	 * Initialization.
	 */
	public void init(Graphics2D g) {
		// font
		InputStream fontStream = this.getClass().getResourceAsStream("/HSGrotesk.otf");
		hsFont = null;
		try {
			hsFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		// font size
		float fontSize = hsFont.getSize();
		float fontWidth = g.getFontMetrics(hsFont).stringWidth(HEAPCON);
		fontSize = ((float)width / fontWidth) * fontSize;
		hsFont = hsFont.deriveFont(Font.BOLD, fontSize * 1.5f);

		// font glyphs
		final String[] heapconLetters = new String[] {"H", "e", "a", "p", "c", "o" , "n"};
		this.letters = new SLetter[heapconLetters.length];

		for (int i = 0; i < heapconLetters.length; i++) {
			this.letters[i] = new SLetter(heapconLetters[i], hsFont);
			this.letters[i].sample(panel);
		}

		// letter positions
		float totalWidth = 0;
		for (SLetter letter : letters) {
			totalWidth += letter.getWidth();
		}

		float start = (width - totalWidth) / 2;

		for (SLetter letter : letters) {
			letter.translateTo(start, height / 2);
			start += letter.getWidth();
		}

	}

	// ---------------------------------------------------------------- game

	private ArrayList<SVector> vectors = new ArrayList<>();
	private int vectorsHidden;
	private float totalTime = 0;

	/**
	 * Update the chaos.
	 */
	public void update(float elapsedTime) {
		totalTime += elapsedTime;

		// TIMINGS
		if (totalTime < SLEEP_TIME) {
			return;
		} else if (totalTime < HALF_TIME) {
			float deltaTime = totalTime - SLEEP_TIME;
			totalVectors = (int) (HALF_VECTORS * deltaTime / (HALF_TIME - SLEEP_TIME));
		} else if (totalTime < MAX_TIME) {
			float deltaTime = totalTime - HALF_TIME;
			totalVectors = (int) ((MAX_VECTORS - HALF_VECTORS) * deltaTime / (MAX_TIME - HALF_TIME)) + HALF_VECTORS;
		} else if (totalTime > MAX_TIME + CHAOS_TIME) {
			//int deltaTime = (int) (totalTime - (MAX_TIME + CHAOS_TIME));
			int chunkSize = 200;
			while (chunkSize > 0 && vectorsHidden < totalVectors) {
				vectors.get(vectorsHidden).hide();
				vectorsHidden++;
				chunkSize--;
			}
		}

		// UPDATE
		final int currentNumberOfVectors = vectors.size();

		for (int i = currentNumberOfVectors; i < totalVectors; i++) {
			SVector svector = svectorFactory.createSVector();
			vectors.add(svector);
		}

		for (SVector vector : vectors) {
			vector.move(width, height);
		}

		for (SLetter letter : letters) {
			letter.hide();
		}

		for (SVector vector : vectors) {
			vector.normal();
			for (SLetter letter : letters) {
				if (letter.containsVector(vector)) {
					letter.forEachPoint((b) -> {
						if (b.isVisible()) {
							return;
						}
						float k = b.closeDistanceTo(vector, Chaos.CLOSE_DISTANCE);
						if (k > 0) {
							b.show(Chaos.BULB_SIZE);
							vector.hit(Chaos.BULB_SIZE);
						}
					});
				}
			}
		}
	}

	/**
	 * Render the chaos.
	 */
	public void render(Graphics2D g) {
		for (SVector vector : vectors) {
			vector.draw(g);
		}
		for (SLetter letter : letters) {
			letter.draw(g);
		}

//		letters[0].show().draw(g);  // H
//		letters[1].show().draw(g);  // e
//		letters[2].show().draw(g);  // a
//		letters[3].show().draw(g);  // p
//		letters[4].show().draw(g);  // c
//		letters[5].show().draw(g);  // o
//		letters[6].show().draw(g);  // n

//		g.setColor(Color.GREEN);
//		g.drawString("" + totalTime, 100, 100);

	}
}
