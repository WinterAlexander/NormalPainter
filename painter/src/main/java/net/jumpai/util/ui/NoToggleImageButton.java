package net.jumpai.util.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import static net.jumpai.util.ReflectionUtil.set;

/**
 * A LibGDX image button that doesn't get toggled on click
 * <p>
 * Created on 2018-05-13.
 *
 * @author Alexander Winter
 */
public class NoToggleImageButton extends ImageButton
{

	{
		clearListeners();
		ClickListener clickListener = new ClickListener();

		// reflection is used to prevent overriding the draw
		// method and copy pasting all of its content
		set(this, "clickListener", clickListener);
		addListener(clickListener);
	}

	public NoToggleImageButton(Skin skin)
	{
		super(skin);
	}

	public NoToggleImageButton(Skin skin, String styleName)
	{
		super(skin, styleName);
	}

	public NoToggleImageButton(ImageButtonStyle style)
	{
		super(style);
	}

	public NoToggleImageButton(Drawable imageUp)
	{
		super(imageUp);
	}

	public NoToggleImageButton(Drawable imageUp, Drawable imageDown)
	{
		super(imageUp, imageDown);
	}

	public NoToggleImageButton(Drawable imageUp, Drawable imageDown, Drawable imageChecked)
	{
		super(imageUp, imageDown, imageChecked);
	}
}
