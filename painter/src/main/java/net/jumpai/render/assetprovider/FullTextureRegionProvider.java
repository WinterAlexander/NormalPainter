package net.jumpai.render.assetprovider;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.jumpai.render.AssetSupplier;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link AssetProvider} that provides a texture region from a full texture
 * <p>
 * Created on 2020-09-10.
 *
 * @author Alexander Winter
 */
public class FullTextureRegionProvider implements AssetProvider<TextureRegion>
{
	private final String path;
	private final AssetLoaderParameters<Texture> params;

	public FullTextureRegionProvider(String path)
	{
		ensureNotNull(path, "path");
		this.path = path;
		this.params = null;
	}

	public FullTextureRegionProvider(String path, AssetLoaderParameters<Texture> params)
	{
		ensureNotNull(path, "path");
		ensureNotNull(params, "params");
		this.path = path;
		this.params = params;
	}

	@Override
	public TextureRegion resolve(AssetSupplier assetSupplier)
	{
		return new TextureRegion(assetSupplier.get(path, Texture.class));
	}

	@Override
	public void load(AssetSupplier assetSupplier)
	{
		if(params != null)
			assetSupplier.load(path, Texture.class, params);
		else
			assetSupplier.load(path, Texture.class);
	}

	@Override
	public void finishLoading(AssetSupplier assetSupplier)
	{
		assetSupplier.finishLoadingAsset(path);
	}
}
