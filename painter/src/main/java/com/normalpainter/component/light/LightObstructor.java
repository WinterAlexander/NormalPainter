package com.normalpainter.component.light;

import com.badlogic.gdx.math.Vector2;

/**
 * Object in the Jumpaï world which obstructs light. LightObstructors have an
 * obstruction range. Not all light is blocked in that range but all obstruction
 * of light must lie within that range.
 * <p>
 * Created on 2019-09-08.
 *
 * @author Alexander Winter
 */
public interface LightObstructor
{
	/**
	 * Checks if the light is obstructed at the specified position. Should
	 * always return false if specified position is not in obstruction range
	 *
	 * @param position where to check if light is blocked
	 * @return true if the light is blocked, otherwise false
	 */
	boolean blocks(Vector2 position);

	/**
	 * Returns the obstruction level, a value from 0 to 1 which is a measure
	 * inverse to how much light "spreads" in the tiles. 1 of obstruction means
	 * no spreading and 0 of obstruction is the same as no obstruction.
	 *
	 * @return obstruction level for this light obstructor
	 */
	float getObstructionLevel();

	/**
	 * Position X of where the obstruction range starts.
	 *
	 * @return start of the obstruction region in x
	 */
	float getObsStartX();

	/**
	 * Position Y of where the obstruction range starts.
	 *
	 * @return start of the obstruction region in y
	 */
	float getObsStartY();

	/**
	 * @return end of the obstruction region in x
	 */
	float getObsEndX();

	/**
	 * @return end of the obstruction region in y
	 */
	float getObsEndY();
}
