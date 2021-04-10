package net.jumpai.render.assetprovider;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * An array of asset providers for texture regions built from an array of
 * regions from an atlas
 * <p>
 * Created on 2020-09-18.
 *
 * @author Alexander Winter
 */
public class AtlasProviderArray extends Array<AssetProvider<TextureRegion>>
{
	public AtlasProviderArray(String baseName, String atlas, int count)
	{
		super(true, count, AssetProvider.class);
		for(int i = 0; i < count; i++)
			add(new AtlasRegionProvider(baseName + i, atlas));
	}
}
