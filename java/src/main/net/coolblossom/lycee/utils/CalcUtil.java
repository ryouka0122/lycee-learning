package net.coolblossom.lycee.utils;

import java.util.Random;

public class CalcUtil {

	/**
	 * 内積計算
	 *
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	static public double innerProduct(double[] lhs, double[] rhs) {
		double result = 0.0;
		for (int i = 0; i < lhs.length; i++) {
			result += lhs[i] * rhs[i];
		}
		return result;
	}

	/**
	 * シグモイド関数
	 * @param x
	 * @return
	 */
	static public double sigmoid(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

	/**
	 * 二項分布
	 * @param n 試行回数
	 * @param p 境界値
	 * @return 境界値を超えた回数
	 */
	static public int binomial(int n, double p) {
		if(p<0.0 || p>1.0) {
			return 0;
		}
		return (int) new Random().ints()
				.limit(n)
				.mapToDouble(x -> Double.valueOf(x) / (1.0 + Integer.MAX_VALUE) )
				.filter(x-> x<p)
				.count();


	}


}
