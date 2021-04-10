package net.jumpai.util.ui.actorhandle;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.function.Consumer;

/**
 * Actor that serves as an handle can be dragged to move another actor
 * <p>
 * Created on 2018-04-22.
 *
 * @author Alexander Winter
 */
public class ActorHandle extends Image
{
	private final Vector2 dragStart = new Vector2();
	private final Vector2 pos = new Vector2(), basePos = new Vector2();

	private final Vector2 tmpVec = new Vector2();

	public ActorHandle(ActorHandleBounds bounds, Actor toMove, Skin skin)
	{
		this(bounds, toMove, skin.get(ActorHandleStyle.class));
	}

	public ActorHandle(ActorHandleBounds bounds, Actor toMove, Skin skin, String styleName)
	{
		this(bounds, toMove, skin.get(styleName, ActorHandleStyle.class));
	}

	public ActorHandle(ActorHandleBounds bounds, Actor toMove, ActorHandleStyle style)
	{
		this(bounds,
				vec -> vec.set(toMove.getX(), toMove.getY()),
				vec -> toMove.moveBy(vec.x, vec.y),
				style);
	}

	public ActorHandle(ActorHandleBounds bounds,
	                   Consumer<Vector2> basePosSetter,
	                   Consumer<Vector2> movementHandler,
	                   ActorHandleStyle style)
	{
		setTouchable(Touchable.enabled);
		setStyle(style);

		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				if(button != Buttons.LEFT)
					return false;

				dragStart.set(x, y);
				pos.set(0, 0);
				basePosSetter.accept(basePos);
				return true; // accept the drag
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer)
			{
				float dx = pos.x;
				float dy = pos.y;

				pos.add(x, y).sub(dragStart).add(basePos);

				bounds.replace(pos);

				pos.sub(basePos);

				dx = pos.x - dx; // Δx = x_f - x_i
				dy = pos.y - dy;

				movementHandler.accept(tmpVec.set(dx, dy));
			}
		});
	}

	public void setStyle(ActorHandleStyle style)
	{
		setDrawable(style.background);
	}

	public Vector2 getPosition()
	{
		return pos;
	}
}
