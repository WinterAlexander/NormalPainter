package net.jumpai.util.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.stream.Stream;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link Label} which selects its style depending on how much space it has
 * <p>
 * Created on 2020-03-19.
 *
 * @author Alexander Winter
 */
public class AutoScaleLabel extends Label
{
	private final LabelStyle[] styles;

	public AutoScaleLabel(CharSequence text, Skin skin)
	{
		this(text, skin,
				"default-huge",
				"default-gigantic",
				"default-giant",
				"default-big",
				"default-large",
				"default",
				"default-semismall",
				"default-small",
				"default-smaller",
				"default-tiny",
				"default-minuscule");
	}

	/**
	 * Constructs an AutoScaleLabel with given text, skin and array of style
	 * names
	 *
	 * @param text text of the label
	 * @param skin skin to use
	 * @param styleNames array of style names
	 */
	public AutoScaleLabel(CharSequence text, Skin skin, String... styleNames)
	{
		this(text, Stream.of(styleNames)
				.map(s -> skin.get(s, LabelStyle.class))
				.toArray(LabelStyle[]::new));
	}

	/**
	 * Constructs an AutoScaleLabel with given text and array of styles
	 *
	 * @param text text of the label
	 * @param styles array of styles, from largest to smallest
	 */
	public AutoScaleLabel(CharSequence text, LabelStyle... styles)
	{
		super(text, ensureNotNull(styles, "styles")[0]);
		this.styles = styles;
		findBestStyle();
	}

	@Override
	public void setText(CharSequence value)
	{
		super.setText(value);
		findBestStyle();
	}

	@Override
	protected void sizeChanged()
	{
		super.sizeChanged();
		findBestStyle();
	}

	private void findBestStyle()
	{
		if(styles == null)
			return; // in super constructor

		int i = 0;

		do
			setStyle(styles[i++]);
		while(i < styles.length
				&& (getWidth() > 0 && getWidth() < getPrefWidth()
					|| getHeight() > 0 && getHeight() < getPrefHeight()));
	}
}
