package com.normalpainter.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader.ShaderProgramParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.normalpainter.app.dialog.OkayDialog;
import com.normalpainter.render.ui.SkinTinter;
import com.normalpainter.app.buffer.GdxPixmap;
import com.normalpainter.app.buffer.RangedGdxBuffer;
import com.normalpainter.util.ui.listener.ClickAdapter;
import com.normalpainter.util.ui.skin.BetterSkinLoader.SkinParam;
import com.normalpainter.render.AssetSupplier;
import com.normalpainter.render.Assets;
import com.normalpainter.app.lighttester.LightTesterScreen;
import com.normalpainter.util.ReflectionUtil;
import com.normalpainter.util.gdx.GdxUtil;
import com.normalpainter.util.log.Logger.LogLevel;
import com.normalpainter.util.log.SimpleLogger;

import java.io.File;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static com.normalpainter.util.gdx.Scene2dUtil.invalidateRecursively;

/**
 * {@link NormalPainterApp}'s main screen
 * <p>
 * Created on 2020-12-19.
 *
 * @author Alexander Winter
 */
public class NormalPainterScreen extends InputAdapter implements StageStackedScreen
{
	public static final float MAX_ALLOWED_WORLD_SCALING = 1.15f;
	public static final float WORLD_WIDTH = 18000f;
	public static final float WORLD_HEIGHT = 10125f;
	public static final float WORLD_PERFECT_SCALE = WORLD_WIDTH / 3840f / MAX_ALLOWED_WORLD_SCALING;
	public static final float STAGE_WIDTH = 1600f;
	public static final float STAGE_HEIGHT = 900f;

	public final NormalPainterApp app;

	public NormalPainterConfig config;

	public NormalPainterStage stage;

	public SpriteBatch batch;
	public OrthographicCamera camera;
	public ShapeRenderer shapeRenderer;

	public LightTesterScreen lightScreen;

	public ColorComponent colorPicker;
	public PaintComponent painter;

	private final I18NBundle mockBundle = new I18NBundle();

	public final AssetSupplier assets = new AssetSupplier(new SimpleLogger(LogLevel.DEBUG));

	public Texture whitePixel;
	public TextureRegion normalRange, normalCursor, bg, flat;

	public Pixmap buffer;

	public boolean renderFlat = false;
	public float maskOpacity = 0.75f, flatOpacity = 0.75f;
	public boolean maskOnTop = false, flatOnTop = false;

	public boolean viewNormals = false;
	public int normalSpacing = 5;
	public boolean livePreview = true;
	public boolean drawLinesOnDrag = true;

	public boolean fillBlue = true;

	private final Vector3 mousePos = new Vector3();

	public final Vector2 lastDraw = new Vector2();

	private int prevPixX = -1, prevPixY = -1;
	private boolean draggingCam = false, draggingAxis = false, drawing = false;

	private final Vector3 camPrev = new Vector3(), camCur = new Vector3();
	private final Vector2 camDrag = new Vector2();

	private final Vector2 tmpVec2 = new Vector2();

	public NormalPainterScreen(NormalPainterApp app)
	{
		this.app = app;
	}

