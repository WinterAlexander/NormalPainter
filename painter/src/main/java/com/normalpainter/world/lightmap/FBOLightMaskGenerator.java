package com.normalpainter.world.lightmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer.FrameBufferBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.normalpainter.world.component.render.RenderPriority;
import com.normalpainter.world.camera.WorldCamera;
import com.normalpainter.world.component.light.LightEmitter;
import com.normalpainter.world.component.light.LightSource;

import static com.badlogic.gdx.graphics.GL20.GL_ONE;
import static com.badlogic.gdx.graphics.GL20.GL_ZERO;
import static com.badlogic.gdx.math.MathUtils.floor;
import static com.normalpainter.util.Validation.ensureNotNull;

/**
 * FBO implementation of a {@link LightMaskGenerator}
 * <p>
 * Created on 2019-10-10.
 *
 * @author Alexander Winter
 */
public class FBOLightMaskGenerator implements LightMaskGenerator
{
	public static final int MAX_LIGHTS = 120;

	private final Vector2 tmpVec2 = new Vector2();
	private final Vector2 scale = new Vector2(1f, 1f), offset = new Vector2(0f, 0f);

	private final OrthographicCamera cam;
	private final FrameBuffer lightMaskFbo;
	private final SpriteBatch batch;

	private final LightMapPageRenderer pageRenderer;
	private final ShaderProgram builderShader;

	private final int fboWidth, fboHeight;
	private final boolean color;

	private final float[] tmpLights = new float[MAX_LIGHTS * 4];
	private final float[] tmpLightColors = new float[MAX_LIGHTS * 3];

	public FBOLightMaskGenerator(LightMapPageRenderer pageRenderer,
	                             ShaderProgram builderShader,
	                             ShaderProgram defaultShader,
	                             int width,
	                             int height,
	                             boolean color)
	{
		ensureNotNull(pageRenderer, "pageRenderer");
		ensureNotNull(builderShader, "builderShader");

		this.pageRenderer = pageRenderer;
		this.builderShader = builderShader;

		this.fboWidth = width;
		this.fboHeight = height;
		this.color = color;

		cam = new OrthographicCamera();
		cam.setToOrtho(false, fboWidth, fboHeight);
		cam.update();

		FrameBufferBuilder frameBufferBuilder = new FrameBufferBuilder(fboWidth, fboHeight);
		frameBufferBuilder.addColorTextureAttachment(GL30.GL_RGBA8, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE);
		if(color)
			frameBufferBuilder.addColorTextureAttachment(GL30.GL_RGB8, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE);

		lightMaskFbo = frameBufferBuilder.build();

		batch = new SpriteBatch(1000, defaultShader);
		batch.setBlendFunction(GL_ONE, GL_ZERO);
	}

	@Override
	public void generateMasks(WorldCamera camera, Iterable<LightEmitter> emitters, LightMap lightMap, float topBorder)
	{
		tmpVec2.set(camera.getViewSize()).scl(camera.getActualZoom());

		float minX = camera.getActualPosition().x - tmpVec2.x / 2f;
		float minY = camera.getActualPosition().y - tmpVec2.y / 2f;

		int pageX = floor(minX / LightMap.LIGHT_MAP_PAGE_SIZE);
		int pageY = floor(minY / LightMap.LIGHT_MAP_PAGE_SIZE);

		Texture bottomLeft = pageRenderer.render(lightMap, pageX, pageY, topBorder);
		Texture bottomRight = pageRenderer.render(lightMap, pageX + 1, pageY, topBorder);
		Texture topLeft = pageRenderer.render(lightMap, pageX, pageY + 1, topBorder);
		Texture topRight = pageRenderer.render(lightMap, pageX + 1, pageY + 1, topBorder);

		batch.setProjectionMatrix(cam.combined);
		lightMaskFbo.begin();

		batch.begin();
		batch.setColor(Color.WHITE);
		batch.setShader(builderShader);

		builderShader.setUniformf("u_paddingOffset", 1f / (LightMap.LIGHT_MAP_PAGE_TILE_COUNT + pageRenderer.getPadding() * 2));

		builderShader.setUniformf("u_cameraOffset", minX, minY);
		builderShader.setUniformf("u_cameraSize", tmpVec2);

		builderShader.setUniformf("u_backmaskScale", tmpVec2.scl(1f / LightMap.LIGHT_MAP_PAGE_SIZE));
		builderShader.setUniformf("u_backmaskOffset", lightMap.getPageOffset(minX, minY).scl(1f / LightMap.LIGHT_MAP_PAGE_SIZE));

		int countLights = 0;

		for(LightEmitter emitter : emitters)
		{
			for(LightSource source : emitter.getLightSources())
			{
				if(lightMap.isSourceBlocked(RenderPriority.NULL, source.getLocation().x, source.getLocation().y))
					continue;

				tmpLights[countLights * 4] = source.getLocation().x;
				tmpLights[countLights * 4 + 1] = source.getLocation().y;
				tmpLights[countLights * 4 + 2] = source.getRadius();
				tmpLights[countLights * 4 + 3] = source.getAttenuation();

				if(color)
				{
					tmpLightColors[countLights * 3] = source.getColor().r;
					tmpLightColors[countLights * 3 + 1] = source.getColor().g;
					tmpLightColors[countLights * 3 + 2] = source.getColor().b;
				}

				countLights++;
			}

			if(countLights >= MAX_LIGHTS)
				break;
		}

		builderShader.setUniformi("u_lightCount", countLights);
		builderShader.setUniform4fv("u_lights", tmpLights, 0, countLights * 4);
		if(color)
			builderShader.setUniform3fv("u_lightColors", tmpLightColors, 0, countLights * 3);

		builderShader.setUniformi("u_lightMap_BR", 1);
		builderShader.setUniformi("u_lightMap_TL", 2);
		builderShader.setUniformi("u_lightMap_TR", 3);

		bottomRight.bind(1);
		topLeft.bind(2);
		topRight.bind(3);

		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		batch.draw(bottomLeft, 0f, 0f, fboWidth, fboHeight);

		batch.setShader(null);
		batch.end();
		lightMaskFbo.end();
	}

	@Override
	public Texture getLightMask()
	{
		return lightMaskFbo.getColorBufferTexture();
	}

	@Override
	public Texture getColorMask()
	{
		if(!color)
			throw new UnsupportedOperationException("Color mask not supported");

		return lightMaskFbo.getTextureAttachments().get(1);
	}

	@Override
	public Vector2 getMaskScale()
	{
		return scale;
	}

	@Override
	public Vector2 getMaskOffset()
	{
		return offset;
	}

	@Override
	public boolean supportsColor()
	{
		return color;
	}

	public FrameBuffer getFBO()
	{
		return lightMaskFbo;
	}

	public LightMapPageRenderer getPageRenderer()
	{
		return pageRenderer;
	}
}
