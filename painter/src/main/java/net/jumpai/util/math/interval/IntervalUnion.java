package net.jumpai.util.math.interval;

/**
 * Union of multiple intervals
 * <p>
 * Created on 2018-08-30.
 *
 * @author Alexander Winter
 */
public class IntervalUnion implements Interval
{
	private final Interval first, second;

	public IntervalUnion(Interval first, Interval second)
	{
		this.first = first;
		this.second = second;
	}

	@Override
	public Interval add(Interval other)
	{
		if(other instanceof SimpleInterval)
		{
			SimpleInterval simInt = (SimpleInterval)other;

			if(first.intersects(simInt))
				return first.add(other).add(second);
			else if(second.intersects(simInt))
				return second.add(other).add(first);
		}
		else if(other instanceof IntervalUnion)
		{
			return add(((IntervalUnion)other).first).add(((IntervalUnion)other).second);
		}
		else if(other instanceof EmptyInterval)
			return this;

		return new IntervalUnion(this, other);
	}

	@Override
	public boolean contains(float value)
	{
		return first.contains(value) || second.contains(value);
	}

	@Override
	public boolean contains(float start, float end)
	{
		return first.contains(start, end) || second.contains(start, end);
	}

	@Override
	public boolean intersects(float start, float end)
	{
		return first.intersects(start, end) || second.intersects(start, end);
	}
}
