package net.jumpai.world.component.location;

import com.badlogic.gdx.math.Vector2;
import net.jumpai.world.World;

/**
 * A component managing location for a WorldObject.
 * <p>
 * The location is composed of the world and the position (x, y) of the object.
 * <p>
 * Created on 2018-01-27.
 *
 * @see Localizable
 * @author Alexander Winter
 */
public interface LocationComponent
{
	World getWorld();

	Vector2 getPosition();
}
