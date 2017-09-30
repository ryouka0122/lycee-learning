package net.coolblossom.lycee.arithmetics.functions;

import net.coolblossom.lycee.arithmetics.functions.base.DifferentiableFunction;
import net.coolblossom.lycee.arithmetics.functions.base.Functor;

/**
 * シグモイド関数オブジェクト
 * @author ryouka0122@github
 *
 */
public class Sigmoid implements DifferentiableFunction<Double, Double> {
	class DifferantiatedSigmoid implements DifferentiableFunction<Double, Double> {

		@Override
		public Double calc(Double x) {
			return x * (1.0 - x);
		}

		@Override
		public Functor<Double, Double> differantiate() {
			return null;
		}

	}

	@Override
	public Double calc(Double x) {
		return 1.0 /  (1.0 + Math.exp(-x));
	}

	@Override
	public Functor<Double, Double> differantiate() {
		return new DifferantiatedSigmoid();
	}

}
