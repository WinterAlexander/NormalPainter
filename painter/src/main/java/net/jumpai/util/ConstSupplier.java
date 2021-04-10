package net.jumpai.util;

import java.util.function.Supplier;

/**
 * {@link Supplier} which always returns the same value
 * <p>
 * Created on 2020-11-30.
 *
 * @author Alexander Winter
 */
public class ConstSupplier<T> implements Supplier<T>
{
	private final T value;

	public ConstSupplier(T value)
	{
		this.value = value;
	}

	@Override
	public T get()
	{
		return value;
	}

	public static <T> ConstSupplier<T> asConst(T value)
	{
		return new ConstSupplier<>(value);
	}
}
