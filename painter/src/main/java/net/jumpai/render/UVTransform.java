package net.jumpai.render;

/**
 * A type of transformation UV texture coordinates can sustain when drawing
 * <p>
 * Created on 2020-11-30.
 *
 * @author Alexander Winter
 */
public enum UVTransform
{
	NONE(true, true, true, false, false, false, false, true),
	FLIPPED_X(false, true, false, false, true, false, true, true),
	FLIPPED_Y(true, false, true, true, false, true, false, false),
	UPSIDE_DOWN(false, false, false, true, true, true, true, false),

	COUNTER_CLOCKWISE(true, false, false, false, false, true, true, true),
	FLIPPED_CLOCKWISE(false, false, true, false, true, true, false, true),
	FLIPPED_COUNTER_CLOCKWISE(true, true, false, true, false, false, true, false),
	CLOCKWISE(false, true, true, true, true, false, false, false),
	;

	static {
		for(UVTransform transform : values())
			for(UVTransform transform2 : values())
			{
				if(transform == transform2)
					continue;

				if(transform.p1u == transform2.p1u
						&& transform.p1v == transform2.p1v
						&& transform.p2u == transform2.p2u
						&& transform.p2v == transform2.p2v
						&& transform.p3u == transform2.p3u
						&& transform.p3v == transform2.p3v
						&& transform.p4u == transform2.p4u
						&& transform.p4v == transform2.p4v)
					throw new IllegalStateException("Duplicates in UVTransform");
			}
	}

	private final boolean p1u, p1v, p2u, p2v, p3u, p3v, p4u, p4v;

	UVTransform(boolean p1u, boolean p1v, boolean p2u, boolean p2v, boolean p3u, boolean p3v, boolean p4u, boolean p4v)
	{
		this.p1u = p1u;
		this.p1v = p1v;
		this.p2u = p2u;
		this.p2v = p2v;
		this.p3u = p3u;
		this.p3v = p3v;
		this.p4u = p4u;
		this.p4v = p4v;
	}

	public UVTransform opposite()
	{
		switch(this)
		{
			case NONE:
			case FLIPPED_X:
			case FLIPPED_Y:
			case UPSIDE_DOWN:
				return this;

			case CLOCKWISE:
				return COUNTER_CLOCKWISE;
			case COUNTER_CLOCKWISE:
				return CLOCKWISE;
			case FLIPPED_CLOCKWISE:
				return FLIPPED_COUNTER_CLOCKWISE;
			case FLIPPED_COUNTER_CLOCKWISE:
				return FLIPPED_CLOCKWISE;

			default:
				throw new Error("Unreachable switch case");
		}
	}

	public UVTransform flipX()
	{
		int v = ordinal();

		return UVTransform.values()[v ^ 1];
	}

	public UVTransform flipY()
	{
		int v = ordinal();

		return UVTransform.values()[v ^ 2];
	}

	/**
	 * Rotates this UV transform the specified amount of 90 degrees in the
	 * counter clockwise direction
	 * @param amount amount of 90 degree rotations to perform, 1 for cc, 2 for 180, 3 or -1 for c
	 * @return rotated UVTransform
	 */
	public UVTransform rotate(int amount)
	{
		amount = Math.floorMod(amount, 4);
		switch(amount)
		{
			case 0:
				return this;
			case 1:
				return rotateCounterClockwise();
			case 2:
				return rotate180();
			case 3:
				return rotateClockwise();
			default:
				throw new Error("Unreachable switch case");
		}
	}

	public UVTransform rotateCounterClockwise()
	{
		switch(this)
		{
			case NONE: return COUNTER_CLOCKWISE;
			case COUNTER_CLOCKWISE: return UPSIDE_DOWN;
			case UPSIDE_DOWN: return CLOCKWISE;
			case CLOCKWISE: return NONE;

			case FLIPPED_X: return FLIPPED_COUNTER_CLOCKWISE;
			case FLIPPED_COUNTER_CLOCKWISE: return FLIPPED_Y;
			case FLIPPED_Y: return FLIPPED_CLOCKWISE;
			case FLIPPED_CLOCKWISE: return FLIPPED_X;

			default:
				throw new Error("Unreachable switch case");
		}
	}

	public UVTransform rotate180()
	{
		return rotateCounterClockwise().rotateCounterClockwise();
	}

	public UVTransform rotateClockwise()
	{
		return rotateCounterClockwise().rotateCounterClockwise().rotateCounterClockwise();
	}

	public float getP1U(float u, float u2)
	{
		return p1u ? u : u2;
	}

	public float getP1V(float v, float v2)
	{
		return p1v ? v : v2;
	}

	public float getP2U(float u, float u2)
	{
		return p2u ? u : u2;
	}

	public float getP2V(float v, float v2)
	{
		return p2v ? v : v2;
	}

	public float getP3U(float u, float u2)
	{
		return p3u ? u : u2;
	}

	public float getP3V(float v, float v2)
	{
		return p3v ? v : v2;
	}

	public float getP4U(float u, float u2)
	{
		return p4u ? u : u2;
	}

	public float getP4V(float v, float v2)
	{
		return p4v ? v : v2;
	}
}
