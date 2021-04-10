package com.normalpainter.render.assetprovider;

import com.normalpainter.util.Validation;
import com.normalpainter.render.AssetSupplier;

/**
 * Provides 3 ShadedTrios to render the 2 possible types of body clothing
 * (shirts and pants) with dynamic lighting
 * <p>
 * Created on 2020-09-15.
 *
 * @author Alexander Winter
 */
public class LitBodyClothingProvider implements ProviderCollection
{
	private final ShadedTrioProvider body, straight, bent;

	public LitBodyClothingProvider(ShadedTrioProvider body,
	                               ShadedTrioProvider straight,
	                               ShadedTrioProvider bent)
	{
		Validation.ensureNotNull(body, "body");
		Validation.ensureNotNull(straight, "straight");
		Validation.ensureNotNull(bent, "bent");
		this.body = body;
		this.straight = straight;
		this.bent = bent;
	}

	@Override
	public void load(AssetSupplier assets)
	{
		body.load(assets);
		straight.load(assets);
		bent.load(assets);
	}

	@Override
	public void finishLoading(AssetSupplier assets)
	{
		body.finishLoading(assets);
		straight.finishLoading(assets);
		bent.finishLoading(assets);
	}

	public ShadedTrioProvider getBody()
	{
		return body;
	}

	public ShadedTrioProvider getStraight()
	{
		return straight;
	}

	public ShadedTrioProvider getBent()
	{
		return bent;
	}
}
