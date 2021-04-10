package net.jumpai.util.math;

import static java.lang.Math.abs;

/**
 * Utility class to interact with floating point numbers
 * <p>
 * Created on 2017-05-16.
 *
 * @author Alexander Winter
 */
public strictfp class FloatUtil
{
	public static final int DEFAULT_ULPS = 5;

	private FloatUtil() {}

	public static boolean areEqual(float a, float b, float epsilon)
	{
		return abs(a - b) <= epsilon;
	}

	public static boolean isGreaterOrEqual(float a, float b, float epsilon)
	{
		return b - a <= epsilon;
	}

	public static boolean isSmallerOrEqual(float a, float b, float epsilon)
	{
		return b - a >= -epsilon;
	}

	public static boolean areEqual(float a, float b)
	{
		return areEqual(a, b, DEFAULT_ULPS * Math.ulp((a + b) / 2));
	}

	public static boolean isGreaterOrEqual(float a, float b)
	{
		return isGreaterOrEqual(a, b, DEFAULT_ULPS * Math.ulp((a + b) / 2));
	}

	public static boolean isSmallerOrEqual(float a, float b)
	{
		return isSmallerOrEqual(a, b, DEFAULT_ULPS * Math.ulp((a + b) / 2));
	}

	public static float max(float a, float b)
	{
		return (a >= b) ? a : b;
	}

	public static float max(float a, float b, float c)
	{
		float a1 = (a >= b) ? a : b;
		return (a1 >= c) ? a1 : c;
	}

	public static float min(float a, float b)
	{
		return (a <= b) ? a : b;
	}

	public static float min(float a, float b, float c)
	{
		float a1 = (a <= b) ? a : b;
		return (a1 <= c) ? a1 : c;
	}
}
