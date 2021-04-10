package com.normalpainter.world.component.light;

import com.badlogic.gdx.math.Vector2;

/**
 * Component implementing the behavior of a {@link LightObstructor}
 * <p>
 * Created on 2019-09-08.
 *
 * @author Alexander Winter
 */
public interface LightObstructionComponent
{
	/**
	 * @see LightObstructor#blocks(Vector2)
	 */
	boolean blocks(Vector2 position);

	/**
	 * @see LightObstructor#getObstructionLevel()
	 */
	float getObstructionLevel();

	/**
	 * @see LightObstructor#getObsStartX()
	 */
	float getObsStartX();

	/**
	 * @see LightObstructor#getObsStartY()
	 */
	float getObsStartY();

	/**
	 * @see LightObstructor#getObsEndX()
	 */
	float getObsEndX();

	/**
	 * @see LightObstructor#getObsEndY()
	 */
	float getObsEndY();
}
