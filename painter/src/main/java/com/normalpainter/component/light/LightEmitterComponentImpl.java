package com.normalpainter.component.light;

import com.badlogic.gdx.utils.Array;
import com.normalpainter.util.Validation;

/**
 * Simple {@link LightEmitterComponent} with a static list of light sources
 * <p>
 * Created on 2019-09-08.
 *
 * @author Alexander Winter
 */
public class LightEmitterComponentImpl implements LightEmitterComponent
{
	private final Array<LightSource> sources;

	public LightEmitterComponentImpl(LightSource... sources)
	{
		Validation.ensureNoneNull(sources, "sources");
		this.sources = new Array<>(sources);
	}

	@Override
	public void tick(float delta)
	{
		for(LightSource lightSource : sources)
			lightSource.update(delta);
	}

	@Override
	public Array<LightSource> getLightSources()
	{
		return sources;
	}
}