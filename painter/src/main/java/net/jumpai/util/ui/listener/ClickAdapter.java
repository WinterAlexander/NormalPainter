package net.jumpai.util.ui.listener;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.function.Consumer;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Runs specified action on click
 * <p>
 * Created on 2017-01-07.
 *
 * @author Alexander Winter
 */
public class ClickAdapter extends ClickListener
{
	private final Consumer<InputEvent> action;
	private final boolean stop;

	public ClickAdapter(Runnable action)
	{
		this(action, false);
	}

	public ClickAdapter(Runnable action, boolean stop)
	{
		this(action, Buttons.LEFT, stop);
	}

	public ClickAdapter(Runnable action, int button)
	{
		this(action, button, false);
	}

	public ClickAdapter(Runnable action, int button, boolean stop)
	{
		super(button);

		ensureNotNull(action, "action");
		this.action = event -> action.run();
		this.stop = stop;
	}

	@Override
	public void clicked(InputEvent event, float x, float y)
	{
		if(event == null
				|| !(event.getListenerActor() instanceof Button)
				|| !((Button)event.getListenerActor()).isDisabled())
			action.accept(event);

		if(stop && event != null)
			event.stop();
	}
}
