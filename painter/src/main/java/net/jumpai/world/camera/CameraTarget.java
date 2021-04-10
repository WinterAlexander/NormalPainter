package net.jumpai.world.camera;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents something the camera can look at and follow (e.g. a LocalPlayer).
 * Not necessarily a WorldObject.
 * <p>
 * Created on 2016-11-28.
 *
 * @author Alexander Winter
 */
public interface CameraTarget
{
	/**
	 * Gets the position the camera should look at (the target)
	 * @return position vector for the camera
	 */
	Vector2 getTargetPosition();

	/**
	 * FOV increase is a value meant to increase the FOV of the camera when
	 * something occurs in the gameplay (i.e. player goes fast). Returns 0
	 * for no FOV increase and 1 for maximal FOV increase.
	 *
	 * @return fov increase value from 0 to 1
	 */
	float getFOVIncrease();

	/**
	 * A camera target can make the screen shake by returning true in this
	 * method. As long as this method returns true, the screen will shake and
	 * stop as soon as it returns false.
	 *
	 * @return true if the target is making the screen shake, otherwise false
	 */
	boolean isScreenShaking();
}
