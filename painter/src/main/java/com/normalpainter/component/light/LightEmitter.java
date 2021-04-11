package com.normalpainter.component.light;

import com.badlogic.gdx.utils.Array;
import com.normalpainter.world.WorldObject;
import com.normalpainter.component.Tickable;
import com.normalpainter.component.location.Localizable;

/**
 * {@link WorldObject} which can emit light by having {@link LightSource}s
 * <p>
 * Created on 2019-09-08.
 *
 * @author Alexander Winter
 */
public interface LightEmitter extends Localizable, Tickable
{
	LightEmitterComponent getLightEmitterComponent();

	default Array<LightSource> getLightSources() {
		return getLightEmitterComponent().getLightSources();
	}

	default void tick(float delta) {
		getLightEmitterComponent().tick(delta);
	}
}