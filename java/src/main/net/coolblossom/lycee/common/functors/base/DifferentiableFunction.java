package net.coolblossom.lycee.common.functors.base;

/**
 * 微分可能インタフェイス
 *
 * @author ryouka0122@github
 *
 * @param <T>
 * @param <R>
 */
public interface DifferentiableFunction<T, R> extends Functor<T, R> {

	Functor<T, R> differantiate();

}
