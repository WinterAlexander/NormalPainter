package net.jumpai.render.assetprovider;

/**
 * {@link BodyClothingProvider} which loads its providers from 3 textures or
 * from a folder specifically for pants
 * <p>
 * Created on 2020-09-20.
 *
 * @author Alexander Winter
 */
public class TexturePantsProvider extends BodyClothingProvider
{
	public TexturePantsProvider(String basePath)
	{
		this(basePath, ".png");
	}

	public TexturePantsProvider(String basePath, String ext)
	{
		this(basePath + "_body" + ext, basePath + "_straightleg" + ext, basePath + "_bentleg" + ext);
	}

	public TexturePantsProvider(String bodyPath, String straightPath, String bentPath)
	{
		super(new FullTextureRegionProvider(bodyPath),
				new FullTextureRegionProvider(straightPath),
				new FullTextureRegionProvider(bentPath));
	}
}