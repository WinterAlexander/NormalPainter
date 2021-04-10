package net.jumpai.util.ui.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Runs an action when a touch down event outside a specified dialog is registered.
 * <p>
 * Created on 2018-02-18.
 *
 * @author Cedric Martens
 */
public class TouchOutOfWindowListener extends TouchOutOfActorListener
{
	public TouchOutOfWindowListener(Window actor, Runnable action)
	{
		super(actor, action);
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		if(actor.getTouchable() == Touchable.disabled)
			return true;

		if(event.getStageX() < actor.getX()
				|| event.getStageX() >= actor.getX() + actor.getWidth()
				|| event.getStageY() < actor.getY()
				|| event.getStageY() >= actor.getY() + actor.getHeight())
		{
			action.run();
			return true;
		}
		return false;
	}
}
