package net.jumpai.util.ui.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

/**
 * FocusListener that runs an action when the keyboard focuses on an actor
 * <p>
 * Created on 2019-01-15.
 *
 * @author Alexander Winter
 */
public class KeyboardFocusAdapter extends FocusListener
{
	private final Runnable action;

	public KeyboardFocusAdapter(Runnable action)
	{
		this.action = action;
	}

	@Override
	public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused)
	{
		if(focused)
			action.run();
	}
}
