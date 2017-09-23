package net.coolblossom.lycee.machinelearning.classification;

import java.util.ArrayList;
import java.util.List;

import net.coolblossom.lycee.common.DataSet;
import net.coolblossom.lycee.machinelearning.classification.scale.DataSetRescaler;
import net.coolblossom.lycee.machinelearning.classification.scale.DefaultRescaler;

/**
 * バッチ学習
 *
 * @author ryouka0122@github
 *
 */
public abstract class BatchLearning extends AbstractClassifier {

	/** バッチ学習で使用するデータセットのリスト */
	private List<DataSet> dataset;

	/** リスケーラー */
	private DataSetRescaler dataSetRescaler;

	public BatchLearning(int dataDimension) {
		super(dataDimension);
		this.dataset = new ArrayList<DataSet>();
		this.dataSetRescaler = new DefaultRescaler();
	}

	public BatchLearning(int dataDimension, DataSetRescaler rescaler) {
		super(dataDimension);
		this.dataset = new ArrayList<DataSet>();
		this.dataSetRescaler = rescaler;
	}

	/**
	 * データの追加
	 *
	 * @param dataSet
	 *            データセット
	 */
	public void add(DataSet dataSet) {
		this.dataset.add(dataSet);
	}

	/**
	 * データの追加（y,x1,x2の順番）
	 *
	 * @param y
	 *            ラベル
	 * @param x1
	 *            観測データ1つ目
	 * @param x2
	 *            観測データ2つ目
	 */
	public void add(double y, double x1, double x2) {
		this.dataset.add(new DataSet(y, 1.0, x1, x2));
	}

	/**
	 * データセットのリストのバラ付きを抑制するためにリスケールしたものを取得するメソッド
	 *
	 * @return リスケールしたあとのデータセットのリスト
	 */
	public List<DataSet> getDataSetList() {
		return dataSetRescaler.rescale(dataset);
	}

	/**
	 * 解析
	 */
	abstract public void analyze();

}
