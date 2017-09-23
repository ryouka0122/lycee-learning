package net.coolblossom.lycee.common;

/**
 * 学習用データセット
 * 
 * @author ryouka0122@github
 *
 */
public class DataSet {
	/** データ次元数(バイアス項を含む数) */
	static final int DATA_DIMENSION = 3;

	/** ラベル */
	public double y;

	/** 観測データ */
	public double[] x;

	/**
	 * コンストラクタ
	 * 
	 * @param y
	 *            ラベル
	 * @param data
	 *            バイアスを含む観測データ
	 */
	public DataSet(double y, double... data) {
		this.y = y;
		if (DATA_DIMENSION != data.length)
			throw new IllegalArgumentException();
		this.x = data;
	}
}
