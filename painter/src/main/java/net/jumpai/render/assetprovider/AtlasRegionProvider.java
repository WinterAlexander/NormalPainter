package net.jumpai.render.assetprovider;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.jumpai.render.AssetSupplier;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link AssetProvider} that provides a texture region that is contained within
 * an atlas
 * <p>
 * Created on 2020-09-10.
 *
 * @author Alexander Winter
 */
public class AtlasRegionProvider implements AssetProvider<TextureRegion>
{
	private final String textureName, atlasPath;

	public AtlasRegionProvider(String textureName, String atlasPath)
	{
		ensureNotNull(textureName, "textureName");
		ensureNotNull(atlasPath, "atlasPath");
		this.textureName = textureName;
		this.atlasPath = atlasPath;
	}

	@Override
	public TextureRegion resolve(AssetSupplier assetSupplier)
	{
		TextureRegion region = assetSupplier.get(atlasPath, TextureAtlas.class).findRegion(textureName);

		if(region == null)
			throw new RuntimeException("Region " + textureName + " of atlas " + atlasPath + " not found in AtlasRegionProvider");

		return region;
	}

	@Override
	public void load(AssetSupplier assetSupplier)
	{
		assetSupplier.load(atlasPath, TextureAtlas.class);
	}

	@Override
	public void finishLoading(AssetSupplier assetSupplier)
	{
		assetSupplier.finishLoadingAsset(atlasPath);
	}
}
