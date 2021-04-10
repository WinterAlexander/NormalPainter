package com.normalpainter.render;

import com.badlogic.gdx.Screen;

/**
 * Screen aware of it's presence in a {@link ScreenStackGame}
 * <p>
 * Created on 2017-11-01.
 *
 * @author Alexander Winter
 */
public interface StackedScreen extends Screen
{
	void finish();
}
