package net.jumpai.util.math;

import java.util.Random;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Random generator that do not spit out the same number twice in a row
 * <p>
 * Created on 2019-07-03.
 *
 * @author Alexander Winter
 */
public class AntiRepetionRandomGenerator
{
	private int min, max;
	private final Random random;

	private int prev = Integer.MAX_VALUE;

	public AntiRepetionRandomGenerator(int max)
	{
		this(0, max, new Random());
	}

	public AntiRepetionRandomGenerator(int min, int max, Random random)
	{
		ensureNotNull(random, "random");

		this.min = min;
		this.max = max;
		this.random = random;
	}

	public int generate()
	{
		int num;
		do
		{
			num = random.nextInt(max - min) + min;
		}
		while(prev == num);
		return prev = num;
	}

	public void reset()
	{
		prev = Integer.MAX_VALUE;
	}

	public int getMin()
	{
		return min;
	}

	public void setMin(int min)
	{
		this.min = min;
	}

	public int getMax()
	{
		return max;
	}

	public void setMax(int max)
	{
		this.max = max;
	}
}
