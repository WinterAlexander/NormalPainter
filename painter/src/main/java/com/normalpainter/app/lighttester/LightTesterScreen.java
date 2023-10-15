package com.normalpainter.app.lighttester;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader.ShaderProgramParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.normalpainter.app.NormalPainterApp;
import com.normalpainter.app.NormalPainterScreen;
import com.normalpainter.app.StageStackedScreen;
import com.normalpainter.util.ui.dynamic.DynamicLabel;
import com.normalpainter.util.ui.dynamic.DynamicStage;
import com.normalpainter.util.ui.dynamic.DynamicTable;
import com.normalpainter.util.ui.listener.ChangeAdapter;
import com.normalpainter.util.ui.listener.TouchOutOfActorListener;
import com.normalpainter.util.ui.skin.BetterSkinLoader.SkinParam;
import com.normalpainter.world.World;
import com.normalpainter.world.WorldCameraImpl;
import com.normalpainter.component.render.RenderPriority;
import com.normalpainter.lightmap.FBOLightMapPageRenderer;
import com.normalpainter.lightmap.FBOLightMaskGenerator;
import com.normalpainter.lightmap.LightMap;
import com.normalpainter.lightmap.LightMapPage;
import com.normalpainter.render.AssetSupplier;
import com.normalpainter.render.Assets;
import com.normalpainter.render.lighting.LightRenderMode;
import com.normalpainter.render.lighting.LightingBatchImpl;
import com.winteralexander.gdx.utils.input.InputUtil;
import com.winteralexander.gdx.utils.log.NullLogger;
import com.normalpainter.component.light.FireLightSource;
import com.normalpainter.component.light.LavaLightSource;
import com.normalpainter.component.light.LightEmitter;
import com.normalpainter.component.light.LightEmitterComponent;
import com.normalpainter.component.light.LightSource;
import com.normalpainter.component.location.LocationComponent;

import java.io.File;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.normalpainter.util.gdx.Scene2dUtil.invalidateRecursively;
import static com.winteralexander.gdx.utils.math.NumberUtil.tryParseFloat;

/**
 * Screen for {@link LightTesterApp} also used in {@link NormalPainterApp}
 * <p>
 * Created on 2020-12-20.
 *
 * @author Alexander Winter
 */
public class LightTesterScreen extends InputAdapter implements StageStackedScreen
{
	private final String texturePath, normalPath;

	private LightingBatchImpl batch;
	public WorldCameraImpl camera;

	private FBOLightMaskGenerator lightMaskGenerator;
	public TextureRegion texture, normal;

	private final LightMap darkLightMap = new LightMap(), litLightMap;

	private int lightType = 0;
	private final AssetSupplier assets = new AssetSupplier(new NullLogger());

	private final LightSource[] sources = new LightSource[5];

	private final Array<LightEmitter> emitters = new Array<>();

	private Texture whitePixel;

	private DynamicStage stage;

	private boolean sun = false;

	private final Vector2 scale = new Vector2(1f, 1f);
	private float rotation = 0f;

	private boolean draggingCam = false;
	private final Vector3 camPrev = new Vector3(), camCur = new Vector3();
	private final Vector2 camDrag = new Vector2();

	public LightTesterScreen(String texturePath, String normalPath)
	{
		this.texturePath = texturePath;
		this.normalPath = normalPath;

		litLightMap = new LightMap() {
			private final LightMapPage page = new LightMapPage() {
				@Override
				public float getLightLevel(int tileX, int tileY) {
					return 1f;
				}
			};

			@Override
			public float getLightLevel(int tileX, int tileY) {
				return 1f;
			}

			@Override
			public LightMapPage getPage(int x, int y) {
				return page;
			}

			@Override
			public boolean isSourceBlocked(RenderPriority priority, float x, float y) {
				return true;
			}
		};
	}

