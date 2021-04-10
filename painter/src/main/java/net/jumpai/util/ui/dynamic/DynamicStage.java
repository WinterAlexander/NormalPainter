package net.jumpai.util.ui.dynamic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Dynamic stage that can update all its dynamic actors
 * <p>
 * Created on 2017-11-04.
 *
 * @author Alexander Winter
 */
public class DynamicStage extends Stage implements DynamicUI
{
	public DynamicStage() {}

	public DynamicStage(Viewport viewport)
	{
		super(viewport);
	}

	public DynamicStage(Viewport viewport, Batch batch)
	{
		super(viewport, batch);
	}

	@Override
	public void update()
	{
		for(Actor actor : getActors())
			if(actor instanceof DynamicUI)
				((DynamicUI)actor).update();
	}
}
