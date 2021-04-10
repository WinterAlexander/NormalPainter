package net.jumpai.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Mesh.VertexDataType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Basically a copy of {@link SpriteBatch} which enables more functionalities
 * for childrens suchs as custom number of vertex attributes
 */
public abstract class AbstractSpriteBatch implements Batch
{
	/**
	 * Number of render calls since the last {@link #begin()}.
	 **/
	public int renderCalls = 0;

	/**
	 * Number of rendering calls, ever. Will not be reset unless set manually.
	 **/
	public int totalRenderCalls = 0;

	/**
	 * The maximum number of sprites rendered in one batch so far.
	 **/
	public int maxSpritesInBatch = 0;

	protected final float[] vertices;
	protected final Matrix4 transformMatrix = new Matrix4();
	protected final Matrix4 projectionMatrix = new Matrix4();
	protected final Matrix4 combinedMatrix = new Matrix4();
	protected final ShaderProgram shader;
	protected final Color color = new Color(1, 1, 1, 1);

	protected int idx = 0;
	protected Texture lastTexture = null;
	protected float invTexWidth = 0, invTexHeight = 0;
	protected boolean drawing = false;
	protected float colorPacked = Color.WHITE_FLOAT_BITS;
	protected Mesh mesh;
	protected boolean blendingDisabled = false;
	protected int blendSrcFunc = GL20.GL_SRC_ALPHA;
	protected int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
	protected int blendSrcFuncAlpha = GL20.GL_SRC_ALPHA;
	protected int blendDstFuncAlpha = GL20.GL_ONE_MINUS_SRC_ALPHA;
	protected ShaderProgram customShader = null;
	protected final int vertexSize;

	/**
	 * Constructs a new SpriteBatch. Sets the projection matrix to an orthographic projection with
	 * y-axis point upwards, x-axis point to the right and the origin being in the bottom left
	 * corner of the screen. The projection will be pixel perfect with respect to the current screen
	 * resolution.
	 * <p>
	 * The defaultShader specifies the shader to use. Note that the names for uniforms for this
	 * default shader are different than the ones expect for shaders set with {@link
	 * #setShader(ShaderProgram)}.
	 *
	 * @param size The max number of sprites in a single batch. Max of 8191.
	 * @param defaultShader The default shader to use. This is not owned by the SpriteBatch and must
	 * be disposed separately.
	 */
	public AbstractSpriteBatch(int size, int vertexSize, ShaderProgram defaultShader, VertexAttribute... attributes)
	{
		ensureNotNull(defaultShader, "defaultShader");

		this.shader = defaultShader;
		this.vertexSize = vertexSize;

		// 32767 is max vertex index, so 32767 / 4 vertices per sprite = 8191 sprites max.
		if(size > 8191)
			throw new IllegalArgumentException("Can't have more than 8191 sprites per batch: " + size);

		VertexDataType vertexDataType = (Gdx.gl30 != null)
				? VertexDataType.VertexBufferObjectWithVAO
				: VertexDataType.VertexArray;

		mesh = new Mesh(vertexDataType, false, size * 4, size * 6, attributes);

		projectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		vertices = new float[size * 4 * vertexSize];

		int len = size * 6;
		short[] indices = new short[len];
		short j = 0;
		for(int i = 0; i < len; i += 6, j += 4)
		{
			indices[i] = j;
			indices[i + 1] = (short)(j + 1);
			indices[i + 2] = (short)(j + 2);
			indices[i + 3] = (short)(j + 2);
			indices[i + 4] = (short)(j + 3);
			indices[i + 5] = j;
		}
		mesh.setIndices(indices);
	}

	@Override
	public void begin()
	{
		if(drawing)
			throw new IllegalStateException("SpriteBatch.end must be called before begin.");
		renderCalls = 0;

		Gdx.gl.glDepthMask(false);
		getShader().begin();
		setupShader();

		drawing = true;
	}

