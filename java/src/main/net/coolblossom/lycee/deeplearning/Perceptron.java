package net.coolblossom.lycee.deeplearning;

import java.util.List;

import net.coolblossom.lycee.common.DataSet;

/**
 * パーセプトロンモデル（3層の単純ニューラルネットワークのモデル）
 * @author ryouka0122@github
 *
 */
public class Perceptron extends AbstractNeuralNetwork<double[], Double, DataSet> {

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
	public Double predict(double[] data) {
		return forward(data);
	}

	/**
	 * バックプロパゲーションによる学習
	 * @param dataSetList 教師データ群
	 */
	@Override
	public void train(List<DataSet> dataSetList) {

		double err;
		int cnt = 0;
		do {
			err = 0.0;
			for(DataSet ds : dataSetList) {

				/** calculate forward */
				Double result = forward(ds.x);

				double diff = ds.y - result;

				/** refine using back propagation */
				refineNetwork(ds.x, hiddenOutput, result, diff);

				/** calculate error value */
				err += diff * diff;
			}
			cnt++;
			// 誤差のチェック
			if(err<permit) break;
		}while(cnt<maxEpoch);
	}

	/**
	 * 順伝播処理
	 * @param x 入力値
	 * @return 出力層の出力値
	 */
	private Double forward(double[] x) {
		for(int i=0 ; i<this.hiddenNodeSize ; i++) {
			hiddenOutput[i] = this.hidden[i].ignite(x);
		}
		return this.output.ignite(hiddenOutput);
	}

	/**
	 * ネットワークの重みパラメータ調整
	 * @param ds 観測データと期待値
	 * @param result 算出値
	 */
	private void refineNetwork(double[] inputData, double[] hi, double result, double error) {
		// Back Propagation
		/** refine output layer */
		double[] delta = refineOutputLayer(hi, result, error);

		/** refine hidden layer */
		refineHiddenLayer(inputData, hi, delta);
	}

	/**
	 * 出力層の調整
	 * @param dataset 観測データ
	 * @param result 出力値
	 * @param error 誤差値
	 * @return 隠れ層に送る誤差値
	 */
	private double[] refineOutputLayer(double[] input, double result, double error) {
		return this.output.refine(input, result, error);
	}

	/**
	 * 隠れ層の調整
	 * @param dataset 観測データ
	 * @param result 出力値
	 * @param error 出力層から受け取った誤差値
	 * @return
	 */
	private double[][] refineHiddenLayer(double[] input, double[] output, double[] error) {
		double[][] deltaAry = new double[hiddenNodeSize][inputNodeSize];
		for(int j=0 ; j<this.hiddenNodeSize ; j++) {
			deltaAry[j] = this.hidden[j].refine(input, output[j], error[j]);
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
			neuron[i].refine(input, output[i], diff[i]);
		}
	}

}
