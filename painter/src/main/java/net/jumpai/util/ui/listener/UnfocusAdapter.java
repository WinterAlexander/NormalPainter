package net.jumpai.util.ui.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

/**
 * Adapter that executes an action when unfocused
 * <p>
 * Created on 2018-07-31.
 *
 * @author Alexander Winter
 */
public class UnfocusAdapter extends FocusListener
{
	private final Runnable action;

	public UnfocusAdapter(Runnable action)
	{
		this.action = action;
	}

	@Override
	public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused)
	{
		if(!focused)
			action.run();
	}
}
