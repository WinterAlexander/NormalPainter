package com.normalpainter.render.assetprovider;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.normalpainter.util.Validation;
import com.normalpainter.render.AssetSupplier;

/**
 * Provides 3 textures for the 2 possible types of body clothing: shirts and
 * pants
 * <p>
 * Created on 2020-09-15.
 *
 * @author Alexander Winter
 */
public class BodyClothingProvider implements ProviderCollection
{
	private final AssetProvider<TextureRegion> body, straight, bent;

	public BodyClothingProvider(AssetProvider<TextureRegion> body,
	                            AssetProvider<TextureRegion> straight,
	                            AssetProvider<TextureRegion> bent)
	{
		Validation.ensureNotNull(body, "body");
		Validation.ensureNotNull(straight, "straight");
		Validation.ensureNotNull(bent, "bent");
		this.body = body;
		this.straight = straight;
		this.bent = bent;
	}

	@Override
	public void load(AssetSupplier assetSupplier)
	{
		body.load(assetSupplier);
		straight.load(assetSupplier);
		bent.load(assetSupplier);
	}

	@Override
	public void finishLoading(AssetSupplier assets)
	{
		body.finishLoading(assets);
		straight.finishLoading(assets);
		bent.finishLoading(assets);
	}

	public AssetProvider<TextureRegion> getBody()
	{
		return body;
	}

	public AssetProvider<TextureRegion> getStraight()
	{
		return straight;
	}

	public AssetProvider<TextureRegion> getBent()
	{
		return bent;
	}
}
