package net.jumpai.world.component.light;

import com.badlogic.gdx.utils.Array;

import java.util.function.BooleanSupplier;

import static net.jumpai.util.Validation.ensureNotNull;

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
		ensureNotNull(lightEmitterComponent, "lightEmitterComponent");
		ensureNotNull(condition, "condition");

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
