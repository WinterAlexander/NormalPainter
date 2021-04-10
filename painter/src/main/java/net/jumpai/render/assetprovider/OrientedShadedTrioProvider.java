package net.jumpai.render.assetprovider;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import net.jumpai.render.AssetSupplier;
import net.jumpai.render.UVTransform;
import net.jumpai.render.lighting.OrientedShadedTrio;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * AssetProvider of {@link OrientedShadedTrio}
 * <p>
 * Created on 2020-12-02.
 *
 * @author Alexander Winter
 */
public class OrientedShadedTrioProvider implements AssetProvider<OrientedShadedTrio>
{
	public final AssetProvider<TextureRegion> flat, normal;
	public final ObjectMap<UVTransform, AssetProvider<TextureRegion>> preshadeds = new ObjectMap<>();

	private OrientedShadedTrio cached = null;

	public OrientedShadedTrioProvider(ShadedTrioProvider shadedTrioProvider,
	                                  ObjectMap<UVTransform, AssetProvider<TextureRegion>> extraPreshadeds)
	{
		ensureNotNull(shadedTrioProvider, "shadedTrioProvider");
		ensureNotNull(extraPreshadeds, "extraPreshadeds");

		this.flat = shadedTrioProvider.flat;
		this.normal = shadedTrioProvider.normal;
		if(!(shadedTrioProvider.preshaded instanceof NullProvider))
			preshadeds.put(UVTransform.NONE, shadedTrioProvider.preshaded);
		preshadeds.putAll(extraPreshadeds);
	}

	public OrientedShadedTrioProvider(AssetProvider<TextureRegion> flat,
	                                  AssetProvider<TextureRegion> normal,
	                                  ObjectMap<UVTransform, AssetProvider<TextureRegion>> preshadeds)
	{
		ensureNotNull(flat, "flat");
		ensureNotNull(normal, "normal");
		ensureNotNull(preshadeds, "preshadeds");

		this.flat = flat;
		this.normal = normal;
		this.preshadeds.putAll(preshadeds);
	}

	@Override
	public OrientedShadedTrio resolve(AssetSupplier assetSupplier)
	{
		if(cached != null)
			return cached;

		ObjectMap<UVTransform, TextureRegion> map = new ObjectMap<>(preshadeds.size);

		for(Entry<UVTransform, AssetProvider<TextureRegion>> entry : preshadeds)
			map.put(entry.key, entry.value.resolve(assetSupplier));

		return cached = new OrientedShadedTrio(flat.resolve(assetSupplier),
				normal.resolve(assetSupplier),
				map);
	}

	@Override
	public void finishLoading(AssetSupplier assetSupplier)
	{
		flat.finishLoading(assetSupplier);
		normal.finishLoading(assetSupplier);
		preshadeds.forEach(v -> v.value.finishLoading(assetSupplier));
	}

	@Override
	public void load(AssetSupplier assetSupplier)
	{
		flat.load(assetSupplier);
		normal.load(assetSupplier);
		preshadeds.forEach(v -> v.value.load(assetSupplier));
	}
}
