package net.coolblossom.lycee.common.functors.base;

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
