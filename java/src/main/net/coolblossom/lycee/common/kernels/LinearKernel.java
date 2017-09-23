package net.coolblossom.lycee.common.kernels;

/**
 * 内積計算するカーネル（線形カーネル）
 * 
 * @author ryouka0122@github
 *
 */
public class LinearKernel implements Kernel {

	@Override
	public double calc(double[] x1, double[] x2) {
		double res = 0.0;
		for (int i = 0; i < x1.length; i++) {
			res += x1[i] * x2[i];
		}
		return res;
	}

}
