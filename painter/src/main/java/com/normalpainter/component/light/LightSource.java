package com.normalpainter.component.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.Validation;

import java.util.function.Supplier;

/**
 * A source of light
 * <p>
 * Created on 2019-09-08.
 *
 * @author Alexander Winter
 */
public class LightSource
{
	private final Supplier<Vector2> location;
	private final Color color;
	private float radius, attenuation;

	public LightSource(Vector2 location, Color color, float radius, float attenuation)
	{
		this(() -> location, color, radius, attenuation);
	}

	public LightSource(Supplier<Vector2> location, Color color, float radius, float attenuation)
	{
		Validation.ensureNotNull(location, "location");
		Validation.ensureNotNull(color, "color");

		this.location = location;
		this.color = color;
		this.radius = radius;
		this.attenuation = attenuation;
	}

	public void update(float delta) {}

	public Vector2 getLocation()
	{
		return location.get();
	}

	public Color getColor()
	{
		return color;
	}

	public float getRadius()
	{
		return radius;
	}

	public void setRadius(float radius)
	{
		this.radius = radius;
	}

	public float getAttenuation()
	{
		return attenuation;
	}

	public void setAttenuation(float attenuation)
	{
		this.attenuation = attenuation;
	}
}