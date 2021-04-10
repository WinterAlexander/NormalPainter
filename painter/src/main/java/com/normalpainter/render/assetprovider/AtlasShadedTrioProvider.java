package com.normalpainter.render.assetprovider;

import com.normalpainter.render.lighting.ShadedTrio;

/**
 * Provides a {@link ShadedTrio} from atlases
 * <p>
 * Created on 2020-12-08.
 *
 * @author Alexander Winter
 */
public class AtlasShadedTrioProvider extends ShadedTrioProvider
{
	public AtlasShadedTrioProvider(String name, String atlasDirectory)
	{
		this(name, atlasDirectory + "/flat.atlas", atlasDirectory + "/normal.atlas", atlasDirectory + "/preshaded.atlas");
	}

	public AtlasShadedTrioProvider(String name, String atlasFlat, String atlasNormal, String atlasPreshaded)
	{
		super(new AtlasRegionProvider(name, atlasFlat),
				new AtlasRegionProvider(name, atlasNormal),
				new AtlasRegionProvider(name, atlasPreshaded));
	}
}
