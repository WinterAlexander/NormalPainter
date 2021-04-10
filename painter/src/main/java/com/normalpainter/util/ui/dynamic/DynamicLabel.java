package com.normalpainter.util.ui.dynamic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.function.Supplier;

/**
 * Labels that update its content from a Supplier instead of having a static
 * string
 * <p>
 * Created on 2017-11-04.
 *
 * @author Alexander Winter
 */
public class DynamicLabel extends Label implements DynamicUI
{
	private final Supplier<CharSequence> source;

	public DynamicLabel(Supplier<CharSequence> source, Skin skin)
	{
		this(source, skin.get(LabelStyle.class));
	}

	public DynamicLabel(Supplier<CharSequence> source, Skin skin, String styleName)
	{
		super(source.get(), skin, styleName);
		this.source = source;
	}

	public DynamicLabel(Supplier<CharSequence> source, Skin skin, String fontName, Color color)
	{
		super(source.get(), skin, fontName, color);
		this.source = source;
	}

	public DynamicLabel(Supplier<CharSequence> source, Skin skin, String fontName, String colorName)
	{
		super(source.get(), skin, fontName, colorName);
		this.source = source;
	}

	public DynamicLabel(Supplier<CharSequence> source, LabelStyle style)
	{
		super(source.get(), style);
		this.source = source;
	}

	@Override
	public void update()
	{
		setText(source.get());
	}
}
