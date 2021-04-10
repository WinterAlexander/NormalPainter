package com.normalpainter.world.camera;

import com.badlogic.gdx.math.Vector2;
import com.normalpainter.world.WorldObject;

/**
 * Represents the boundaries of a world, usually implemented with a world
 * border. Do not confuse with the bounds of the screen according to the camera.
 * <p>
 * Created on 2017-01-24.
 *
 * @author Alexander Winter
 */
public interface WorldBoundaries extends WorldObject
{
	Vector2 getMin();

	Vector2 getMax();
}
