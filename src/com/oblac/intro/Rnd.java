package com.oblac.intro;

import java.util.concurrent.ThreadLocalRandom;

public class Rnd {

	public static int nextInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
}
