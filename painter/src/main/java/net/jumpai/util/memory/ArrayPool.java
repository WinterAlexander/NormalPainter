package net.jumpai.util.memory;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * {@link Pool} of {@link Array} with parameters for array initialization
 * <p>
 * Created on 2019-10-28.
 *
 * @author Alexander Winter
 */
public class ArrayPool<T> extends Pool<Array<T>>
{
	private final boolean ordered;
	private final int initialArrayCapacity;

	public ArrayPool(boolean ordered, int initialArrayCapacity)
	{
		this.ordered = ordered;
		this.initialArrayCapacity = initialArrayCapacity;
	}

	public ArrayPool(boolean ordered,
	                 int initialArrayCapacity,
	                 int initialPoolCapacity,
	                 int max)
	{
		super(initialPoolCapacity, max);
		this.ordered = ordered;
		this.initialArrayCapacity = initialArrayCapacity;
	}

	@Override
	protected Array<T> newObject()
	{
		return new Array<>(ordered, initialArrayCapacity);
	}
}
