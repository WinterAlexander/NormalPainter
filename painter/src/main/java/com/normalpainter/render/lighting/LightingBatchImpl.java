package com.normalpainter.render.lighting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.MathUtils;
import com.normalpainter.render.AbstractSpriteBatch;
import com.winteralexander.gdx.utils.Validation;
import com.normalpainter.lightmap.LightMaskGenerator;
import com.winteralexander.gdx.utils.gfx.UVTransform;

import static com.badlogic.gdx.graphics.GL20.GL_TEXTURE0;

/**
 * {@link LightingBatch} implementation
 * <p>
 * Created on 2020-03-23.
 *
 * @author Alexander Winter
 */
public class LightingBatchImpl extends AbstractSpriteBatch implements LightingBatch
{
	private static final int TEXTURE_UNIT = 0;
	private static final int NORMAL_UNIT = 1;
	private static final int PRESHADED_UNIT = 2;
	private static final int LIGHT_MASK_UNIT = 3;
	private static final int COLOR_LIGHT_MASK_UNIT = 4;

	private final LightMaskGenerator lightMaskGenerator;

	private boolean lightingShader = true;
	private final boolean lightColor;

	private Texture normalTexture = null, preshadedTexture = null;

	private LightRenderMode mode;

	public LightingBatchImpl(ShaderProgram defaultShader,
	                         LightMaskGenerator lightMaskGenerator,
	                         boolean lightColor)
	{
		super(1000, 11, defaultShader,
				new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
				new VertexAttribute(Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
				new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"),
				new VertexAttribute(Usage.Generic, 4, "a_lightCoords"),
				new VertexAttribute(Usage.Generic, 2, "a_normalRotation"));

		Validation.ensureNotNull(lightMaskGenerator, "lightMaskGenerator");
		this.mode = LightRenderMode.OFF;
		this.lightMaskGenerator = lightMaskGenerator;
		this.lightColor = lightColor;
	}

	@Override
	public void begin()
	{
		super.begin();
		lightMaskGenerator.getLightMask().bind(LIGHT_MASK_UNIT);
		if(lightColor && lightMaskGenerator.supportsColor())
			lightMaskGenerator.getColorMask().bind(COLOR_LIGHT_MASK_UNIT);
		Gdx.gl.glActiveTexture(GL_TEXTURE0 + TEXTURE_UNIT);
	}

	@Override
	public void setupShader()
	{
		super.setupShader();

		if(!lightingShader)
			return;

		getShader().setUniformf("u_lightMaskOffset", lightMaskGenerator.getMaskOffset());
		getShader().setUniformf("u_lightMaskScale", lightMaskGenerator.getMaskScale());
		getShader().setUniformi("u_normal", NORMAL_UNIT);
		getShader().setUniformi("u_preshaded", PRESHADED_UNIT);
		getShader().setUniformi("u_lightMask", LIGHT_MASK_UNIT);

		if(lightColor && lightMaskGenerator.supportsColor())
			getShader().setUniformi("u_colorLightMask", COLOR_LIGHT_MASK_UNIT);
	}

	@Override
	public void drawNormalMapped(TextureRegion region, TextureRegion normal,
	                             float x, float y,
	                             float originX, float originY,
	                             float width, float height,
	                             float scaleX, float scaleY,
	                             float rotation)
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		float[] vertices = this.vertices;

		Texture texture = region.getTexture();
		setNormal(normal.getTexture());
		if(texture != lastTexture)
			switchTexture(texture);
		else if(idx == vertices.length)
			flush();

		// bottom left and top right corner points relative to origin
		final float worldOriginX = x + originX;
		final float worldOriginY = y + originY;
		float fx = -originX;
		float fy = -originY;
		float fx2 = width - originX;
		float fy2 = height - originY;

		// scale
		fx *= scaleX;
		fy *= scaleY;
		fx2 *= scaleX;
		fy2 *= scaleY;

		// construct corner points, start from top left and go counter clockwise
		final float p1x = fx;
		final float p1y = fy;
		final float p2x = fx;
		final float p2y = fy2;
		final float p3x = fx2;
		final float p3y = fy2;
		final float p4x = fx2;
		final float p4y = fy;

		float x1;
		float y1;
		float x2;
		float y2;
		float x3;
		float y3;
		float x4;
		float y4;

		float cos = 1f;
		float sin = 0f;

		// rotate
		if(rotation != 0)
		{
			cos = MathUtils.cosDeg(rotation);
			sin = MathUtils.sinDeg(rotation);

			x1 = cos * p1x - sin * p1y;
			y1 = sin * p1x + cos * p1y;

			x2 = cos * p2x - sin * p2y;
			y2 = sin * p2x + cos * p2y;

			x3 = cos * p3x - sin * p3y;
			y3 = sin * p3x + cos * p3y;

			x4 = x1 + (x3 - x2);
			y4 = y3 - (y2 - y1);
		}
		else
		{
			x1 = p1x;
			y1 = p1y;

			x2 = p2x;
			y2 = p2y;

			x3 = p3x;
			y3 = p3y;

			x4 = p4x;
			y4 = p4y;
		}

		x1 += worldOriginX;
		y1 += worldOriginY;
		x2 += worldOriginX;
		y2 += worldOriginY;
		x3 += worldOriginX;
		y3 += worldOriginY;
		x4 += worldOriginX;
		y4 += worldOriginY;

		float u = region.getU();
		float v = region.getV2();
		float u2 = region.getU2();
		float v2 = region.getV();

		float normalU = normal.getU();
		float normalV = normal.getV2();
		float normalU2 = normal.getU2();
		float normalV2 = normal.getV();

		boolean normInvertX = false;
		boolean normInvertY = false;

		if(scaleX < 0)
			normInvertX = true;

		if(scaleY < 0)
			normInvertY = true;

		addVertices(x1, y1, x2, y2, x3, y3, x4, y4,
				u, v, u2, v2, normalU, normalV, normalU2, normalV2,
				cos, sin, normInvertX, normInvertY);
	}

