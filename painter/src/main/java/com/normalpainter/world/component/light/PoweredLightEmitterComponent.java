package com.normalpainter.world.component.light;

import com.badlogic.gdx.utils.Array;
import com.normalpainter.util.Validation;
import com.normalpainter.world.World;

/**
 * {@link LightEmitterComponent} which has different light sources when the
 * power is on or off
 * <p>
 * Created on 2020-09-20.
 *
 * @author Alexander Winter
 */
public class PoweredLightEmitterComponent implements LightEmitterComponent
{
	private final World world;
	private final Array<LightSource> powerOnSources;
	private final Array<LightSource> powerOffSources;

	public PoweredLightEmitterComponent(World world, Array<LightSource> powerOnSources, Array<LightSource> powerOffSources)
	{
		Validation.ensureNotNull(world, "world");
		Validation.ensureNotNull(powerOnSources, "powerOnSources");
		Validation.ensureNotNull(powerOffSources, "powerOffSources");
		this.world = world;
		this.powerOnSources = powerOnSources;
		this.powerOffSources = powerOffSources;
	}

	@Override
	public Array<LightSource> getLightSources()
	{
		return world.isPowerOn() ? powerOnSources : powerOffSources;
	}

	@Override
	public void tick(float delta)
	{
		for(LightSource lightSource : world.isPowerOn() ? powerOnSources : powerOffSources)
			lightSource.update(delta);
	}
}
