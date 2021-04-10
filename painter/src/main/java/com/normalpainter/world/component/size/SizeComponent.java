package com.normalpainter.world.component.size;

import com.badlogic.gdx.math.Vector2;

/**
 * Component for Sized objects that provides size and origin
 * <p>
 * Created on 2018-01-23.
 *
 * @author Alexander Winter
 */
public interface SizeComponent
{
	/**
	 * @see Sized#getSize()
	 */
	Vector2 getSize();

	/**
	 * @see Sized#getOrigin()
	 */
	Vector2 getOrigin();
}
