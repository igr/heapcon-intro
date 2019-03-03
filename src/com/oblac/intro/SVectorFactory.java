package com.oblac.intro;

public class SVectorFactory {

	private final int width;
	private final int height;

	public SVectorFactory(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public SVector createSVector() {
		int x = Rnd.nextInt(1, width - 1);
		int y = Rnd.nextInt(1, height - 1);

		float angle = 0;
		int delta = 15;
		switch (Rnd.nextInt(1, 4)) {
			case 1: angle = Rnd.nextInt(55 - delta, 55 + delta); break;
			case 2: angle = Rnd.nextInt(125 - delta, 125 + delta); break;
			case 3: angle = Rnd.nextInt(235 - delta, 235 + delta); break;
			case 4: angle = Rnd.nextInt(305 - delta, 305 + delta); break;
		}

		return new SVector(
			new SPoint(x, y),
			Chaos.MAX_LEN,
			angle,
			Rnd.nextInt(1, 2) == 1 ? HsColors.RED : HsColors.BLUE
		);
	}
}