	@Override
	public void end()
	{
		if(!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before end.");
		if(idx > 0)
			flush();
		lastTexture = null;
		drawing = false;

		GL20 gl = Gdx.gl;
		gl.glDepthMask(true);
		if(isBlendingEnabled())
			gl.glDisable(GL20.GL_BLEND);

		getShader().end();
	}

	@Override
	public void dispose()
	{
		mesh.dispose();
	}

	@Override
	public void setColor(Color tint)
	{
		color.set(tint);
		colorPacked = tint.toFloatBits();
	}

	protected void setupShader()
	{
		combinedMatrix.set(projectionMatrix).mul(transformMatrix);
		ShaderProgram shader = getShader();
		shader.setUniformMatrix("u_projTrans", combinedMatrix);
		shader.setUniformi("u_texture", 0);
	}

	@Override
	public void setColor(float r, float g, float b, float a)
	{
		color.set(r, g, b, a);
		colorPacked = color.toFloatBits();
	}

	protected void switchTexture(Texture texture)
	{
		flush();
		lastTexture = texture;
		invTexWidth = 1.0f / texture.getWidth();
		invTexHeight = 1.0f / texture.getHeight();
	}

	@Override
	public Color getColor()
	{
		return color;
	}

	@Override
	public void setPackedColor(float packedColor)
	{
		Color.abgr8888ToColor(color, packedColor);
		this.colorPacked = packedColor;
	}

	@Override
	public float getPackedColor()
	{
		return colorPacked;
	}

	@Override
	public void flush()
	{
		if(idx == 0)
			return;

		renderCalls++;
		totalRenderCalls++;
		int spritesInBatch = idx / (vertexSize * 4);
		if(spritesInBatch > maxSpritesInBatch)
			maxSpritesInBatch = spritesInBatch;
		int count = spritesInBatch * 6;

		lastTexture.bind();
		Mesh mesh = this.mesh;
		mesh.setVertices(vertices, 0, idx);
		mesh.getIndicesBuffer().position(0);
		mesh.getIndicesBuffer().limit(count);

		if(blendingDisabled)
			Gdx.gl.glDisable(GL20.GL_BLEND);
		else
		{
			Gdx.gl.glEnable(GL20.GL_BLEND);
			if(blendSrcFunc != -1)
				Gdx.gl.glBlendFuncSeparate(blendSrcFunc, blendDstFunc, blendSrcFuncAlpha, blendDstFuncAlpha);
		}

		mesh.render(getShader(), GL20.GL_TRIANGLES, 0, count);

		idx = 0;
	}

	@Override
	public void disableBlending()
	{
		if(blendingDisabled)
			return;
		flush();
		blendingDisabled = true;
	}

	@Override
	public void enableBlending()
	{
		if(!blendingDisabled)
			return;
		flush();
		blendingDisabled = false;
	}

	@Override
	public void setBlendFunction(int srcFunc, int dstFunc)
	{
		setBlendFunctionSeparate(srcFunc, dstFunc, srcFunc, dstFunc);
	}

	@Override
	public void setBlendFunctionSeparate(int srcFuncColor, int dstFuncColor, int srcFuncAlpha, int dstFuncAlpha)
	{
		if(blendSrcFunc == srcFuncColor
				&& blendDstFunc == dstFuncColor
				&& blendSrcFuncAlpha == srcFuncAlpha
				&& blendDstFuncAlpha == dstFuncAlpha)
			return;

		flush();
		blendSrcFunc = srcFuncColor;
		blendDstFunc = dstFuncColor;
		blendSrcFuncAlpha = srcFuncAlpha;
		blendDstFuncAlpha = dstFuncAlpha;
	}

	@Override
	public int getBlendSrcFunc()
	{
		return blendSrcFunc;
	}

	@Override
	public int getBlendDstFunc()
	{
		return blendDstFunc;
	}

	@Override
	public int getBlendSrcFuncAlpha()
	{
		return blendSrcFuncAlpha;
	}

	@Override
	public int getBlendDstFuncAlpha()
	{
		return blendDstFuncAlpha;
	}

	@Override
	public Matrix4 getProjectionMatrix()
	{
		return projectionMatrix;
	}

	@Override
	public Matrix4 getTransformMatrix()
	{
		return transformMatrix;
	}

	@Override
	public void setProjectionMatrix(Matrix4 projection)
	{
		if(drawing)
			flush();

		projectionMatrix.set(projection);

		if(drawing)
			setupShader();
	}

	@Override
	public void setTransformMatrix(Matrix4 transform)
	{
		if(drawing)
			flush();

		transformMatrix.set(transform);

		if(drawing)
			setupShader();
	}

	@Override
	public void setShader(ShaderProgram shader)
	{
		if(drawing)
		{
			flush();
			getShader().end();
		}

		customShader = shader;

		if(drawing)
		{
			getShader().begin();
			setupShader();
		}
	}

	@Override
	public ShaderProgram getShader()
	{
		return customShader == null ? shader : customShader;
	}

	@Override
	public boolean isBlendingEnabled()
	{
		return !blendingDisabled;
	}

	public boolean isDrawing()
	{
		return drawing;
	}
}
