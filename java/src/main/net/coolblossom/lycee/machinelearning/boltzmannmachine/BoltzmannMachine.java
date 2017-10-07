package net.coolblossom.lycee.machinelearning.boltzmannmachine;

/**
 * ボルツマンマシンインタフェイス
 * @author ryouka0122@github
 *
 */
public interface BoltzmannMachine {

	double[] predict(double[] input);


	void refine(double[] input, double[] output, double[] expected);

}
