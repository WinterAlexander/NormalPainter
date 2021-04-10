package com.normalpainter.world.component.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Supplier;

/**
 * {@link LightSource} that comes from a source of lava
 * <p>
 * Created on 2020-04-19.
 *
 * @author Alexander Winter
 */
public class LavaLightSource extends BurningLightSource
{
	private static final Color LIGHT_COLOR = new Color(0xFF6200FF);

	public LavaLightSource(Vector2 location)
	{
		this(location, 4000f, 200f, 0.3f, 0.05f);
	}

	public LavaLightSource(Vector2 location, float baseRadius, float radiusChange, float baseAtt, float attChange)
	{
		super(location, LIGHT_COLOR, baseRadius, radiusChange, baseAtt, attChange);
	}

	public LavaLightSource(Supplier<Vector2> location)
	{
		this(location, 4000f, 200f, 0.3f, 0.05f);
	}

	public LavaLightSource(Supplier<Vector2> location, float baseRadius, float radiusChange, float baseAtt, float attChange)
	{
		super(location, LIGHT_COLOR, baseRadius, radiusChange, baseAtt, attChange);
	}
}
