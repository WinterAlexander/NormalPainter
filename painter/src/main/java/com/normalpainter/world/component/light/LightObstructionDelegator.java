package com.normalpainter.world.component.light;

import com.badlogic.gdx.math.Vector2;

/**
 * {@link LightObstructor} which delegates its duty to a
 * {@link LightObstructionComponent}
 * <p>
 * Created on 2019-09-08.
 *
 * @author Alexander Winter
 */
public interface LightObstructionDelegator extends LightObstructor
{
	LightObstructionComponent getLightObstructionComponent();

	@Override
	default boolean blocks(Vector2 position) {
		return getLightObstructionComponent().blocks(position);
	}

	@Override
	default float getObstructionLevel() {
		return getLightObstructionComponent().getObstructionLevel();
	}

	@Override
	default float getObsStartX() {
		return getLightObstructionComponent().getObsStartX();
	}

	@Override
	default float getObsStartY() {
		return getLightObstructionComponent().getObsStartY();
	}

	@Override
	default float getObsEndX() {
		return getLightObstructionComponent().getObsEndX();
	}

	@Override
	default float getObsEndY() {
		return getLightObstructionComponent().getObsEndY();
	}
}
