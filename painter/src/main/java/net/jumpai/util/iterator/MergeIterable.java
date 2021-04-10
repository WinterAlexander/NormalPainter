package net.jumpai.util.iterator;

import java.util.Iterator;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link Iterable} made out of 2 other iterables merged together
 * <p>
 * Created on 2020-11-15.
 *
 * @author Alexander Winter
 */
public class MergeIterable<T> implements Iterable<T>
{
	private final Iterable<T> iterable1, iterator2;

	private final MergeIterator<T> tmpIterator = new MergeIterator<>();

	public MergeIterable(Iterable<T> iterable1, Iterable<T> iterable2)
	{
		ensureNotNull(iterable1, "iterable1");
		ensureNotNull(iterable2, "iterable2");
		this.iterable1 = iterable1;
		this.iterator2 = iterable2;
	}

	@Override
	public Iterator<T> iterator()
	{
		tmpIterator.iterator1 = iterable1.iterator();
		tmpIterator.iterator2 = iterator2.iterator();
		tmpIterator.first = true;
		return tmpIterator;
	}

	private static class MergeIterator<T> implements Iterator<T>
	{
		private Iterator<T> iterator1, iterator2;
		private boolean first = true;

		@Override
		public boolean hasNext()
		{
			return first ? iterator1.hasNext() || iterator2.hasNext() : iterator2.hasNext();
		}

		@Override
		public T next()
		{
			if(first && !iterator1.hasNext())
				first = false;

			return first ? iterator1.next() : iterator2.next();
		}
	}
}
