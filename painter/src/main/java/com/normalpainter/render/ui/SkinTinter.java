package com.normalpainter.render.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.normalpainter.util.event.ListenableImpl;

import static com.normalpainter.util.Validation.ensureNotNull;

/**
 * Tints the color of a {@link Skin} according to a {@link ColorPalette}
 * assuming the skin is following the conventions
 * <p>
 * Created on 2019-11-13.
 *
 * @author Alexander Winter
 */
public class SkinTinter extends ListenableImpl<ColorManagerListener>
{
	private static final float COLOR_TRANSITION_RATE = 0.05f;

	private final Skin skin;

	private ColorPalette colorPalette = new ColorPalette(new Color(0x273043FF),
			new Color(0x0090ACFF),
			new Color(0x67C261FF),
			new Color(0xDA2C38FF),
			new Color(0xE8C547FF));

	private final Color tmpColor = new Color();

	public SkinTinter(Skin skin)
	{
		ensureNotNull(skin, "skin");
		this.skin = skin;

		skin.getColor("alpha").set(colorPalette.getAlpha());
		skin.getColor("beta").set(colorPalette.getBeta());
		skin.getColor("gamma").set(colorPalette.getGamma());
		skin.getColor("delta").set(colorPalette.getDelta());
		skin.getColor("omega").set(colorPalette.getOmega());
	}

	public void update()
	{
		float antiTrans = 1f - COLOR_TRANSITION_RATE;
		skin.getColor("alpha").mul(antiTrans).add(tmpColor.set(colorPalette.getAlpha()).mul(COLOR_TRANSITION_RATE));
		skin.getColor("beta").mul(antiTrans).add(tmpColor.set(colorPalette.getBeta()).mul(COLOR_TRANSITION_RATE));
		skin.getColor("gamma").mul(antiTrans).add(tmpColor.set(colorPalette.getGamma()).mul(COLOR_TRANSITION_RATE));
		skin.getColor("delta").mul(antiTrans).add(tmpColor.set(colorPalette.getDelta()).mul(COLOR_TRANSITION_RATE));
		skin.getColor("omega").mul(antiTrans).add(tmpColor.set(colorPalette.getOmega()).mul(COLOR_TRANSITION_RATE));
	}

	public ColorPalette getColorPalette()
	{
		return colorPalette;
	}

	public void setColorPalette(ColorPalette colorPalette)
	{
		ensureNotNull(colorPalette, "colorPalette");

		this.colorPalette = colorPalette;

		trigger(ColorManagerListener::colorPaletteChanged);
	}
}
