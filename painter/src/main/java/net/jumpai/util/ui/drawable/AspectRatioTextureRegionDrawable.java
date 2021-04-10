package net.jumpai.util.ui.drawable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * {@link TextureRegionDrawable} that respects the aspect ratio of its
 * {@link TextureRegion} by drawing it in a smaller portion of the drawable if
 * necessary
 * <p>
 * Created on 2019-12-10.
 *
 * @author Alexander Winter
 */
public class AspectRatioTextureRegionDrawable extends TextureRegionDrawable
{
	public AspectRatioTextureRegionDrawable(Texture texture)
	{
		super(texture);
	}

	public AspectRatioTextureRegionDrawable(TextureRegion region)
	{
		super(region);
	}

	public AspectRatioTextureRegionDrawable(TextureRegionDrawable drawable)
	{
		super(drawable);
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		if(getRegion().getRegionWidth() > getRegion().getRegionHeight())
		{
			float prevHeight = height;
			height = width * getRegion().getRegionHeight() / getRegion().getRegionWidth();
			y += (prevHeight - height) / 2;
		}
		else
		{
			float prevWidth = width;
			width = height * getRegion().getRegionWidth() / getRegion().getRegionHeight();
			x += (prevWidth - width) / 2;
		}

		super.draw(batch, x, y, width, height);
	}

	@Override
	public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation)
	{
		if(getRegion().getRegionWidth() > getRegion().getRegionHeight())
		{
			float prevHeight = height;
			height = width * getRegion().getRegionHeight() / getRegion().getRegionWidth();
			y += (prevHeight - height) / 2;
		}
		else
		{
			float prevWidth = width;
			width = height * getRegion().getRegionWidth() / getRegion().getRegionHeight();
			x += (prevWidth - width) / 2;
		}

		super.draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
	}
}