	public void create()
	{
		ShaderProgram.pedantic = false;

		Assets.DEFAULT_SHADER.load(assets);
		Assets.LIGHTMASK_BUILDER_COLOR_SHADER.load(assets);
		Assets.WHITE_PIXEL.load(assets);
		Assets.BACK_LIGHT_SHADER.load(assets);

		BitmapFontParameter fontParam = new BitmapFontParameter();
		fontParam.minFilter = TextureFilter.Linear;
		fontParam.magFilter = TextureFilter.Linear; //fonts only get upscaled on giant resolutions
		fontParam.genMipMaps = false;

		assets.load("gfx/fonts/baloo/16.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/24.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/32.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/48.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/64.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/128.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/256.fnt", BitmapFont.class, fontParam);

		ShaderProgramParameter tintShaderParam = assets.loadShader("tint", "shaders/default.vs.glsl", "shaders/tint.fs.glsl");

		SkinParam param = new SkinParam(
				"gfx/uiskin.atlas",
				NormalPainterScreen.STAGE_WIDTH,
				Gdx.graphics.getWidth(),
				tintShaderParam,
				"gfx/fonts/baloo/16.fnt",
				"gfx/fonts/baloo/24.fnt",
				"gfx/fonts/baloo/32.fnt",
				"gfx/fonts/baloo/48.fnt",
				"gfx/fonts/baloo/64.fnt",
				"gfx/fonts/baloo/128.fnt",
				"gfx/fonts/baloo/256.fnt");

		assets.load("gfx/uiskin.json", Skin.class, param);

		TextureParameter linearParam = new TextureParameter();
		linearParam.minFilter = TextureFilter.MipMapLinearLinear;
		linearParam.magFilter = TextureFilter.Linear;
		linearParam.genMipMaps = true;

		if(texturePath != null)
			assets.load(texturePath, Texture.class, linearParam);

		if(normalPath != null)
			assets.load(normalPath, Texture.class, linearParam);

		assets.finishLoading();

		lightMaskGenerator = new FBOLightMaskGenerator(
				new FBOLightMapPageRenderer(
						Assets.BACK_LIGHT_SHADER.resolve(assets),
						Assets.DEFAULT_SHADER.resolve(assets).getNoLightingShader(),
						whitePixel = Assets.WHITE_PIXEL.resolve(assets)),
				Assets.LIGHTMASK_BUILDER_COLOR_SHADER.resolve(assets),
				Assets.DEFAULT_SHADER.resolve(assets).getNoLightingShader(),
				1920 * 2,
				1080 * 2,
				true);

		if(texturePath != null)
			texture = assets.getTexture(texturePath);
		if(normalPath != null)
			normal = assets.getTexture(normalPath);

		batch = new LightingBatchImpl(Assets.DEFAULT_SHADER.resolve(assets).getColorLightingShader(),
				lightMaskGenerator,
				true);

		camera = new WorldCameraImpl();

		camera.setToOrtho(false, 18000f, 10125f);
		camera.position.set(0, 0, 0);

		emitters.add(new LightEmitter() {
			@Override
			public LightEmitterComponent getLightEmitterComponent() {
				return new LightEmitterComponent() {
					@Override
					public Array<LightSource> getLightSources() {
						Array<LightSource> arr = new Array<>();
						arr.add(sources[lightType]);
						return arr;
					}

					@Override
					public void tick(float delta) {}
				};
			}

			@Override
			public LocationComponent getLocationComponent()
			{
				return new LocationComponent() {
					@Override
					public World getWorld() {
						return null;
					}

					@Override
					public Vector2 getPosition() {
						Vector3 vec3 = new Vector3();
						vec3.x = Gdx.input.getX();
						vec3.y = Gdx.input.getY();

						camera.unproject(vec3);
						return new Vector2(vec3.x, vec3.y);
					}
				};
			}
		});

		sources[0] = new LightSource(() -> emitters.get(0).getPosition(), Color.WHITE, 6000f, 0.2f);
		sources[1] = new FireLightSource(() -> emitters.get(0).getPosition(), 6000f);
		sources[2] = new LavaLightSource(() -> emitters.get(0).getPosition());
		sources[3] = new LightSource(() -> emitters.get(0).getPosition(), Color.GREEN, 2000f, 0.4f);
		sources[4] = new LightSource(() -> emitters.get(0).getPosition(), Color.WHITE, 8000f, 0.03f);

		stage = new DynamicStage(new FitViewport(NormalPainterScreen.STAGE_WIDTH, NormalPainterScreen.STAGE_HEIGHT), batch);

		DynamicTable table = new DynamicTable(assets.getSkin());
		table.setFillParent(true);
		table.top();
		table.left();
		table.pad(10f);
		table.add("LightTester v1.3 for MakerKing").left().row();

		if(texturePath != null)
			table.add(new DynamicLabel(() -> "Texture " + texturePath, assets.getSkin(), "default-tiny")).left().row();

		if(normalPath != null)
			table.add(new DynamicLabel(() -> "Normal " + normalPath, assets.getSkin(), "default-tiny")).left().row();

		table.add(new DynamicLabel(() -> "Light type " + this.lightType, assets.getSkin(), "default-tiny")).left().padBottom(20f).row();
		table.add("Press F1 to reload assets", "default-tiny").left().row();
		table.add("Press F5 to take a screenshot", "default-tiny").left().row();
		table.add("Press 1-4 to change the light type", "default-tiny").left().row();
		table.add("Use mouse wheel to zoom (holding CTRL zooms slower)", "default-tiny").left().row();

		DynamicTable config = new DynamicTable(assets.getSkin());
		config.left();
		config.add("Scale: ", "default-tiny").left();
		TextField scaleX = new TextField("1", assets.getSkin(), "default-tiny");
		scaleX.addListener(new ChangeAdapter(() -> scale.x = tryParseFloat(scaleX.getText(), 1f)));

		TextField scaleY = new TextField("1", assets.getSkin(), "default-tiny");
		scaleY.addListener(new ChangeAdapter(() -> scale.y = tryParseFloat(scaleY.getText(), 1f)));

		config.add(scaleX).width(40f).left();
		config.add(" x ", "default-tiny").left();
		config.add(scaleY).width(40f).left().row();

		config.add("Rotation: ", "default-tiny").left();

		TextField rotField = new TextField("0", assets.getSkin(), "default-tiny") {
			@Override
			public float getPrefWidth() {
				return 40f;
			}
		};
		rotField.addListener(new ChangeAdapter(() -> rotation = tryParseFloat(rotField.getText(), 0f)));

		config.add(rotField).fillX().colspan(3).left();

		stage.addListener(new TouchOutOfActorListener(config, () -> stage.unfocusAll()));

		table.add().expandY().row();
		table.add(config).left();

		stage.addActor(table);
	}

