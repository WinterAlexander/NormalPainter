package net.jumpai.render.assetprovider;

/**
 * {@link ShadedTrioProvider} with no preshaded texture loaded from texture
 * files
 * <p>
 * Created on 2020-11-30.
 *
 * @author Alexander Winter
 */
public class TextureShadedDuoProvider extends ShadedTrioProvider
{
	public TextureShadedDuoProvider(String path)
	{
		this(path + ".png", path + "_n.png");
	}

	public TextureShadedDuoProvider(String flatPath, String normalPath)
	{
		super(new FullTextureRegionProvider(flatPath),
				new FullTextureRegionProvider(normalPath));
	}
}