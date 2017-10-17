package net.coolblossom.lycee.machinelearning.boltzmannmachine;

import java.util.Set;

/**
 * ボルツマンマシンインタフェイス
 * @author ryouka0122@github
 *
 */
public interface BoltzmannMachine {

	/**
	 * 予測
	 * @param input 入力データ（観測データ）
	 * @return 予測値
	 */
	double[] predict(int[] input);

	/**
	 * 学習
	 * @param dataSet 教師データ群
	 */
	void learn(Set<RBMDataSet> dataSet);


}
