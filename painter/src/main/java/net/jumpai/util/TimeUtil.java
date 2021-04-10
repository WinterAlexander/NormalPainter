package net.jumpai.util;

import com.badlogic.gdx.utils.StringBuilder;

/**
 * Utility class to display time
 * <p>
 * Created on 2017-02-01.
 *
 * @author Alexander Winter
 */
public class TimeUtil
{
	public static String displayFromMillis(long millisTimestamp)
	{
		int hours = (int)(millisTimestamp / 1000 / 60 / 60);
		int minutes = (int)(millisTimestamp / 1000 / 60 % 60);
		int seconds = (int)(millisTimestamp / 1000 % 60);
		int millis = (int)(millisTimestamp % 1000);

		return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
	}

	public static String displayFromTicks(int ticks, float subFrame)
	{
		return displayFromMillis(toMillis(ticks, subFrame));
	}

	public static void initTimeDisplay(StringBuilder sb)
	{
		sb.setLength(12);
		sb.setCharAt(2, ':');
		sb.setCharAt(5, ':');
		sb.setCharAt(8, '.');
	}

	public static void displayIntoFromMillis(StringBuilder sb, long millisTimestamp)
	{
		int hours = (int)(millisTimestamp / 1000 / 60 / 60);
		sb.setCharAt(0, (char)('0' + hours / 10));
		sb.setCharAt(1, (char)('0' + hours % 10));

		int minutes = (int)(millisTimestamp / 1000 / 60 % 60);
		sb.setCharAt(3, (char)('0' + minutes / 10));
		sb.setCharAt(4, (char)('0' + minutes % 10));

		int seconds = (int)(millisTimestamp / 1000 % 60);
		sb.setCharAt(6, (char)('0' + seconds / 10));
		sb.setCharAt(7, (char)('0' + seconds % 10));

		int millis = (int)(millisTimestamp % 1000);
		sb.setCharAt(9, (char)('0' + millis / 100));
		sb.setCharAt(10, (char)('0' + millis / 10 % 10));
		sb.setCharAt(11, (char)('0' + millis % 10));
	}

	public static void displayIntoFromTicks(StringBuilder sb, int ticks, float subFrame)
	{
		displayIntoFromMillis(sb, toMillis(ticks, subFrame));
	}

	/**
	 * Converts ticks and a subframe ratio to an amount of milliseconds
	 *
	 * @param length length of the run
	 * @param subFrame subFrame ratio
	 * @return time in milliseconds
	 */
	public static long toMillis(int length, float subFrame)
	{
		return Math.round((length * 1000d - subFrame * 1000d) / 60d);
	}
}
