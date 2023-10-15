package com.normalpainter.world;

import com.winteralexander.gdx.utils.log.Logger;

/**
 * Something a WorldObject can exist in
 * <p>
 * Created on 2018-05-28.
 *
 * @author Alexander Winter
 */
public interface World
{
	/**
	 * @return camera of the world
	 */
	WorldCamera getCamera();

	/**
	 * Returns an iterator of all objects of the world. Avoid using this method
	 * if possible.
	 *
	 * @return all objects in the world
	 */
	Iterable<WorldObject> getObjects();

	/**
	 * Adds specified WorldObject to the world
	 *
	 * @param object object to add
	 */
	void add(WorldObject object);

	/**
	 * Removes specified WorldObject to the world
	 *
	 * @param object object to remove
	 */
	void remove(WorldObject object);

	/**
	 * The id of a frame is number identifying this frame compared to all other
	 * frames of the world. Id may be reset at anytime depending on the World's
	 * implementation.
	 *
	 * @return id of the currrent frame
	 */
	long getFrameId();

	/**
	 * World-wide state for electrical objects. When the power is on, electrical
	 * objects should be turned on and vice versa.
	 *
	 * @return true if the power is on in this world, otherwise false
	 */
	boolean isPowerOn();

	Logger getLogger();
}
