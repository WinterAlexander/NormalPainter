package com.normalpainter.world;

import com.badlogic.gdx.math.Vector2;
import com.normalpainter.component.location.Localizable;
import com.normalpainter.component.size.Sized;

import static com.normalpainter.util.math.MathUtil.boxFullyInBox;
import static com.normalpainter.util.math.MathUtil.inBox;

/**
 * Camera of a {@link World}
 * <p>
 * Created on 2018-05-30.
 *
 * @author Alexander Winter
 */
public interface WorldCamera
{
	/**
	 * Returns the position of the camera, excluding visual effects like FOV
	 * changes or screen shaking
	 *
	 * @return current position of the camera
	 */
	Vector2 getLogicalPosition();

	/**
	 * Returns the raw final position of the camera including visual effects
	 *
	 * @return actual position of camera
	 */
	default Vector2 getActualPosition() {
		return getLogicalPosition();
	}

	/**
	 * @return size of the viewport of the camera
	 */
	Vector2 getViewSize();

	/**
	 * Returns the zoom of the camera, excluding visual effects like FOV
	 * changes or screen shaking
	 *
	 * @return zoom of the world camera
	 */
	float getLogicalZoom();

	/**
	 * Returns the final zoom of the camera including visual effects
	 *
	 * @return actual zoom of the camera
	 */
	default float getActualZoom() {
		return getLogicalZoom();
	}

	/**
	 * Checks if the point is in the view of this camera
	 *
	 * @param x x coordinate of the point
	 * @param y y coordinate of the point
	 * @return true if in the view, otherwise false
	 */
	default boolean contains(float x, float y) {
		Vector2 pos = getLogicalPosition();
		Vector2 size = getViewSize();
		float zoom = getLogicalZoom();

		return inBox(x, y, pos.x - size.x * zoom / 2f, pos.y - size.y * zoom / 2f, size.x * zoom, size.y * zoom);
	}

	/**
	 * Checks if the specified object is fully contained
	 *
	 * @param object object to check if in view
	 * @return true if in the view, otherwise false
	 */
	default boolean contains(Localizable object) {

		if(!(object instanceof Sized))
			return contains(object.getX(), object.getY());

		Vector2 pos = getLogicalPosition();
		Vector2 size = getViewSize();
		float zoom = getLogicalZoom();

		return boxFullyInBox(object.getX() + ((Sized)object).getOrigin().x,
				object.getY() + ((Sized)object).getOrigin().y,
				((Sized)object).getWidth(),
				((Sized)object).getHeight(),
				pos.x - size.x * zoom / 2f,
				pos.y - size.y * zoom / 2f,
				size.x * zoom,
				size.y * zoom);
	}
}
