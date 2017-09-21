package net.coolblossom.lycee.machinelearning.classification.kernels;

/**
 * SMOで使用するカーネル定義
 * @author ryouka0122@github
 *
 */
public interface Kernel {
	/**
	 * ２つのベクトルを元に計算して一位の値を返す
	 * @param x1 １つ目のデータ
	 * @param x2 ２つ目のデータ
	 * @return 計算結果
	 */
	double calc(double[] x1, double[] x2);

}
