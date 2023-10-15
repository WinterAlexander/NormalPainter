package com.normalpainter.util.ui.skin;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader.ShaderProgramParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.normalpainter.render.AssetSupplier;
import com.normalpainter.util.ui.skin.BetterSkinLoader.SkinParam;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Loader that loads a skin that supports dynamic fonts and shaders
 * <p>
 * Created on 2018-02-23.
 *
 * @author Alexander Winter
 */
public class BetterSkinLoader extends AsynchronousAssetLoader<Skin, SkinParam>
{
	public BetterSkinLoader(FileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
	                                              FileHandle file,
	                                              SkinParam parameter)
	{
		Array<AssetDescriptor> deps = new Array<>();
		deps.add(new AssetDescriptor<>(parameter.atlas, TextureAtlas.class));

		for(String fontDep : parameter.fontDependencies)
			deps.add(new AssetDescriptor<>(fontDep, BitmapFont.class));

		deps.add(new AssetDescriptor<>("tint", ShaderProgram.class, parameter.tintShaderParam));

		return deps;
	}

	@Override
	public void loadAsync(AssetManager manager,
	                      String fileName,
	                      FileHandle file,
	                      SkinParam parameter) {}

	@Override
	public Skin loadSync(AssetManager manager,
	                     String fileName,
	                     FileHandle file,
	                     SkinParam parameter)
	{
		TextureAtlas atlas = manager.get(parameter.atlas, TextureAtlas.class);
		Skin skin = new BetterSkin(atlas, manager.get("tint", ShaderProgram.class));
		skin.load(file);
		((AssetSupplier)manager).generateFonts(skin,
				parameter.stageWidth,
				parameter.screenMaxWidth);
		return skin;
	}

	public static class SkinParam extends AssetLoaderParameters<Skin>
	{
		public String atlas;
		public float stageWidth;
		public int screenMaxWidth;
		public ShaderProgramParameter tintShaderParam;
		public String[] fontDependencies;

		public SkinParam(String atlas,
		                 float stageWidth,
		                 int screenMaxWidth,
		                 ShaderProgramParameter tintShaderParam,
		                 String... fontDependencies)
		{
			ensureNotNull(atlas, "atlas");
			ensureNotNull(tintShaderParam, "tintShaderParam");
			ensureNotNull(fontDependencies, "fontDependencies");

			this.atlas = atlas;
			this.stageWidth = stageWidth;
			this.screenMaxWidth = screenMaxWidth;
			this.tintShaderParam = tintShaderParam;
			this.fontDependencies = fontDependencies;
		}
	}
}