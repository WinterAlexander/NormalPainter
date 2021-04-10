package net.jumpai.util.ui.textfield.validated;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * {@link TextFieldStyle} made for {@link ValidatedTextField}.
 * <p>
 * Created on 2019-05-29.
 *
 * @author Alexander Winter
 */
public class ValidatedTextFieldStyle extends TextFieldStyle
{
	public Drawable valid, loading, invalid;
	public Drawable validBackground, invalidBackground;

	public BitmapFont popupFont;
	public Color popupFontColor = Color.WHITE;
	public Drawable popupBackground;
	public float popupWidth = 300f;

	public float popupOffsetX = 20f;
	public float popupOffsetY = 40f;

	public ValidatedTextFieldStyle() {}

	public ValidatedTextFieldStyle(BitmapFont font,
	                               Color fontColor,
	                               Drawable cursor,
	                               Drawable selection,
	                               Drawable background,
	                               Drawable valid,
	                               Drawable loading,
	                               Drawable invalid,
	                               Drawable validBackground,
	                               Drawable invalidBackground,
	                               BitmapFont popupFont,
	                               Color popupFontColor,
	                               Drawable popupBackground,
	                               float popupWidth)
	{
		super(font, fontColor, cursor, selection, background);

		this.valid = valid;
		this.loading = loading;
		this.invalid = invalid;
		this.validBackground = validBackground;
		this.invalidBackground = invalidBackground;
		this.popupFont = popupFont;
		this.popupFontColor = popupFontColor;
		this.popupBackground = popupBackground;
		this.popupWidth = popupWidth;
	}

	public ValidatedTextFieldStyle(ValidatedTextFieldStyle style)
	{
		super(style);

		this.valid = style.valid;
		this.loading = style.loading;
		this.invalid = style.invalid;
		this.validBackground = style.validBackground;
		this.invalidBackground = style.invalidBackground;
		this.popupFont = style.popupFont;

		if(style.popupFontColor != null)
			this.popupFontColor = style.popupFontColor.cpy();

		this.popupBackground = style.popupBackground;
		this.popupWidth = style.popupWidth;
	}
}
