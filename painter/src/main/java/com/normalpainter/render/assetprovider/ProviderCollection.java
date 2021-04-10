package com.normalpainter.render.assetprovider;

import com.normalpainter.render.AssetSupplier;

/**
 * An object that represents a collection of {@link AssetProvider} and allows to
 * load all of them
 * <p>
 * Created on 2020-09-15.
 *
 * @author Alexander Winter
 */
public interface ProviderCollection
{
	void load(AssetSupplier assets);

	void finishLoading(AssetSupplier assets);
}
