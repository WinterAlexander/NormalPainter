package net.jumpai.util.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.signum;

/**
 * Utility class for math functions
 * <p>
 * Created on 2016-12-07.
 *
 * @author Alexander Winter
 */
public class MathUtil
{
	private MathUtil() {}

	public static boolean lineCrossesRect(float lineX1, float lineY1, float lineX2, float lineY2, //line
	                                      float rectX, float rectY, float width, float height) //rect
	{
		float rectX1 = min(rectX, rectX + width);
		float rectX2 = max(rectX, rectX + width);

		float rectY1 = min(rectY, rectY + height);
		float rectY2 = max(rectY, rectY + height);

		float lineMinX = min(lineX1, lineX2);
		float lineMinY = min(lineY1, lineY2);
		float lineMaxX = max(lineX1, lineX2);
		float lineMaxY = max(lineY1, lineY2);

		if(inBox(lineX1, lineY1, rectX, rectY, width, height) || inBox(lineX2, lineY2, rectX, rectY, width, height))
			return true;

		if(lineMinX < rectX1 && lineMaxX > rectX1) //if it crosses the left limit
		{
			float lineYAtCollision = lineY1 + (lineY2 - lineY1) * (rectX1 - lineX1) / (lineX2 - lineX1);

			if(lineYAtCollision > rectY1 && lineYAtCollision < rectY2)
				return true;
		}

		if(lineMinX < rectX2 && lineMaxX > rectX2) //if it crosses the right limit
		{
			float lineYAtCollision = lineY1 + (lineY2 - lineY1) * (rectX2 - lineX1) / (lineX2 - lineX1);

			if(lineYAtCollision > rectY1 && lineYAtCollision < rectY2)
				return true;
		}

		if(lineMinY < rectY1 && lineMaxY > rectY1) //if it crosses the bottom limit
		{
			float lineXAtCollision = lineX1 + (lineX2 - lineX1) * (rectY1 - lineY1) / (lineY2 - lineY1);

			if(lineXAtCollision > rectX1 && lineXAtCollision < rectX2)
				return true;
		}

		if(lineMinY < rectY2 && lineMaxY > rectY2) //if it crosses the top limit
		{
			float lineXAtCollision = lineX1 + (lineX2 - lineX1) * (rectY2 - lineY1) / (lineY2 - lineY1);

			if(lineXAtCollision > rectX1 && lineXAtCollision < rectX2)
				return true;
		}

		return false;
	}

	public static boolean inBox(float px, float py, float x, float y, float size)
	{
		return inBox(px, py, x, y, size, size);
	}

	public static boolean inBox(float px, float py, float x, float y, float width, float height)
	{
		if(px > x + width)
			return false;

		if(px < x)
			return false;

		if(py > y + height)
			return false;

		if(py < y)
			return false;

		return true;
	}

	public static boolean inBox(Vector2 point, Vector2 startPos, Vector2 size)
	{
		return inBox(point.x, point.y, startPos.x, startPos.y, size.x, size.y);
	}

	public static boolean inBox(Vector2 point, Vector2 startPos, Vector2 size, Vector2 origin)
	{
		return inBox(point.x, point.y, startPos.x + origin.x, startPos.y + origin.y, size.x, size.y);
	}

	public static boolean boxCollidesBox(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2)
	{
		if(x1 + w1 <= x2)
			return false;

		if(x1 >= x2 + w2)
			return false;

		if(y1 + h1 <= y2)
			return false;

		if(y1 >= y2 + h2)
			return false;

		return true;
	}

	/**
	 * Checks if box 1 is fully inside box 2
	 *
	 * @param x1 x position of box 1
	 * @param y1 y position of box 1
	 * @param w1 width of box 1
	 * @param h1 height of box 1
	 * @param x2 x position of box 2
	 * @param y2 y position of box 2
	 * @param w2 width of box 2
	 * @param h2 height of box 2
	 * @return true if box 1 is fully inside box 2, otherwise false
	 */
	public static boolean boxFullyInBox(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2)
	{
		return x1 + w1 <= x2 + w2 && x1 >= x2 && y1 + h1 <= y2 + h2 && y1 >= y2;
	}

	public static float pow(float base, int exponent)
	{
		float result = base;

		for(int i = exponent - 1; i --> 0; )
			result *= base;

		return result;
	}

	public static float round(float value, int digits)
	{
		float power = pow(10, digits);
		return Math.round(value * power) / power;
	}

	/**
	 * @return the sign of the number, 1 for positive, -1 for positive
	 */
	public static float signOf(float number)
	{
		float s = Math.signum(number);
		return s == 0.0f ? 1f : s;
	}

	/**
	 * @return the sign of the number, 1 for positive, -1 for positive
	 */
	public static int signOf(int number)
	{
		float s = Math.signum(number);
		return s == 0.0f ? 1 : (int)s;
	}

	public static Vector2 push(Vector2 input, Vector2 direction, float scale, float cap)
	{
		return push(input, direction, scale, cap, input);
	}

	public static Vector2 push(Vector2 input, Vector2 direction, float scale, float cap, Vector2 vel)
	{
		vel.set(input);

		if(vel.x * direction.x < cap)
		{
			vel.x += direction.x * scale;

			if(vel.x * direction.x > cap)
				vel.x = cap * direction.x;
		}

		if(vel.y * direction.y < cap)
		{
			vel.y += direction.y * scale;

			if(vel.y * direction.y > cap)
				vel.y = cap * direction.y;
		}

		return vel;
	}

