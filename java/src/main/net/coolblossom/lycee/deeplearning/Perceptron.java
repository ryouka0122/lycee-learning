package net.coolblossom.lycee.deeplearning;

import java.util.List;

import net.coolblossom.lycee.common.DataSet;
import net.coolblossom.lycee.utils.RandomUtil;

/**
 * パーセプトロンモデル（3層の単純ニューラルネットワークのモデル）
 * @author ryouka0122@github
 *
 */
public class Perceptron extends NeuralNetwork<DataSet> {

	/** 入力層ノード数 */
	int inputNodeSize;

	/** 中間層ノード数 */
	int hiddenNodeSize;

	/** 出力層ノード数 */
	int outputNodeSize;

	/** 中間層 */
	Neuron[] hidden;

	/** 中間層の出力値*/
	double[] hiddenOutput;

	/** 出力層 */
	Neuron output;


	/**
	 * コンストラクタ
	 * @param inpuｔ 入力層ノード数
	 * @param hidden 中間層ノード数
	 * @param output 出力層ノード数
	 */
	public Perceptron(int input, int hidden, int output) {
		super(10.0, 0.001, Integer.MAX_VALUE);

		// 各層のノード数
		this.inputNodeSize = input;
		this.hiddenNodeSize = hidden;
		this.outputNodeSize = output;

		// 隠れ層の初期化
		this.hidden = new Neuron[hidden];
		for(int i=0 ; i<hidden ; i++) {
			this.hidden[i] = new Neuron(input, this.lr, () -> 0.0);
		}
		this.hiddenOutput = new double[hidden];

		// 出力層の初期化
		this.output = new Neuron(hidden, this.lr, () -> 0.0);

	}

	@Override
	public double[] predict(double[] data) {
		return new double[] { forward(data) };
	}

	/**
	 * バックプロパゲーションによる学習
	 * @param dataSetList 教師データ群
	 */
	@Override
	public void train(List<DataSet> dataSetList) {
		// 重みパラメータの初期化
		initWeight(weightHidden);
		initWeight(weightOutput);

		double err;
		int cnt = 0;
		do {
			err = 0.0;
			for(DataSet ds : dataSetList) { // train
				/** calculate forward */
				double result = forward(ds.x);

				// =================================================
				// back propagation
				//
				/** refine output layer */
				double[] delta = refineOutputLayer(ds, result, new double[]{ds.y-result});

				/** refine hidden layer */
				delta = refineHiddenLayer(ds, result, delta);

				/** calculate error value */
				err += (result - ds.y) * (result - ds.y);
			}
			cnt++;
		}while(cnt!=maxEpoch && err>permit);
	}

	/**
	 * 順伝播処理
	 * @param x 入力値
	 * @return 出力層の出力値
	 */
	private double forward(double[] x) {
		for(int i=0 ; i<this.hiddenNodeSize ; i++) {
			hiddenOutput[i] = this.hidden[i].ignite(x);
		}
		return this.output.ignite(hiddenOutput);
	}

	private double[] refineOutputLayer(DataSet dataset, double result, double[] error) {
		double diff = 0.0;
		for(int i=0 ; i<error.length ; i++) {
			diff += error[i] * result * (1.0 - result);
		}
		return this.output.refine(hiddenOutput, diff);
	}

	private double[][] refineHiddenLayer(DataSet dataset, double result, double[] error) {
		double[][] deltaAry = new double[hiddenNodeSize][inputNodeSize];
		for(int j=0 ; j<this.hiddenNodeSize ; j++) {
			double diff = error[j] * hiddenOutput[j] * (1.0 - hiddenOutput[j]);
			double[] delta = this.hidden[j].refine(dataset.x, diff);
		}
		return deltaAry;
	}

	/**
	 * 処理層の更新
	 * @param neuron その層を表現するニューロン群
	 * @param input 入力値
	 * @param output 出力値
	 * @param expected 期待値
	 */
	private void refineLayer(Neuron[] neuron, double[] input, double[] output, double[] diff) {
		for(int i=0 ; i<neuron.length ; i++) {
			double z = diff[i] * output[i] * (1.0 - output[i]);
			neuron[i].refine(input, z);;
		}
	}

	private void initWeight(double[][] w) {
		for(int j=0 ; j<w.length ; j++) {
			initWeight(w[j]);
		}
	}

	private void initWeight(double[] w) {
		for(int i=0 ; i<w.length ; i++) {
			w[i] = RandomUtil.random();
		}
	}

}
