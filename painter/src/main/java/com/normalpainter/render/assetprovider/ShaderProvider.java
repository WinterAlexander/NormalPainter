package com.normalpainter.render.assetprovider;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.winteralexander.gdx.utils.Validation;
import com.normalpainter.render.AssetSupplier;

/**
 * {@link AssetProvider} for a ShaderProgram
 * <p>
 * Created on 2020-09-11.
 *
 * @author Alexander Winter
 */
public class ShaderProvider implements AssetProvider<ShaderProgram>
{
	private final String shaderName, vertexShader, fragmentShader;
	private final String[] flags;

	public ShaderProvider(String shaderName,
	                      String vertexShader,
	                      String fragmentShader,
	                      String... flags)
	{
		Validation.ensureNotNull(shaderName, "shaderName");
		Validation.ensureNotNull(vertexShader, "vertexShader");
		Validation.ensureNotNull(fragmentShader, "fragmentShader");
		Validation.ensureNotNull(flags, "flags");

		this.shaderName = shaderName;
		this.vertexShader = vertexShader;
		this.fragmentShader = fragmentShader;
		this.flags = flags;
	}

	@Override
	public ShaderProgram resolve(AssetSupplier assetSupplier)
	{
		return assetSupplier.getShader(shaderName);
	}

	@Override
	public void load(AssetSupplier assetSupplier)
	{
		assetSupplier.loadShader(shaderName, vertexShader, fragmentShader, flags);
	}

	@Override
	public void finishLoading(AssetSupplier assetSupplier)
	{
		assetSupplier.finishLoadingAsset(shaderName);
	}
}
