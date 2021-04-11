package com.normalpainter.component.size;

import com.badlogic.gdx.math.Vector2;
import com.normalpainter.util.math.MathUtil;
import com.normalpainter.component.location.Localizable;

import static com.normalpainter.util.math.MathUtil.inBox;

/**
 * Represents a WorldObject that has a size and an origin to locate its size
 * relative to its position.
 * <p>
 * Created on 2016-12-02.
 *
 * @author Alexander Winter
 */
public interface Sized extends Localizable
{
	SizeComponent getSizeComponent();

	/**
	 * @return width of this object
	 */
	default float getWidth() {
		return getSize().x;
	}

	/**
	 * @return height of this object
	 */
	default float getHeight() {
		return getSize().y;
	}

	/**
	 * The size of a sized object is a vector with the width of the object as
	 * its x component and the height of the object as its y component.
	 *
	 * @return size of object
	 */
	default Vector2 getSize() {
		return getSizeComponent().getSize();
	}

	/**
	 * The origin of a sized object is a vector starting from the position of
	 * the object going to the bottom left corner of its AABB defined by it's
	 * size. The origin of an object is used to locate the bounds of an object
	 * relative to its position.
	 * <p>
	 * An origin of (0, 0) means that the position is at the bottom left corner
	 * of the object while an origin of (width / -2, height / -2) has the
	 * position on its center.
	 *
	 * @return origin of object
	 */
	default Vector2 getOrigin() {
		return getSizeComponent().getOrigin();
	}

	default boolean contains(float x, float y) {
		return MathUtil.inBox(x, y, getX() + getOrigin().x, getY() + getOrigin().y, getWidth(), getHeight());
	}

	default boolean contains(Vector2 point) {
		return contains(point.x, point.y);
	}

	default boolean intersects(float x, float y, float width, float height) {
		return MathUtil.boxCollidesBox(x, y, width, height, getX() + getOrigin().x, getY() + getOrigin().y, getWidth(), getHeight());
	}

	default boolean intersects(Vector2 start, Vector2 size) {
		return intersects(start.x, start.y, size.x, size.y);
	}

	default boolean intersects(Vector2 position, Vector2 size, Vector2 origin) {
		return intersects(position.x + origin.x, position.y + origin.y, size.x, size.y);
	}

	default boolean intersects(Sized other) {
		return intersects(other.getPosition(), other.getSize(), other.getOrigin());
	}

	default boolean intersectsAny(Iterable<?> others) {
		for(Object other : others)
			if(other instanceof Sized && ((Sized)other).intersects(this))
				return true;

		return false;
	}

	default Vector2 getCenter(Vector2 out) {
		return out.set(getCenterX(), getCenterY());
	}

	default float getCenterX() {
		return getX() + getWidth() / 2 + getOrigin().x;
	}

	default float getCenterY() {
		return getY() + getHeight() / 2 + getOrigin().y;
	}

	default float getStartX() {
		return getX() + getOrigin().x;
	}

	default float getStartY() {
		return getY() + getOrigin().y;
	}

	default float getEndX() {
		return getStartX() + getWidth();
	}

	default float getEndY() {
		return getStartY() + getHeight();
	}
}
