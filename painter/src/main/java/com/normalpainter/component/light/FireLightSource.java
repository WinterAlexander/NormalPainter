package com.normalpainter.component.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Supplier;

/**
 * {@link LightSource} that comes from a fire
 * <p>
 * Created on 2020-04-19.
 *
 * @author Alexander Winter
 */
public class FireLightSource extends BurningLightSource
{
	private static final Color LIGHT_COLOR = new Color(0xFFAA00FF);

	public FireLightSource(Vector2 location, float radius)
	{
		super(location, LIGHT_COLOR, radius, 750f, 0.2f, 0.05f);
	}

	public FireLightSource(Vector2 location, float baseRadius, float radiusChange, float baseAtt, float attChange)
	{
		super(location, LIGHT_COLOR, baseRadius, radiusChange, baseAtt, attChange);
	}

	public FireLightSource(Supplier<Vector2> location, float radius)
	{
		super(location, LIGHT_COLOR, radius, 750f, 0.2f, 0.05f);
	}

	public FireLightSource(Supplier<Vector2> location, float baseRadius, float radiusChange, float baseAtt, float attChange)
	{
		super(location, LIGHT_COLOR, baseRadius, radiusChange, baseAtt, attChange);
	}
}
