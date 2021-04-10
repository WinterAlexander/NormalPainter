package net.jumpai.tools.normalpainter;

import com.badlogic.gdx.scenes.scene2d.Actor;
import net.jumpai.util.ui.dynamic.DynamicUI;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Fake actor used only to execute some runnable on update
 * <p>
 * Created on 2020-12-20.
 *
 * @author Alexander Winter
 */
public class DynamicCallback extends Actor implements DynamicUI
{
	public Runnable todo;

	public DynamicCallback(Runnable todo)
	{
		ensureNotNull(todo, "todo");
		this.todo = todo;
	}

	@Override
	public void update()
	{
		todo.run();
	}
}
