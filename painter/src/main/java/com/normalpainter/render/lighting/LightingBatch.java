package com.normalpainter.render.lighting;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.normalpainter.lightmap.LightMaskGenerator;
import com.winteralexander.gdx.utils.gfx.UVTransform;

/**
 * A {@link Batch} that supports lighting draw calls
 * <p>
 * Created on 2019-10-10.
 *
 * @author Alexander Winter
 */
public interface LightingBatch extends Batch
{
	void drawNormalMapped(TextureRegion flat,
	                      TextureRegion normal,
	                      float x, float y,
	                      float width, float height);

	void drawNormalMapped(TextureRegion flat,
	                      TextureRegion normal,
	                      TextureRegion preshaded,
	                      float x, float y,
	                      float width, float height);

	void drawNormalMapped(Texture flat,
	                      Texture normal,
	                      float x, float y,
	                      float width, float height,
	                      float u, float v,
	                      float u2, float v2,
	                      float normalU, float normalV,
	                      float normalU2, float normalV2);

	void drawNormalMapped(Texture flat,
	                      Texture normal,
	                      Texture preshaded,
	                      float x, float y,
                          float width, float height,
                          float u, float v,
                          float u2, float v2,
                          float normalU, float normalV,
                          float normalU2, float normalV2,
                          float preshadedU, float preshadedV,
                          float preshadedU2, float preshadedV2);

	void drawNormalMapped(Texture flat,
	                      Texture normal,
	                      float x, float y,
	                      float width, float height);

	void drawNormalMapped(Texture flat,
	                      Texture normal,
	                      Texture preshaded,
	                      float x, float y,
	                      float width, float height);


	/**
	 * Draws the specified texture region with its specified normal map along
	 * with the draw parameters. The normal map should be rotated and scaled
	 * as well as the region.
	 *
	 * @param flat flat texture to shade with the normal
	 * @param normal normal map
	 * @param x x position
	 * @param y y position
	 * @param originX origin x of the rotation and scaling
	 * @param originY origin y of the rotation and scaling
	 * @param width width to draw
	 * @param height height to draw
	 * @param scaleX scale in x
	 * @param scaleY scale in y
	 * @param rotation rotation of the texture
	 */
	void drawNormalMapped(TextureRegion flat,
	                      TextureRegion normal,
	                      float x, float y,
	                      float originX, float originY,
	                      float width, float height,
	                      float scaleX, float scaleY,
	                      float rotation);

	/**
	 * Draws the specified texture region with its specified normal map along
	 * with the draw parameters. The normal map should be rotated and scaled
	 * as well as the region.
	 *
	 * @param flat flat texture to shade with the normal
	 * @param normal normal map
	 * @param preshaded sunshaded texture to draw where there is sunlight
	 * @param x x position
	 * @param y y position
	 * @param originX origin x of the rotation and scaling
	 * @param originY origin y of the rotation and scaling
	 * @param width width to draw
	 * @param height height to draw
	 * @param scaleX scale in x
	 * @param scaleY scale in y
	 * @param rotation rotation of the texture
	 * @param preshadedTransform transform to apply to the preshaded coordinates
	 */
	void drawNormalMapped(TextureRegion flat,
	                      TextureRegion normal,
	                      TextureRegion preshaded,
	                      float x, float y,
	                      float originX, float originY,
	                      float width, float height,
	                      float scaleX, float scaleY,
	                      float rotation,
	                      UVTransform preshadedTransform);

	/**
	 * @see #drawNormalMapped(TextureRegion, TextureRegion, TextureRegion, float, float, float, float, float, float, float, float, float, UVTransform)
	 */
	default void drawNormalMapped(TextureRegion flat,
	                      TextureRegion normal,
	                      TextureRegion preshaded,
	                      float x, float y,
	                      float originX, float originY,
	                      float width, float height,
	                      float scaleX, float scaleY,
	                      float rotation) {
		drawNormalMapped(flat, normal, preshaded,
				x, y,
				originX, originY,
				width, height,
				scaleX, scaleY,
				rotation,
				UVTransform.NONE);
	}

	LightRenderMode getLightRenderMode();

	void setLightRenderMode(LightRenderMode mode);

	void setLightingShader(ShaderProgram shader);

	LightMaskGenerator getLightMaskGenerator();

	boolean supportsColor();
}
