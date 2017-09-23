package net.coolblossom;

import net.coolblossom.lycee.machinelearning.classification.batch.svm.SequentialMinimalOptimization;
import net.coolblossom.lycee.machinelearning.classification.batch.svm.SupportVectorMachine;
import net.coolblossom.lycee.machinelearning.classification.scale.StandardizedRescaler;

public class SampleSVM_SMO extends SampleProgram {

	/**
	 * SMO(Sequential Minimal Optimization)による学習（SVM）
	 * @param softMarginPermit ソフトマージン
	 * @param permit 終了条件の許容誤差
	 */
	void SVM_SMO(double softMarginPermit, double permit) {
		System.out.println("* * * * SupportVectorMachine SMO(softMargin/Permit="+softMarginPermit+"/permit"+permit+") * * * *");
		classify_batch(
				new SupportVectorMachine(
						new SequentialMinimalOptimization(3, softMarginPermit, permit)
						, new StandardizedRescaler()	// 取り扱うデータを正規化
				)
		);
	}

	@Override
	void execute() {
		// [バッチ学習] SMOによる分類器
		SVM_SMO(1000, 1.0e-2);
		SVM_SMO( 100, 1.0e-2);
		SVM_SMO(  10, 1.0e-2);
		SVM_SMO(   1, 1.0e-2);
	}

}
