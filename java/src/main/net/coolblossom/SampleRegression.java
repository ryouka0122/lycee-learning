package net.coolblossom;

import net.coolblossom.lycee.machinelearning.classification.batch.LinearRegression;
import net.coolblossom.lycee.machinelearning.classification.scale.ResizeRescaler;

public class SampleRegression extends SampleProgram {

	/**
	 * 線形回帰
	 */
	@Override
	void execute() {
		System.out.println("* * * * LinearRegression * * * *");
		classify_batch(
				new LinearRegression(
						new ResizeRescaler()
						)
		);
		System.out.println();
	}

}
