package net.jumpai.util.gfx;

import com.badlogic.gdx.graphics.Color;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Utility class for libgdx colors
 * <p>
 * Created on 2018-06-16.
 *
 * @author Alexander Winter
 */
public class ColorUtil
{
	private ColorUtil() {}

	public static boolean isBlack(Color color)
	{
		return (color.toIntBits() & 0x00FFFFFF) == 0;
	}

	public static void toArrayRGB(Color color, float[] array)
	{
		ensureNotNull(color, "color");
		ensureNotNull(array, "array");

		array[0] = color.r;
		array[1] = color.g;
		array[2] = color.b;
	}
}
