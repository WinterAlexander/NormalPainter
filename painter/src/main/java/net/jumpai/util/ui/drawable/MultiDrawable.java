package net.jumpai.util.ui.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Multiple drawables into one
 * <p>
 * Created on 2017-11-15.
 *
 * @author Alexander Winter
 */
public class MultiDrawable implements Drawable
{
	private final Drawable[] drawables;

	public MultiDrawable(Drawable... drawables)
	{
		this.drawables = drawables;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		for(Drawable drawable : drawables)
			drawable.draw(batch, x, y, width, height);
	}

	@Override
	public float getLeftWidth()
	{
		float val = 0;

		for(Drawable drawable : drawables)
		{
			float current = drawable.getLeftWidth();

			if(current > val)
				val = current;
		}

		return val;
	}

	@Override
	public void setLeftWidth(float leftWidth)
	{
		for(Drawable drawable : drawables)
			drawable.setLeftWidth(leftWidth);
	}

	@Override
	public float getRightWidth()
	{
		float val = 0;

		for(Drawable drawable : drawables)
		{
			float current = drawable.getRightWidth();

			if(current > val)
				val = current;
		}

		return val;
	}

	@Override
	public void setRightWidth(float rightWidth)
	{
		for(Drawable drawable : drawables)
			drawable.setRightWidth(rightWidth);
	}

	@Override
	public float getTopHeight()
	{
		float val = 0;

		for(Drawable drawable : drawables)
		{
			float current = drawable.getTopHeight();

			if(current > val)
				val = current;
		}

		return val;
	}

	@Override
	public void setTopHeight(float topHeight)
	{
		for(Drawable drawable : drawables)
			drawable.setTopHeight(topHeight);
	}

	@Override
	public float getBottomHeight()
	{
		float val = 0;

		for(Drawable drawable : drawables)
		{
			float current = drawable.getBottomHeight();

			if(current > val)
				val = current;
		}

		return val;
	}

	@Override
	public void setBottomHeight(float bottomHeight)
	{
		for(Drawable drawable : drawables)
			drawable.setBottomHeight(bottomHeight);
	}

	@Override
	public float getMinWidth()
	{
		float val = 0;

		for(Drawable drawable : drawables)
		{
			float current = drawable.getMinWidth();

			if(current > val)
				val = current;
		}

		return val;
	}

	@Override
	public void setMinWidth(float minWidth)
	{
		for(Drawable drawable : drawables)
			drawable.setMinWidth(minWidth);
	}

	@Override
	public float getMinHeight()
	{
		float val = 0;

		for(Drawable drawable : drawables)
		{
			float current = drawable.getMinHeight();

			if(current > val)
				val = current;
		}

		return val;
	}

	@Override
	public void setMinHeight(float minHeight)
	{
		for(Drawable drawable : drawables)
			drawable.setMinHeight(minHeight);
	}

	public Drawable[] getDrawables()
	{
		return drawables;
	}
}
