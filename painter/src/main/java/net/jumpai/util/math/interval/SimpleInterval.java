package net.jumpai.util.math.interval;

import static net.jumpai.util.math.FloatUtil.max;
import static net.jumpai.util.math.FloatUtil.min;

/**
 * Interval from a start and an end, always inclusive
 * <p>
 * Created on 2018-08-30.
 *
 * @author Alexander Winter
 */
public class SimpleInterval implements Interval
{
	public final float start, end;

	public SimpleInterval(float x1, float x2)
	{
		this.start = min(x1, x2);
		this.end = max(x1, x2);
	}

	@Override
	public Interval add(Interval other)
	{
		if(other instanceof SimpleInterval)
		{
			SimpleInterval simInt = (SimpleInterval)other;

			if(simInt.start <= end && start <= simInt.end)
				return new SimpleInterval(min(start, simInt.start), max(end, simInt.end));
			return new IntervalUnion(this, other);
		}

		return other.add(this);
	}

	@Override
	public boolean contains(float value)
	{
		return value >= start && value <= end;
	}

	@Override
	public boolean contains(float start, float end)
	{
		return start >= this.start && end <= this.end;
	}

	@Override
	public boolean intersects(float start, float end)
	{
		return start <= this.end && this.start <= end;
	}
}
