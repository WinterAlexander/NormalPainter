package net.jumpai.util.iterator;

import java.util.Iterator;

/**
 * Represents an iterator and iterable that prevents to create itself when being itered.
 * <p>
 * Created on 2017-04-25.
 *
 * @author Alexander Winter
 */
public interface ReusableIterator<T> extends Iterator<T>, Iterable<T>
{
	void reset();

	@Override
	default Iterator<T> iterator()
	{
		reset();
		return this;
	}
}
