package net.jumpai.render;

import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import net.jumpai.render.assetprovider.AssetProvider;
import net.jumpai.render.assetprovider.DirectAssetProvider;
import net.jumpai.render.assetprovider.LightingShaderPackProvider;
import net.jumpai.render.assetprovider.ProviderCollection;
import net.jumpai.render.assetprovider.ShaderProvider;
import net.jumpai.render.lighting.LightingShaderPack;
import net.jumpai.world.lightmap.FBOLightMaskGenerator;

import java.lang.reflect.Field;

/**
 * Static interface to list all assets
 * <p>
 * Created on 2020-09-10.
 *
 * @author Alexander Winter
 */
public interface Assets
{
	TextureParameter NEAREST_PARAM = new TextureParameter();
	TextureParameter NOMIPMAP_PARAM = new TextureParameter() {{
		minFilter = TextureFilter.Linear;
		magFilter = TextureFilter.Linear;
		genMipMaps = false;
	}};

	AssetProvider<Texture> WHITE_PIXEL = new DirectAssetProvider<>("gfx/white_pixel.png", Texture.class, NEAREST_PARAM);


	AssetProvider<LightingShaderPack> DEFAULT_SHADER = new LightingShaderPackProvider("default",
			"shaders/default.vs.glsl",
			"shaders/default.fs.glsl");

	AssetProvider<LightingShaderPack> ICE_REFLECTION_SHADER = new LightingShaderPackProvider("ice-reflection",
			"shaders/ice.vs.glsl",
			"shaders/ice.fs.glsl");

	AssetProvider<LightingShaderPack> MASK_SHADER = new LightingShaderPackProvider("mask",
			"shaders/default.vs.glsl",
			"shaders/mask.fs.glsl");

	AssetProvider<LightingShaderPack> LAND_BIOME_SHADER = new LightingShaderPackProvider("land",
			"shaders/land.vs.glsl",
			"shaders/land.fs.glsl");

	AssetProvider<LightingShaderPack> WRAP_SHADER = new LightingShaderPackProvider("wrap",
			"shaders/default.vs.glsl",
			"shaders/wrap.fs.glsl");

	AssetProvider<ShaderProgram> ROAST_SHADER = new ShaderProvider("roast",
			"shaders/default.vs.glsl",
			"shaders/roast.fs.glsl");

	AssetProvider<LightingShaderPack> ELECTROCUTION_SHADER = new LightingShaderPackProvider("electrocution",
			"shaders/default.vs.glsl",
			"shaders/electrocution.fs.glsl");

	AssetProvider<LightingShaderPack> ENTITY_DAMAGE = new LightingShaderPackProvider("entity-damage",
			"shaders/default.vs.glsl",
			"shaders/entity-damage.fs.glsl");

	AssetProvider<LightingShaderPack> WATERFALL_SHADER = new LightingShaderPackProvider("waterfall",
			"shaders/default.vs.glsl",
			"shaders/waterfall.fs.glsl");

	AssetProvider<LightingShaderPack> BOOMERANG_SHADER = new LightingShaderPackProvider("boomerang",
			"shaders/boomerang.vs.glsl",
			"shaders/boomerang.fs.glsl");

	AssetProvider<ShaderProgram> BIOME_BRUSH_SHADER = new ShaderProvider("biome-brush",
			"shaders/default.vs.glsl",
			"shaders/biome-brush.fs.glsl");

	AssetProvider<ShaderProgram> BACK_LIGHT_SHADER = new ShaderProvider("point-light",
			"shaders/default.vs.glsl",
			"shaders/radial-gradient.fs.glsl");

	AssetProvider<ShaderProgram> LIGHTMASK_BUILDER_SHADER = new ShaderProvider("lightmask-builder",
			"shaders/lightmask-builder.vs.glsl",
			"shaders/lightmask-builder.fs.glsl",
			"MAX_LIGHTS " + FBOLightMaskGenerator.MAX_LIGHTS);

	AssetProvider<ShaderProgram> LIGHTMASK_BUILDER_COLOR_SHADER = new ShaderProvider("lightmask-builder-color",
			"shaders/lightmask-builder.vs.glsl",
			"shaders/lightmask-builder-color.fs.glsl",
			"MAX_LIGHTS " + FBOLightMaskGenerator.MAX_LIGHTS);

	AssetProvider<ShaderProgram> BACKGROUND_BIOME_SHADER = new ShaderProvider("background-biome",
			"shaders/default.vs.glsl",
			"shaders/background-biome.fs.glsl");

	AssetProvider<ShaderProgram> GRAYSCALE_SHADER = new ShaderProvider("grayscale",
			"shaders/default.vs.glsl",
			"shaders/grayscale.fs.glsl");

	AssetProvider<ShaderProgram> EDITOR_CANT_PLACE_SHADER = new ShaderProvider("editor-cant-place",
			"shaders/default.vs.glsl",
			"shaders/editor-cant-place.fs.glsl");

	AssetProvider<ShaderProgram> DARK_FRAME_SHADER = new ShaderProvider("dark-frame",
			"shaders/default.vs.glsl",
			"shaders/dark-frame.fs.glsl");

	AssetProvider<ShaderProgram> IMMEDIATE_RENDERER_SHADER = new ShaderProvider("immediate-renderer",
			"shaders/immediate.vs.glsl",
			"shaders/immediate.fs.glsl");

	static void loadAll(AssetSupplier assetSupplier)
	{
		try
		{
			for(Field field : Assets.class.getFields())
				loadObj(field.get(null), assetSupplier);
		}
		catch(IllegalAccessException ex)
		{
			throw new RuntimeException("Could not load all fields in JumpaiAssets", ex);
		}
	}

	static void loadObj(Object value, AssetSupplier assetSupplier)
	{
		if(value instanceof AssetProvider)
			((AssetProvider<?>)value).load(assetSupplier);
		else if(value instanceof ProviderCollection)
			((ProviderCollection)value).load(assetSupplier);
		else if(value instanceof Array)
			((Array<?>)value).forEach(obj -> loadObj(obj, assetSupplier));
		else if(value instanceof ObjectMap)
			((ObjectMap<?, ?>)value).values().forEach(obj -> loadObj(obj, assetSupplier));
	}
}
