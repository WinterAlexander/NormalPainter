package com.normalpainter.lightmap;

import com.badlogic.gdx.graphics.Texture;

/**
 * Renders {@link LightMapPageRenderer} into textures for use in lighting
 * shader
 * <p>
 * Created on 2020-07-31.
 *
 * @author Alexander Winter
 */
public interface LightMapPageRenderer
{
	/**
	 * Renders the specified page if not renderer and returns the resulting
	 * texture
	 * @param map map to render the page of
	 * @param pageX position x of the page
	 * @param pageY position y of the page
	 * @param topBorder top of the border to make above the border always lit
	 * @return texture for the page
	 */
	Texture render(LightMap map, int pageX, int pageY, float topBorder);

	/**
	 * Returns the amount of padding used when rendering the texture. Padding
	 * allows proper texture filtering at edges of the light map by including
	 * to the texture slightly more than required.
	 *
	 * @return amount of padding used for each texture rendered
	 */
	int getPadding();

	/**
	 * Resets the cache of the specified light map page
	 *
	 * @param pageX position x of the page
	 * @param pageY position y of the page
	 */
	void resetPageCache(int pageX, int pageY);

	/**
	 * Resets the cache of every page ever rendered by this page renderer
	 */
	void resetAllPageCaches();

	/**
	 * Disposes of this page renderer, clearing up all used resources
	 */
	void dispose();
}
