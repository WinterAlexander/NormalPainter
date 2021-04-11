package com.normalpainter.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static java.lang.StrictMath.signum;
import static com.normalpainter.util.math.MathUtil.pow2;

/**
 * A camera for the WorldScreen, it gives a snapshot to the player by looking at where it is going
 * <p>
 * Created on 2016-11-28.
 *
 * @author Alexander Winter
 */
public class WorldCameraImpl extends OrthographicCamera implements WorldCamera
{
	private final Vector2 tmpPosition = new Vector2();
	private final Vector2 tmpViewSize = new Vector2();

	/**
	 * Position of the GameCamera as returned by getPosition(), actual position
	 * of the OrthographicCamera differs from this one for zoom adjustments only.
	 */
	private final Vector2 logicalPosition = new Vector2();

	public void setPosition(Vector2 pos)
	{
		setPosition(pos.x, pos.y);
	}

	public void setPosition(Vector3 pos)
	{
		setPosition(pos.x, pos.y);
	}

	/**
	 * Used to teleport the camera to a specified position
	 */
	public void setPosition(float x, float y)
	{
		logicalPosition.set(x, y);

		position.set(logicalPosition, 0f);
	}

	@Override
	public Vector2 getLogicalPosition()
	{
		return logicalPosition;
	}

	@Override
	public Vector2 getActualPosition()
	{
		tmpPosition.set(position.x, position.y);
		return tmpPosition;
	}

	@Override
	public Vector2 getViewSize()
	{
		tmpViewSize.set(viewportWidth, viewportHeight);
		return tmpViewSize;
	}

	@Override
	public float getLogicalZoom()
	{
		return 1f;
	}

	@Override
	public float getActualZoom()
	{
		return zoom;
	}
}
