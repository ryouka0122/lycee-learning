package net.coolblossom.lycee.machinelearning.boltzmannmachine;

import java.util.Set;
import java.util.stream.IntStream;

import net.coolblossom.lycee.utils.CalcUtil;

/**
 * 制限ボルツマンマシン
 * @author ryouka0122@github
 *
 */
public class RestrictedBoltzmannMachine implements BoltzmannMachine {

	/** 学習率(learning rate) */
	double lr;

	/** 可視層ノード数 */
	int visibleNodeSize;

	/** 隠れ層ノード数 */
	int hiddenNodeSize;

	/** 重み */
	double[][] weight;

	/** 可視層バイアス */
	double[] visibleBias;

	/** 隠れ層バイアス */
	double[] hiddenBias;


	public RestrictedBoltzmannMachine(int vSize, int hSize) {
		this(vSize, hSize, 1.0);
	}

	public RestrictedBoltzmannMachine(int vSize, int hSize, double lr) {
		this.visibleNodeSize =  vSize;
		this.hiddenNodeSize =  hSize;
		this.lr = lr;

		this.weight = new double[hSize][vSize];
		this.visibleBias = new double[vSize];
		this.hiddenBias = new double[hSize];
	}

	@Override
	public double[] predict(int[] input) {
		double[] h = new double[hiddenNodeSize];
		for(int j=0 ; j<hiddenNodeSize ; j++) {
			h[j] = propup(input, j, hiddenBias[j]);
		}

		double[] result = new double[visibleNodeSize];

		for(int i=0 ; i<visibleNodeSize ; i++) {
			double x = visibleBias[i];
			for(int j=0 ; j<hiddenNodeSize ; j++) {
				x += weight[j][i] * h[j];
			}
			result[i] = CalcUtil.sigmoid(x);
		}

		return result;
	}

	@Override
	public void learn(Set<RBMDataSet> dataSet) {
		int N = dataSet.size();
		dataSet.forEach(ds -> {
			this.contrastive_divergence(N, ds.x, 1);
		});
		;
	}


	class CDkDataSet {
		public double[] mean;
		public int[] sample;

		public CDkDataSet(int size) {
			this.allocate(size);
		}

		public void allocate(int size) {
			mean = new double[size];
			sample = new int[size];
		}

	}

	/**
	 * CD-k法による最適化
	 * @param N データ数
	 * @param input 観測データ
	 * @param k CD-k法のk値
	 */
	private void contrastive_divergence(int N, int[] input, int k) {
		double K = this.lr / N;		// 事前計算

		// 各種データ
		CDkDataSet prev_hidden, next_visible, next_hidden;

		prev_hidden = sample_h_given_v(input);

		//CD-k法のギブズサンプリング
		CDkDataSet p = prev_hidden;
		// TODO ループの意味がわかるまでコメントアウト
		//for(int i=0 ; i<k ; i++) {
			next_visible = sample_v_given_h(p.sample);
			next_hidden = sample_h_given_v(next_visible.sample);
		//	p = next_hidden;
		//}

		// 各パラメータの更新処理
		for(int j=0 ; j<hiddenNodeSize ; j++) {
			for(int i=0 ; i<visibleNodeSize ; i++) {
				weight[j][i] += K * (prev_hidden.mean[j] * input[i] - next_hidden.mean[j] * next_visible.sample[i]);
			}
			hiddenBias[j] += K * (prev_hidden.sample[j] - next_hidden.mean[j]);
		}
		for(int i=0 ; i<visibleNodeSize ; i++) {
			visibleBias[i] += K * (input[i] - next_visible.sample[i]);
		}

	}

	/**
	 * サンプリング（可視層条件付き）
	 * @param input
	 * @return
	 */
	private CDkDataSet sample_h_given_v(int[] input) {
		CDkDataSet hidden = new CDkDataSet(this.hiddenNodeSize);
		IntStream.range(0, hiddenNodeSize)
		.forEach( j -> {
			hidden.mean[j] = propup(input, j, hiddenBias[j]);
			hidden.sample[j] = CalcUtil.binomial(1, hidden.mean[j]);
		});
		return hidden;
	}

	/**
	 * サンプリング（隠れ層条件付き）
	 * @param input
	 * @return
	 */
	private CDkDataSet sample_v_given_h(int[] input) {
		CDkDataSet visible = new CDkDataSet(this.visibleNodeSize);
		IntStream.range(0, visibleNodeSize)
		.forEach( i -> {
			visible.mean[i] = propdown(input, i, visibleBias[i]);
			visible.sample[i] = CalcUtil.binomial(1, visible.mean[i]);
		});
		return visible;
	}

	/**
	 * 隠れ層ノードの出力計算
	 * @param input 可視層ノードから届くデータ
	 * @param i 隠れ層ノードのインデックス
	 * @param bias 隠れ層ノードのバイアス
	 * @return 出力値
	 */
	private double propup(int[] input, int j, double bias) {
		double result = bias;
		for(int i=0 ; i < visibleNodeSize ; i++) {
			result += input[i]*weight[j][i];
		}
		return CalcUtil.sigmoid(result);
	}

	/**
	 * 可視層ノードの出力計算
	 * @param input 隠れ層から届くデータ
	 * @param i 可視層ノードのインデックス
	 * @param bias 可視層ノードのバイアス
	 * @return 出力値
	 */
	private double propdown(int[] input, int i, double bias) {
		double result = bias;
		for(int j=0 ; j < hiddenNodeSize ; j++) {
			result += input[j]*weight[j][i];
		}
		return CalcUtil.sigmoid(result);
	}





}
