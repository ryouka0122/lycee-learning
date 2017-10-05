package net.coolblossom.lycee.machinelearning.deeplearning.filters;

/**
 * 最大値プールフィルタ
 * @author ryouka0122@github
 *
 */
public class MaxPool extends PoolingFilter {

	/**
	 * コンストラクタ
	 * @param poolSize
	 */
	public MaxPool(int poolSize) {
		this(poolSize, poolSize);
	}

	/**
	 * コンストラクタ
	 * @param width
	 * @param height
	 */
	public MaxPool(int width, int height) {
		super(width, height, Math::max);
	}

}
