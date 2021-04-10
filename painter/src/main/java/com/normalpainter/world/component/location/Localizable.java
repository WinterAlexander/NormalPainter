package com.normalpainter.world.component.location;

import com.badlogic.gdx.math.Vector2;
import com.normalpainter.world.World;
import com.normalpainter.world.WorldObject;

/**
 * Represents an object that has a position in the world.
 * <p>
 * Created on 2017-01-02.
 *
 * @author Alexander Winter
 */
public interface Localizable extends WorldObject
{
	LocationComponent getLocationComponent();

	@Override
	default World getWorld() {
		return getLocationComponent().getWorld();
	}

	default Vector2 getPosition() {
		return getLocationComponent().getPosition();
	}

	default float getX() {
		return getPosition().x;
	}

	default float getY() {
		return getPosition().y;
	}
}
