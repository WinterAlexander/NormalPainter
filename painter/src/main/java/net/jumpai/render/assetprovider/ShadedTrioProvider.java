package net.jumpai.render.assetprovider;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.jumpai.render.AssetSupplier;
import net.jumpai.render.lighting.ShadedTrio;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * AssetProvider for a {@link ShadedTrio}
 * <p>
 * Created on 2020-11-13.
 *
 * @author Alexander Winter
 */
public class ShadedTrioProvider implements AssetProvider<ShadedTrio>
{
	public final AssetProvider<TextureRegion> flat, normal, preshaded;

	private ShadedTrio cached = null;

	public ShadedTrioProvider(AssetProvider<TextureRegion> flat,
	                          AssetProvider<TextureRegion> normal)
	{
		ensureNotNull(flat, "flat");
		ensureNotNull(normal, "normal");
		this.flat = flat;
		this.normal = normal;
		this.preshaded = new NullProvider<>();
	}

	public ShadedTrioProvider(AssetProvider<TextureRegion> flat,
	                          AssetProvider<TextureRegion> normal,
	                          AssetProvider<TextureRegion> preshaded)
	{
		ensureNotNull(flat, "flat");
		ensureNotNull(normal, "normal");
		ensureNotNull(preshaded, "preshaded");

		this.flat = flat;
		this.normal = normal;
		this.preshaded = preshaded;
	}

	public AssetProvider<TextureRegion> getUnlit()
	{
		return preshaded instanceof NullProvider ? flat : preshaded;
	}

	@Override
	public ShadedTrio resolve(AssetSupplier assetSupplier)
	{
		if(cached != null)
			return cached;

		return cached = new ShadedTrio(flat.resolve(assetSupplier),
				normal.resolve(assetSupplier),
				preshaded.resolve(assetSupplier));
	}

	@Override
	public void finishLoading(AssetSupplier assetSupplier)
	{
		flat.finishLoading(assetSupplier);
		normal.finishLoading(assetSupplier);
		preshaded.finishLoading(assetSupplier);
	}

	@Override
	public void load(AssetSupplier assetSupplier)
	{
		flat.load(assetSupplier);
		normal.load(assetSupplier);
		preshaded.load(assetSupplier);
	}
}
