package net.jumpai.util.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Stage.TouchFocus;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Predicate;
import com.badlogic.gdx.utils.SnapshotArray;
import net.jumpai.util.ReflectionUtil;
import net.jumpai.util.ui.drawable.TintedDrawable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.utils.Pools.free;
import static com.badlogic.gdx.utils.Pools.obtain;
import static net.jumpai.util.ReflectionUtil.get;
import static net.jumpai.util.ReflectionUtil.set;

/**
 * Utility class for scene2d usage
 * <p>
 * Created on 2018-01-11.
 *
 * @author Alexander Winter
 */
public class Scene2dUtil
{
	private Scene2dUtil() {}

	public static void ignoreInputEvent(InputEvent event)
	{
		set(event, "handled", false);
	}

	public static void performInputEvent(Actor actor, InputEvent.Type event)
	{
		InputEvent inputEvent = obtain(InputEvent.class);
		inputEvent.reset();
		inputEvent.setButton(0);
		inputEvent.setRelatedActor(actor);

		try
		{
			inputEvent.setType(event);
			actor.fire(inputEvent);
		}
		finally
		{
			free(inputEvent);
		}
	}

	public static void triggerClickListener(Actor actor)
	{
		for(EventListener eventListener : actor.getCaptureListeners())
			if(eventListener instanceof ClickListener)
				((ClickListener)eventListener).clicked(null, 0, 0);

		for(EventListener eventListener : actor.getListeners())
			if(eventListener instanceof ClickListener)
				((ClickListener)eventListener).clicked(null, 0, 0);
	}

	public static void sneakyCancelTouchFocusExcept(Stage stage,
	                                                EventListener exceptListener,
	                                                Actor exceptActor,
	                                                float x,
	                                                float y)
	{
		InputEvent event = Pools.obtain(InputEvent.class);
		event.setStage(stage);
		event.setType(InputEvent.Type.touchUp);
		event.setStageX(x);
		event.setStageY(y);

		// Cancel all current touch focuses except for the specified listener, allowing for concurrent modification, and never
		// cancel the same focus twice.

		SnapshotArray<TouchFocus> touchFocuses = get(stage, "touchFocuses");
		TouchFocus[] items = touchFocuses.begin();
		for(int i = 0, n = touchFocuses.size; i < n; i++)
		{
			TouchFocus focus = items[i];
			if(get(focus, "listener") == exceptListener
					&& get(focus, "listenerActor") == exceptActor)
				continue;

			if(!touchFocuses.removeValue(focus, true))
				continue; // Touch focus already gone.

			event.setTarget(get(focus, "target"));
			event.setListenerActor(get(focus, "listenerActor"));
			event.setPointer(get(focus, "pointer"));
			event.setButton(get(focus, "button"));
			ReflectionUtil.<EventListener>get(focus, "listener").handle(event);
		}

		touchFocuses.end();

		Pools.free(event);
	}

	public static boolean hasDialog(Stage stage)
	{
		return stage.getActors().select(a -> a instanceof Dialog).iterator().hasNext();
	}

	public static boolean hasModalDialog(Stage stage)
	{
		return stage.getActors().select(a -> a instanceof Dialog && ((Dialog)a).isModal()).iterator().hasNext();
	}

	public static boolean isDescendant(Actor actor, Predicate<Actor> parent)
	{
		while(actor != null)
		{
			if(parent.evaluate(actor))
				return true;
			actor = actor.getParent();
		}
		return false;
	}

	public static void invalidateRecursively(Actor actor)
	{
		if(actor instanceof Layout)
			((Layout)actor).invalidate();

		if(actor instanceof Group)
			for(Actor children : ((Group)actor).getChildren())
				invalidateRecursively(children);
	}

	public static void setColorRecursively(Actor actor, Color color)
	{
		actor.setColor(color);
		if(actor instanceof Group)
			for(Actor child : ((Group)actor).getChildren())
				setColorRecursively(child, color);
	}

	public static void scale9Patch(NinePatch ninePatch, float scaleX, float scaleY)
	{
		ninePatch.setLeftWidth(ninePatch.getLeftWidth() * scaleX);
		ninePatch.setRightWidth(ninePatch.getRightWidth() * scaleX);
		ninePatch.setMiddleWidth(ninePatch.getMiddleWidth() * scaleX);

		ninePatch.setBottomHeight(ninePatch.getBottomHeight() * scaleY);
		ninePatch.setTopHeight(ninePatch.getTopHeight() * scaleY);
		ninePatch.setMiddleHeight(ninePatch.getMiddleHeight() * scaleY);

		ninePatch.setPadLeft(ninePatch.getPadLeft() * scaleX);
		ninePatch.setPadRight(ninePatch.getPadRight() * scaleX);
		ninePatch.setPadTop(ninePatch.getPadTop() * scaleY);
		ninePatch.setPadBottom(ninePatch.getPadBottom() * scaleY);
	}

	public static ImageButton makeImageButton(TextureRegion tex, Color hover, Color down)
	{
		return makeImageButton(tex, Color.WHITE, hover, down);
	}

	public static ImageButton makeImageButton(TextureRegion tex, Color normal, Color hover, Color down)
	{
		return makeImageButton(tex, normal, hover, down, new Color(1f, 1f, 1f, 0.5f));
	}

	public static ImageButton makeImageButton(TextureRegion tex, Color normal, Color hover, Color down, Color disabled)
	{
		ImageButtonStyle style = new ImageButtonStyle();
		Drawable drawable = new TextureRegionDrawable(tex);
		style.imageUp = new TintedDrawable(drawable, normal);
		style.imageOver = new TintedDrawable(drawable, hover);
		style.imageDown = new TintedDrawable(drawable, down);
		style.imageDisabled = new TintedDrawable(drawable, disabled);

		return new ImageButton(style);
	}

	public static boolean isKeyEvent(InputEvent event)
	{
		return event.getType() == Type.keyDown
				|| event.getType() == Type.keyUp
				|| event.getType() == Type.keyTyped;
	}

	public static Action every(Runnable runnable, float delay)
	{
		return forever(sequence(run(runnable), delay(delay)));
	}

	public static Action shakeHorizontal(float amountX, float shakingSpeed, float duration)
	{
		return repeat(Math.round(duration / shakingSpeed / 2f),
				sequence(moveBy(amountX, 0, shakingSpeed),
						moveBy(-amountX, 0, shakingSpeed)));
	}

	public static Action shakeVertical(float amountY, float shakingSpeed, float duration)
	{
		return repeat(Math.round(duration / shakingSpeed / 2f),
				sequence(moveBy(0, amountY, shakingSpeed),
						moveBy(0, -amountY, shakingSpeed)));
	}

	public static void processMoveActions(Actor actor)
	{
		for(int i = 0; i < actor.getActions().size; i++)
		{
			Action action = actor.getActions().get(i);
			if(action instanceof MoveToAction)
				actor.setPosition(((MoveToAction)action).getX(), ((MoveToAction)action).getY());

			else if(action instanceof MoveByAction)
			{
				float rem = 1f - get(action, "lastPercent", Float.class);
				actor.moveBy(((MoveByAction)action).getAmountX() * rem, ((MoveByAction)action).getAmountY() * rem);
			}
			else
				continue;

			actor.getActions().removeIndex(i);
			break;
		}
	}

	public static final FPowIn pow1Point5In = new FPowIn(1.5f);

	public static class FPowIn extends Interpolation
	{
		private final float power;

		public FPowIn(float power)
		{
			this.power = power;
		}

		public float apply (float a)
		{
			return (float)Math.pow(a, power);
		}
	}
}
