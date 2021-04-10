package net.jumpai.render.assetprovider;

/**
 * {@link BodyClothingProvider} which loads its providers from 3 textures or
 * from a folder specifically for shirts
 * <p>
 * Created on 2020-09-15.
 *
 * @author Alexander Winter
 */
public class TextureShirtProvider extends BodyClothingProvider
{
	public TextureShirtProvider(String basePath)
	{
		this(basePath, ".png");
	}

	public TextureShirtProvider(String basePath, String ext)
	{
		this(basePath + "_body" + ext, basePath + "_straightarm" + ext, basePath + "_bentarm" + ext);
	}

	public TextureShirtProvider(String bodyPath, String straightPath, String bentPath)
	{
		super(new FullTextureRegionProvider(bodyPath),
				new FullTextureRegionProvider(straightPath),
				new FullTextureRegionProvider(bentPath));
	}
}