	public void create()
	{
		ObjectMap<String, String> props = new ObjectMap<>();
		props.put("okdialog_ok", "Ok");
		ReflectionUtil.set(mockBundle, "properties", props);

		colorPicker = new ColorComponent(this);
		painter = new PaintComponent(this);

		colorPicker.jPenWrapper.addListener(painter);

		ShaderProgram.pedantic = false;

		Assets.DEFAULT_SHADER.load(assets);
		Assets.LIGHTMASK_BUILDER_COLOR_SHADER.load(assets);
		Assets.WHITE_PIXEL.load(assets);
		Assets.BACK_LIGHT_SHADER.load(assets);

		TextureParameter mipMapParams = new TextureParameter();
		mipMapParams.minFilter = TextureFilter.MipMapLinearLinear;
		mipMapParams.magFilter = TextureFilter.Linear;
		mipMapParams.genMipMaps = true;

		BitmapFontParameter fontParam = new BitmapFontParameter();
		fontParam.minFilter = TextureFilter.Linear;
		fontParam.magFilter = TextureFilter.Linear; //fonts only get upscaled on giant resolutions
		fontParam.genMipMaps = false;

		Assets.WHITE_PIXEL.load(assets);

		assets.load("gfx/fonts/baloo/16.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/24.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/32.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/48.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/64.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/128.fnt", BitmapFont.class, fontParam);
		assets.load("gfx/fonts/baloo/256.fnt", BitmapFont.class, fontParam);

		assets.load("gfx/normal_cursor.png", Texture.class, mipMapParams);
		assets.load("gfx/normal_range.png", Texture.class, mipMapParams);

		assets.load("gfx/bg.png", Texture.class);

		ShaderProgramParameter tintShaderParam = assets.loadShader("tint", "shaders/default.vs.glsl", "shaders/tint.fs.glsl");

		SkinParam param = new SkinParam(
				"gfx/uiskin.atlas",
				STAGE_WIDTH,
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

		assets.finishLoading();

		whitePixel = Assets.WHITE_PIXEL.resolve(assets);
		normalRange = assets.getTexture("gfx/normal_range.png");
		normalCursor = assets.getTexture("gfx/normal_cursor.png");
		Texture bgTex = assets.get("gfx/bg.png", Texture.class);
		bgTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		bg = new TextureRegion(bgTex, 0, 0, 1920, 1080);

		new SkinTinter(assets.getSkin());
		batch = new SpriteBatch(1000, Assets.DEFAULT_SHADER.resolve(assets).getNoLightingShader());
		shapeRenderer = new ShapeRenderer();

		camera = new OrthographicCamera();

		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		camera.position.set(0, 0, 0);

		stage = new NormalPainterStage(this);

		lightScreen = new LightTesterScreen(null, null);
		lightScreen.create();
		lightScreen.getStage().clear();

		TextButton backButton = new TextButton("Back (ESC)", assets.getSkin(), "default");
		backButton.pack();
		backButton.setPosition(10f, 10f);
		backButton.addListener(new ClickAdapter(this::backFromTest));

		Label label = new Label("Press S to toggle sunlight", assets.getSkin(), "default");
		label.pack();
		label.setPosition(10f, 20f + backButton.getHeight());

		lightScreen.getStage().addActor(backButton);
		lightScreen.getStage().addActor(label);

		config = new NormalPainterConfig();
		config.load(new File("config.data"));
		config.apply(this);

		stage.loadFlat(true);
		stage.loadMask(true);
		stage.load(true);

		float bestZoomX = painter.pixmap.getWidth() * WORLD_PERFECT_SCALE / WORLD_WIDTH;
		float bestZoomY = painter.pixmap.getHeight() * WORLD_PERFECT_SCALE / WORLD_HEIGHT;

		camera.zoom = max(bestZoomX, bestZoomY) * 1.05f;
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);

		if(assets.isFinished())
		{
			assets.generateFonts(assets.getSkin(), STAGE_WIDTH, width);

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

		Texture texture = new Texture(painter.preview);
		Texture maskTex = painter.mask != null ? new Texture(painter.mask) : null;

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

		Gdx.gl.glViewport(
				stage.getViewport().getScreenX(),
				stage.getViewport().getScreenY(),
				stage.getViewport().getScreenWidth(),
				stage.getViewport().getScreenHeight());

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.setColor(Color.WHITE);

		batch.draw(bg,
				camera.position.x - camera.viewportWidth * camera.zoom / 2f,
				camera.position.y - camera.viewportHeight * camera.zoom / 2f,
				camera.viewportWidth * camera.zoom * 1920 / stage.getViewport().getScreenWidth(),
				camera.viewportHeight * camera.zoom * 1080 / stage.getViewport().getScreenHeight());

		if(renderFlat && flat != null && !flatOnTop)
		{
			batch.getColor().a = flatOpacity;
			batch.setColor(batch.getColor());

			float w = flat.getRegionWidth() * WORLD_PERFECT_SCALE;
			float h = flat.getRegionHeight() * WORLD_PERFECT_SCALE;
			batch.draw(flat, -w / 2f, -h / 2f, w, h);

			batch.getColor().a = 1f;
			batch.setColor(batch.getColor());
		}

		if(painter.mask != null && !maskOnTop)
		{
			float w = painter.mask.getWidth() * WORLD_PERFECT_SCALE;
			float h = painter.mask.getHeight() * WORLD_PERFECT_SCALE;

			batch.getColor().a = maskOpacity;
			batch.setColor(batch.getColor());

			batch.draw(maskTex, -w / 2f, -h / 2f, w, h);

			batch.getColor().a = 1f;
			batch.setColor(batch.getColor());
		}

		float width = texture.getWidth() * WORLD_PERFECT_SCALE;
		float height = texture.getHeight() * WORLD_PERFECT_SCALE;

		batch.draw(texture,
				-width / 2f,
				-height / 2f,
				width,
				height);

		if(renderFlat && flat != null && flatOnTop)
		{
			batch.getColor().a = flatOpacity;
			batch.setColor(batch.getColor());

			float w = flat.getRegionWidth() * WORLD_PERFECT_SCALE;
			float h = flat.getRegionHeight() * WORLD_PERFECT_SCALE;
			batch.draw(flat, -w / 2f, -h / 2f, w, h);

			batch.getColor().a = 1f;
			batch.setColor(batch.getColor());
		}

		if(maskTex != null && maskOnTop)
		{
			float w = painter.mask.getWidth() * WORLD_PERFECT_SCALE;
			float h = painter.mask.getHeight() * WORLD_PERFECT_SCALE;

			batch.getColor().a = maskOpacity;
			batch.setColor(batch.getColor());

			batch.draw(maskTex, -w / 2f, -h / 2f, w, h);

			batch.getColor().a = 1f;
			batch.setColor(batch.getColor());
		}

		batch.end();

		texture.dispose();
		if(maskTex != null)
			maskTex.dispose();

		mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0f);

		camera.unproject(mousePos,
				stage.getViewport().getScreenX(),
				stage.getViewport().getScreenY(),
				stage.getViewport().getScreenWidth(),
				stage.getViewport().getScreenHeight());

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);

