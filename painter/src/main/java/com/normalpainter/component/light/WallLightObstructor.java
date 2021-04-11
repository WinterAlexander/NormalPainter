package com.normalpainter.component.light;

/**
 * {@link LightObstructor} that blocks light only partially like a wall does
 * <p>
 * Created on 2020-04-16.
 *
 * @author Alexander Winter
 */
public interface WallLightObstructor extends LightFullObstructor
{
	@Override
	default float getObstructionLevel() {
		return 0.15f;
	}
}
