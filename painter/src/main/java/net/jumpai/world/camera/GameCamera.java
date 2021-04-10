package net.jumpai.world.camera;

/**
 * Camera present in a game world
 * <p>
 * Created on 2017-11-08.
 *
 * @author Alexander Winter
 */
public interface GameCamera extends WorldCamera
{
	/**
	 * Makes the camera jump to the target instead of gradually reaching it
	 */
	void jump();

	/**
	 * Makes the camera move softly until it reached it's target
	 */
	void softTransition();

	/**
	 * Makes the camera jump to the target instead of gradually reaching it
	 */
	void tick(float delta);

	/**
	 * @return current target of the camera
	 */
	CameraTarget getTarget();

	/**
	 * Changes the target of the camera
	 *
	 * @param target target of camera
	 */
	void setTarget(CameraTarget target);

	/**
	 * @return boundaries of the camera
	 */
	WorldBoundaries getBounds();

	/**
	 * Changes the boundaries of the camera
	 *
	 * @param bounds boundaries of the camera
	 */
	void setBounds(WorldBoundaries bounds);
}
