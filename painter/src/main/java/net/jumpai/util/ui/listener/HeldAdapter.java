package net.jumpai.util.ui.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import net.jumpai.util.MutableBox;

import java.util.function.IntPredicate;

import static net.jumpai.util.gdx.GdxUtil.postRunnable;

/**
 * Runs specified action at every amount of time when an actor is held
 * <p>
 * Created on 2018-05-17.
 *
 * @author Alexander Winter
 */
public class HeldAdapter extends InputListener
{
	private final Runnable action;
	private final long interval;
	private IntPredicate buttonFilter;

	private long pressTime = -1;

	public HeldAdapter(Runnable action, long interval, IntPredicate buttonFilter)
	{
		this.action = action;
		this.interval = interval;
		this.buttonFilter = buttonFilter;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		if(!buttonFilter.test(button))
			return false;

		MutableBox<Runnable> repeat = new MutableBox<>();

		long pressTime = this.pressTime = Gdx.graphics.getFrameId();

		repeat.set(() -> {

			if(this.pressTime != pressTime)
				return; //not the same press

			action.run();
			postRunnable(repeat.get(), interval);
		});

		repeat.get().run();
		return true;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button)
	{
		if(!buttonFilter.test(button))
			return;

		pressTime = -1;
	}
}
