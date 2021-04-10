package net.jumpai.world.lightmap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import net.jumpai.render.lighting.LightingBatch;
import net.jumpai.world.camera.WorldCamera;
import net.jumpai.world.component.light.LightEmitter;

/**
 * Generates lighting masks used by {@link LightingBatch} to compute the
 * lighting of every point on screen.
 * <p>
 * Created on 2019-10-10.
 *
 * @author Alexander Winter
 */
public interface LightMaskGenerator
{
	/**
	 * Generates masks for usage by the {@link LightingBatch}. Masks can be
	 * later accessed by the {@link #getLightMask()} and {@link #getColorMask()}
	 * methods.
	 *
	 * @param camera camera to generate the mask relative to
	 * @param topBorder top side of the world border
	 */
	void generateMasks(WorldCamera camera, Iterable<LightEmitter> emitters, LightMap lightMap, float topBorder);

	/**
	 * Mask that specifies the intensity of light at every point on the screen.
	 * Will return the mask only if it has been generated by the
	 * {@link #generateMasks(WorldCamera, Iterable, LightMap, float)}.
	 * Otherwise {@link IllegalStateException} is thrown.
	 *
	 * @return light direction mask
	 * @throws IllegalStateException if the mask hasn't been generated
	 */
	Texture getLightMask();

	/**
	 * Mask that specified the color of the light at every point on the screen.
	 * Will return the mask only if it has been generated by the
	 * {@link #generateMasks(WorldCamera, Iterable, LightMap, float)} method and
	 * if color is supported. If not generated {@link IllegalStateException} is
	 * thrown and if colors are not supported
	 * {@link UnsupportedOperationException} is thrown.
	 *
	 * @return light color mask
	 * @throws IllegalStateException if the mask hasn't been generated
	 * @throws UnsupportedOperationException if color is not supported
	 */
	Texture getColorMask();

	/**
	 * Returns the scale that must be applied to normalized window coordinates
	 * to become mask coordinates. Scale must be applied <strong>before</strong>
	 * the offset.
	 * <p>
	 * If p is a point on screen p = (x, y) then (p * scale + offset) is a point
	 * on the mask corresponding to p on screen.
	 *
	 * @return vector containing the scale
	 */
	Vector2 getMaskScale();

	/**
	 * Returns the offset of the mask relative to the bottom left edge that
	 * needs to be applied in order to align the coordinates of the screen with
	 * the mask. Offset must be applied <strong>after</strong> the scale.
	 * <p>
	 * If p is a point on screen p = (x, y) then (p * scale + offset) is a point
	 * on the mask corresponding to p on screen.
	 *
	 * @return vector containing the offset
	 */
	Vector2 getMaskOffset();

	/**
	 * Checks if this instance of {@link LightMaskGenerator} supports colors. If
	 * not, all light colors should be assumed to be white and calls to {@link
	 * #getColorMask()} will throw an exception.
	 *
	 * @return true if color masks are supported, otherwise false
	 */
	boolean supportsColor();
}
