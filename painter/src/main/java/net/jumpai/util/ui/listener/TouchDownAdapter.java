package net.jumpai.util.ui.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * LibGDX's InputListener that only listens to touch down events
 * <p>
 * Created on 2018-06-17.
 *
 * @author Alexander Winter
 */
public class TouchDownAdapter extends InputListener
{
	private final TouchDownAction action;

	public TouchDownAdapter(TouchDownAction action)
	{
		this.action = action;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		return action.touchDown(event, x, y, pointer, button);
	}

	@FunctionalInterface
	public interface TouchDownAction
	{
		boolean touchDown(InputEvent event, float x, float y, int pointer, int button);
	}
}
