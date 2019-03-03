package com.oblac.intro;

import javax.swing.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class Intro extends JFrame {

	public static void main(String[] args) {
		new Thread() {
			{
				setDaemon(true);
				start();
			}

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(Integer.MAX_VALUE);
					} catch (Throwable t) {
					}
				}
			}
		};
		java.awt.EventQueue.invokeLater(() -> {
			try {
				Intro intro = new Intro();
				intro.setVisible(true);
				intro.start(1 / 60f, 5);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		});
	}

	public Intro() {
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setSize(1920, 1080);

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		});
	}

	// ---------------------------------------------------------------- game

	float localTime = 0f;

	public final void start(final float fixedTimeStep, final int maxSubSteps) {
		this.createBufferStrategy(2);
		final Graphics2D g = (Graphics2D) this.getGraphics();
		init(g);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		new Thread() {
			{
				setDaemon(true);
			}

			@Override
			public void run() {
				long start = System.nanoTime();
				while (true) {
					long now = System.nanoTime();
					float elapsed = (now - start) / 1000000000f;
					start = now;
					internalUpdateWithFixedTimeStep(elapsed, maxSubSteps, fixedTimeStep);
					internalUpdateGraphicsInterpolated();
					if (1000000000 * fixedTimeStep - (System.nanoTime() - start) > 1000000) {
						try {
							Thread.sleep(0, 999999);
						} catch (InterruptedException ex) {
						}
					}
				}
			}
		}.start();

		System.out.println("width:" + this.getWidth());
		System.out.println("started...");
	}

	/**
	 * Updates game state if possible and sets localTime for interpolation.
	 */
	private void internalUpdateWithFixedTimeStep(float elapsedSeconds, int maxSubSteps, float fixedTimeStep) {
		int numSubSteps = 0;
		if (maxSubSteps != 0) {
			// fixed timestep with interpolation
			localTime += elapsedSeconds;
			if (localTime >= fixedTimeStep) {
				numSubSteps = (int) (localTime / fixedTimeStep);
				localTime -= numSubSteps * fixedTimeStep;
			}
		}
		if (numSubSteps != 0) {
			// clamp the number of substeps, to prevent simulation grinding spiralling down to a halt
			int clampedSubSteps = (numSubSteps > maxSubSteps) ? maxSubSteps : numSubSteps;
			for (int i = 0; i < clampedSubSteps; i++) {
				update(fixedTimeStep);
			}
		}
	}

	/**
	 * Calls render with Graphics2D context and takes care of double buffering.
	 */
	private void internalUpdateGraphicsInterpolated() {
		BufferStrategy bf = this.getBufferStrategy();
		Graphics2D g = null;
		try {
			g = (Graphics2D) bf.getDrawGraphics();
			render(g, localTime);
		} finally {
			g.dispose();
		}
		// shows the contents of the backbuffer on the screen.
		bf.show();

		// tell the System to do the drawing now, otherwise it can take a few extra ms until
		// drawing is done which looks very jerky
		Toolkit.getDefaultToolkit().sync();
	}

	private Chaos chaos;

	/**
	 * Init Game (override/replace)
	 */
	protected void init(Graphics2D g) {
		chaos = new Chaos(this, this.getWidth(), this.getHeight());
		chaos.init(g);
	}

	/**
	 * Update game. Elapsed time is fixed.
	 */
	protected void update(float elapsedTime) {
		chaos.update(elapsedTime);
	}

	/**
	 * Render the game.
	 *
	 * @param interpolationTime time of the rendering within a fixed timestep (in seconds)
	 */
	protected void render(Graphics2D g, float interpolationTime) {
		g.setBackground(HsColors.BLACK);
		g.clearRect(0, 0, getWidth(), getHeight());
		chaos.render(g);
	}


}
