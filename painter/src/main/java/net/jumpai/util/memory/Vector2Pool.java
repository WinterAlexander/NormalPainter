package net.jumpai.util.memory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Simple pool creating Vector2 objects
 * <p>
 * Created on 2019-03-05.
 *
 * @author Alexander Winter
 */
public class Vector2Pool extends Pool<Vector2>
{
	@Override
	protected Vector2 newObject()
	{
		return new Vector2();
	}
}
