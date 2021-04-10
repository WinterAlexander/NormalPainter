package com.normalpainter.render.assetprovider;

/**
 * {@link LitBodyClothingProvider} which loads its providers from 3 Texture
 * ShadedTrios or from a folder specifically for shirts
 * <p>
 * Created on 2020-09-15.
 *
 * @author Alexander Winter
 */
public class LitTextureShirtProvider extends LitBodyClothingProvider
{
	public LitTextureShirtProvider(String basePath)
	{
		this(basePath, true, true);
	}

	public LitTextureShirtProvider(String basePath, boolean preshadedBody, boolean preshadedArms)
	{
		this(basePath + "_body", basePath + "_straightarm", basePath + "_bentarm", preshadedBody, preshadedArms);
	}

	public LitTextureShirtProvider(String bodyPath, String straightPath, String bentPath)
	{
		this(bodyPath, straightPath, bentPath, true, true);
	}

	public LitTextureShirtProvider(String bodyPath, String straightPath, String bentPath, boolean preshadedBody, boolean preshadedArms)
	{
		super(preshadedBody ? new TextureShadedTrioProvider(bodyPath) : new TextureShadedDuoProvider(bodyPath),
				preshadedArms ? new TextureShadedTrioProvider(straightPath) : new TextureShadedDuoProvider(straightPath),
				preshadedArms ? new TextureShadedTrioProvider(bentPath) : new TextureShadedDuoProvider(bentPath));
	}
}
