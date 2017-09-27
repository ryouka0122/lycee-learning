package net.coolblossom.lycee.deeplearning;

import java.util.function.Supplier;

import net.coolblossom.lycee.utils.CalcUtil;

/**
 * ニューロンモデル
 * @author ryouka0122@github
 *
 */
public class Neuron {

	/** 重みパラメータ */
	double[] weight;

	/** バイアス */
	double bias;

	/** 学習率 */
	double lr;

	public Neuron(int inputNodeSize, double lr) {
		this(inputNodeSize, lr, () -> 0.0);
	}

	public Neuron(int inputNodeSize, double lr, Supplier<Double> initializer) {
		this.weight = new double[inputNodeSize];
		for(int i=0 ; i<inputNodeSize ; i++) {
			this.weight[i] = initializer.get();
		}
		this.bias = initializer.get();
		this.lr = lr;
	}

	public double calc(int N, double[] input, double[] weight, double bias) {
		double result = 0.0;
		for(int i=0 ; i<N ; i++) {
			result += input[i] * weight[i];
		}
		return CalcUtil.sigmoid(result + bias);
	}

	/**
	 * 発火
	 * @param input 入力値
	 * @return 出力値
	 */
	public double ignite(double[] input) {
		double result = this.bias;
		for(int i=0 ; i<this.weight.length ; i++) {
			result += input[i] * weight[i];
		}
		return CalcUtil.sigmoid(result);
	}

	/**
	 * 調整(Back Propagation用)
	 * @param input 入力値
	 * @param diff 誤差値(次の層から届く伝播値)
	 * @return 前層に伝播させる誤差値
	 */
	public double[] refine(double[] input, double diff) {
		double K = this.lr * diff;
		double[] delta = new double[weight.length];
		for(int i=0 ; i<weight.length ; i++) {
			this.weight[i] += K * input[i];
			delta[i] = this.weight[i] * diff;
		}
		this.bias += K;
		return delta;
	}
}
