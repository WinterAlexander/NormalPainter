package net.jumpai.util.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import static net.jumpai.util.ReflectionUtil.set;

/**
 * A LibGDX button that doesn't get toggled on click
 * <p>
 * Created on 2018-04-21.
 *
 * @author Alexander Winter
 */
public class NoToggleButton extends Button
{
	{
		clearListeners();
		ClickListener clickListener = new ClickListener();

		// reflection is used to prevent overriding the draw
		// method and copy pasting all of its content
		set(this, "clickListener", clickListener);
		addListener(clickListener);
	}

	public NoToggleButton(Skin skin)
	{
		super(skin);
	}

	public NoToggleButton(Skin skin, String styleName)
	{
		super(skin, styleName);
	}

	public NoToggleButton(Actor child, Skin skin, String styleName)
	{
		super(child, skin, styleName);
	}

	public NoToggleButton(Actor child, ButtonStyle style)
	{
		super(child, style);
	}

	public NoToggleButton(ButtonStyle style)
	{
		super(style);
	}

	public NoToggleButton() {}

	public NoToggleButton(Drawable up)
	{
		super(up);
	}

	public NoToggleButton(Drawable up, Drawable down)
	{
		super(up, down);
	}

	public NoToggleButton(Drawable up, Drawable down, Drawable checked)
	{
		super(up, down, checked);
	}

	public NoToggleButton(Actor child, Skin skin)
	{
		super(child, skin);
	}
}
