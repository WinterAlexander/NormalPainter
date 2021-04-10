package net.jumpai.render.assetprovider;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import net.jumpai.render.AssetSupplier;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link Tileset} made out of regions in a specified atlas
 * <p>
 * Created on 2020-09-11.
 *
 * @author Alexander Winter
 */
public class AtlasTileset extends Tileset
{
	private final String atlasPath;

	public AtlasTileset(String atlasPath)
	{
		this(atlasPath, "");
	}

	public AtlasTileset(String atlasPath, String prefix)
	{
		super(new AtlasRegionProvider(prefix + "center", atlasPath),
				new AtlasRegionProvider(prefix + "top", atlasPath),
				new AtlasRegionProvider(prefix + "bottom", atlasPath),
				new AtlasRegionProvider(prefix + "left", atlasPath),
				new AtlasRegionProvider(prefix + "right", atlasPath),
				new AtlasRegionProvider(prefix + "topleftcorner", atlasPath),
				new AtlasRegionProvider(prefix + "toprightcorner", atlasPath),
				new AtlasRegionProvider(prefix + "bottomleftcorner", atlasPath),
				new AtlasRegionProvider(prefix + "bottomrightcorner", atlasPath),
				new AtlasRegionProvider(prefix + "topleftinside", atlasPath),
				new AtlasRegionProvider(prefix + "toprightinside", atlasPath),
				new AtlasRegionProvider(prefix + "bottomleftinside", atlasPath),
				new AtlasRegionProvider(prefix + "bottomrightinside", atlasPath));

		ensureNotNull(atlasPath, "atlasPath");
		this.atlasPath = atlasPath;
	}

	@Override
	public void load(AssetSupplier assets)
	{
		assets.load(atlasPath, TextureAtlas.class);
	}
}
