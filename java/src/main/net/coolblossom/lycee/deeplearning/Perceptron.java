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

	/** 中間層の重みパラメータ */
	double[][] weightHidden;

	/** 中間層のバイアス */
	double[] biasHidden;

	/** 出力層の重みパラメータ */
	double[] weightOutput;

	/** 出力層のバイアス */
	double biasOutput;

	/** 中間層の出力値*/
	double[] hiddenOutput;

	/**
	 * コンストラクタ
	 * @param inpuｔ 入力層ノード数
	 * @param hidden 中間層ノード数
	 * @param output 出力層ノード数
	 */
	public Perceptron(int input, int hidden, int output) {
		super(10.0, 0.001, Integer.MAX_VALUE);

		this.inputNodeSize = input;
		this.hiddenNodeSize = hidden;
		this.outputNodeSize = output;

		this.weightHidden = new double[hidden][input];
		this.biasHidden = new double[hidden];
		this.weightOutput = new double[hidden];
		this.biasOutput = 0.0;
		this.hiddenOutput = new double[hidden];

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
				refineOutputLayer(ds, result);

				/** refine hidden layer */
				refineHiddenLayer(ds, result);

				/** calculate error value */
				err += (result - ds.y) * (result - ds.y);
			}
			cnt++;
		}while(cnt!=maxEpoch && err>permit);
	}

	private double forward(double[] x) {
		Neuron neuron = new Neuron();
		for(int i=0 ; i<this.hiddenNodeSize ; i++) {
			hiddenOutput[i] = neuron.calc(inputNodeSize, x, weightHidden[i], biasHidden[i]);
		}
		return neuron.calc(hiddenNodeSize, hiddenOutput, weightOutput, biasOutput);
	}

	private void refineOutputLayer(DataSet dataset, double result) {
		double diff = (dataset.y-result) * result * (1.0 - result);
		double K = this.lr * diff;
		for(int i=0 ; i<this.hiddenNodeSize ; i++) {
			this.weightOutput[i] += K * hiddenOutput[i];
		}
		this.biasOutput += K;
	}

	private void refineHiddenLayer(DataSet dataset, double result) {
		double z = (dataset.y - result) * result * (1.0 - result);
		for(int j=0 ; j<this.hiddenNodeSize ; j++) {
			double diff = hiddenOutput[j] * (1.0 - hiddenOutput[j]) * weightOutput[j] * z;
			double K = this.lr * diff;
			for(int i=0; i<this.inputNodeSize ; i++) {
				weightHidden[j][i] += K * dataset.y;
			}
			biasHidden[j] += K;
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
