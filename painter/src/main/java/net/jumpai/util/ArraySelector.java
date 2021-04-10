package net.jumpai.util;

import com.badlogic.gdx.utils.Array;

/**
 * Class helping selection of an element of an array
 * <p>
 * Created on 2017-03-10.
 *
 * @author Alexander Winter
 */
public class ArraySelector<T>
{
	private Array<T> array;
	private int index = 0;

	private boolean rotation, canUnselect;

	public ArraySelector(Array<T> array, T start)
	{
		this(array, start, true);
	}

	public ArraySelector(Array<T> array, T start, boolean identity)
	{
		this(array, array.indexOf(start, identity));
	}

	public ArraySelector(Array<T> array, int index)
	{
		array.get(index);

		this.array = array;
		this.index = index;
		this.rotation = true;
		this.canUnselect = false;
	}

	public T current()
	{
		validateIndex(); //array size can change anytime

		if(index < 0)
			return null;

		return array.get(index);
	}

	public T next()
	{
		index++;
		validateIndex();

		if(index < 0)
			return null;

		return array.get(index);
	}

	public T previous()
	{
		index--;
		validateIndex();

		if(index < 0)
			return null;

		return array.get(index);
	}

	private void validateIndex()
	{
		if(array.size == 0)
		{
			index = -1;
			return;
		}

		if(index >= array.size)
		{
			if(rotation)
				index = 0;
			else
				index = array.size - 1;
		}

		if(index < 0)
		{
			if(rotation)
				index = array.size - 1;
			else if(canUnselect)
				index = -1;
			else
				index = 0;
		}
	}

	public boolean isRotation()
	{
		return rotation;
	}

	public void setRotation(boolean rotation)
	{
		this.rotation = rotation;
	}

	public boolean canUnselect()
	{
		return canUnselect;
	}

	public void setCanUnselect(boolean canUnselect)
	{
		this.canUnselect = canUnselect;
	}
}
