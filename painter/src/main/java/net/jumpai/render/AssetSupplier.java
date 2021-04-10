package net.jumpai.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader.ShaderProgramParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import net.jumpai.util.log.Logger;
import net.jumpai.util.ui.skin.BetterSkinLoader;
import net.jumpai.util.ui.skin.DynamicFont;

import static java.lang.Integer.parseInt;
import static net.jumpai.util.ReflectionUtil.set;
import static net.jumpai.util.Validation.ensureNotNull;
import static net.jumpai.util.gdx.FontUtil.copy;

/**
 * Supplies assets in a convenient way
 * <p>
 * Created on 2017-02-28.
 *
 * @author Alexander Winter
 */
public class AssetSupplier extends AssetManager
{
	public static final int GLSL_VERSION = 120;
	public static final int GLSL3_VERSION = 140;

	protected final ObjectMap<Class<?>, AssetLoaderParameters<?>> defaultParams = new ObjectMap<>();
	protected final ObjectMap<String, AssetLoaderParameters<?>> lastParams = new ObjectMap<>();
	protected final Logger logger;

	public AssetSupplier(Logger logger)
	{
		ensureNotNull(logger, "logger");
		this.logger = logger;

		setLoader(Skin.class, new BetterSkinLoader(getFileHandleResolver()));
		setLoader(ShaderProgram.class, new IncludeShaderLoader(getFileHandleResolver()));

		setErrorListener((asset, throwable) ->
				logger.error("Error loading asset " + asset.fileName, throwable));
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized <T> void load(String fileName, Class<T> type)
	{
		load(fileName, type, (AssetLoaderParameters<T>)defaultParams.get(type));
	}

	@Override
	public synchronized <T> void load(String fileName, Class<T> type, AssetLoaderParameters<T> parameter)
	{
		lastParams.put(fileName, parameter);
		super.load(fileName, type, parameter);
	}

	public void reload(String asset)
	{
		Class type = getAssetType(asset);
		AssetLoaderParameters params = lastParams.get(asset);

		if(isLoaded(asset))
			unload(asset);

		load(asset, type, params);
		finishLoadingAsset(asset);
	}

	public ShaderProgramParameter loadShader(String name, String vertexShader, String fragmentShader, String... flags)
	{
		ShaderProgramParameter param = new ShaderProgramParameter();
		param.vertexFile = vertexShader;
		param.fragmentFile = fragmentShader;

		boolean glsl3 = Gdx.graphics.isGL30Available();

		StringBuilder sb = new StringBuilder("#version " + (glsl3 ? GLSL3_VERSION : GLSL_VERSION) + "\n");
		for(String flag : flags)
			sb.append("#define ").append(flag).append('\n');

		if(glsl3)
			sb.append("#define GLSL3\n");

		param.prependFragmentCode = param.prependVertexCode = sb.toString();

		load(name, ShaderProgram.class, param);
		return param;
	}

	public void generateFonts(Skin skin, float stageWidth, int screenMaxWidth)
	{
		ObjectMap<String, DynamicFont> fonts = skin.getAll(DynamicFont.class);
		Array<BitmapFont> allFonts = getAll(BitmapFont.class, new Array<>());

		for(String name : fonts.keys())
		{
			DynamicFont spec = fonts.get(name);
			OrderedMap<Integer, BitmapFont> family = new OrderedMap<>();

			for(BitmapFont current : allFonts)
				if(current.getData().getFontFile().parent().path().equals(spec.family))
					try
					{
						family.put(parseInt(current.getData().getFontFile().nameWithoutExtension()), current);
					}
					catch(NumberFormatException ignored) {} //not a valid size named font for that

			if(family.size == 0)
				throw new RuntimeException("Font family is empty");

			family.orderedKeys().sort(Integer::compare);

			float optimalFontSize = spec.size / stageWidth * screenMaxWidth;
			int optimalAvailable = family.orderedKeys().get(0); //first by default

			for(int current : family.keys())
				if(current >= optimalFontSize)
				{
					optimalAvailable = current;
					break;
				}

			BitmapFont source = family.get(optimalAvailable);
			BitmapFont font = skin.getFont(name);

			if(font.getData().getFontFile().nameWithoutExtension().equals(Integer.toString(optimalAvailable))
			&& font.getData().getFontFile().path().contains(spec.family))
				continue;

			font.getCache().clear();
			font.getRegions().clear();
			font.getRegions().addAll(source.getRegions());
			font.setOwnsTexture(source.ownsTexture());
			font.setUseIntegerPositions(source.usesIntegerPositions());
			set(font, "data", copy(source.getData()));
			set(font, "flipped", source.isFlipped());

			font.getData().setScale(spec.size / (float)optimalAvailable);
			skin.add(name, font);
		}
	}

	public TextureRegion getTexture(String name)
	{
		return new TextureRegion(get(name, Texture.class));
	}

	public AtlasRegion getTexture(String name, String atlasPath)
	{
		return get(atlasPath, TextureAtlas.class).findRegion(name);
	}

	public ShaderProgram getShader(String name)
	{
		ShaderProgram shader = get(name, ShaderProgram.class);

		if(!shader.isCompiled())
			throw new GdxRuntimeException("Shader " + name + " not compiled.\n" + shader.getLog());

		return shader;
	}

	public Skin getSkin()
	{
		return get("gfx/uiskin.json", Skin.class);
	}
}
