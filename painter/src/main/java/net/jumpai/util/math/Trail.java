package net.jumpai.util.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.jumpai.CustomSerializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static net.jumpai.util.io.StreamUtil.readBoolean;
import static net.jumpai.util.io.StreamUtil.readFloat;
import static net.jumpai.util.io.StreamUtil.readShort;
import static net.jumpai.util.io.StreamUtil.writeBoolean;
import static net.jumpai.util.io.StreamUtil.writeFloat;
import static net.jumpai.util.io.StreamUtil.writeShort;

/**
 * Represents a trail, a series of points in 2D space. The start of the trail
 * is assumed to be the zero vector and destinations are relative to it.
 * <p>
 * Created on 2019-05-10.
 *
 * @author Alexander Winter
 */
public class Trail implements CustomSerializable
{
	public final Array<Vector2> destinations = new Array<>();
	public boolean cycle = false;

	@Override
	public void readFrom(InputStream input) throws IOException
	{
		destinations.clear();
		cycle = readBoolean(input);
		int size = readShort(input);
		destinations.ensureCapacity(size);

		for(int i = 0; i < size; i++)
			destinations.add(new Vector2(readFloat(input), readFloat(input)));
	}

	@Override
	public void writeTo(OutputStream output) throws IOException
	{
		writeBoolean(output, cycle);
		writeShort(output, destinations.size);
		for(Vector2 dest : destinations)
		{
			writeFloat(output, dest.x);
			writeFloat(output, dest.y);
		}
	}

	public void set(Trail trail)
	{
		destinations.clear();
		destinations.ensureCapacity(trail.destinations.size);

		for(Vector2 vec : trail.destinations)
			destinations.add(vec.cpy());

		cycle = trail.cycle;
	}

	public Trail cpy()
	{
		Trail trail = new Trail();
		trail.set(this);
		return trail;
	}
}
