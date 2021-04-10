package com.normalpainter.render.assetprovider;

import com.normalpainter.util.CollectionUtil;
import com.normalpainter.util.Validation;
import com.normalpainter.render.AssetSupplier;
import com.normalpainter.render.lighting.LightingShaderPack;

import static com.normalpainter.util.CollectionUtil.mergeWithArray;

/**
 * {@link AssetProvider} for a {@link LightingShaderPack}
 * <p>
 * Created on 2020-09-13.
 *
 * @author Alexander Winter
 */
public class LightingShaderPackProvider implements AssetProvider<LightingShaderPack>
{
	private static final String LIGHTING_SUFFIX = "-lighting";
	private static final String COLOR_LIGHTING_SUFFIX = "-color-lighting";

	private final String baseName;
	private final String vertexSourceName, fragmentSourceName;
	private final String[] flags;

	public LightingShaderPackProvider(String baseName,
	                                  String vertexSourceName,
	                                  String fragmentSourceName,
	                                  String... flags)
	{
		Validation.ensureNotNull(baseName, "baseName");
		Validation.ensureNotNull(vertexSourceName, "vertexSourceName");
		Validation.ensureNotNull(fragmentSourceName, "fragmentSourceName");
		Validation.ensureNotNull(flags, "flags");

		this.baseName = baseName;
		this.vertexSourceName = vertexSourceName;
		this.fragmentSourceName = fragmentSourceName;
		this.flags = flags;
	}

	@Override
	public LightingShaderPack resolve(AssetSupplier assetSupplier)
	{
		return new LightingShaderPack(
				assetSupplier.getShader(baseName),
				assetSupplier.getShader(baseName + LIGHTING_SUFFIX),
				assetSupplier.getShader(baseName + COLOR_LIGHTING_SUFFIX));
	}

	@Override
	public void load(AssetSupplier assetSupplier)
	{
		assetSupplier.loadShader(baseName,
				vertexSourceName,
				fragmentSourceName,
				flags);
		assetSupplier.loadShader(baseName + LIGHTING_SUFFIX,
				vertexSourceName,
				fragmentSourceName,
				CollectionUtil.mergeWithArray(flags, "LIGHTING_ENABLED"));
		assetSupplier.loadShader(baseName + COLOR_LIGHTING_SUFFIX,
				vertexSourceName,
				fragmentSourceName,
				CollectionUtil.mergeWithArray(flags, "LIGHTING_ENABLED", "LIGHTING_COLOR_ENABLED"));
	}

	@Override
	public void finishLoading(AssetSupplier assetSupplier)
	{
		assetSupplier.finishLoadingAsset(baseName);
		assetSupplier.finishLoadingAsset(baseName + LIGHTING_SUFFIX);
		assetSupplier.finishLoadingAsset(baseName + COLOR_LIGHTING_SUFFIX);
	}
}
