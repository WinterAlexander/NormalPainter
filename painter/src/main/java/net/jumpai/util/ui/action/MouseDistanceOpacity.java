package net.jumpai.util.ui.action;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Align;

import static net.jumpai.util.Validation.ensureNotNull;
import static net.jumpai.util.math.MathUtil.pow2;

/**
 * Action that sets the actor opacity to a value depending on the distance
 * between the mouse and center of the actor.
 * <p>
 * Created on 2019-05-16.
 *
 * @author Alexander Winter
 */
public class MouseDistanceOpacity extends Action
{
	private final OpacityFunction function;

	private final Vector3 tmpVec3 = new Vector3();

	public MouseDistanceOpacity(float opaqueDst, float transDst)
	{
		this(x -> {
			float minDst2 = pow2(opaqueDst);
			float maxDst2 = pow2(transDst);

			if(x < minDst2)
				return 1f;

			if(x > maxDst2)
				return 0f;
			return (minDst2 - x) / (maxDst2 - minDst2);
		});
	}

	public MouseDistanceOpacity(OpacityFunction function)
	{
		ensureNotNull(function, "function");
		this.function = function;
	}

	@Override
	public boolean act(float delta)
	{
		tmpVec3.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
		getActor().getStage().getViewport().unproject(tmpVec3);

		float dst2 = tmpVec3.dst2(actor.getX(Align.center), actor.getY(Align.center), 0f);

		actor.getColor().a = function.getAlpha(dst2);

		return false;
	}

	public interface OpacityFunction
	{
		float getAlpha(float distanceSquared);
	}
}
