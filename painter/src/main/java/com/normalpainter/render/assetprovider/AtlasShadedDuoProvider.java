package com.normalpainter.render.assetprovider;

import com.normalpainter.render.lighting.ShadedTrio;

/**
 * Provides a {@link ShadedTrio} from atlases without a preshaded region
 * <p>
 * Created on 2020-12-08.
 *
 * @author Alexander Winter
 */
public class AtlasShadedDuoProvider extends ShadedTrioProvider
{
	public AtlasShadedDuoProvider(String name, String atlasDir)
	{
		super(new AtlasRegionProvider(name, atlasDir + "/flat.atlas"),
				new AtlasRegionProvider(name, atlasDir + "/normal.atlas"));
	}
}