	public static float decelerate(float velocity, float deceleration, float delta)
	{
		float dir = signum(velocity);

		velocity -= deceleration * delta * dir;

		if(velocity * dir < 0) //if velocity switched it's sign by deceleration
			velocity = 0; //set it to 0, deceleration don't make you switch side

		return velocity;
	}

	public static Vector2 decelerate(Vector2 velocity, float deceleration, float delta)
	{
		float xDir = signum(velocity.x);
		float yDir = signum(velocity.y);

		velocity.x -= deceleration * delta * xDir;

		if(velocity.x * xDir < 0)
			velocity.x = 0;

		velocity.y -= deceleration * delta * yDir;

		if(velocity.y * yDir < 0)
			velocity.y = 0;

		return velocity;
	}

	public static double pow2(double value)
	{
		return value * value;
	}

	public static float pow2(float value)
	{
		return value * value;
	}

	public static int pow2(int value)
	{
		return value * value;
	}

	public static double pow3(double value)
	{
		return value * value * value;
	}

	public static float pow3(float value)
	{
		return value * value * value;
	}

	public static int pow3(int value)
	{
		return value * value * value;
	}

	/**
	 * Negative modulo function. Used for a value to "wrap around" an interval.
	 *
	 * @param value value that can be positive or negative
	 * @param divider divider must be positive
	 * @return remainder of the division
	 */
	public static float negMod(float value, float divider)
	{
		return value - divider * (int)floor(value / divider);
	}

	/**
	 * Cosine function taking in parameter an amount of 90 degrees instead of
	 * an angle. Faster than regular cos when only working with 90 degree angles.
	 *
	 * @param rotation amount of 90 degrees, 4 meaning 1 turn
	 * @return the cosine value of specified rotation
	 */
	public static float cos90(float rotation)
	{
		int rot = Math.floorMod(Math.round(rotation / 90f), 4);

		switch(rot)
		{
			case 0:
				return 1f;
			case 1:
			case 3:
				return 0f;

			case 2:
				return -1f;

			default:
				throw new Error();
		}
	}

	/**
	 * Sine function taking in parameter an amount of 90 degrees instead of
	 * an angle. Faster than regular sin when only working with 90 degree angles.
	 *
	 * @param rotation amount of 90 degrees, 4 meaning 1 turn
	 * @return the sine value of specified rotation
	 */
	public static float sin90(float rotation)
	{
		int rot = Math.floorMod(Math.round(rotation / 90f), 4);

		switch(rot)
		{
			case 0:
			case 2:
				return 0f;

			case 1:
				return 1f;

			case 3:
				return -1f;

			default:
				throw new Error();
		}
	}

	/**
	 * Returns the length of an axis aligned vector
	 *
	 * @param aaVector vector to get the length from
	 * @return length of that axis aligned vector
	 */
	public static float aaLength(Vector2 aaVector)
	{
		return abs(aaVector.x + aaVector.y);
	}

	/**
	 * Cheapest sigmoid function
	 *
	 * @param value input of sigmoid function, from -∞ to ∞
	 * @return value between -1 and 1
	 */
	public static float sigmoid(float value)
	{
		return sigmoid(value, 1.0f);
	}

	/**
	 * Sigmoid function with specified slope at origin, slope is specified as anti slope which is
	 * 1.0f / slope
	 * @param value input of sigmoid function, from -∞ to ∞
	 * @param antiSlope 1.0f / slope, which tweaks the steepness of the curve around the origin
	 * @return value between -1 and 1
	 */
	public static float sigmoid(float value, float antiSlope)
	{
		return value / (antiSlope + abs(value));
	}

	/**
	 * Projects the specified angle to the edge of a rectangle of specified size
	 *
	 * @param angle angle to project at, in degrees
	 * @param width width of the rectangle to project on
	 * @param height height of the rectangle to project on
	 * @return displacement from the center of the rectangle to the projection on the edge
	 */
	public static Vector2 projectToRectEdge(double angle, float width, float height, Vector2 out)
	{
		return projectToRectEdgeRad(Math.toRadians(angle), width, height, out);
	}

	/**
	 * Projects the specified angle to the edge of a rectangle of specified size
	 *
	 * @param angle angle to project at, in radians
	 * @param width width of the rectangle to project on
	 * @param height height of the rectangle to project on
	 * @return displacement from the center of the rectangle to the projection on the edge
	 */
	public static Vector2 projectToRectEdgeRad(double angle, float width, float height, Vector2 out)
	{
		float theta = negMod((float)angle + MathUtils.PI, MathUtils.PI2) - MathUtils.PI;

		float diag = MathUtils.atan2(height, width);
		float tangent = (float)Math.tan(angle);

		if (theta > -diag && theta <= diag)
		{
			out.x = width / 2f;
			out.y = width / 2f * tangent;
		}
		else if(theta > diag && theta <= MathUtils.PI - diag)
		{
			out.x = height / 2f / tangent;
			out.y = height / 2f;
		}
		else if(theta > MathUtils.PI - diag && theta <= MathUtils.PI + diag
			|| theta < -MathUtils.PI + diag && theta >= -MathUtils.PI - diag)
		{
			out.x = -width / 2f;
			out.y = -width / 2f * tangent;
		}
		else
		{
			out.x = -height / 2f / tangent;
			out.y = -height / 2f;
		}

		return out;
	}
}
