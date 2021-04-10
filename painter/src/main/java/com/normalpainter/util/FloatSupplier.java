package com.normalpainter.util;

import java.util.function.Supplier;

/**
 * Supplier specialized for floating point values
 * <p>
 * Created on 2018-05-30.
 *
 * @author Alexander Winter
 */
@FunctionalInterface
public interface FloatSupplier extends Supplier<Float>
{
	float getAsFloat();

	@Override
	default Float get() {
		return getAsFloat();
	}
}
