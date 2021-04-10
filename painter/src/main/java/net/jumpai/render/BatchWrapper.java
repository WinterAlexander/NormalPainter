package net.jumpai.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Wraps a {@link Batch} and allows extending its functionality
 * <p>
 * Created on 2020-11-15.
 *
 * @author Alexander Winter
 */
public class BatchWrapper implements Batch
{
	private final Batch batch;

	public BatchWrapper(Batch batch)
	{
		ensureNotNull(batch, "batch");
		this.batch = batch;
	}

	@Override
	public void begin()
	{
		batch.begin();
	}

	@Override
	public void end()
	{
		batch.end();
	}

	@Override
	public void setColor(Color tint)
	{
		batch.setColor(tint);
	}

	@Override
	public void setColor(float r, float g, float b, float a)
	{
		batch.setColor(r, g, b, a);
	}

	@Override
	public Color getColor()
	{
		return batch.getColor();
	}

	@Override
	public void setPackedColor(float packedColor)
	{
		batch.setPackedColor(packedColor);
	}

	@Override
	public float getPackedColor()
	{
		return batch.getPackedColor();
	}

	@Override
	public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY)
	{
		batch.draw(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
	}

	@Override
	public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY)
	{
		batch.draw(texture, x, y, width, height, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
	}

	@Override
	public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight)
	{
		batch.draw(texture, x, y, srcX, srcY, srcWidth, srcHeight);
	}

	@Override
	public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2)
	{
		batch.draw(texture, x, y, width, height, u, v, u2, v2);
	}

	@Override
	public void draw(Texture texture, float x, float y)
	{
		batch.draw(texture, x, y);
	}

	@Override
	public void draw(Texture texture, float x, float y, float width, float height)
	{
		batch.draw(texture, x, y, width, height);
	}

	@Override
	public void draw(Texture texture, float[] spriteVertices, int offset, int count)
	{
		batch.draw(texture, spriteVertices, offset, count);
	}

	@Override
	public void draw(TextureRegion region, float x, float y)
	{
		batch.draw(region, x, y);
	}

	@Override
	public void draw(TextureRegion region, float x, float y, float width, float height)
	{
		batch.draw(region, x, y, width, height);
	}

	@Override
	public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation)
	{
		batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
	}

	@Override
	public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise)
	{
		batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation, clockwise);
	}

	@Override
	public void draw(TextureRegion region, float width, float height, Affine2 transform)
	{
		batch.draw(region, width, height, transform);
	}

	@Override
	public void flush()
	{
		batch.flush();
	}

	@Override
	public void disableBlending()
	{
		batch.disableBlending();
	}

	@Override
	public void enableBlending()
	{
		batch.enableBlending();
	}

	@Override
	public void setBlendFunction(int srcFunc, int dstFunc)
	{
		batch.setBlendFunction(srcFunc, dstFunc);
	}

	@Override
	public void setBlendFunctionSeparate(int srcFuncColor, int dstFuncColor, int srcFuncAlpha, int dstFuncAlpha)
	{
		batch.setBlendFunctionSeparate(srcFuncColor, dstFuncColor, srcFuncAlpha, dstFuncAlpha);
	}

	@Override
	public int getBlendSrcFunc()
	{
		return batch.getBlendSrcFunc();
	}

	@Override
	public int getBlendDstFunc()
	{
		return batch.getBlendDstFunc();
	}

	@Override
	public int getBlendSrcFuncAlpha()
	{
		return batch.getBlendSrcFuncAlpha();
	}

	@Override
	public int getBlendDstFuncAlpha()
	{
		return batch.getBlendDstFuncAlpha();
	}

	@Override
	public Matrix4 getProjectionMatrix()
	{
		return batch.getProjectionMatrix();
	}

	@Override
	public Matrix4 getTransformMatrix()
	{
		return batch.getTransformMatrix();
	}

	@Override
	public void setProjectionMatrix(Matrix4 projection)
	{
		batch.setProjectionMatrix(projection);
	}

	@Override
	public void setTransformMatrix(Matrix4 transform)
	{
		batch.setTransformMatrix(transform);
	}

	@Override
	public void setShader(ShaderProgram shader)
	{
		batch.setShader(shader);
	}

	@Override
	public ShaderProgram getShader()
	{
		return batch.getShader();
	}

	@Override
	public boolean isBlendingEnabled()
	{
		return batch.isBlendingEnabled();
	}

	@Override
	public boolean isDrawing()
	{
		return batch.isDrawing();
	}

	@Override
	public void dispose()
	{
		batch.dispose();
	}

	public Batch getInnerBatch()
	{
		return batch;
	}
}
