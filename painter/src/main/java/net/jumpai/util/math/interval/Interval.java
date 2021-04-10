package net.jumpai.util.math.interval;

/**
 * An interval. Can contains values or other {@link SimpleInterval}s.
 * <p>
 * Created on 2018-08-30.
 *
 * @author Alexander Winter
 */
public interface Interval
{
	Interval add(Interval other);

	boolean contains(float value);

	boolean contains(float start, float end);

	boolean intersects(float start, float end);

	default boolean contains(SimpleInterval simpleInterval) {
		return contains(simpleInterval.start, simpleInterval.end);
	}

	default boolean intersects(SimpleInterval simpleInterval) {
		return intersects(simpleInterval.start, simpleInterval.end);
	}
}
