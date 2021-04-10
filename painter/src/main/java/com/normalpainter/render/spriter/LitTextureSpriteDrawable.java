package com.normalpainter.render.spriter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.normalpainter.render.lighting.LightRenderMode;
import com.normalpainter.util.Validation;
import me.winter.gdx.animation.Sprite;
import me.winter.gdx.animation.drawable.TextureSpriteDrawable;
import com.normalpainter.render.lighting.LightingBatch;

/**
 * Draws the content of a texture along with its normal map
 * <p>
 * Created on 2020-05-05.
 *
 * @author Alexander Winter
 */
public class LitTextureSpriteDrawable extends TextureSpriteDrawable
{
	private final TextureRegion flat, normalMap, preshaded;
	private final LightRenderMode mode;

	private final boolean usePreshadedWhenLit;

	public LitTextureSpriteDrawable(TextureRegion flat,
	                                TextureRegion normalMap,
	                                TextureRegion preshaded,
	                                float pivotX, float pivotY,
	                                LightRenderMode mode)
	{
		this(flat, normalMap, preshaded, pivotX, pivotY, mode, true);
	}

	public LitTextureSpriteDrawable(TextureRegion flat,
	                                TextureRegion normalMap,
	                                TextureRegion preshaded,
	                                float pivotX, float pivotY,
	                                LightRenderMode mode,
	                                boolean usePreshadedWhenLit)
	{
		super(preshaded == null ? flat : preshaded, pivotX, pivotY);

		Validation.ensureNotNull(mode, "mode");

		this.flat = flat;
		this.normalMap = normalMap;
		this.preshaded = preshaded;
		this.mode = mode;
		this.usePreshadedWhenLit = usePreshadedWhenLit;
	}

	public LitTextureSpriteDrawable(TextureRegion flat,
	                                TextureRegion normalMap,
	                                TextureRegion preshaded,
	                                float pivotX, float pivotY,
	                                float width, float height,
	                                LightRenderMode mode)
	{
		this(flat, normalMap, preshaded, pivotX, pivotY, width, height, mode, true);
	}

	public LitTextureSpriteDrawable(TextureRegion flat,
	                                TextureRegion normalMap,
	                                TextureRegion preshaded,
	                                float pivotX, float pivotY,
	                                float width, float height,
	                                LightRenderMode mode,
	                                boolean usePreshadedWhenLit)
	{
		super(preshaded == null ? flat : preshaded, pivotX, pivotY, width, height);

		Validation.ensureNotNull(mode, "mode");

		this.flat = flat;
		this.normalMap = normalMap;
		this.preshaded = preshaded;
		this.mode = mode;
		this.usePreshadedWhenLit = usePreshadedWhenLit;
	}

	@Override
	public void draw(Sprite sprite, Batch batch)
	{
		if(!(batch instanceof LightingBatch) || normalMap == null)
		{
			super.draw(sprite, batch);
			return;
		}

		LightingBatch lBatch = (LightingBatch)batch;
		LightRenderMode prevMode = lBatch.getLightRenderMode();
		lBatch.setLightRenderMode(mode);

		float originX = width * getPivotX();
		float originY = height * getPivotY();

		float prevColor = batch.getPackedColor();

		Color tmp = batch.getColor();
		tmp.a *= sprite.getAlpha();
		batch.setColor(tmp);

		if(preshaded == null || !usePreshadedWhenLit)
		{
			lBatch.drawNormalMapped(flat, normalMap,
					sprite.getPosition().x - originX,
					sprite.getPosition().y - originY,
					originX,
					originY,
					width,
					height,
					sprite.getScale().x,
					sprite.getScale().y,
					sprite.getAngle());
		}
		else
		{
			lBatch.drawNormalMapped(flat, normalMap, preshaded,
					sprite.getPosition().x - originX,
					sprite.getPosition().y - originY,
					originX,
					originY,
					width,
					height,
					sprite.getScale().x,
					sprite.getScale().y,
					sprite.getAngle());
		}
		batch.setPackedColor(prevColor);

		lBatch.setLightRenderMode(prevMode);
	}

	public TextureRegion getFlat()
	{
		return flat;
	}

	public TextureRegion getNormalMap()
	{
		return normalMap;
	}

	public TextureRegion getPreshaded()
	{
		return preshaded;
	}

	public boolean usePreshadedWhenLit()
	{
		return usePreshadedWhenLit;
	}
}
