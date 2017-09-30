package net.coolblossom.lycee.deeplearning;

import java.util.List;

/**
 * ニューラルネットワーク系機械学習インタフェイス
 * @author ryouka0122@github
 *
 * @param <InputType>
 * @param <ResultType>
 * @param <DataSetType>
 */
public interface NeuralNetwork<InputType, ResultType, DataSetType> {

	/**
	 * 予測
	 * @param data 観測データ群
	 * @return 予測値
	 */
	ResultType predict(InputType data);

	/**
	 * 学習
	 * @param dataSetList 教師データ群
	 */
	void train(List<DataSetType> dataSetList);

}