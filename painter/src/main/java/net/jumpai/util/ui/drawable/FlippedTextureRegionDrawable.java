package net.jumpai.util.ui.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Drawable that draws a specified TextureRegion flipped by specified directions.
 * <p>
 * Created on 2018-04-23.
 *
 * @author Alexander Winter
 */
public class FlippedTextureRegionDrawable extends TextureRegionDrawable
{
	public boolean flipX, flipY;

	public FlippedTextureRegionDrawable() {}

	public FlippedTextureRegionDrawable(TextureRegion region, boolean flipX, boolean flipY)
	{
		super(region);
		this.flipX = flipX;
		this.flipY = flipY;
	}

	public FlippedTextureRegionDrawable(FlippedTextureRegionDrawable drawable)
	{
		super(drawable);
		this.flipX = drawable.flipX;
		this.flipY = drawable.flipY;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		draw(batch,
				x, y,
				width / 2, height / 2,
				width, height,
				flipX ? -1f : 1f, flipY ? -1f : 1f, 0);
	}
}
