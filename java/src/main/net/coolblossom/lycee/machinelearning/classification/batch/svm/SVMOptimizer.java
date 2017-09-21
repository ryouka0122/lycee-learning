package net.coolblossom.lycee.machinelearning.classification.batch.svm;

import java.util.List;

import net.coolblossom.lycee.machinelearning.classification.DataSet;

public abstract class SVMOptimizer {

	/** データ次数 */
	protected int dimension;

	/** ソフトマージンのレンジ */
	protected double softMarginPermit;

	/** 停止条件の許容誤差 */
	protected double permit;

	/**
	 * コンストラクタ
	 * @param dimension
	 * @param margin
	 * @param permit
	 */
	protected SVMOptimizer(int dimension, double margin, double permit) {
		this.dimension = dimension;
		this.softMarginPermit = margin;
		this.permit = permit;
	}

	public int getDimension() {
		return this.dimension;
	}

	public double getMargin() {
		return this.softMarginPermit;
	}

	public double getPermit() {
		return this.permit;
	}




	/**
	 * 最適化処理
	 * @param dataSetList 最適化に使用するデータセット
	 * @return 最適化後のパラメータ配列
	 */
	abstract public double[] optimize(List<DataSet> dataSetList);

}
