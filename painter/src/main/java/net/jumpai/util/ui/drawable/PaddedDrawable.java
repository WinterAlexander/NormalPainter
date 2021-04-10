package net.jumpai.util.ui.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * {@link WrapperDrawable} which draws its drawable with some padding
 * <p>
 * Created on 2020-03-07.
 *
 * @author Alexander Winter
 */
public class PaddedDrawable extends WrapperDrawable
{
	public float padLeft, padRight, padTop, padBottom;

	public PaddedDrawable()
	{
		this(new NullDrawable());
	}

	public PaddedDrawable(Drawable drawable)
	{
		super(drawable);
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		super.draw(batch, x + padLeft, y + padTop, width - (padLeft + padRight), height - (padBottom + padTop));
	}

	@Override
	public float getLeftWidth()
	{
		return super.getLeftWidth() + padLeft;
	}

	@Override
	public float getRightWidth()
	{
		return super.getRightWidth() + padRight;
	}

	@Override
	public float getTopHeight()
	{
		return super.getTopHeight() + padTop;
	}

	@Override
	public float getBottomHeight()
	{
		return super.getBottomHeight() + padBottom;
	}

	@Override
	public float getMinWidth()
	{
		return super.getMinWidth() + padLeft + padRight;
	}

	@Override
	public float getMinHeight()
	{
		return super.getMinHeight() + padTop + padBottom;
	}
}
