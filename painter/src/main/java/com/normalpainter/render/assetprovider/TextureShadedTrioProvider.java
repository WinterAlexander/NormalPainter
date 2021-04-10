package com.normalpainter.render.assetprovider;

/**
 * {@link ShadedTrioProvider} implementation loading its assets from texture
 * files
 * <p>
 * Created on 2020-11-13.
 *
 * @author Alexander Winter
 */
public class TextureShadedTrioProvider extends ShadedTrioProvider
{
	public TextureShadedTrioProvider(String path)
	{
		this(path + "_f.png", path + "_n.png", path + ".png");
	}

	public TextureShadedTrioProvider(String flatPath, String normalPath, String preshadedPath)
	{
		super(new FullTextureRegionProvider(flatPath),
				new FullTextureRegionProvider(normalPath),
				new FullTextureRegionProvider(preshadedPath));
	}
}
