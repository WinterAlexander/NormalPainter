package com.normalpainter.render.assetprovider;

import com.normalpainter.render.AssetSupplier;

/**
 * {@link AssetProvider} which provides nothing
 * <p>
 * Created on 2020-11-30.
 *
 * @author Alexander Winter
 */
public class NullProvider<T> implements AssetProvider<T>
{
	@Override
	public T resolve(AssetSupplier assetSupplier)
	{
		return null;
	}

	@Override
	public void load(AssetSupplier assetSupplier) {}

	@Override
	public void finishLoading(AssetSupplier assetSupplier) {}
}
