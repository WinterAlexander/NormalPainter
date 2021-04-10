package com.normalpainter.util.ui.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import static com.normalpainter.util.Validation.ensureNotNull;

/**
 * {@link Drawable} that draws itself from another drawable
 * <p>
 * Created on 2019-01-21.
 *
 * @author Alexander Winter
 */
public class WrapperDrawable implements Drawable
{
	protected Drawable drawable;

	public WrapperDrawable(Drawable drawable)
	{
		setDrawable(drawable);
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		drawable.draw(batch, x, y, width, height);
	}

	@Override
	public float getLeftWidth()
	{
		return drawable.getLeftWidth();
	}

	@Override
	public void setLeftWidth(float leftWidth)
	{
		drawable.setLeftWidth(leftWidth);
	}

	@Override
	public float getRightWidth()
	{
		return drawable.getRightWidth();
	}

	@Override
	public void setRightWidth(float rightWidth)
	{
		drawable.setRightWidth(rightWidth);
	}

	@Override
	public float getTopHeight()
	{
		return drawable.getTopHeight();
	}

	@Override
	public void setTopHeight(float topHeight)
	{
		drawable.setTopHeight(topHeight);
	}

	@Override
	public float getBottomHeight()
	{
		return drawable.getBottomHeight();
	}

	@Override
	public void setBottomHeight(float bottomHeight)
	{
		drawable.setBottomHeight(bottomHeight);
	}

	@Override
	public float getMinWidth()
	{
		return drawable.getMinWidth();
	}

	@Override
	public void setMinWidth(float minWidth)
	{
		drawable.setMinWidth(minWidth);
	}

	@Override
	public float getMinHeight()
	{
		return drawable.getMinHeight();
	}

	@Override
	public void setMinHeight(float minHeight)
	{
		drawable.setMinHeight(minHeight);
	}

	public Drawable getDrawable()
	{
		return drawable;
	}

	public void setDrawable(Drawable drawable)
	{
		ensureNotNull(drawable, "drawable");
		this.drawable = drawable;
	}
}