		if(!Gdx.input.isKeyPressed(Keys.B))
		{
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.rectLine(-width / 2 - WORLD_PERFECT_SCALE / 2f,
					-height / 2 - WORLD_PERFECT_SCALE / 2f,
					width / 2 + WORLD_PERFECT_SCALE / 2f,
					-height / 2 - WORLD_PERFECT_SCALE / 2f,
					WORLD_PERFECT_SCALE);
			shapeRenderer.rectLine(-width / 2 - WORLD_PERFECT_SCALE / 2f,
					-height / 2 - WORLD_PERFECT_SCALE / 2f,
					-width / 2 - WORLD_PERFECT_SCALE / 2f,
					height / 2 + WORLD_PERFECT_SCALE / 2f,
					WORLD_PERFECT_SCALE);
			shapeRenderer.rectLine(width / 2 + WORLD_PERFECT_SCALE / 2f,
					height / 2 + WORLD_PERFECT_SCALE / 2f,
					width / 2 + WORLD_PERFECT_SCALE / 2f,
					-height / 2 - WORLD_PERFECT_SCALE / 2f,
					WORLD_PERFECT_SCALE);
			shapeRenderer.rectLine(width / 2 + WORLD_PERFECT_SCALE / 2f,
					height / 2 + WORLD_PERFECT_SCALE / 2f,
					-width / 2 - WORLD_PERFECT_SCALE / 2f,
					height / 2 + WORLD_PERFECT_SCALE / 2f,
					WORLD_PERFECT_SCALE);
		}

		if(viewNormals)
		{
			for(int i = 0; i < painter.pixmap.getWidth(); i += normalSpacing)
			{
				for(int j = 0; j < painter.pixmap.getHeight(); j += normalSpacing)
				{
					float x = -width / 2f + (i + 0.5f) * WORLD_PERFECT_SCALE;
					float y = -height / 2f + (j + 0.5f) * WORLD_PERFECT_SCALE;
					y = -y;

					painter.tmpColor.set(painter.preview.getPixel(i, j));

					if(painter.tmpColor.a <= 0f)
						continue;

					tmpVec2.set(painter.tmpColor.r * 2f - 1f, painter.tmpColor.g * 2f - 1f);

					float fullLength = normalSpacing * 0.75f * painter.tmpColor.a * WORLD_PERFECT_SCALE;
					tmpVec2.scl(fullLength);

					shapeRenderer.setColor(min(painter.tmpColor.r, min(painter.tmpColor.g, painter.tmpColor.b)) > 0.5f ? Color.BLACK : Color.WHITE);

					float arrowX = tmpVec2.x;
					float arrowY = tmpVec2.y;

					shapeRenderer.line(x, y, x + arrowX, y + arrowY);

					tmpVec2.scl(0.25f);

					tmpVec2.rotate(15f);
					shapeRenderer.line(x + arrowX, y + arrowY, x + arrowX - tmpVec2.x, y + arrowY - tmpVec2.y);

					tmpVec2.rotate(-30f);
					shapeRenderer.line(x + arrowX, y + arrowY, x + arrowX - tmpVec2.x, y + arrowY - tmpVec2.y);

				}
			}
		}

