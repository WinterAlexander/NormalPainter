package com.normalpainter.util.ui.dynamic;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

/**
 * {@link Stack} that update its {@link DynamicUI} childrens when updated
 * <p>
 * Created on 2017-11-07.
 *
 * @author Alexander Winter
 */
public class DynamicStack extends Stack implements DynamicUI
{
	public DynamicStack() {}

	public DynamicStack(Actor... actors)
	{
		super(actors);
	}

	@Override
	public void update()
	{
		for(Actor actor : getChildren())
			if(actor instanceof DynamicUI)
				((DynamicUI)actor).update();
	}
}
