package net.jumpai.world.lightmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.IntMap;
import net.jumpai.util.gfx.YieldingFrameBuffer;

import static net.jumpai.util.Validation.ensureNotNull;
import static net.jumpai.world.lightmap.LightMap.LIGHT_MAP_PAGE_SIZE;
import static net.jumpai.world.lightmap.LightMap.LIGHT_MAP_PAGE_TILE_COUNT;
import static net.jumpai.world.lightmap.LightMap.LIGHT_MAP_TILE_SIZE;

/**
 * Renders {@link LightMapPage}s using FBOs
 * <p>
 * Created on 2020-07-31.
 *
 * @author Alexander Winter
 */
public class FBOLightMapPageRenderer implements LightMapPageRenderer
{
	private static final int CIRCLE_SIZE = 11;
	/**
	 * Padding applied to the page renders (how much of neighboring pages tiles
	 * are rendered around a page). This fixes the TextureFilter issue that
	 * arises when interpolating around the edges
	 */
	private static final int PADDING = 1;

	private final IntMap<Texture> renders = new IntMap<>();

	private final OrthographicCamera cam;
	private final SpriteBatch batch;

	private final ShaderProgram radialShader;
	/**
	 * Dummy texture passed to the shader to be able to render using a batch
	 */
	private final Texture dummyRegion;

	public FBOLightMapPageRenderer(ShaderProgram radialShader, ShaderProgram defaultShader, Texture dummyRegion)
	{
		ensureNotNull(radialShader, "radialShader");
		ensureNotNull(dummyRegion, "dummyRegion");
		this.radialShader = radialShader;
		this.dummyRegion = dummyRegion;

		cam = new OrthographicCamera();
		cam.setToOrtho(true, LIGHT_MAP_PAGE_TILE_COUNT + PADDING * 2, LIGHT_MAP_PAGE_TILE_COUNT + PADDING * 2);
		cam.update();

		batch = new SpriteBatch(1000, defaultShader);
	}

	@Override
	public Texture render(LightMap map, int pageX, int pageY, float topBorder)
	{
		int pageId = (pageX & 0xFFFF) | ((pageY & 0xFFFF) << 16);
		Texture texture = renders.get(pageId);

		if(texture != null)
			return texture;

		FrameBuffer fbo = new YieldingFrameBuffer(Format.RGBA8888,
				LIGHT_MAP_PAGE_TILE_COUNT + PADDING * 2,
				LIGHT_MAP_PAGE_TILE_COUNT + PADDING * 2,
				false);

		batch.setProjectionMatrix(cam.combined);
		fbo.begin();

		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.setColor(Color.WHITE);
		batch.setShader(radialShader);

		int start = -CIRCLE_SIZE / 2 - PADDING;
		int end = LIGHT_MAP_PAGE_TILE_COUNT + CIRCLE_SIZE / 2 + PADDING;

		for(int x = start; x < end; x++)
			for(int y = start; y < end; y++)
			{
				int curPageX, curPageY;

				if(x < 0)
					curPageX = pageX - 1;
				else if(x >= LIGHT_MAP_PAGE_TILE_COUNT)
					curPageX = pageX + 1;
				else
					curPageX = pageX;

				if(y < 0)
					curPageY = pageY - 1;
				else if(y >= LIGHT_MAP_PAGE_TILE_COUNT)
					curPageY = pageY + 1;
				else
					curPageY = pageY;

				LightMapPage page = map.getPage(curPageX, curPageY);

				int inPageX = Math.floorMod(x, LIGHT_MAP_PAGE_TILE_COUNT);
				int inPageY = Math.floorMod(y, LIGHT_MAP_PAGE_TILE_COUNT);

				float posY = pageY * LIGHT_MAP_PAGE_SIZE + y * LIGHT_MAP_TILE_SIZE;

				float alpha;

				if(posY >= topBorder)
					alpha = 1f;
				else
				{
					alpha = page == null ? 0f : page.getLightLevel(inPageX, inPageY);

					if(alpha == 0f)
						continue;
				}

				batch.setColor(alpha, alpha, alpha, 0.75f);
				//noinspection IntegerDivisionInFloatingPointContext
				batch.draw(dummyRegion,
						x - CIRCLE_SIZE / 2f + 0.5f + PADDING, y - CIRCLE_SIZE / 2f + 0.5f + PADDING,
						CIRCLE_SIZE, CIRCLE_SIZE);
			}

		batch.end();
		fbo.end();

		texture = fbo.getColorBufferTexture();
		renders.put(pageId, texture);
		fbo.dispose();
		return texture;
	}

	@Override
	public int getPadding()
	{
		return PADDING;
	}

	@Override
	public void resetPageCache(int pageX, int pageY)
	{
		int pageId = (pageX & 0xFFFF) | ((pageY & 0xFFFF) << 16);
		Texture texture = renders.remove(pageId);
		if(texture != null)
			texture.dispose();
	}

	@Override
	public void resetAllPageCaches()
	{
		renders.forEach(e -> e.value.dispose());
		renders.clear();
	}

	@Override
	public void dispose()
	{
		resetAllPageCaches();
	}
}
