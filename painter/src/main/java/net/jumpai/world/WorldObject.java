package net.jumpai.world;

/**
 * An object in a World, can be anything
 * <p>
 * Created on 2016-11-27.
 *
 * @author Alexander Winter
 */
public interface WorldObject
{
	/**
	 * Returns the world in which this WorldObject was made for. Note that
	 * removing the object from the world doesn't remove the object's reference
	 * for its world. An object can't change its world, instead a deep copy of
	 * the object must be done.
	 *
	 * @return world of this WorldObject
	 */
	World getWorld();
}
