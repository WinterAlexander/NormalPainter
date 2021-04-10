package net.jumpai.render.assetprovider;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * An array of asset providers for regions built from an array of textures in a
 * folder
 * <p>
 * Created on 2020-09-18.
 *
 * @author Alexander Winter
 */
public class TextureProviderArray extends Array<AssetProvider<TextureRegion>>
{
	public TextureProviderArray(String basePath, int count)
	{
		this(basePath, count, ".png");
	}

	public TextureProviderArray(String basePath, int count, String suffix)
	{
		super(true, count, AssetProvider.class);
		for(int i = 0; i < count; i++)
			add(new FullTextureRegionProvider(basePath + i + suffix));
	}
}
