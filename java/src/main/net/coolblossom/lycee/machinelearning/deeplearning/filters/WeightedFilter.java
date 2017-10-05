package net.coolblossom.lycee.machinelearning.deeplearning.filters;

import java.util.function.Supplier;

/**
 * CNN用2次元フィルタ
 * @author ryouka0122@github
 *
 */
public class WeightedFilter implements CnnFilter {

	/** 重み */
	public double[][] weight;

	/** X方向サイズ */
	public int width;

	/** Y方向サイズ */
	public int height;

	/**
	 * コンストラクタ
	 * @param wKernel サイズ
	 */
	public WeightedFilter(int wKernel) {
		this(wKernel, wKernel);
	}

	/**
	 * コンストラクタ
	 * @param wKernel 横方向サイズ
	 * @param hKernel 縦方向サイズ
	 */
	public WeightedFilter(int wKernel, int hKernel) {
		this.width = wKernel;
		this.height = hKernel;
		this.weight = new double[wKernel][hKernel];
	}

	/**
	 * コンストラクタ
	 * @param wKernel 横方向サイズ
	 * @param hKernel 縦方向サイズ
	 */
	public WeightedFilter(int wKernel, int hKernel, Supplier<Double> initializer) {
		this.width = wKernel;
		this.height = hKernel;
		this.weight = new double[wKernel][hKernel];
		for(int j=0 ; j<width ; j++) {
			for(int i=0 ; i<height ; i++) {
				weight[j][i] = initializer.get();
			}
		}
	}

	/**
	 * 畳み込み計算
	 * @param data データ
	 * @param xStart データのX方向開始位置
	 * @param yStart データのY方向開始位置
	 * @return 算出値
	 */
	@Override
	public double calc(double data[][], int xStart, int yStart) {
		double result = 0.0;
		for(int j=0 ; j<height ; j++) {
			for(int i=0 ; i<width ; i++) {
				result += weight[i][j] * data[xStart + i][yStart + j];
			}
		}
		return result;
	}

}
