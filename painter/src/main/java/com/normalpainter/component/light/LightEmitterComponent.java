package com.normalpainter.component.light;

import com.badlogic.gdx.utils.Array;

/**
 * Component implementing the behavior of a {@link LightEmitter}
 * <p>
 * Created on 2019-09-08.
 *
 * @author Alexander Winter
 */
public interface LightEmitterComponent
{
	/**
	 * @see LightEmitter#getLightSources()
	 */
	Array<LightSource> getLightSources();

	/**
	 * @see LightEmitter#tick(float)
	 */
	void tick(float delta);
}