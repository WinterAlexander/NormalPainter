package net.jumpai.util.iterator;

/**
 * Represents an Iterator that can be accessed using an Index
 * <p>
 * Created on 2017-04-25.
 *
 * @author Alexander Winter
 */
public interface IndexIterator<T> extends ReusableIterator<T>
{
	/**
	 * @return size of this iterator, amount of elements
	 */
	int size();

	/**
	 * @param index accessor index
	 * @return object at the specified index
	 */
	T objectAt(int index);
}
