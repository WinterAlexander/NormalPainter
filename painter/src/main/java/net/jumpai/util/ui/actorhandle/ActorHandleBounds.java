package net.jumpai.util.ui.actorhandle;

import com.badlogic.gdx.math.Vector2;

import static net.jumpai.util.Validation.ensurePositive;
import static net.jumpai.util.ui.actorhandle.ActorHandleBounds.DragDirection.BOTTOM;
import static net.jumpai.util.ui.actorhandle.ActorHandleBounds.DragDirection.LEFT;
import static net.jumpai.util.ui.actorhandle.ActorHandleBounds.DragDirection.RIGHT;
import static net.jumpai.util.ui.actorhandle.ActorHandleBounds.DragDirection.TOP;

/**
 * Bounds of the dragging of an ActorHandle
 * <p>
 * Created on 2018-04-22.
 *
 * @author Alexander Winter
 */
public class ActorHandleBounds
{
	private float top, left, bottom, right;

	public ActorHandleBounds(DragDirection direction, float size)
	{
		this(direction == TOP ? size : 0,
				direction == LEFT ? size : 0,
				direction == BOTTOM ? size : 0,
				direction == RIGHT ? size : 0);
	}

	public ActorHandleBounds(float top, float left, float bottom, float right)
	{
		setTop(top);
		setLeft(left);
		setBottom(bottom);
		setRight(right);
	}

	/**
	 * Ensures the specified position are within bounds
	 *
	 * @param pos position to replace
	 */
	public void replace(Vector2 pos)
	{
		if(pos.x < -left)
			pos.x = -left;

		if(pos.x > right)
			pos.x = right;

		if(pos.y > top)
			pos.y = top;

		if(pos.y < -bottom)
			pos.y = -bottom;
	}

	public float getTop()
	{
		return top;
	}

	public float getLeft()
	{
		return left;
	}

	public float getBottom()
	{
		return bottom;
	}

	public float getRight()
	{
		return right;
	}

	public void setTop(float top)
	{
		ensurePositive(top, "top");
		this.top = top;
	}

	public void setLeft(float left)
	{
		ensurePositive(left, "left");
		this.left = left;
	}

	public void setBottom(float bottom)
	{
		ensurePositive(bottom, "bottom");
		this.bottom = bottom;
	}

	public void setRight(float right)
	{
		ensurePositive(right, "right");
		this.right = right;
	}

	public enum DragDirection
	{
		TOP, LEFT, BOTTOM, RIGHT
	}
}
