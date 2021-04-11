package com.normalpainter.render;

import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.normalpainter.lightmap.FBOLightMaskGenerator;
import com.normalpainter.render.assetprovider.AssetProvider;
import com.normalpainter.render.assetprovider.DirectAssetProvider;
import com.normalpainter.render.assetprovider.LightingShaderPackProvider;
import com.normalpainter.render.assetprovider.ProviderCollection;
import com.normalpainter.render.assetprovider.ShaderProvider;
import com.normalpainter.render.lighting.LightingShaderPack;

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

	AssetProvider<Texture> WHITE_PIXEL = new DirectAssetProvider<>("gfx/white_pixel.png", Texture.class, NEAREST_PARAM);

	AssetProvider<LightingShaderPack> DEFAULT_SHADER = new LightingShaderPackProvider("default",
			"shaders/default.vs.glsl",
			"shaders/default.fs.glsl");

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
