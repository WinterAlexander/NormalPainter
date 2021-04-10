package net.jumpai.util.ui.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * EventListener that only listens to key down events of a specific key
 * <p>
 * Created on 2018-04-21.
 *
 * @author Alexander Winter
 */
public class KeyListener extends InputListener
{
	private final int keycode;
	private final boolean down;
	private final BiFunction<InputEvent, Integer, Boolean> function;

	public KeyListener(int keycode, Runnable function)
	{
		this(keycode, (e, i) -> {
			function.run();
			return true;
		});
	}

	public KeyListener(int keycode, boolean down, Runnable function)
	{
		this(keycode, down, (e, i) -> {
			function.run();
			return true;
		});
	}

	public KeyListener(int keycode, BooleanSupplier function)
	{
		this(keycode, (e, i) -> function.getAsBoolean());
	}

	public KeyListener(int keycode, boolean down, BooleanSupplier function)
	{
		this(keycode, down, (e, i) -> function.getAsBoolean());
	}

	public KeyListener(int keycode, Function<InputEvent, Boolean> function)
	{
		this(keycode, (e, i) -> function.apply(e));
	}

	public KeyListener(int keycode, boolean down, Function<InputEvent, Boolean> function)
	{
		this(keycode, down, (e, i) -> function.apply(e));
	}

	public KeyListener(int keycode, BiFunction<InputEvent, Integer, Boolean> function)
	{
		this(keycode, true, function);
	}

	public KeyListener(int keycode, boolean down, BiFunction<InputEvent, Integer, Boolean> function)
	{
		this.keycode = keycode;
		this.down = down;
		this.function = function;
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode)
	{
		if(!down || this.keycode != keycode)
			return false;

		return function.apply(event, keycode);
	}

	@Override
	public boolean keyUp(InputEvent event, int keycode)
	{
		if(down || this.keycode != keycode)
			return false;

		return function.apply(event, keycode);
	}
}
