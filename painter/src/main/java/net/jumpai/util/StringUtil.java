package net.jumpai.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Iterator;
import java.util.Locale;

/**
 * Utility class for string operations
 * <p>
 * Created on 2018-01-04.
 *
 * @author Alexander Winter
 */
public class StringUtil
{
	private StringUtil() {}

	public static String join(Iterable<String> strings, String separator)
	{
		Iterator<String> it = strings.iterator();

		if(!it.hasNext())
			return "";

		StringBuilder sb = new StringBuilder(it.next());
		sb.append(separator);

		while(it.hasNext())
			sb.append(separator).append(it.next());
		return sb.toString();
	}

	public static String join(String[] strings, String separator)
	{
		if(strings.length == 0)
			return "";

		StringBuilder sb = new StringBuilder(strings[0]);

		for(int i = 1; i < strings.length; i++)
			sb.append(separator).append(strings[i]);
		return sb.toString();
	}

	public static boolean matchesFilter(String input, String filter)
	{
		if(filter == null
		|| filter.length() == 0
		|| input == null
		|| input.length() == 0)
			return true;

		return containsIgnoreCase(input, filter);
	}

	public static String toString(long value, String digitSeparator)
	{
		char[] chars = Long.toString(value).toCharArray();
		StringBuilder sb = new StringBuilder(Math.round(chars.length * 1.25f)); //one digit separator every 4 chars

		for(int a = 0, i = chars.length - 1; i >= 0; a++, i--)
		{
			sb.insert(0, chars[i]);
			if(a >= 2 && i != 0)
			{
				sb.insert(0, digitSeparator);
				a = -1;
			}
		}
		return sb.toString();
	}

	public static String toTinyString(long value)
	{
		if(value < 1_000)
			return Long.toString(value);

		if(value < 100_000)
			return new BigDecimal(value / 1_000.0).round(new MathContext(2)).toPlainString() + "k";

		if(value < 100_000_000)
			return new BigDecimal(value / 1_000_000.0).round(new MathContext(2)).toPlainString() + "M";

		return new BigDecimal(value / 1_000_000_000.0).round(new MathContext(2)).toPlainString() + "B";
	}

	/**
	 * Returns the second parameter required to convert a number to an english
	 * ordinal
	 *
	 * @param value value to convert to ordinal
	 * @return second parameter in ordinal conversion
	 */
	public static int ordinalSecondParam(int value)
	{
		return value % 100 >= 11 && value % 100 <= 13
				? 0
				: value % 10;
	}

	public static boolean equalsIgnoreCase(String s1, String s2)
	{
		return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
	}

	public static boolean containsIgnoreCase(String container, String element)
	{
		return container.toLowerCase(Locale.ENGLISH)
				.contains(element.toLowerCase(Locale.ENGLISH));
	}

	public static boolean containsIgnoreCase(String[] array, String element)
	{
		for(String current : array)
			if(equalsIgnoreCase(element, current))
				return true;
		return false;
	}

	public static int indexOfIgnoreCase(String[] array, String element)
	{
		for(int i = 0; i < array.length; i++)
			if(equalsIgnoreCase(element, array[i]))
				return i;
		return -1;
	}

	public static char lastChar(String string)
	{
		return string.charAt(string.length() - 1);
	}

	public static String padLeft(String string, char character, int count)
	{
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < count; i++)
			sb.append(character);
		sb.append(string);
		return sb.toString();
	}
}
