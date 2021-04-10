package com.normalpainter.util.ui.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * Runs an action when a touch down event outside a specified actor is registered.
 * Does not work for scene2d dialogs.
 * <p>
 * Created on 2018-01-18.
 *
 * @author Alexander Winter
 */
public class TouchOutOfActorListener extends InputListener
{
	protected final Actor actor;
	protected final BooleanSupplier condition;
	protected final Runnable action;
	protected final boolean passive;
	protected final Predicate<Actor> exceptionFilter;

	public TouchOutOfActorListener(Actor actor, Runnable action)
	{
		this(actor, action, false);
	}

	public TouchOutOfActorListener(Actor actor, Runnable action, boolean passive)
	{
		this(actor, action, passive, a -> false);
	}

	public TouchOutOfActorListener(Actor actor, Runnable action, boolean passive, Predicate<Actor> exceptionFilter)
	{
		this(actor, actor::isVisible, action, passive, exceptionFilter);
	}

	public TouchOutOfActorListener(Actor actor, BooleanSupplier condition, Runnable action, boolean passive, Predicate<Actor> exceptionFilter)
	{
		this.actor = actor;
		this.condition = condition;
		this.action = action;
		this.passive = passive;
		this.exceptionFilter = exceptionFilter;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		if(!condition.getAsBoolean() || actor.getStage() == null)
			return false;

		Actor hit = actor.getStage().hit(x, y, true);

		if(hit == null || !(hit.isDescendantOf(actor) || exceptionFilter.test(hit)))
		{
			action.run();
			return !passive;
		}
		return false;
	}
}
