package com.normalpainter.world.component;

import com.normalpainter.world.WorldObject;

/**
 * Represents a WorldObject that should be ticked every frame
 * <p>
 * Created on 2016-11-28.
 *
 * @author Alexander Winter
 */
public interface Tickable extends WorldObject
{
	/**
	 * Executes this Tickable's logic
	 * @param delta time between this tick and previous one
	 */
	void tick(float delta);
}
