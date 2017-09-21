package net.coolblossom.lycee.machinelearning.classification;

import java.util.ArrayList;
import java.util.List;

/**
 * バッチ学習
 * @author ryouka0122@github
 *
 */
public abstract class BatchLearning extends AbstractClassifier {

	/** バッチ学習で使用するデータセットのリスト */
	protected List<DataSet> dataset;

	public BatchLearning(int dataDimension) {
		super(dataDimension);
		this.dataset = new ArrayList<DataSet>();
	}

	/**
	 * データの追加（y,x1,x2の順番）
	 * @param y ラベル
	 * @param x1 観測データ1つ目
	 * @param x2 観測データ2つ目
	 */
	public void add(int y, int x1, int x2) {
		this.dataset.add( new DataSet(y, 1.0, x1, x2) );
	}

	/**
	 * 解析
	 */
	abstract public void analyze();

}
