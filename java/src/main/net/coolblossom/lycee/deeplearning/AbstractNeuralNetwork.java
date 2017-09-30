package net.coolblossom.lycee.deeplearning;

/**
 * ニューラルネットワーク
 * @author ryouka0122@github
 *
 * @param <T> 観測データタイプ
 * @param <R> 出力タイプ
 * @param <D> 学習用データ・タイプ
 */
public abstract class AbstractNeuralNetwork<T, R, D>
	implements NeuralNetwork<T, R, D>
{

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
	public AbstractNeuralNetwork(double lr, double permit, int maxEpoch) {
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

	public void setTrainingParameters(double lr, double permit, int maxEpoch) {
		setLearningRate(lr);
		setPermit(permit);
		setMaxEpoch(maxEpoch);
	}

	/**
	 * ネットワークの重みパラメータ調整
	 * @param ds 観測データと期待値
	 * @param result 算出値
	 */
	//abstract protected void refineNetwork(D ds, R result);
}
