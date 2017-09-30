package net.coolblossom.lycee.arithmetics.functions.base;

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
