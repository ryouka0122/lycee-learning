package net.coolblossom.lycee.utils;

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

	static public double sigmoid(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

}
