package com.normalpainter.world.component.location;

import com.badlogic.gdx.math.Vector2;
import com.normalpainter.world.World;

import static com.normalpainter.util.Validation.ensureNotNull;

/**
 * Simple implementation of LocationComponent using a vector for position.
 * <p>
 * Created on 2018-01-27.
 *
 * @author Alexander Winter
 */
public class LocationComponentImpl implements LocationComponent
{
	private final World world;
	private final Vector2 position = new Vector2();

	public LocationComponentImpl(World world, float x, float y)
	{
		ensureNotNull(world, "world");
		this.world = world;
		this.position.set(x, y);
	}

	@Override
	public World getWorld()
	{
		return world;
	}

	@Override
	public Vector2 getPosition()
	{
		return position;
	}
}
