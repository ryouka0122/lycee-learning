package net.coolblossom;

import net.coolblossom.lycee.machinelearning.classification.batch.svm.DualCoordinateDescent;
import net.coolblossom.lycee.machinelearning.classification.batch.svm.SupportVectorMachine;

/**
 * SVMのサンプルプログラム
 * @author ryouka0122@github
 *
 */
public class SampleSVM_CDM extends SampleProgram {

	/**
	 * Coordinate Descent法による学習（SVM）
	 * @param margin ソフトマージン
	 * @param permit 終了条件の許容誤差
	 * @param epoch エポック数
	 */
	void SVM_CDM(double margin, double permit, int epoch) {
		System.out.println("* * * * SupportVectorMachine CDM(C="+margin+"/permit="+permit+"/epoch="+epoch+") * * * *");
		classify_batch(
				new SupportVectorMachine(
						new DualCoordinateDescent(3, margin, permit, epoch)
				)
		);
	}

	/**
	 * Coordinate Descent法による学習（SVM）
	 * @param margin ソフトマージン
	 * @param permit 終了条件の許容誤差
	 */
	void SVM_CDM(double margin, double permit) {
		SVM_CDM(margin, permit, 1);
	}

	@Override
	void execute() {
		// [バッチ学習] 座標降下法によるSVM
		SVM_CDM(1.0, 1e-2, 2000);
		SVM_CDM(1.0, 1e-2, 2000);
		SVM_CDM(1.0, 1e-2, 2000);
		SVM_CDM(1.0, 1e-2, 2000);
		SVM_CDM(1.0, 1e-2, 1);
		SVM_CDM(1.0, 1e-2, 10);
		SVM_CDM(1.0, 1e-2, 100);
		SVM_CDM(1.0, 1e-2, 1000);
		//SVM_CDM(1.0, 0.1e-4, 1000);
	}
}
