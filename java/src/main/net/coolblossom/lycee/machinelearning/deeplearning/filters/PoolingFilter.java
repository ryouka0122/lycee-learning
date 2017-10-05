package net.coolblossom.lycee.machinelearning.deeplearning.filters;

import java.util.function.BiFunction;

/**
 * プーリングフィルタ
 * @author ryouka0122@github
 *
 */
public class PoolingFilter implements CnnFilter {

	/** X方向サイズ */
	protected int width;

	/** Y方向サイズ */
	protected int height;

	/** プーリングロジック */
	protected BiFunction<Double, Double, Double> function;

	/**
	 * コンストラクタ
	 * @param width
	 * @param height
	 */
	public PoolingFilter(int width, BiFunction<Double, Double, Double> function) {
		this(width, width, function);
	}

	/**
	 * コンストラクタ
	 * @param width
	 * @param height
	 */
	public PoolingFilter(int width, int height, BiFunction<Double, Double, Double> function) {
		this.width = width;
		this.height = height;
		this.function = function;
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
				result = function.apply(result, data[xStart + i][yStart + j]);
			}
		}
		return result;
	}

}
