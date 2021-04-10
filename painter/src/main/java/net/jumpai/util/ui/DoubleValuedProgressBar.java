package net.jumpai.util.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import net.jumpai.util.ReflectionUtil;

/**
 * An always-horizontal progress bar which has a second value displayed as
 * the end of the knobAfter drawable
 * <p>
 * Created on 2020-03-04.
 *
 * @author Alexander Winter
 */
public class DoubleValuedProgressBar extends ProgressBar
{
	private float endValue, endAnimateDuration, endAnimateFromValue, endAnimateTime;
	private Interpolation endAnimateInterpolation = Interpolation.linear, endVisualInterpolation = Interpolation.linear;

	public DoubleValuedProgressBar(float min, float max, float stepSize, Skin skin)
	{
		super(min, max, stepSize, false, skin);
		endValue = min;
	}

	public DoubleValuedProgressBar(float min, float max, float stepSize, Skin skin, String styleName)
	{
		super(min, max, stepSize, false, skin, styleName);
		endValue = min;
	}

	public DoubleValuedProgressBar(float min, float max, float stepSize, ProgressBarStyle style)
	{
		super(min, max, stepSize, false, style);
		endValue = min;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		if(endAnimateTime > 0)
		{
			endAnimateTime -= delta;
			Stage stage = getStage();
			if(stage != null && stage.getActionsRequestRendering())
				Gdx.graphics.requestRendering();
		}
	}

	protected Drawable getBackgroundDrawable()
	{
		return (isDisabled() && getStyle().disabledBackground != null)
				? getStyle().disabledBackground
				: getStyle().background;
	}

	protected Drawable getKnobBeforeDrawable()
	{
		return (isDisabled() && this.getStyle().disabledKnobBefore != null)
				? this.getStyle().disabledKnobBefore
				: this.getStyle().knobBefore;
	}

	protected Drawable getKnobAfterDrawable()
	{
		return (isDisabled() && this.getStyle().disabledKnobAfter != null)
				? this.getStyle().disabledKnobAfter
				: this.getStyle().knobAfter;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		final Drawable knob = getKnobDrawable();
		final Drawable bg = getBackgroundDrawable();
		final Drawable knobBefore = getKnobBeforeDrawable();
		final Drawable knobAfter = getKnobAfterDrawable();

		Color color = getColor();
		float x = getX();
		float y = getY();
		float width = getWidth();
		float height = getHeight();
		float knobWidth = knob == null ? 0 : knob.getMinWidth();
		float knobHeight = knob == null ? 0 : knob.getMinHeight();
		float percent = getVisualPercent();
		float position;
		boolean secondPartShowing = isSecondPartShowing();

		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float positionWidth = width;
		boolean round = ReflectionUtil.get(this, "round");

		float bgLeftWidth = 0, bgRightWidth = 0;
		if(bg != null)
		{
			if(round)
				bg.draw(batch, x, Math.round(y + (height - bg.getMinHeight()) * 0.5f), width, Math.round(bg.getMinHeight()));
			else
				bg.draw(batch, x, y + (height - bg.getMinHeight()) * 0.5f, width, bg.getMinHeight());
			bgLeftWidth = bg.getLeftWidth();
			bgRightWidth = bg.getRightWidth();
			positionWidth -= bgLeftWidth + bgRightWidth;
		}

		float knobWidthHalf;

		if(knob == null)
		{
			knobWidthHalf = knobBefore == null ? 0 : knobBefore.getMinWidth() * 0.5f;
			position = (positionWidth - knobWidthHalf) * percent;
			position = Math.min(positionWidth - knobWidthHalf, position);
		}
		else
		{
			knobWidthHalf = knobWidth * 0.5f;
			position = (positionWidth - knobWidth) * percent;
			position = Math.min(positionWidth - knobWidth, position) + bgLeftWidth;
		}

		position = Math.max(Math.min(0, bgLeftWidth), position);

		if(knobBefore != null)
		{
			if(round)
				knobBefore.draw(batch,
						Math.round(x + bgLeftWidth),
						Math.round(y + (height - knobBefore.getMinHeight()) * 0.5f),
						Math.round(position + knobWidthHalf),
						Math.round(knobBefore.getMinHeight()));
			else
				knobBefore.draw(batch,
						x + bgLeftWidth,
						y + (height - knobBefore.getMinHeight()) * 0.5f,
						position + knobWidthHalf,
						knobBefore.getMinHeight());
		}

		if(secondPartShowing && knobAfter != null)
		{
			float knobAfterWidth = (width - knobWidthHalf - bgRightWidth) * getVisualEndPercent() - position;
			knobAfterWidth = Math.max(knobAfterWidth, knobAfter.getMinWidth());
			knobAfterWidth = Math.min(knobAfterWidth, width - knobWidthHalf - bgRightWidth - position);

			if(round)
				knobAfter.draw(batch,
						Math.round(x + position + knobWidthHalf),
						Math.round(y + (height - knobAfter.getMinHeight()) * 0.5f),
						Math.round(knobAfterWidth),
						Math.round(knobAfter.getMinHeight()));
			else
				knobAfter.draw(batch,
						x + position + knobWidthHalf,
						y + (height - knobAfter.getMinHeight()) * 0.5f,
						knobAfterWidth,
						knobAfter.getMinHeight());
		}

		if(secondPartShowing && knob != null)
		{
			if(round)
				knob.draw(batch,
						Math.round(x + position),
						Math.round(y + (height - knobHeight) * 0.5f),
						Math.round(knobWidth),
						Math.round(knobHeight));
			else
				knob.draw(batch,
						x + position,
						y + (height - knobHeight) * 0.5f,
						knobWidth,
						knobHeight);
		}

		ReflectionUtil.set(this, "position", position);
	}

	public boolean isSecondPartShowing()
	{
		return getEndPercent() > getPercent();
	}

	public boolean setEndValue(float value) {
		value = clamp(Math.round(value / getStepSize()) * getStepSize());
		float oldValue = this.endValue;
		if (value == oldValue) return false;
		float oldVisualValue = getVisualValue();
		this.endValue = value;
		ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
		boolean cancelled = fire(changeEvent);
		if (cancelled)
			this.endValue = oldValue;
		else if (endAnimateDuration > 0) {
			endAnimateFromValue = oldVisualValue;
			endAnimateTime = endAnimateDuration;
		}
		Pools.free(changeEvent);
		return !cancelled;
	}

	@Override
	public void setRange(float min, float max)
	{
		super.setRange(min, max);
		if(endValue > max)
			setEndValue(max);
		if(endValue < min)
			setEndValue(min);
	}

	public void setEndAnimateDuration(float duration)
	{
		this.endAnimateDuration = duration;
	}

	public float getVisualEndValue()
	{
		if (endAnimateTime > 0)
			return endAnimateInterpolation.apply(endAnimateFromValue, endValue, 1f - endAnimateTime / endAnimateDuration);
		return endValue;
	}

	public float getEndPercent()
	{
		if(getMinValue() == getMaxValue())
			return 0;

		return (endValue - getMinValue()) / (getMaxValue() - getMinValue());
	}

	public float getVisualEndPercent()
	{
		if(getMinValue() == getMaxValue())
			return 0;

		return endVisualInterpolation.apply((getVisualEndValue() - getMinValue()) / (getMaxValue() - getMinValue()));
	}

	public void setEndAnimateInterpolation(Interpolation endAnimateInterpolation)
	{
		this.endAnimateInterpolation = endAnimateInterpolation;
	}

	public void setEndVisualInterpolation(Interpolation endVisualInterpolation)
	{
		this.endVisualInterpolation = endVisualInterpolation;
	}
}
