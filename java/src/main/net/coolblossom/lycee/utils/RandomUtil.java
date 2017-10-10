package net.coolblossom.lycee.utils;

import java.util.Random;

public class RandomUtil {

	private static Random rand = new Random(System.currentTimeMillis());

	synchronized private static double getRandomValue() {
		return rand.nextDouble();
	}

	/**
	 * [0.0, 1.0)の範囲の乱数を生成
	 * @return 乱数
	 */
	public static double random() {
		return getRandomValue();
	}
}
