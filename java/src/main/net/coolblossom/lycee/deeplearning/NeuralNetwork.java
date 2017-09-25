package net.coolblossom.lycee.deeplearning;

import java.util.List;

/**
 * ニューラルネットワーク
 * @author ryouka0122@github
 *
 */
public abstract class NeuralNetwork<TrainDataSet> {
	/** 学習率 */
	protected double lr;

	/** 収束許容値 */
	protected double permit;

	/** 最大エポック数 */
	protected int maxEpoch;

	/**
	 * コンストラクタ
	 * @param lr
	 * @param permit
	 * @param maxEpoch
	 */
	public NeuralNetwork(double lr, double permit, int maxEpoch) {
		this.lr = lr;
		this.permit = permit;
		this.maxEpoch = maxEpoch;
	}

	public void setLearningRate(double lr) {
		this.lr = lr;
	}

	public double getLearningRate() {
		return this.lr;
	}

	public void setPermit(double permit) {
		this.permit = permit;
	}

	public double getPermit() {
		return this.permit;
	}

	public void setMaxEpoch(int maxEpoch) {
		this.maxEpoch = maxEpoch;
	}

	public int getMaxEpoch() {
		return this.maxEpoch;
	}

	public void setTrainingParameter(double lr, double permit, int maxEpoch) {
		setLearningRate(lr);
		setPermit(permit);
		setMaxEpoch(maxEpoch);
	}

	/**
	 * 予測
	 * @param data 観測データ群
	 * @return 予測値
	 */
	abstract double[] predict(double[] data);

	/**
	 * 学習
	 * @param dataSetList 教師データ群
	 */
	abstract void train(List<TrainDataSet> dataSetList);


}
