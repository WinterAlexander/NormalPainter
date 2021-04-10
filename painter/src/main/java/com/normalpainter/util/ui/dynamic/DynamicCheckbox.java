package com.normalpainter.util.ui.dynamic;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.function.Supplier;

/**
 * Checkbox that dynamically update its label
 * <p>
 * Created on 2018-02-27.
 *
 * @author Alexander Winter
 */
public class DynamicCheckbox extends CheckBox implements DynamicUI
{
	private final Supplier<String> source;

	public DynamicCheckbox(Supplier<String> source, Skin skin)
	{
		super(source.get(), skin);
		this.source = source;
	}

	public DynamicCheckbox(Supplier<String> source, Skin skin, String styleName)
	{
		super(source.get(), skin, styleName);
		this.source = source;
	}

	public DynamicCheckbox(Supplier<String> source, CheckBoxStyle style)
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
