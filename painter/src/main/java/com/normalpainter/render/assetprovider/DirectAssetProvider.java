package com.normalpainter.render.assetprovider;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.normalpainter.util.Validation;
import com.normalpainter.render.AssetSupplier;

/**
 * {@link AssetProvider} that provides an asset using its direct path
 * <p>
 * Created on 2020-09-10.
 *
 * @author Alexander Winter
 */
public class DirectAssetProvider<T> implements AssetProvider<T>
{
	private final String path;
	private final Class<T> type;
	private final AssetLoaderParameters<T> params;

	public DirectAssetProvider(String path, Class<T> type)
	{
		this(path, type, null);
	}

	public DirectAssetProvider(String path, Class<T> type, AssetLoaderParameters<T> params)
	{
		Validation.ensureNotNull(path, "path");
		Validation.ensureNotNull(type, "type");
		this.path = path;
		this.type = type;
		this.params = params;
	}

	@Override
	public T resolve(AssetSupplier assetSupplier)
	{
		return assetSupplier.get(path);
	}

	@Override
	public void load(AssetSupplier assetSupplier)
	{
		if(params != null)
			assetSupplier.load(path, type, params);
		else
			assetSupplier.load(path, type);
	}

	@Override
	public void finishLoading(AssetSupplier assetSupplier)
	{
		assetSupplier.finishLoadingAsset(path);
	}
}
