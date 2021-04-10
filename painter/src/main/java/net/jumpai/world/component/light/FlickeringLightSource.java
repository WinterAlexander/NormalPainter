package net.jumpai.world.component.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Supplier;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link LightSource} that comes from electricity
 * <p>
 * Created on 2020-09-20.
 *
 * @author Alexander Winter
 */
public class FlickeringLightSource extends LightSource
{
	private final float minAtt, maxAtt;

	private final Vector2 actualLoc = new Vector2();
	private final Vector2 maxOffset;

	public FlickeringLightSource(Vector2 location, Vector2 maxOffset, Color color, float radius, float minAtt, float maxAtt)
	{
		super(location, color, radius, minAtt);
		ensureNotNull(maxOffset, "maxOffset");
		this.maxOffset = maxOffset;
		this.minAtt = minAtt;
		this.maxAtt = maxAtt;

		actualLoc.set(super.getLocation());
	}

	public FlickeringLightSource(Supplier<Vector2> location, Vector2 maxOffset, Color color, float radius, float minAtt, float maxAtt)
	{
		super(location, color, radius, minAtt);
		ensureNotNull(maxOffset, "maxOffset");
		this.maxOffset = maxOffset;
		this.minAtt = minAtt;
		this.maxAtt = maxAtt;

		actualLoc.set(super.getLocation());
	}

	@Override
	public void update(float delta)
	{
		actualLoc.set(super.getLocation()).mulAdd(maxOffset, MathUtils.random(-1f, 1f));
		setAttenuation(minAtt + (maxAtt - minAtt) * MathUtils.random());
	}

	@Override
	public Vector2 getLocation()
	{
		return actualLoc;
	}
}
