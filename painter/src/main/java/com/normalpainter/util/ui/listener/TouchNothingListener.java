package com.normalpainter.util.ui.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Runs a runnable when a touch event on no actor is received for an actor's
 * stage.
 * <p>
 * Created on 2018-04-07.
 *
 * @author Cedric Martens
 * @author Alexander Winter
 */
public class TouchNothingListener extends InputListener
{
	protected final Runnable action;
	protected final Actor actor;

	public TouchNothingListener(Actor actor, Runnable action)
	{
		this.action = action;
		this.actor = actor;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		if(actor == null || actor.getStage() == null)
			return false;

		if(actor.getStage().hit(x, y, true) == null)
			action.run();

		return false;
	}
}
