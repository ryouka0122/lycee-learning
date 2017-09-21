package net.coolblossom.lycee.machinelearning.classification.online;

import net.coolblossom.lycee.machinelearning.classification.OnlineLearning;
import net.coolblossom.lycee.machinelearning.classification.loss.LossLogic;

/**
 * Possive-Aggressive Algorithm
 * @author ryouka0122@github
 *
 */
public class PossiveAggressiveAlgorithm extends OnlineLearning {

	// PAのバリエーション
	private PAType type;

	// 正則化用外部パラメータ
	private double C;

	// 損失ロジック（ヒンジ損失）
	private LossLogic loss;

	/**
	 * コンストラクタ
	 * @param dimension 観測データ項数（定数項含む）
	 * @param type PAアルゴリズムの種類
	 * @param C 正則化項の重みパラメータ
	 */
	public PossiveAggressiveAlgorithm(int dimension, PAType type, double C) {
		super(dimension);
		this.type = type;
		this.C = C;
		this.loss = LossLogic.HingeLoss;
	}

	@Override
	public void refine(int correctLabel, double[] data) {
		// 損失の計算
		double lossValue = loss.calc(parameters, correctLabel, data);
		if(lossValue==0.0) return;

		// 更新率の計算
		double K = type.calc(C, lossValue, data);

		// 更新
		for(int i=0 ; i<parameters.length ; i++) {
			parameters[i] += K * correctLabel * data[i];
		}
	}

}
