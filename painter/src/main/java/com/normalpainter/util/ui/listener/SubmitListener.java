package com.normalpainter.util.ui.listener;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Triggered when enter is pressed
 * <p>
 * Created on 2017-11-18.
 *
 * @author Alexander Winter
 */
public class SubmitListener extends InputListener
{
	private final Runnable action;

	public SubmitListener(Runnable action)
	{
		this.action = action;
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode)
	{
		if(keycode == Keys.ENTER)
		{
			action.run();
			return true;
		}
		return false;
	}
}