	@Override
	public void show()
	{
		InputUtil.registerInput(stage);
		InputUtil.registerInput(this);
	}

	@Override
	public void hide()
	{
		InputUtil.unregisterInput(stage);
		InputUtil.unregisterInput(this);
	}

	@Override
	public void finish() {}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);

		if(assets.isFinished())
		{
			assets.generateFonts(assets.getSkin(), NormalPainterScreen.STAGE_WIDTH, width);

			for(BitmapFont font : assets.getAll(BitmapFont.class, new Array<>()))
				font.setFixedWidthGlyphs("0123456789");

			invalidateRecursively(stage.getRoot());
		}
	}

	@Override
	public void render(float delta)
	{
		stage.act();
		stage.update();
		camera.update();

		for(LightSource source : sources)
			source.update(Gdx.graphics.getDeltaTime());

		lightMaskGenerator.generateMasks(camera, emitters, sun ? litLightMap : darkLightMap, Float.POSITIVE_INFINITY);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

		Gdx.gl.glViewport(
				stage.getViewport().getScreenX(),
				stage.getViewport().getScreenY(),
				stage.getViewport().getScreenWidth(),
				stage.getViewport().getScreenHeight());

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		batch.setLightRenderMode(LightRenderMode.STANDARD);

		float width = texture.getRegionWidth() * NormalPainterScreen.WORLD_PERFECT_SCALE;
		float height = texture.getRegionHeight() * NormalPainterScreen.WORLD_PERFECT_SCALE;

		batch.draw(whitePixel,
				camera.position.x - camera.viewportWidth * camera.zoom / 2f,
				camera.position.y - camera.viewportHeight * camera.zoom / 2f,
				camera.viewportWidth * camera.zoom,
				camera.viewportHeight * camera.zoom);

		batch.drawNormalMapped(texture,
				normal,
				-width / 2f,
				-height / 2f,
				width / 2f,
				height / 2f,
				width,
				height,
				scale.x,
				scale.y,
				rotation);

		batch.setLightRenderMode(LightRenderMode.OFF);

		batch.end();

		stage.draw();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

	@Override
	public boolean scrolled(float amountX, float amount)
	{
		float zoomGain;
		if(!(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)))
			zoomGain = 1.1f;
		else
			zoomGain = 1.02f;

		if(amount > 0)
			camera.zoom *= zoomGain;
		else if(amount < 0)
			camera.zoom /= zoomGain;

		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if(button == 1)
		{
			draggingCam = true;
			camDrag.set(screenX, screenY);
			return true;
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{

		if(draggingCam)
		{
			camPrev.set(camDrag.x, camDrag.y, 0);
			camCur.set(screenX, screenY, 0);

			camera.unproject(camPrev,
					stage.getViewport().getScreenX(),
					stage.getViewport().getScreenY(),
					stage.getViewport().getScreenWidth(),
					stage.getViewport().getScreenHeight());
			camera.unproject(camCur,
					stage.getViewport().getScreenX(),
					stage.getViewport().getScreenY(),
					stage.getViewport().getScreenWidth(),
					stage.getViewport().getScreenHeight());

			camera.position.sub(camCur.x - camPrev.x, camCur.y - camPrev.y, 0f);

			camDrag.set(screenX, screenY);
			return true;
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if(button == 1)
		{
			draggingCam = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean keyDown(int keycode)
	{
		if(keycode == Keys.NUM_1)
		{
			lightType = 0;
			return true;
		}
		else if(keycode == Keys.NUM_2)
		{
			lightType = 1;
			return true;
		}
		else if(keycode == Keys.NUM_3)
		{
			lightType = 2;
			return true;
		}
		else if(keycode == Keys.NUM_4)
		{
			lightType = 3;
			return true;
		}
		else if(keycode == Keys.NUM_5)
		{
			lightType = 4;
			return true;
		}

		if(keycode == Keys.S)
		{
			sun = !sun;
			lightMaskGenerator.getPageRenderer().resetAllPageCaches();
			return true;
		}

		if(keycode == Keys.F1)
		{
			reloadAssets();
			return true;
		}

		if(keycode != Keys.F5)
			return false;

		screenshot();
		return true;
	}

	private void reloadAssets()
	{
		if(texturePath != null)
			assets.reload(texturePath);
		if(normalPath != null)
			assets.reload(normalPath);
		assets.finishLoading();

		if(texturePath != null)
			texture = assets.getTexture(texturePath);
		if(normalPath != null)
			normal = assets.getTexture(normalPath);
	}

	private void screenshot()
	{
		byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

		Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
		BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
		PixmapIO.writePNG(new FileHandle(new File("lightapp_" + System.currentTimeMillis() + ".png")), pixmap);
		pixmap.dispose();
	}

	@Override
	public Stage getStage()
	{
		return stage;
	}
}