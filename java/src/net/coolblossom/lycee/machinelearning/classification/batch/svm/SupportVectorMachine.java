package net.coolblossom.lycee.machinelearning.classification.batch.svm;

import net.coolblossom.lycee.machinelearning.classification.BatchLearning;

/**
 * サポートベクターマシン(SVM)
 * @author ryouka0122@github
 *
 */
public class SupportVectorMachine extends BatchLearning {

	/** サポートベクターマシンで使用する最適化アルゴリズム */
	private SVMOptimizer optimizer;

	/**
	 * コンストラクタ
	 * @param optimizer 最適化アルゴリズム
	 */
	public SupportVectorMachine(SVMOptimizer optimizer) {
		super(optimizer.getDimension());
		this.optimizer = optimizer;
	}

	@Override
	public void analyze() {
		// オプティマイザで最適化されたパラメータを受け取る
		this.parameters = optimizer.optimize(this.dataset);
		return;
	}

}
