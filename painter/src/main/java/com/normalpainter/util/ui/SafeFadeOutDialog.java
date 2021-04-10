package com.normalpainter.util.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Dialog that allows safe fade out by blocking all events when fading out instead
 * of only blocking touchDowns as libGDX does by default
 * <p>
 * Created on 2018-08-26.
 *
 * @author Alexander Winter
 */
public class SafeFadeOutDialog extends Dialog
{
	{
		ignoreTouchDown = new InputListener() {
			@Override
			public boolean handle(Event event) {
				event.cancel();
				return false;
			}
		};
	}

	public SafeFadeOutDialog(String title, Skin skin)
	{
		super(title, skin);
	}

	public SafeFadeOutDialog(String title, Skin skin, String windowStyleName)
	{
		super(title, skin, windowStyleName);
	}

	public SafeFadeOutDialog(String title, WindowStyle windowStyle)
	{
		super(title, windowStyle);
	}
}
