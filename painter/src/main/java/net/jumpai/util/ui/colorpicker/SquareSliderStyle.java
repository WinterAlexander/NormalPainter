package net.jumpai.util.ui.colorpicker;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Style for a {@link SquareSlider}
 * <p>
 * Created on 2019-06-13.
 *
 * @author Alexander Winter
 */
public class SquareSliderStyle
{
	public Drawable knob, background;

	public SquareSliderStyle() {}

	public SquareSliderStyle(Drawable knob, Drawable background)
	{
		this.knob = knob;
		this.background = background;
	}

	public SquareSliderStyle(SquareSliderStyle squareSliderStyle)
	{
		ensureNotNull(squareSliderStyle, "squareSliderStyle");

		this.knob = squareSliderStyle.knob;
		this.background = squareSliderStyle.background;
	}
}