		if(colorPicker.pinned)
		{
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.line(colorPicker.pinnedPoint.x - 100f * camera.zoom, colorPicker.pinnedPoint.y - 100f * camera.zoom,
					colorPicker.pinnedPoint.x + 100f * camera.zoom, colorPicker.pinnedPoint.y + 100f * camera.zoom);
			shapeRenderer.line(colorPicker.pinnedPoint.x - 100f * camera.zoom, colorPicker.pinnedPoint.y + 100f * camera.zoom,
					colorPicker.pinnedPoint.x + 100f * camera.zoom, colorPicker.pinnedPoint.y - 100f * camera.zoom);
		}


		if(!(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT) || Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)))
		{
			float size = painter.brushSize * ((drawing && painter.enablePressure) ? painter.pressure : 1f) * WORLD_PERFECT_SCALE;
			colorPicker.setColor();
			shapeRenderer.setColor(painter.color);
			shapeRenderer.circle(mousePos.x, mousePos.y, size);

			shapeRenderer.setColor(min(painter.color.r, min(painter.color.g, painter.color.b)) > 0.5f ? Color.BLACK : Color.WHITE);
			shapeRenderer.line(mousePos.x, mousePos.y, mousePos.x + colorPicker.axisPos.x * size, mousePos.y + colorPicker.axisPos.y * size);
		}

		shapeRenderer.end();

		batch.begin();

		colorPicker.draw();

		batch.end();

		stage.draw();
	}

	private void drawAt(int screenX, int screenY, boolean dragged)
	{
		if(!dragged)
		{
			drawing = true;
			if(painter.maskMultiply)
				painter.maskBufferBackup.copy(painter.maskBuffer);
			else
				painter.backup.copy(painter.pixmap);
		}

		mousePos.set(screenX, screenY, 0f);

		camera.unproject(mousePos,
				stage.getViewport().getScreenX(),
				stage.getViewport().getScreenY(),
				stage.getViewport().getScreenWidth(),
				stage.getViewport().getScreenHeight());

		int pixelX = Math.round(mousePos.x / WORLD_PERFECT_SCALE) + painter.pixmap.getWidth() / 2;
		int pixelY = painter.pixmap.getHeight() / 2 - Math.round(mousePos.y / WORLD_PERFECT_SCALE);

		colorPicker.setColor();

		boolean change;
		if(dragged && drawLinesOnDrag)
			change = painter.fillLine(prevPixX, prevPixY, pixelX, pixelY);
		else
			change = painter.fillCircle(pixelX, pixelY);

		if(livePreview && change)
			painter.updatePreview(false);

		prevPixX = pixelX;
		prevPixY = pixelY;
		app.unsavedChanges = true;
	}

	@Override
	public boolean scrolled(int amount)
	{
		if(app.getScreen() != this)
			return false;

		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT))
		{
			colorPicker.axisPos.rotate(amount * colorPicker.normalRotateSpeed);
			colorPicker.updateNormalDir();
			return true;
		}

		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT))
		{
			painter.brushSize += amount;
			painter.brushSize = max(0, painter.brushSize);
			return true;
		}

		camera.zoom *= Math.pow(1.1f, amount);
		return true;
	}

	@Override
	public boolean keyDown(int keycode)
	{
		if(keycode == Keys.F5 && app.getScreen() == this)
		{
			if(flat == null)
			{
				new OkayDialog(assets, "A flat asset is needed for light testing").show(stage);
				return true;
			}

			lightScreen.texture = flat;
			buffer.setColor(0.5f, 0.5f, 1f, 1f);
			buffer.fillRectangle(0, 0, buffer.getWidth(), buffer.getHeight());
			buffer.setBlending(Blending.SourceOver);
			buffer.drawPixmap(painter.preview, 0, 0);

			lightScreen.normal = new TextureRegion(new Texture(buffer));
			lightScreen.camera.setPosition(camera.position.x, camera.position.y);
			lightScreen.camera.zoom = camera.zoom;
			app.setScreen(lightScreen);
			return true;
		}

		if((keycode == Keys.F5 || keycode == Keys.ESCAPE) && app.getScreen() != this)
		{
			backFromTest();
			return true;
		}

		if(app.getScreen() != this)
			return false;

		if(keycode == Keys.N && (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)))
		{
			painter.normalizeAll();
			return true;
		}
		else if(keycode == Keys.S && (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)))
		{
			stage.save();
			return true;
		}
		else if(keycode == Keys.Z && (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				|| Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT))
		{
			if(painter.maskMultiply)
			{
				RangedGdxBuffer tmp = painter.maskBuffer;
				painter.maskBuffer = painter.maskBufferBackup;
				painter.updatePreview(true);

				painter.maskBufferBackup = tmp;
			}
			else
			{
				GdxPixmap tmp = painter.pixmap;
				painter.pixmap = painter.backup;
				painter.updatePreview(true);

				painter.backup = tmp;
			}
			return true;
		}
		else if(keycode == Keys.SPACE)
		{
			painter.flushMask();
			stage.update();
			return true;
		}
		else if(keycode == Keys.L)
		{
			if(colorPicker.minRadius >= colorPicker.maxRadius)
			{
				colorPicker.minRadius = 0f;
				colorPicker.maxRadius = 1f;
				stage.update();
				return true;
			}

			colorPicker.minRadius = colorPicker.maxRadius = colorPicker.axisPos.len();
			stage.update();
			return true;
		}
		else if(keycode == Keys.Y)
		{
			colorPicker.invert = !colorPicker.invert;
			colorPicker.updateNormalDir();
			stage.update();
			return true;
		}
		else if(keycode == Keys.I)
		{
			colorPicker.invertPinning = !colorPicker.invertPinning;
			stage.update();
			return true;
		}
		else if(keycode == Keys.M)
		{
			if(drawing)
			{
				painter.endDraw();
				drawing = false;
			}

			painter.maskMultiply = !painter.maskMultiply;
			stage.update();
			return true;
		}
		else if(keycode == Keys.T)
		{
			renderFlat = !renderFlat;
			stage.update();
			return true;
		}
		else if(keycode == Keys.FORWARD_DEL || keycode == Keys.DEL || keycode == Keys.E)
		{
			painter.drawMode = DrawMode.Erase;
			stage.update();
		}
		else if(keycode == Keys.B)
		{
			painter.drawMode = DrawMode.Behind;
			stage.update();
		}
		else if(keycode == Keys.R)
		{
			painter.drawMode = DrawMode.Normal;
			stage.update();
		}
		else if(keycode == Keys.F)
		{
			colorPicker.axisPos.scl(-1f, 1f);
			colorPicker.updateNormalDir();
		}
		else if(keycode == Keys.U)
		{
			colorPicker.axisPos.scl(1f, -1f);
			colorPicker.updateNormalDir();
		}
		else if(keycode == Keys.NUMPAD_1)
		{
			colorPicker.axisPos.set(-1f, -1f).nor().scl(colorPicker.numpadIntensity);
			colorPicker.updateNormalDir();
			return true;
		}
		else if(keycode == Keys.NUMPAD_2)
		{
			colorPicker.axisPos.set(0f, -1f).nor().scl(colorPicker.numpadIntensity);
			colorPicker.updateNormalDir();
			return true;
		}
		else if(keycode == Keys.NUMPAD_3)
		{
			colorPicker.axisPos.set(1f, -1f).nor().scl(colorPicker.numpadIntensity);
			colorPicker.updateNormalDir();
			return true;
		}
		else if(keycode == Keys.NUMPAD_4)
		{
			colorPicker.axisPos.set(-1f, 0f).nor().scl(colorPicker.numpadIntensity);
			colorPicker.updateNormalDir();
			return true;
		}
		else if(keycode == Keys.NUMPAD_5)
		{
			colorPicker.axisPos.set(0f, 0f);
			colorPicker.updateNormalDir();
			return true;
		}
		else if(keycode == Keys.NUMPAD_6)
		{
			colorPicker.axisPos.set(1f, 0f).nor().scl(colorPicker.numpadIntensity);
			colorPicker.updateNormalDir();
			return true;
		}
		else if(keycode == Keys.NUMPAD_7)
		{
			colorPicker.axisPos.set(-1f, 1f).nor().scl(colorPicker.numpadIntensity);
			colorPicker.updateNormalDir();
			return true;
		}
		else if(keycode == Keys.NUMPAD_8)
		{
			colorPicker.axisPos.set(0f, 1f).nor().scl(colorPicker.numpadIntensity);
			colorPicker.updateNormalDir();
			return true;
		}
		else if(keycode == Keys.NUMPAD_9)
		{
			colorPicker.axisPos.set(1f, 1f).nor().scl(colorPicker.numpadIntensity);
			colorPicker.updateNormalDir();
			return true;
		}
		else if(keycode == Keys.F8)
		{
			stage.setDebugAll(!stage.isDebugAll());
			return true;
		}

		return false;
	}

	private void backFromTest()
	{
		Gdx.app.postRunnable(() -> {
			camera.position.set(lightScreen.camera.position);
			camera.zoom = lightScreen.camera.zoom;
			app.finishScreen();
			lightScreen.normal.getTexture().dispose();
			lightScreen.normal = null;
		});
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		colorPicker.mouseMoved(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if(app.getScreen() != this)
			return false;

		if(button == 0)
		{
			if(Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT) || Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
			{
				if(colorPicker.pinned)
				{
					colorPicker.pinned = false;
					return true;
				}

				mousePos.set(screenX, screenY, 0f);

				camera.unproject(mousePos,
						stage.getViewport().getScreenX(),
						stage.getViewport().getScreenY(),
						stage.getViewport().getScreenWidth(),
						stage.getViewport().getScreenHeight());

				colorPicker.pinned = true;
				colorPicker.normalRelToPath = false;
				colorPicker.pinnedPoint.set(mousePos.x, mousePos.y);
				return true;
			}

			if(Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT) || Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			{
				mousePos.set(screenX, screenY, 0f);

				camera.unproject(mousePos,
						stage.getViewport().getScreenX(),
						stage.getViewport().getScreenY(),
						stage.getViewport().getScreenWidth(),
						stage.getViewport().getScreenHeight());

				int pixelX = Math.round(mousePos.x / WORLD_PERFECT_SCALE) + painter.pixmap.getWidth() / 2;
				int pixelY = painter.pixmap.getHeight() / 2 - Math.round(mousePos.y / WORLD_PERFECT_SCALE);

				painter.tmpColor.set(painter.preview.getPixel(pixelX, pixelY));

				colorPicker.axisPos.x = painter.tmpColor.r * 2f - 1f;
				colorPicker.axisPos.y = painter.tmpColor.g * 2f - 1f;
				colorPicker.updateNormalDir();
				return true;
			}

			if(colorPicker.updateAxis(screenX, screenY, false))
			{
				draggingAxis = true;
				return true;
			}

			drawAt(screenX, screenY, false);

			mousePos.set(screenX, screenY, 0f);

			camera.unproject(mousePos,
					stage.getViewport().getScreenX(),
					stage.getViewport().getScreenY(),
					stage.getViewport().getScreenWidth(),
					stage.getViewport().getScreenHeight());

			lastDraw.set(mousePos.x, mousePos.y);
			return true;
		}

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
		if(app.getScreen() != this)
			return false;

		colorPicker.mouseMoved(screenX, screenY);

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

		if(draggingAxis)
		{
			colorPicker.updateAxis(screenX, screenY, true);
			return true;
		}

		if(drawing)
		{
			drawAt(screenX, screenY, true);

			mousePos.set(screenX, screenY, 0f);

			camera.unproject(mousePos,
					stage.getViewport().getScreenX(),
					stage.getViewport().getScreenY(),
					stage.getViewport().getScreenWidth(),
					stage.getViewport().getScreenHeight());

			lastDraw.scl(0.1f);
			lastDraw.add(mousePos.x * 0.9f, mousePos.y * 0.9f);
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if(app.getScreen() != this)
			return false;

		if(button == 0)
		{
			painter.endDraw();
			drawing = false;
			draggingAxis = false;
			prevPixX = prevPixY = -1;
		}
		else if(button == 1)
			draggingCam = false;
		return true;
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void finish() {}

	public void saveConfig()
	{
		config.setFrom(this);
		config.save(new File("config.data"));
	}

	@Override
	public void show()
	{
		GdxUtil.unregisterInput(this);
		GdxUtil.registerInput(stage);
		GdxUtil.registerInput(this);
	}

	@Override
	public void hide()
	{
		GdxUtil.unregisterInput(stage);
	}

	@Override
	public void dispose()
	{
		batch.dispose();
	}

	@Override
	public Stage getStage()
	{
		return stage;
	}
}