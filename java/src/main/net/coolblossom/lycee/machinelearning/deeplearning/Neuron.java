package net.coolblossom.lycee.machinelearning.deeplearning;

import java.util.function.Supplier;

import net.coolblossom.lycee.common.functors.Sigmoid;
import net.coolblossom.lycee.common.functors.base.DifferentiableFunction;
import net.coolblossom.lycee.common.functors.base.Functor;

/**
 * ニューロンモデル
 * @author ryouka0122@github
 *
 */
public class Neuron {

	/** 重みパラメータ */
	private double[] weight;

	/** バイアス */
	private double bias;

	/** 学習率 */
	private double lr;

	/** 活性化関数 */
	private DifferentiableFunction<Double, Double> activator;

	/** 活性化関数の微分後の関数（BackPropagation用） */
	private Functor<Double, Double> differrantiator;

	/**
	 * コンストラクタ
	 * @param inputNodeSize 入力ノード数
	 * @param lr 学習率
	 * @param initializer 初期化関数
	 */
	public Neuron(int inputNodeSize, double lr, Supplier<Double> initializer) {
		this.weight = new double[inputNodeSize];
		for(int i=0 ; i<inputNodeSize ; i++) {
			this.weight[i] = initializer.get();
		}
		this.bias = initializer.get();
		this.lr = lr;
		this.activator = new Sigmoid();
		this.differrantiator = this.activator.differantiate();
	}

	public Neuron(int inputNodeSize, double lr) {
		this(inputNodeSize, lr, () -> 0.0);
	}

	public double calc(int N, double[] input, double[] weight, double bias) {
		double result = 0.0;
		for(int i=0 ; i<N ; i++) {
			result += input[i] * weight[i];
		}
		return activator.calc(result + bias);
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
		return activator.calc(result);
	}

	/**
	 * 調整(Back Propagation用)
	 * @param input 入力値
	 * @param diff 誤差値(次の層から届く伝播値)
	 * @return 前層に伝播させる誤差値
	 */
	public double[] refine(double[] input, double result, double error) {
		double diff = error * differrantiator.calc(result);
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
