package net.coolblossom.lycee.utils;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class CollectionUtil {

	public static <T, U> void zip(
			Stream<T> lhs,
			Stream<U> rhs,
			BiConsumer<T, U> consumer
	) {
		Iterator<T> l = lhs.iterator();
		Iterator<U> r = rhs.iterator();
		while(l.hasNext())
			consumer.accept(l.next(), r.next());
	}

}
