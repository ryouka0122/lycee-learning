package net.coolblossom.lycee.arithmetics.functions.base;

/**
 * 演算可能オブジェクト
 *
 * @author ryouka0122@github
 *
 * @param <T>
 * @param <R>
 */
public interface Calculator<T, R> {

	R calc(T x);

}
