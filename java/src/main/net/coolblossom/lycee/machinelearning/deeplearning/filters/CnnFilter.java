package net.coolblossom.lycee.machinelearning.deeplearning.filters;

/**
 * CNN用フィルタインタフェイス
 * @author ryouka0122@github
 *
 */
public interface CnnFilter {

	/**
	 * 畳み込み計算
	 * @param data データ
	 * @param xStart データのX方向開始位置
	 * @param yStart データのY方向開始位置
	 * @return 算出値
	 */
	double calc(double data[][], int xStart, int yStart);

}
