package net.jumpai.util.ui.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.function.Consumer;

/**
 * Runs specified action on change
 * <p>
 * Created on 2018-06-02.
 *
 * @author Alexander Winter
 */
public class ChangeAdapter extends ChangeListener
{
	private final Consumer<Actor> action;

	public ChangeAdapter(Runnable runnable)
	{
		this(a -> runnable.run());
	}

	public ChangeAdapter(Consumer<Actor> action)
	{
		this.action = action;
	}

	@Override
	public void changed(ChangeEvent event, Actor actor)
	{
		action.accept(actor);
	}
}
