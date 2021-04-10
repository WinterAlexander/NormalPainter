package net.jumpai.util.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * {@link Label} that allows to set its line height
 * <p>
 * Created on 2019-02-02.
 *
 * @author Alexander Winter
 */
public class BetterLabel extends Label
{
	private float lineHeight = -1f;

	public BetterLabel(CharSequence text, Skin skin)
	{
		super(text, skin);
	}

	public BetterLabel(CharSequence text, Skin skin, String styleName)
	{
		super(text, skin, styleName);
	}

	public BetterLabel(CharSequence text, Skin skin, String fontName, Color color)
	{
		super(text, skin, fontName, color);
	}

	public BetterLabel(CharSequence text, Skin skin, String fontName, String colorName)
	{
		super(text, skin, fontName, colorName);
	}

	public BetterLabel(CharSequence text, LabelStyle style)
	{
		super(text, style);
	}

	@Override
	public void layout()
	{
		if(lineHeight < 0f)
		{
			super.layout();
			return;
		}

		float prev = getStyle().font.getData().lineHeight / getStyle().font.getData().scaleY;
		getStyle().font.getData().setLineHeight(lineHeight);
		super.layout();
		getStyle().font.getData().setLineHeight(prev);
	}

	@Override
	public float getPrefWidth()
	{
		float prev = getStyle().font.getData().lineHeight / getStyle().font.getData().scaleY;
		getStyle().font.getData().setLineHeight(lineHeight);

		float pw = super.getPrefWidth();

		getStyle().font.getData().setLineHeight(prev);
		return pw;
	}

	@Override
	public float getPrefHeight()
	{
		float prev = getStyle().font.getData().lineHeight / getStyle().font.getData().scaleY;
		getStyle().font.getData().setLineHeight(lineHeight);

		float ph = super.getPrefHeight();

		getStyle().font.getData().setLineHeight(prev);
		return ph;
	}

	public float getLineHeight()
	{
		if(lineHeight < 0f)
			return getStyle().font.getData().lineHeight;
		return lineHeight;
	}

	public void setLineHeight(float lineHeight)
	{
		this.lineHeight = lineHeight;
		invalidate();
	}
}
