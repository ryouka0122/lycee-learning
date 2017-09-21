package net.coolblossom.lycee.machinelearning.classification;

/**
 * 分類器インタフェイス
 * @author ryouka0122@github
 *
 */
public interface Classifier {

	/**
	 * 予測分類
	 * @param data 観測データ
	 * @return 予測値
	 */
	double predict(double[] data);

}
