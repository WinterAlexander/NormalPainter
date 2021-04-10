package net.jumpai.util.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static net.jumpai.util.ReflectionUtil.set;

/**
 * A LibGDX text button that doesn't get toggled on click
 * <p>
 * Created on 2018-07-25.
 *
 * @author Alexander Winter
 */
public class NoToggleTextButton extends TextButton
{
	{
		clearListeners();
		ClickListener clickListener = new ClickListener();

		// reflection is used to prevent overriding the draw
		// method and copy pasting all of its content
		set(this, "clickListener", clickListener);
		addListener(clickListener);
	}

	public NoToggleTextButton(String text, Skin skin)
	{
		super(text, skin);
	}

	public NoToggleTextButton(String text, Skin skin, String styleName)
	{
		super(text, skin, styleName);
	}

	public NoToggleTextButton(String text, TextButtonStyle style)
	{
		super(text, style);
	}
}
