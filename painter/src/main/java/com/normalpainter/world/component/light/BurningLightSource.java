package com.normalpainter.world.component.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Supplier;

/**
 * {@link LightSource} that flickers
 * <p>
 * Created on 2020-09-20.
 *
 * @author Alexander Winter
 */
public class BurningLightSource extends LightSource
{
	private final float baseRadius, radiusChange;
	private final float baseAtt, attChange;

	private boolean frameSkip = false, frameSkip2 = false;

	public BurningLightSource(Vector2 location,
	                          Color color,
	                          float baseRadius,
	                          float radiusChange,
	                          float baseAtt,
	                          float attChange)
	{
		super(location, color, baseRadius, baseAtt);

		this.baseRadius = baseRadius;
		this.radiusChange = radiusChange;
		this.baseAtt = baseAtt;
		this.attChange = attChange;
	}


	public BurningLightSource(Supplier<Vector2> location,
	                          Color color,
	                          float baseRadius,
	                          float radiusChange,
	                          float baseAtt,
	                          float attChange)
	{
		super(location, color, baseRadius, baseAtt);

		this.baseRadius = baseRadius;
		this.radiusChange = radiusChange;
		this.baseAtt = baseAtt;
		this.attChange = attChange;
	}

	@Override
	public void update(float delta)
	{
		if(frameSkip = !frameSkip)
			return;

		if(frameSkip2 = !frameSkip2)
			return;

		float prevAtt = getAttenuation();
		float newAtt = baseAtt + MathUtils.random() * attChange;

		setAttenuation(prevAtt * 0.5f + newAtt * 0.5f);

		float prevRad = getRadius();
		float newRad = baseRadius - MathUtils.random() * radiusChange;

		setRadius(prevRad * 0.5f + newRad * 0.5f);
	}
}
