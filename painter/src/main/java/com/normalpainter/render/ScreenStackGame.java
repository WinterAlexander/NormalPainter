package com.normalpainter.render;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Predicate;
import com.normalpainter.util.log.Logger;
import com.normalpainter.util.log.NullLogger;

import static com.normalpainter.util.Validation.ensureNotNull;

/**
 * LibGDX screen implemented with stack
 * <p>
 * Created on 2017-11-01.
 *
 * @author Alexander Winter
 */
public abstract class ScreenStackGame<S extends StackedScreen>
		implements ApplicationListener
{
	private final Array<S> screenStack = new Array<>();

	protected Logger __debugLogger = new NullLogger();

	/**
	 * Sets the current screen to be the specified screen.
	 *
	 * {@link Screen#hide()} is called on the previous screen and {@link Screen#show()} is called on this new screen.
	 *
	 * History of screens is kept and can be navigated using {@link ScreenStackGame#finishScreen()}
	 *
	 * @param screen must not be {@code null}
	 */
	public void setScreen(S screen)
	{
		ensureNotNull(screen, "screen");

		if(screenStack.size != 0)
		{
			getScreen().hide();
			__debugLogger.debug("New " + screen.getClass().getSimpleName() + " added, previous was " + getScreen().getClass().getSimpleName());
		}
		else
			__debugLogger.debug("New " + screen.getClass().getSimpleName() + " added, first in stack");

		screenStack.add(screen);
		screen.show();
		screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	/**
	 * Finishes the current screen and replaces it immediately with a new screen,
	 * without interacting with the other screen on the stack
	 * @param replaceBy screen to replace the current screen
	 */
	public void replaceScreen(S replaceBy)
	{
		if(screenStack.peek() == replaceBy)
			return;

		Screen current = screenStack.pop();

		current.hide();
		current.dispose();

		__debugLogger.debug(current.getClass().getSimpleName() + " finished, replacing with " + replaceBy.getClass().getSimpleName());

		screenStack.add(replaceBy);
		replaceBy.show();
		replaceBy.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	/**
	 * Replaces all screen that mets a specified condition and then add the
	 * specified screen
	 * @param condition condition to be replaced as a screen
	 * @param replaceBy screen to add afterward
	 */
	public void replaceAllUntil(Predicate<S> condition, S replaceBy)
	{
		if(screenStack.peek() == replaceBy)
			return;

		screenStack.peek().hide();

		while(condition.evaluate(screenStack.peek()))
		{
			Screen toRemove = screenStack.pop();
			toRemove.dispose();

			__debugLogger.debug(toRemove.getClass().getSimpleName() + " finished by replaceAll");
		}

		screenStack.add(replaceBy);
		replaceBy.show();
		replaceBy.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		__debugLogger.debug("Replacing with " + replaceBy.getClass().getSimpleName());
	}

	/**
	 * Restores the first screen (starting from the top of the stack) that
	 * respects the specified predicate
	 * @see #restore(S)
	 * @param predicate condition to be
	 * @throws IllegalArgumentException if the predicate didn't match any screen
	 */
	public void restore(Predicate<S> predicate)
	{
		for(int i = screenStack.size; i --> 0 ;)
			if(predicate.evaluate(screenStack.get(i)))
			{
				restore(screenStack.get(i));
				return;
			}

		throw new IllegalArgumentException("predicate didn't match any screen");
	}

	/**
	 * Removes all screens until the specified screen is shown
	 * @param screen screen to restore
	 * @throws IllegalArgumentException is the screen isn't in the stack
	 */
	public void restore(S screen)
	{
		if(!screenStack.contains(screen, true))
			throw new IllegalArgumentException("screen not in the stack");

		if(screenStack.peek() == screen)
			return;

		screenStack.peek().hide();

		do
		{
			S toRemove = screenStack.pop();
			toRemove.dispose();

			__debugLogger.debug(toRemove.getClass().getSimpleName() + " finished by restore");
		}
		while(screenStack.peek() != screen);

		getScreen().show();
		getScreen().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		__debugLogger.debug("Restored " + getScreen().getClass().getSimpleName());
	}

	/**
	 * Finishes the current screen and disposes of it
	 */
	public void finishScreen()
	{
		S screen = screenStack.pop();

		screen.hide();
		screen.dispose();

		if(screenStack.size == 0)
		{
			__debugLogger.debug(screen.getClass().getSimpleName() + " finished, no more in stack");
			return;
		}

		__debugLogger.debug(screen.getClass().getSimpleName() + " finished, next in stack is " + getScreen().getClass().getSimpleName());

		getScreen().show();
		getScreen().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void dispose()
	{
		__debugLogger.debug("Dispose called");

		screenStack.peek().hide();

		while(screenStack.size > 0)
		{
			S screen = screenStack.pop();
			screen.dispose();
		}
	}

	@Override
	public void pause()
	{
		if(getScreen() != null)
			getScreen().pause();
	}

	@Override
	public void resume()
	{
		if(getScreen() != null)
			getScreen().resume();
	}

	@Override
	public void render()
	{
		if(getScreen() != null)
			getScreen().render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height)
	{
		if(getScreen() != null)
			getScreen().resize(width, height);
	}

	public Array<S> getScreens()
	{
		return screenStack;
	}

	/**
	 * @return the currently active {@link Screen}
	 */
	public S getScreen()
	{
		if(screenStack.size == 0)
			return null;
		return screenStack.peek();
	}

	public boolean contains(S screen)
	{
		return screenStack.contains(screen, true);
	}
}
