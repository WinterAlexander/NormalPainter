package com.normalpainter.util.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

/**
 * A slider but you can specify it's pref width/height instead of having a hardcoded value of 140
 * <p>
 * Created on 2018-04-03.
 *
 * @author Alexander Winter
 */
public class BetterSlider extends Slider
{
	private final float prefSize;

	public BetterSlider(float min, float max, float stepSize, boolean vertical, Skin skin, float prefSize)
	{
		super(min, max, stepSize, vertical, skin);
		this.prefSize = prefSize;
	}

	public BetterSlider(float min, float max, float stepSize, boolean vertical, Skin skin, String styleName, float prefSize)
	{
		super(min, max, stepSize, vertical, skin, styleName);
		this.prefSize = prefSize;
	}

	public BetterSlider(float min, float max, float stepSize, boolean vertical, SliderStyle style, float prefSize)
	{
		super(min, max, stepSize, vertical, style);
		this.prefSize = prefSize;
	}

	@Override
	public float getPrefWidth()
	{
		if(!isVertical())
			return prefSize;

		return super.getPrefWidth();
	}

	@Override
	public float getPrefHeight()
	{
		if(isVertical())
			return prefSize;

		return super.getPrefHeight();
	}
}
