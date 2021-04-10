package com.normalpainter.render.assetprovider;

import com.normalpainter.render.AssetSupplier;

/**
 * Provides an asset that is in an {@link AssetSupplier} and specifies how to
 * load said asset
 * <p>
 * Created on 2020-09-10.
 *
 * @author Alexander Winter
 */
public interface AssetProvider<T>
{
	T resolve(AssetSupplier assetSupplier);

	void load(AssetSupplier assetSupplier);

	void finishLoading(AssetSupplier assetSupplier);
}