	@Override
	public void drawNormalMapped(TextureRegion flat,
	                             TextureRegion normal,
	                             TextureRegion preshaded,
	                             float x, float y,
	                             float originX, float originY,
	                             float width, float height,
	                             float scaleX, float scaleY,
	                             float rotation,
	                             UVTransform preshadedTransform)
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		float[] vertices = this.vertices;

		Texture texture = flat.getTexture();
		setNormal(normal.getTexture());
		setPreshaded(preshaded.getTexture());
		if(texture != lastTexture)
			switchTexture(texture);
		else if(idx == vertices.length)
			flush();

		// bottom left and top right corner points relative to origin
		final float worldOriginX = x + originX;
		final float worldOriginY = y + originY;
		float fx = -originX;
		float fy = -originY;
		float fx2 = width - originX;
		float fy2 = height - originY;

		// scale
		fx *= scaleX;
		fy *= scaleY;
		fx2 *= scaleX;
		fy2 *= scaleY;

		// construct corner points, start from top left and go counter clockwise
		final float p1x = fx;
		final float p1y = fy;
		final float p2x = fx;
		final float p2y = fy2;
		final float p3x = fx2;
		final float p3y = fy2;
		final float p4x = fx2;
		final float p4y = fy;

		float x1;
		float y1;
		float x2;
		float y2;
		float x3;
		float y3;
		float x4;
		float y4;

		float cos = 1f;
		float sin = 0f;

		// rotate
		if(rotation != 0)
		{
			cos = MathUtils.cosDeg(rotation);
			sin = MathUtils.sinDeg(rotation);

			x1 = cos * p1x - sin * p1y;
			y1 = sin * p1x + cos * p1y;

			x2 = cos * p2x - sin * p2y;
			y2 = sin * p2x + cos * p2y;

			x3 = cos * p3x - sin * p3y;
			y3 = sin * p3x + cos * p3y;

			x4 = x1 + (x3 - x2);
			y4 = y3 - (y2 - y1);
		}
		else
		{
			x1 = p1x;
			y1 = p1y;

			x2 = p2x;
			y2 = p2y;

			x3 = p3x;
			y3 = p3y;

			x4 = p4x;
			y4 = p4y;
		}

		x1 += worldOriginX;
		y1 += worldOriginY;
		x2 += worldOriginX;
		y2 += worldOriginY;
		x3 += worldOriginX;
		y3 += worldOriginY;
		x4 += worldOriginX;
		y4 += worldOriginY;

		float u = flat.getU();
		float v = flat.getV2();
		float u2 = flat.getU2();
		float v2 = flat.getV();

		float normalU = normal.getU();
		float normalV = normal.getV2();
		float normalU2 = normal.getU2();
		float normalV2 = normal.getV();

		float preshadedU = preshaded.getU();
		float preshadedV = preshaded.getV2();
		float preshadedU2 = preshaded.getU2();
		float preshadedV2 = preshaded.getV();

		boolean normInvertX = false;
		boolean normInvertY = false;

		if(scaleX < 0)
			normInvertX = true;

		if(scaleY < 0)
			normInvertY = true;


