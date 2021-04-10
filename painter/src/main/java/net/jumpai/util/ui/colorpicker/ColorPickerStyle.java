package net.jumpai.util.ui.colorpicker;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Style for a {@link ColorPicker}
 * <p>
 * Created on 2019-06-13.
 *
 * @author Alexander Winter
 */
public class ColorPickerStyle
{
	public BitmapFont font;
	public Drawable fieldCursor, fieldSelection;

	public Drawable hueGradient;
	public Drawable satBriGradient;
	public Drawable satBriGradientBackground;
	public Drawable satBriKnob, satBriKnobBackground;
	public Drawable hueKnob, hueKnobBackground;
	public Drawable background;

	public ColorPickerStyle() {}

	public ColorPickerStyle(BitmapFont font,
	                        Drawable fieldCursor,
	                        Drawable fieldSelection,
	                        Drawable hueGradient,
	                        Drawable satBriGradient,
	                        Drawable satBriGradientBackground,
	                        Drawable satBriKnob,
	                        Drawable satBriKnobBackground,
	                        Drawable hueKnob,
	                        Drawable hueKnobBackground,
	                        Drawable background)
	{
		this.font = font;
		this.fieldCursor = fieldCursor;
		this.fieldSelection = fieldSelection;

		this.hueGradient = hueGradient;
		this.satBriGradient = satBriGradient;
		this.satBriGradientBackground = satBriGradientBackground;
		this.satBriKnob = satBriKnob;
		this.satBriKnobBackground = satBriKnobBackground;
		this.hueKnob = hueKnob;
		this.hueKnobBackground = hueKnobBackground;
		this.background = background;
	}

	public ColorPickerStyle(ColorPickerStyle colorPickerStyle)
	{
		ensureNotNull(colorPickerStyle, "colorPickerStyle");

		font = colorPickerStyle.font;
		fieldCursor = colorPickerStyle.fieldCursor;
		fieldSelection = colorPickerStyle.fieldSelection;

		hueGradient = colorPickerStyle.hueGradient;
		satBriGradient = colorPickerStyle.satBriGradient;
		satBriGradientBackground = colorPickerStyle.satBriGradientBackground;
		satBriKnob = colorPickerStyle.satBriKnob;
		satBriKnobBackground = colorPickerStyle.satBriKnobBackground;
		hueKnob = colorPickerStyle.hueKnob;
		hueKnobBackground = colorPickerStyle.hueKnobBackground;
		background = colorPickerStyle.background;
	}
}
