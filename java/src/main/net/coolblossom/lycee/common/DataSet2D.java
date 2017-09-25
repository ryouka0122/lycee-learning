package net.coolblossom.lycee.common;

/**
 * 学習用データセット（2次元版）
 *
 * @author ryouka0122@github
 *
 */
public class DataSet2D {

	/** ラベル */
	public double y;

	/** 観測データ */
	public double[][] x;

	/** データサイズ */
	public int width;

	/**
	 * コンストラクタ
	 *
	 * @param y
	 *            ラベル
	 * @param data
	 *            観測データ
	 */
	public DataSet2D(double y, int w, double... data) {
		this.y = y;
		this.width = w;
		this.x = new double[w][w];

		for(int i=0 ; i<w ; i++) {
			for(int j=0 ; j<w ; j++) {
				this.x[i][j] = data[i*w+j];
			}
		}
	}
}
