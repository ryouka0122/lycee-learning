package net.coolblossom.lycee.machinelearning.classification.online;

import net.coolblossom.lycee.utils.CalcUtil;

public abstract class OnlineAlgorithm {

	protected int dimension;

	protected double[] parameters;

	/**
	 * コンストラクタ
	 * @param dimension 観測データ数
	 */
	public OnlineAlgorithm(int dimension) {
		this.dimension = dimension;
		parameters = new double [1+dimension];
		for(int i=0 ; i<parameters.length ; i++) {
			parameters[i] = 0.0;
		}
	}

	/**
	 * 重みパラメータの取得
	 * @return
	 */
	public double[] getParameters() {
		return this.parameters;
	}

	/**
	 * 予測
	 * @param data 観測データ
	 * @return 予測値（ラベル）
	 */
	public int predict(double[] data) {
		// 観測データと重みパラメータの内積  y = <w, x>
		double result = CalcUtil.innerProduct(parameters, data);
		return sign(result);
	}

	/**
	 * 更新
	 * @param correctLabel 正しいラベル
	 * @param data 観測データ
	 */
	abstract public void refine(int correctLabel, double[] data);


	/**
	 * 符号化メソッド
	 * @param v
	 * @return
	 */
	private int sign(double v) {
		return v>=0 ? 1 : -1;
	}
}
