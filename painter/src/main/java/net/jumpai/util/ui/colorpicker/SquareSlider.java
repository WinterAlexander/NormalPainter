package net.jumpai.util.ui.colorpicker;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Slider that allows selection in 2 dimensions
 * <p>
 * Created on 2019-06-13.
 *
 * @author Alexander Winter
 */
public class SquareSlider extends WidgetGroup
{
	private final Vector2 value = new Vector2();

	private final SquareSliderStyle style;
	private final Image knob;

	private final Vector2 min, max;

	public SquareSlider(Vector2 min, Vector2 max, SquareSliderStyle style)
	{
		ensureNotNull(min, "min");
		ensureNotNull(max, "max");
		ensureNotNull(style, "style");

		this.min = min;
		this.max = max;
		this.style = style;

		knob = new Image(style.knob);
		knob.setSize(style.knob.getMinWidth(),
				style.knob.getMinHeight());
		knob.setTouchable(Touchable.disabled);
		addActor(knob);

		setTouchable(Touchable.enabled);
		addListener(new SquareSliderListener(this));
	}

	@Override
	public void act(float delta)
	{
		knob.setPosition(value.x * getWidth(),
				value.y * getHeight(),
				Align.center);

		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.validate();
		style.background.draw(batch,
				getX(),
				getY(),
				getWidth(),
				getHeight());
		super.draw(batch, parentAlpha);
	}

	public Vector2 getValue()
	{
		return value;
	}

	public void setValue(float x, float y)
	{
		x = clamp(x, min.x, max.x);
		y = clamp(y, min.y, max.y);

		float oldX = value.x;
		float oldY = value.y;

		value.set(x, y);

		ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);

		if(fire(changeEvent))
			value.set(oldX, oldY);

		Pools.free(changeEvent);
	}

	public Vector2 getMin()
	{
		return min;
	}

	public Vector2 getMax()
	{
		return max;
	}
}
