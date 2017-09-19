package net.coolblossom.lycee.machinelearning.classification.online;

import net.coolblossom.lycee.machinelearning.classification.kernels.InnerProduct;
import net.coolblossom.lycee.machinelearning.classification.kernels.Kernel;

/**
 * Possive-Aggressive Algorithmのタイプ
 * @author ryouka0122@github
 *
 */
public enum PAType {
	/** ハードマージン */
	PA {
		@Override
		public double calc(double C, double loss, double[] data) {
			return loss / kernel.calc(data,data);
		}
	},
	/** ソフトマージン(L1正則) */
	PA1 {
		@Override
		public double calc(double C, double loss, double[] data) {
			return Math.min(C, loss / kernel.calc(data,data));
		}
	},
	/** ソフトマージン(L2正則) */
	PA2 {
		@Override
		public double calc(double C, double loss, double[] data) {
			return loss / ( kernel.calc(data,data) + 1.0/(2.0*C) );
		}
	};
	abstract public double calc(double C, double loss, double[] data);

	Kernel kernel;
	private PAType() {
		this.kernel = new InnerProduct();
	}
}