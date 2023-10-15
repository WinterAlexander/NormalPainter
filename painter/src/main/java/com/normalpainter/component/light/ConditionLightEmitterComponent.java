package com.normalpainter.component.light;

import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.Validation;

import java.util.function.BooleanSupplier;

/**
 * {@link LightEmitterComponent} which only emits light when the specified
 * condition is fulfilled
 * <p>
 * Created on 2020-11-15.
 *
 * @author Alexander Winter
 */
public class ConditionLightEmitterComponent implements LightEmitterComponent
{
	private final LightEmitterComponent lightEmitterComponent;
	private final BooleanSupplier condition;

	private final Array<LightSource> empty = new Array<>(0);

	public ConditionLightEmitterComponent(LightEmitterComponent lightEmitterComponent, BooleanSupplier condition)
	{
		Validation.ensureNotNull(lightEmitterComponent, "lightEmitterComponent");
		Validation.ensureNotNull(condition, "condition");

		this.lightEmitterComponent = lightEmitterComponent;
		this.condition = condition;
	}

	@Override
	public Array<LightSource> getLightSources()
	{
		return condition.getAsBoolean() ? lightEmitterComponent.getLightSources() : empty;
	}

	@Override
	public void tick(float delta)
	{
		lightEmitterComponent.tick(delta);
	}
}
