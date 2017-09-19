package net.coolblossom.lycee.machinelearning.classification.kernels;

/**
 * SMOで使用するカーネル定義
 * @author ryouka0122@github
 *
 */
public interface Kernel {
	double calc(double[] x1, double[] x2);

}
