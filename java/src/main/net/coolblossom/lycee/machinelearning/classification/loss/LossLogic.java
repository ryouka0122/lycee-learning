package net.coolblossom.lycee.machinelearning.classification.loss;

import net.coolblossom.lycee.common.DataSet;
import net.coolblossom.lycee.utils.CalcUtil;

/**
 * 損失ロジック
 * 
 * @author ryouka0122@github
 *
 */
public enum LossLogic {

	/** 2乗損失 */
	SquareLoss {
		@Override
		public double calc(double[] w, double y, double[] x) {
			double result = 1.0 - y * CalcUtil.innerProduct(w, x);
			return result * result;
		}
	},
	/** 0/1損失 */
	BinaryLoss {
		@Override
		public double calc(double[] w, double y, double[] x) {
			double result = y * CalcUtil.innerProduct(w, x);
			return result > 0 ? 0 : 1;
		}
	},
	/** ヒンジ損失 */
	HingeLoss {
		@Override
		public double calc(double[] w, double y, double[] x) {
			double result = 1.0 - y * CalcUtil.innerProduct(w, x);
			return result > 0.0 ? result : 0.0;
		}
	},
	/** 指数損失 */
	ExponentialLoss {
		@Override
		public double calc(double[] w, double y, double[] x) {
			double result = -1.0 * y * CalcUtil.innerProduct(w, x);
			return Math.exp(result);
		}
	},
	/** Logistic損失 */
	LogisticLoss {
		@Override
		public double calc(double[] w, double y, double[] x) {
			double result = Math.exp(-1.0 * y * CalcUtil.innerProduct(w, x));
			return Math.log(1.0 + result);
		}
	};

	/**
	 * 損失計算するメソッド
	 * 
	 * @param w
	 *            重み
	 * @param y
	 *            観測データ（ラベル）
	 * @param x
	 *            観測データ（入力値）
	 * @return 損失値
	 */
	public abstract double calc(double[] w, double y, double[] x);

	/**
	 * 損失計算するメソッド
	 * 
	 * @param w
	 *            重み
	 * @param dataSet
	 *            観測データ
	 * @return 損失値
	 */
	public double calc(double[] w, DataSet dataSet) {
		return calc(w, dataSet.y, dataSet.x);
	}

}
