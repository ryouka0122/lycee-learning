package net.coolblossom.lycee.machinelearning.classification;

/**
 * オンライン学習
 * 
 * @author ryouka0122@github
 *
 */
public abstract class OnlineLearning extends AbstractClassifier {

	/**
	 * コンストラクタ
	 * 
	 * @param dataDimension
	 *            データ次数（バイアス項含む）
	 */
	public OnlineLearning(int dataDimension) {
		super(dataDimension);
	}

	/**
	 * 更新
	 * 
	 * @param correctLabel
	 *            正しいラベル
	 * @param data
	 *            観測データ
	 */
	abstract public void refine(double correctLabel, double[] data);

}
