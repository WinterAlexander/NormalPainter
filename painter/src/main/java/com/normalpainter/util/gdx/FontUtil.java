package com.normalpainter.util.gdx;

import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;

/**
 * Utilitaly class to do stuff with libgdx's fonts
 * <p>
 * Created on 2018-02-24.
 *
 * @author Alexander Winter
 */
public class FontUtil
{
	private FontUtil() {}

	public static BitmapFontData copy(BitmapFontData old)
	{
		BitmapFontData data = new BitmapFontData();

		data.ascent = old.ascent;
		data.blankLineScale = old.blankLineScale;
		data.breakChars = old.breakChars;
		data.capChars = old.capChars;
		data.capHeight = old.capHeight;
		data.cursorX = old.cursorX;
		data.descent = old.descent;
		data.down = old.down;
		data.flipped = old.flipped;
		data.fontFile = old.fontFile;
		System.arraycopy(old.glyphs, 0, data.glyphs, 0, old.glyphs.length);
		data.imagePaths = old.imagePaths;
		data.lineHeight = old.lineHeight;
		data.markupEnabled = old.markupEnabled;
		data.missingGlyph = old.missingGlyph;
		data.padBottom = old.padBottom;
		data.padTop = old.padTop;
		data.padLeft = old.padLeft;
		data.padRight = old.padRight;
		data.scaleX = old.scaleX;
		data.scaleY = old.scaleY;
		data.spaceXadvance = old.spaceXadvance;
		data.xHeight = old.xHeight;
		data.xChars = old.xChars;

		return data;
	}
}
