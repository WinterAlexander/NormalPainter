package com.normalpainter.util.async;

import java.util.function.Consumer;

/**
 * Object that is used to wrap a callback object into a callback of another type
 * <p>
 * Created on 2017-10-26.
 *
 * @see GdxCallback#gdxWrapper
 * @author Alexander Winter
 */
@FunctionalInterface
public interface CallbackWrapper
{
	<T> Consumer<T> wrap(Consumer<T> consumer);
}
