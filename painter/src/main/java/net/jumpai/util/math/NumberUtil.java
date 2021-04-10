package net.jumpai.util.math;

/**
 * Utility class to handle numbers
 * <p>
 * Created on 2018-03-11.
 *
 * @author Alexander Winter
 */
public class NumberUtil
{
	private NumberUtil() {}

	/**
	 * Parses specified input of base 10 into an integer
	 *
	 * @param input string representation to parse
	 * @param defaultVal value to return if it fails
	 * @return parsed integer or defaultVal
	 */
	public static int tryParseInt(String input, int defaultVal)
	{
		return tryParseInt(input, 10, defaultVal);
	}

	/**
	 * Parses specified input of specified base into an integer
	 *
	 * @param input input to parse
	 * @param radix base in which the integer is represented
	 * @param defaultVal default value if the input couldn't be parsed
	 * @return int represented by input
	 */
	public static int tryParseInt(String input, int radix, int defaultVal)
	{
		if(input == null)
			return defaultVal;

		try
		{
			return Integer.parseInt(input, radix);
		}
		catch(NumberFormatException ex)
		{
			return defaultVal;
		}
	}

	/**
	 * Parses specified input into a long
	 *
	 * @param input input to parse
	 * @param defaultVal default value if the input couldn't be parsed
	 * @return long represented by input
	 */
	public static long tryParseLong(String input, long defaultVal)
	{
		if(input == null)
			return defaultVal;

		try
		{
			return Long.parseLong(input);
		}
		catch(NumberFormatException ex)
		{
			return defaultVal;
		}
	}

	/**
	 * Parses specified input into a float
	 *
	 * @param input input to parse
	 * @param defaultVal default value if the input couldn't be parsed
	 * @return float represented by input
	 */
	public static float tryParseFloat(String input, float defaultVal)
	{
		if(input == null)
			return defaultVal;

		try
		{
			return Float.parseFloat(input);
		}
		catch(NumberFormatException ex)
		{
			return defaultVal;
		}
	}
	/**
	 * Parses specified input into a double
	 *
	 * @param input input to parse
	 * @param defaultVal default value if the input couldn't be parsed
	 * @return double represented by input
	 */
	public static double tryParseDouble(String input, double defaultVal)
	{
		if(input == null)
			return defaultVal;

		try
		{
			return Double.parseDouble(input);
		}
		catch(NumberFormatException ex)
		{
			return defaultVal;
		}
	}

	/**
	 * Checks if the specified number is an integer (int, long, short or byte)
	 *
	 * @param number number to check
	 * @return true if specified number is an integer, otherwise false
	 */
	public static boolean isInteger(Number number)
	{
		return number instanceof Integer
				|| number instanceof Long
				|| number instanceof Short
				|| number instanceof Byte;
	}
}
