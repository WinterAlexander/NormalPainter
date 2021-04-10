package net.jumpai.world.camera;

import com.badlogic.gdx.math.Vector2;

/**
 * Camera that does nothing to play a run without visual feedback
 * <p>
 * Created on 2017-11-08.
 *
 * @author Alexander Winters
 */
public class NullCamera implements GameCamera
{
	@Override
	public void jump() {}

	@Override
	public void softTransition() {}

	@Override
	public void tick(float delta) {}

	@Override
	public CameraTarget getTarget() { return null; }

	@Override
	public void setTarget(CameraTarget target) {}

	@Override
	public WorldBoundaries getBounds() { return null; }

	@Override
	public void setBounds(WorldBoundaries bounds) {}

	@Override
	public Vector2 getLogicalPosition() { return null; }

	@Override
	public Vector2 getViewSize() { return null; }

	@Override
	public float getLogicalZoom() { return 1f; }
}
