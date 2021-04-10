package net.jumpai.util.iterator;

import com.badlogic.gdx.utils.Array;

/**
 * Implementation of an IndexIterator using a LibGDX array
 * <p>
 * Created on 2017-04-25.
 *
 * @author Alexander Winter
 */
public class GDXArrayIndexIterator<T> implements IndexIterator<T>
{
	private Array<T> array;
	private int index;

	public GDXArrayIndexIterator(Array<T> array)
	{
		this.array = array;
	}

	@Override
	public int size()
	{
		return array.size;
	}

	@Override
	public T objectAt(int index)
	{
		return array.get(index);
	}

	@Override
	public boolean hasNext()
	{
		return index < array.size;
	}

	@Override
	public T next()
	{
		return array.get(index++);
	}

	@Override
	public void reset()
	{
		index = 0;
	}
}