		addVertices(x1, y1, x2, y2, x3, y3, x4, y4,
				u, v, u2, v2,
				normalU, normalV, normalU2, normalV2,
				preshadedU, preshadedV, preshadedU2, preshadedV2,
				cos, sin, normInvertX, normInvertY,
				preshadedTransform);
	}

	@Override
	public void draw(Texture texture,
	                 float x, float y,
	                 float originX, float originY,
	                 float width, float height,
	                 float scaleX, float scaleY,
	                 float rotation,
	                 int srcX, int srcY,
	                 int srcWidth, int srcHeight,
	                 boolean flipX, boolean flipY)
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		if(texture != lastTexture)
			switchTexture(texture);
		else if(idx == vertices.length)
			flush();

		// bottom left and top right corner points relative to origin
		final float worldOriginX = x + originX;
		final float worldOriginY = y + originY;
		float fx = -originX;
		float fy = -originY;
		float fx2 = width - originX;
		float fy2 = height - originY;

		// scale
		if(scaleX != 1 || scaleY != 1)
		{
			fx *= scaleX;
			fy *= scaleY;
			fx2 *= scaleX;
			fy2 *= scaleY;
		}

		// construct corner points, start from top left and go counter clockwise
		final float p1x = fx;
		final float p1y = fy;
		final float p2x = fx;
		final float p2y = fy2;
		final float p3x = fx2;
		final float p3y = fy2;
		final float p4x = fx2;
		final float p4y = fy;

		float x1;
		float y1;
		float x2;
		float y2;
		float x3;
		float y3;
		float x4;
		float y4;

		float cos = 1f;
		float sin = 0f;

		// rotate
		if(rotation != 0)
		{
			cos = MathUtils.cosDeg(rotation);
			sin = MathUtils.sinDeg(rotation);

			x1 = cos * p1x - sin * p1y;
			y1 = sin * p1x + cos * p1y;

			x2 = cos * p2x - sin * p2y;
			y2 = sin * p2x + cos * p2y;

			x3 = cos * p3x - sin * p3y;
			y3 = sin * p3x + cos * p3y;

			x4 = x1 + (x3 - x2);
			y4 = y3 - (y2 - y1);
		}
		else
		{
			x1 = p1x;
			y1 = p1y;

			x2 = p2x;
			y2 = p2y;

			x3 = p3x;
			y3 = p3y;

			x4 = p4x;
			y4 = p4y;
		}

		x1 += worldOriginX;
		y1 += worldOriginY;
		x2 += worldOriginX;
		y2 += worldOriginY;
		x3 += worldOriginX;
		y3 += worldOriginY;
		x4 += worldOriginX;
		y4 += worldOriginY;

		float u = srcX * invTexWidth;
		float v = (srcY + srcHeight) * invTexHeight;
		float u2 = (srcX + srcWidth) * invTexWidth;
		float v2 = srcY * invTexHeight;

		if(flipX)
		{
			float tmp = u;
			u = u2;
			u2 = tmp;
		}

		if(flipY)
		{
			float tmp = v;
			v = v2;
			v2 = tmp;
		}

		addVertices(x1, y1, x2, y2, x3, y3, x4, y4, u, v, u2, v2);
	}

	@Override
	public void draw(Texture texture,
	                 float x, float y,
	                 float width, float height,
	                 int srcX, int srcY,
	                 int srcWidth, int srcHeight,
	                 boolean flipX, boolean flipY)
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		if(texture != lastTexture)
			switchTexture(texture);
		else if(idx == vertices.length)
			flush();

		float u = srcX * invTexWidth;
		float v = (srcY + srcHeight) * invTexHeight;
		float u2 = (srcX + srcWidth) * invTexWidth;
		float v2 = srcY * invTexHeight;
		final float fx2 = x + width;
		final float fy2 = y + height;

		if(flipX)
		{
			float tmp = u;
			u = u2;
			u2 = tmp;
		}

		if(flipY)
		{
			float tmp = v;
			v = v2;
			v2 = tmp;
		}

		addVertices(x, y, x, fy2, fx2, fy2, fx2, y, u, v, u2, v2);
	}

	@Override
	public void draw(Texture texture,
	                 float x, float y,
	                 int srcX, int srcY,
	                 int srcWidth, int srcHeight)
	{
		draw(texture, x, y, srcWidth, srcHeight, srcX, srcY, srcWidth, srcHeight, false, false);
	}

	@Override
	public void draw(Texture texture,
	                 float x, float y,
	                 float width, float height,
	                 float u, float v,
	                 float u2, float v2)
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		float[] vertices = this.vertices;

		if(texture != lastTexture)
			switchTexture(texture);
		else if(idx == vertices.length)
			flush();

		final float fx2 = x + width;
		final float fy2 = y + height;

		addVertices(x, y, x, fy2, fx2, fy2, fx2, y, u, v, u2, v2);
	}

	@Override
	public void drawNormalMapped(Texture texture, Texture normal,
	                             float x, float y,
	                             float width, float height,
	                             float u, float v,
	                             float u2, float v2,
	                             float normalU, float normalV,
	                             float normalU2, float normalV2)
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		float[] vertices = this.vertices;

		setNormal(normal);
		if(texture != lastTexture)
			switchTexture(texture);
		else if(idx == vertices.length)
			flush();

		final float fx2 = x + width;
		final float fy2 = y + height;

		addVertices(x, y, x, fy2, fx2, fy2, fx2, y, u, v, u2, v2,
				normalU, normalV, normalU2, normalV2, 1f, 0f, false, false);
	}

	@Override
	public void drawNormalMapped(Texture flat,
	                             Texture normal,
	                             Texture preshaded,
	                             float x, float y,
	                             float width, float height,
	                             float u, float v, float u2, float v2,
	                             float normalU, float normalV, float normalU2, float normalV2,
	                             float preshadedU, float preshadedV, float preshadedU2, float preshadedV2)
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		float[] vertices = this.vertices;

		setNormal(normal);
		setPreshaded(preshaded);
		if(flat != lastTexture)
			switchTexture(flat);
		else if(idx == vertices.length)
			flush();

		final float fx2 = x + width;
		final float fy2 = y + height;

		addVertices(x, y, x, fy2, fx2, fy2, fx2,
				y, u, v, u2, v2,
				normalU, normalV, normalU2, normalV2,
				preshadedU, preshadedV, preshadedU2, preshadedV2,
				1f, 0f, false, false,
				UVTransform.NONE);
	}

	@Override
	public void draw(Texture texture, float x, float y)
	{
		draw(texture, x, y, texture.getWidth(), texture.getHeight());
	}

	@Override
	public void draw(Texture texture,
	                 float x, float y,
	                 float width, float height)
	{
		draw(texture, x, y, width, height,
				0f, 1f, 1f, 0f);
	}

	@Override
	public void drawNormalMapped(Texture texture,
	                             Texture normal,
	                             float x, float y,
	                             float width, float height)
	{
		drawNormalMapped(texture, normal, x, y, width, height,
				0f, 1f, 1f, 0f, 0f, 1f, 1f, 0f);
	}

	@Override
	public void drawNormalMapped(Texture flat,
	                             Texture normal,
	                             Texture preshaded,
	                             float x, float y,
	                             float width, float height)
	{
		drawNormalMapped(flat, normal, preshaded, x, y, width, height,
				0f, 1f, 1f, 0f, 0f, 1f, 1f, 0f, 0f, 1f, 1f, 0f);
	}

	@Override
	public void draw(TextureRegion region, float x, float y)
	{
		draw(region, x, y, region.getRegionWidth(), region.getRegionHeight());
	}

	@Override
	public void draw(TextureRegion region, float x, float y, float width, float height)
	{
		draw(region.getTexture(), x, y, width, height, region.getU(), region.getV2(), region.getU2(), region.getV());
	}

	@Override
	public void drawNormalMapped(TextureRegion region,
	                      TextureRegion normal,
	                      float x, float y,
	                      float width, float height)
	{
		drawNormalMapped(region.getTexture(),
				normal.getTexture(),
				x, y, width, height,
				region.getU(), region.getV2(),
				region.getU2(), region.getV(),
				normal.getU(), normal.getV2(),
				normal.getU2(), normal.getV());
	}

	@Override
	public void drawNormalMapped(TextureRegion flat,
	                             TextureRegion normal,
	                             TextureRegion preshaded,
	                             float x, float y,
	                             float width, float height)
	{
		drawNormalMapped(flat.getTexture(),
				normal.getTexture(),
				preshaded.getTexture(),
				x, y, width, height,
				flat.getU(), flat.getV2(),
				flat.getU2(), flat.getV(),
				normal.getU(), normal.getV2(),
				normal.getU2(), normal.getV(),
				preshaded.getU(), preshaded.getV2(),
				preshaded.getU2(), preshaded.getV());
	}

	@Override
	public void draw(TextureRegion region,
	                 float x, float y,
	                 float originX, float originY,
	                 float width, float height,
	                 float scaleX, float scaleY,
	                 float rotation)
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		float[] vertices = this.vertices;

		Texture texture = region.getTexture();
		if(texture != lastTexture)
			switchTexture(texture);
		else if(idx == vertices.length)
			flush();

		// bottom left and top right corner points relative to origin
		final float worldOriginX = x + originX;
		final float worldOriginY = y + originY;
		float fx = -originX;
		float fy = -originY;
		float fx2 = width - originX;
		float fy2 = height - originY;

		// scale
		fx *= scaleX;
		fy *= scaleY;
		fx2 *= scaleX;
		fy2 *= scaleY;

		// construct corner points, start from top left and go counter clockwise
		final float p1x = fx;
		final float p1y = fy;
		final float p2x = fx;
		final float p2y = fy2;
		final float p3x = fx2;
		final float p3y = fy2;
		final float p4x = fx2;
		final float p4y = fy;

		float x1;
		float y1;
		float x2;
		float y2;
		float x3;
		float y3;
		float x4;
		float y4;

		// rotate
		if(rotation != 0)
		{
			float cos = MathUtils.cosDeg(rotation);
			float sin = MathUtils.sinDeg(rotation);

			x1 = cos * p1x - sin * p1y;
			y1 = sin * p1x + cos * p1y;

			x2 = cos * p2x - sin * p2y;
			y2 = sin * p2x + cos * p2y;

			x3 = cos * p3x - sin * p3y;
			y3 = sin * p3x + cos * p3y;

			x4 = x1 + (x3 - x2);
			y4 = y3 - (y2 - y1);
		}
		else
		{
			x1 = p1x;
			y1 = p1y;

			x2 = p2x;
			y2 = p2y;

			x3 = p3x;
			y3 = p3y;

			x4 = p4x;
			y4 = p4y;
		}

		x1 += worldOriginX;
		y1 += worldOriginY;
		x2 += worldOriginX;
		y2 += worldOriginY;
		x3 += worldOriginX;
		y3 += worldOriginY;
		x4 += worldOriginX;
		y4 += worldOriginY;

		final float u = region.getU();
		final float v = region.getV2();
		final float u2 = region.getU2();
		final float v2 = region.getV();

		addVertices(x1, y1, x2, y2, x3, y3, x4, y4, u, v, u2, v2);
	}

	@Override
	public void draw(TextureRegion region,
	                 float x, float y,
	                 float originX, float originY,
	                 float width, float height,
	                 float scaleX, float scaleY,
	                 float rotation,
	                 boolean clockwise)
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		float[] vertices = this.vertices;

		Texture texture = region.getTexture();
		if(texture != lastTexture)
			switchTexture(texture);
		else if(idx == vertices.length)
			flush();

		// bottom left and top right corner points relative to origin
		final float worldOriginX = x + originX;
		final float worldOriginY = y + originY;
		float fx = -originX;
		float fy = -originY;
		float fx2 = width - originX;
		float fy2 = height - originY;

		// scale
		fx *= scaleX;
		fy *= scaleY;
		fx2 *= scaleX;
		fy2 *= scaleY;

		// construct corner points, start from top left and go counter clockwise
		final float p1x = fx;
		final float p1y = fy;
		final float p2x = fx;
		final float p2y = fy2;
		final float p3x = fx2;
		final float p3y = fy2;
		final float p4x = fx2;
		final float p4y = fy;

		float x1;
		float y1;
		float x2;
		float y2;
		float x3;
		float y3;
		float x4;
		float y4;

		// rotate
		if(rotation != 0)
		{
			float cos = MathUtils.cosDeg(rotation);
			float sin = MathUtils.sinDeg(rotation);

			x1 = cos * p1x - sin * p1y;
			y1 = sin * p1x + cos * p1y;

			x2 = cos * p2x - sin * p2y;
			y2 = sin * p2x + cos * p2y;

			x3 = cos * p3x - sin * p3y;
			y3 = sin * p3x + cos * p3y;

			x4 = x1 + (x3 - x2);
			y4 = y3 - (y2 - y1);
		}
		else
		{
			x1 = p1x;
			y1 = p1y;

			x2 = p2x;
			y2 = p2y;

			x3 = p3x;
			y3 = p3y;

			x4 = p4x;
			y4 = p4y;
		}

		x1 += worldOriginX;
		y1 += worldOriginY;
		x2 += worldOriginX;
		y2 += worldOriginY;
		x3 += worldOriginX;
		y3 += worldOriginY;
		x4 += worldOriginX;
		y4 += worldOriginY;

		float u1, v1, u2, v2, u3, v3, u4, v4;
		if(clockwise)
		{
			u1 = region.getU2();
			v1 = region.getV2();
			u2 = region.getU();
			v2 = region.getV2();
			u3 = region.getU();
			v3 = region.getV();
			u4 = region.getU2();
			v4 = region.getV();
		}
		else
		{
			u1 = region.getU();
			v1 = region.getV();
			u2 = region.getU2();
			v2 = region.getV();
			u3 = region.getU2();
			v3 = region.getV2();
			u4 = region.getU();
			v4 = region.getV2();
		}

		addVertices(x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4);
	}

	@Override
	public void draw(TextureRegion region, float width, float height, Affine2 transform)
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		Texture texture = region.getTexture();
		if(texture != lastTexture)
			switchTexture(texture);
		else if(idx == vertices.length)
			flush();

		// construct corner points
		float x1 = transform.m02;
		float y1 = transform.m12;
		float x2 = transform.m01 * height + transform.m02;
		float y2 = transform.m11 * height + transform.m12;
		float x3 = transform.m00 * width + transform.m01 * height + transform.m02;
		float y3 = transform.m10 * width + transform.m11 * height + transform.m12;
		float x4 = transform.m00 * width + transform.m02;
		float y4 = transform.m10 * width + transform.m12;

		float u = region.getU();
		float v = region.getV2();
		float u2 = region.getU2();
		float v2 = region.getV();

		// very approximate and doesn't support inversion
		addVertices(x1, y1, x2, y2, x3, y3, x4, y4, u, v, u2, v2);
	}

	protected void addVertices(float x1, float y1,
	                           float x2, float y2,
	                           float x3, float y3,
	                           float x4, float y4,
	                           float u, float v,
	                           float u2, float v2)
	{
		addVertices(x1, y1, x2, y2, x3, y3, x4, y4, u, v, u, v2, u2, v2, u2, v);
	}

	protected void addVertices(float x1, float y1,
	                           float x2, float y2,
	                           float x3, float y3,
	                           float x4, float y4,
	                           float u1, float v1,
	                           float u2, float v2,
	                           float u3, float v3,
	                           float u4, float v4)
	{
		float color = this.colorPacked;

		vertices[idx] = x1;
		vertices[idx + 1] = y1;
		vertices[idx + 2] = color;
		vertices[idx + 3] = u1;
		vertices[idx + 4] = v1;
		vertices[idx + 5] = 2f;
		vertices[idx + 6] = mode == LightRenderMode.OFF ? 2f : 0f;
		vertices[idx + 7] = 2f;
		vertices[idx + 8] = 2f;
		vertices[idx + 9] = 0f;
		vertices[idx + 10] = 0f;

		vertices[idx + 11] = x2;
		vertices[idx + 12] = y2;
		vertices[idx + 13] = color;
		vertices[idx + 14] = u2;
		vertices[idx + 15] = v2;
		vertices[idx + 16] = 2f;
		vertices[idx + 17] = mode == LightRenderMode.OFF ? 2f : 0f;
		vertices[idx + 18] = 2f;
		vertices[idx + 19] = 2f;
		vertices[idx + 20] = 0f;
		vertices[idx + 21] = 0f;

		vertices[idx + 22] = x3;
		vertices[idx + 23] = y3;
		vertices[idx + 24] = color;
		vertices[idx + 25] = u3;
		vertices[idx + 26] = v3;
		vertices[idx + 27] = 2f;
		vertices[idx + 28] = mode == LightRenderMode.OFF ? 2f : 0f;
		vertices[idx + 29] = 2f;
		vertices[idx + 30] = 2f;
		vertices[idx + 31] = 0f;
		vertices[idx + 32] = 0f;

		vertices[idx + 33] = x4;
		vertices[idx + 34] = y4;
		vertices[idx + 35] = color;
		vertices[idx + 36] = u4;
		vertices[idx + 37] = v4;
		vertices[idx + 38] = 2f;
		vertices[idx + 39] = mode == LightRenderMode.OFF ? 2f : 0f;
		vertices[idx + 40] = 2f;
		vertices[idx + 41] = 2f;
		vertices[idx + 42] = 0f;
		vertices[idx + 43] = 0f;
		this.idx = idx + 44;
	}

	public void addVertices(float x1, float y1,
	                        float x2, float y2,
	                        float x3, float y3,
	                        float x4, float y4,
	                        float u, float v,
	                        float u2, float v2,
	                        float normalU, float normalV,
	                        float normalU2, float normalV2,
	                        float cosRot, float sinRot,
	                        boolean invertNormX, boolean invertNormY)
	{

		float color = this.colorPacked;

		vertices[idx] = x1;
		vertices[idx + 1] = y1;
		vertices[idx + 2] = color;
		vertices[idx + 3] = u;
		vertices[idx + 4] = v;
		vertices[idx + 5] = normalU * (invertNormX ? -1f : 1f);
		vertices[idx + 6] = mode == LightRenderMode.OFF ? 2f : (normalV * (invertNormY ? -1f : 1f));
		vertices[idx + 7] = 2f * (mode == LightRenderMode.CELSHADED ? -1f : 1f);
		vertices[idx + 8] = 2f * (mode == LightRenderMode.CAPPED ? -1f : 1f);
		vertices[idx + 9] = cosRot;
		vertices[idx + 10] = sinRot;

		vertices[idx + 11] = x2;
		vertices[idx + 12] = y2;
		vertices[idx + 13] = color;
		vertices[idx + 14] = u;
		vertices[idx + 15] = v2;
		vertices[idx + 16] = normalU * (invertNormX ? -1f : 1f);
		vertices[idx + 17] = mode == LightRenderMode.OFF ? 2f : (normalV2 * (invertNormY ? -1f : 1f));
		vertices[idx + 18] = 2f * (mode == LightRenderMode.CELSHADED ? -1f : 1f);
		vertices[idx + 19] = 2f * (mode == LightRenderMode.CAPPED ? -1f : 1f);
		vertices[idx + 20] = cosRot;
		vertices[idx + 21] = sinRot;

		vertices[idx + 22] = x3;
		vertices[idx + 23] = y3;
		vertices[idx + 24] = color;
		vertices[idx + 25] = u2;
		vertices[idx + 26] = v2;
		vertices[idx + 27] = normalU2 * (invertNormX ? -1f : 1f);
		vertices[idx + 28] = mode == LightRenderMode.OFF ? 2f : (normalV2 * (invertNormY ? -1f : 1f));
		vertices[idx + 29] = 2f * (mode == LightRenderMode.CELSHADED ? -1f : 1f);
		vertices[idx + 30] = 2f * (mode == LightRenderMode.CAPPED ? -1f : 1f);
		vertices[idx + 31] = cosRot;
		vertices[idx + 32] = sinRot;

		vertices[idx + 33] = x4;
		vertices[idx + 34] = y4;
		vertices[idx + 35] = color;
		vertices[idx + 36] = u2;
		vertices[idx + 37] = v;
		vertices[idx + 38] = normalU2 * (invertNormX ? -1f : 1f);
		vertices[idx + 39] = mode == LightRenderMode.OFF ? 2f : (normalV * (invertNormY ? -1f : 1f));
		vertices[idx + 40] = 2f * (mode == LightRenderMode.CELSHADED ? -1f : 1f);
		vertices[idx + 41] = 2f * (mode == LightRenderMode.CAPPED ? -1f : 1f);
		vertices[idx + 42] = cosRot;
		vertices[idx + 43] = sinRot;
		this.idx = idx + 44;
	}

	public void addVertices(float x1, float y1,
	                        float x2, float y2,
	                        float x3, float y3,
	                        float x4, float y4,
	                        float u, float v,
	                        float u2, float v2,
	                        float normalU, float normalV,
	                        float normalU2, float normalV2,
	                        float preshadedU, float preshadedV,
	                        float preshadedU2, float preshadedV2,
	                        float cosRot, float sinRot,
	                        boolean invertNormX, boolean invertNormY,
	                        UVTransform preshadedTransform)
	{

		float color = this.colorPacked;

		float pU = preshadedU * (mode == LightRenderMode.CELSHADED ? -1f : 1f);
		float pV = preshadedV * (mode == LightRenderMode.CAPPED ? -1f : 1f);
		float pU2 = preshadedU2 * (mode == LightRenderMode.CELSHADED ? -1f : 1f);
		float pV2 = preshadedV2 * (mode == LightRenderMode.CAPPED ? -1f : 1f);

		vertices[idx] = x1;
		vertices[idx + 1] = y1;
		vertices[idx + 2] = color;
		vertices[idx + 3] = u;
		vertices[idx + 4] = v;
		vertices[idx + 5] = normalU * (invertNormX ? -1f : 1f);
		vertices[idx + 6] = mode == LightRenderMode.OFF ? 2f : (normalV * (invertNormY ? -1f : 1f));
		vertices[idx + 7] = preshadedTransform.getP1U(pU, pU2);
		vertices[idx + 8] = preshadedTransform.getP1V(pV, pV2);
		vertices[idx + 9] = cosRot;
		vertices[idx + 10] = sinRot;

		vertices[idx + 11] = x2;
		vertices[idx + 12] = y2;
		vertices[idx + 13] = color;
		vertices[idx + 14] = u;
		vertices[idx + 15] = v2;
		vertices[idx + 16] = normalU * (invertNormX ? -1f : 1f);
		vertices[idx + 17] = mode == LightRenderMode.OFF ? 2f : (normalV2 * (invertNormY ? -1f : 1f));
		vertices[idx + 18] = preshadedTransform.getP2U(pU, pU2);
		vertices[idx + 19] = preshadedTransform.getP2V(pV, pV2);
		vertices[idx + 20] = cosRot;
		vertices[idx + 21] = sinRot;

		vertices[idx + 22] = x3;
		vertices[idx + 23] = y3;
		vertices[idx + 24] = color;
		vertices[idx + 25] = u2;
		vertices[idx + 26] = v2;
		vertices[idx + 27] = normalU2 * (invertNormX ? -1f : 1f);
		vertices[idx + 28] = mode == LightRenderMode.OFF ? 2f : (normalV2 * (invertNormY ? -1f : 1f));
		vertices[idx + 29] = preshadedTransform.getP3U(pU, pU2);
		vertices[idx + 30] = preshadedTransform.getP3V(pV, pV2);
		vertices[idx + 31] = cosRot;
		vertices[idx + 32] = sinRot;

		vertices[idx + 33] = x4;
		vertices[idx + 34] = y4;
		vertices[idx + 35] = color;
		vertices[idx + 36] = u2;
		vertices[idx + 37] = v;
		vertices[idx + 38] = normalU2 * (invertNormX ? -1f : 1f);
		vertices[idx + 39] = mode == LightRenderMode.OFF ? 2f : (normalV * (invertNormY ? -1f : 1f));
		vertices[idx + 40] = preshadedTransform.getP4U(pU, pU2);
		vertices[idx + 41] = preshadedTransform.getP4V(pV, pV2);
		vertices[idx + 42] = cosRot;
		vertices[idx + 43] = sinRot;
		this.idx = idx + 44;
	}

	@Override
	public void draw(Texture texture, float[] spriteVertices, int offset, int count)
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		float[] spriteVertices2 = new float[count * vertexSize / 5];

		for(int i = offset, k = 0; i < offset + count; i += 5, k += vertexSize)
		{
			for(int j = 0; j < 5; j++)
				spriteVertices2[k + j] = spriteVertices[i + j];

			spriteVertices2[k + 5] = 2f;
			spriteVertices2[k + 6] = mode == LightRenderMode.OFF ? 2f : 0f;
			spriteVertices2[k + 7] = 2f;
			spriteVertices2[k + 8] = 2f;
			spriteVertices2[k + 9] = 1f;
			spriteVertices2[k + 10] = 0f;
		}

		offset = 0;
		count = count * vertexSize / 5;
		spriteVertices = spriteVertices2;

		int verticesLength = vertices.length;
		int remainingVertices = verticesLength;
		if(texture != lastTexture)
			switchTexture(texture);
		else
		{
			remainingVertices -= idx;
			if(remainingVertices == 0)
			{
				flush();
				remainingVertices = verticesLength;
			}
		}
		int copyCount = Math.min(remainingVertices, count);

		System.arraycopy(spriteVertices, offset, vertices, idx, copyCount);
		idx += copyCount;
		count -= copyCount;
		while(count > 0)
		{
			offset += copyCount;
			flush();
			copyCount = Math.min(verticesLength, count);
			System.arraycopy(spriteVertices, offset, vertices, 0, copyCount);
			idx += copyCount;
			count -= copyCount;
		}
	}

	@Override
	public void flush()
	{
		if(idx == 0)
			return;

		if(lightingShader)
		{
			if(normalTexture != null)
				normalTexture.bind(NORMAL_UNIT);

			if(preshadedTexture != null)
				preshadedTexture.bind(PRESHADED_UNIT);

			Gdx.gl.glActiveTexture(GL_TEXTURE0 + TEXTURE_UNIT);
		}

		super.flush();
	}

	private void setNormal(Texture normal)
	{
		if(normal == null && normalTexture == null)
			return;

		if(normal == null || normal != normalTexture)
		{
			flush();
			normalTexture = normal;
		}
	}

	private void setPreshaded(Texture preshaded)
	{
		if(preshaded == null && preshadedTexture == null)
			return;

		if(preshaded == null || preshaded != preshadedTexture)
		{
			flush();
			preshadedTexture = preshaded;
		}
	}

	@Override
	public LightMaskGenerator getLightMaskGenerator()
	{
		return lightMaskGenerator;
	}

	@Override
	public LightRenderMode getLightRenderMode()
	{
		return mode;
	}

	@Override
	public void setLightRenderMode(LightRenderMode mode)
	{
		Validation.ensureNotNull(mode, "mode");
		this.mode = mode;
	}

	@Override
	public void setShader(ShaderProgram shader)
	{
		if(drawing)
		{
			flush();
			getShader().end();
		}

		lightingShader = shader == null || shader == this.shader;
		customShader = shader;

		if(drawing)
		{
			getShader().begin();
			setupShader();
		}
	}

	@Override
	public void setLightingShader(ShaderProgram shader)
	{
		lightingShader = true;
		super.setShader(shader);
	}

	@Override
	public boolean supportsColor()
	{
		return lightColor && lightMaskGenerator.supportsColor();
	}
}
