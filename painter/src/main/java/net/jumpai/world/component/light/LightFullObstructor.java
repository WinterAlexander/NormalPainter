package net.jumpai.world.component.light;

import com.badlogic.gdx.math.Vector2;
import net.jumpai.util.math.MathUtil;
import net.jumpai.world.component.size.Sized;

/**
 * Implementation of {@link LightObstructor} which simply blocks everything
 * within its obstruction range, defaulted as the size of the {@link Sized}
 * object
 * <p>
 * Created on 2019-09-08.
 *
 * @author Alexander Winter
 */
public interface LightFullObstructor extends LightObstructor, Sized
{
	@Override
	default boolean blocks(Vector2 position) {
		float x = getObsStartX();
		float y = getObsStartY();
		float w = getObsEndX() - x;
		float h = getObsEndY() - y;
		return MathUtil.inBox(position.x, position.y, x, y, w, h);
	}

	@Override
	default float getObstructionLevel() {
		return 1f;
	}

	@Override
	default float getObsStartX() {
		return getStartX();
	}

	@Override
	default float getObsStartY() {
		return getStartY();
	}

	@Override
	default float getObsEndX() {
		return getEndX();
	}

	@Override
	default float getObsEndY() {
		return getEndY();
	}
}
