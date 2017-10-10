package net.coolblossom.lycee.machinelearning.boltzmannmachine;

/**
 * RBM用データセット
 *
 * @author ryouka0122@github
 *
 */
public class RBMDataSet {

	/** ラベル */
	public double y;

	/** 観測データ */
	public int[] x;

	/**
	 * コンストラクタ
	 *
	 * @param y
	 *            ラベル
	 * @param data
	 *            バイアスを含む観測データ
	 */
	public RBMDataSet(double y, int... data) {
		this.y = y;
		this.x = data;
	}
}
