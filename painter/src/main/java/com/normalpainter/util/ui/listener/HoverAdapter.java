package com.normalpainter.util.ui.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * InputListener that only listens to enter and exit events of pointer == -1
 * LibGDX made it so events without pointer == -1 are not really enter and exit
 * events of mouse over the actor.
 * <p>
 * Created on 2018-07-09.
 *
 * @author Alexander Winter
 */
public class HoverAdapter extends InputListener
{
	private final Runnable enter, exit;

	public HoverAdapter(Runnable enter)
	{
		this(enter, () -> {});
	}

	public HoverAdapter(Runnable enter, Runnable exit)
	{
		this.enter = enter;
		this.exit = exit;
	}

	@Override
	public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
	{
		if(pointer == -1)
			enter.run();
	}

	@Override
	public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
	{
		if(pointer == -1)
			exit.run();
	}
}
