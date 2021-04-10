package net.jumpai.util.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import net.jumpai.render.UVTransform;

/**
 * Utility class to draw in a Batch
 * <p>
 * Created on 2016-12-23.
 *
 * @author Alexander Winter
 */
public class BatchUtil
{
	private BatchUtil() {}

	public static void setAlpha(Batch batch, float alpha)
	{
		Color color = batch.getColor();
		batch.setColor(color.r, color.g, color.b, alpha);
	}

	public static void premultiplyAlpha(Batch batch)
	{
		Color color = batch.getColor();
		batch.setColor(color.r * color.a, color.g * color.a, color.b * color.a, color.a);
	}

	public static void draw(Batch batch, Texture texture, Vector2 position, Vector2 origin, Vector2 size)
	{
		batch.draw(texture, position.x + origin.x, position.y + origin.y, size.x, size.y);
	}

	public static void draw(Batch batch, TextureRegion region, Vector2 position, Vector2 origin, Vector2 size)
	{
		batch.draw(region, position.x + origin.x, position.y + origin.y, size.x, size.y);
	}

	public static void draw(Batch batch, TextureRegion region, Vector2 position, Vector2 origin, Vector2 size, float rotation)
	{
		batch.draw(region, position.x + origin.x, position.y + origin.y, -origin.x, -origin.y, size.x, size.y, 1, 1, rotation);
	}

	public static void draw(Batch batch, TextureRegion region, float x, float y, float width, float height, boolean flippedX, boolean flippedY)
	{
		batch.draw(region, x + (flippedX ? width : 0), y + (flippedY ? height : 0), 0, 0, width, height, flippedX ? -1f : 1f, flippedY ? -1f : 1f, 0f);
	}

	public static void draw(Batch batch, TextureRegion region,
	                        float x, float y,
	                        float originX, float originY,
	                        float width, float height,
	                        float scaleX, float scaleY,
	                        float rotation,
	                        UVTransform uvTransform)
	{
		switch(uvTransform)
		{
			case NONE:
				batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
				return;
			case FLIPPED_X:
				batch.draw(region.getTexture(), x, y, originX, originY, width, height, scaleX, scaleY, rotation,
						region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(),
						true, false);
				return;
			case FLIPPED_Y:
				batch.draw(region.getTexture(), x, y, originX, originY, width, height, scaleX, scaleY, rotation,
						region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(),
						false, true);
				return;
			case UPSIDE_DOWN:
				batch.draw(region.getTexture(), x, y, originX, originY, width, height, scaleX, scaleY, rotation,
						region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(),
						true, true);
				return;
			case COUNTER_CLOCKWISE:
				batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation, false);
				return;
			case CLOCKWISE:
				batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation, true);
				return;

			case FLIPPED_CLOCKWISE:
			case FLIPPED_COUNTER_CLOCKWISE:
				throw new UnsupportedOperationException("UV transform not supported");
		}
	}
}
