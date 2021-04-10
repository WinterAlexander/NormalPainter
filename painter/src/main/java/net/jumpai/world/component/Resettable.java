package net.jumpai.world.component;

import net.jumpai.world.WorldObject;

/**
 * A world object that can be resetted
 * <p>
 * Created on 2017-02-04.
 *
 * @author Alexander Winter
 */
public interface Resettable extends WorldObject
{
	/**
	 * Resets this WorldObject to this initial state
	 */
	void reset();
}
