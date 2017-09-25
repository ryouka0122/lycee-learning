package net.coolblossom.lycee.deeplearning;

import net.coolblossom.lycee.utils.CalcUtil;

/**
 * ニューロンモデル
 * @author ryouka0122@github
 *
 */
public class Neuron {

	public double calc(int N, double[] input, double[] weight, double bias) {
		double result = 0.0;
		for(int i=0 ; i<N ; i++) {
			result += input[i] * weight[i];
		}
		return CalcUtil.sigmoid(result + bias);
	}


}
