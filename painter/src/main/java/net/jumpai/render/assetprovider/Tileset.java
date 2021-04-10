package net.jumpai.render.assetprovider;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.jumpai.render.AssetSupplier;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Represents a tileset, set of 13 tiles that allow rendering a structure made
 * out of connected tiles
 * <p>
 * Created on 2020-09-11.
 *
 * @author Alexander Winter
 */
public class Tileset implements ProviderCollection
{
	public final AssetProvider<TextureRegion> center;
	public final AssetProvider<TextureRegion> top, bottom, left, right;
	public final AssetProvider<TextureRegion> topLeft, topRight, bottomLeft, bottomRight;
	public final AssetProvider<TextureRegion> topLeftInside, topRightInside, bottomLeftInside, bottomRightInside;

	public Tileset(AssetProvider<TextureRegion> center,
	               AssetProvider<TextureRegion> top,
	               AssetProvider<TextureRegion> bottom,
	               AssetProvider<TextureRegion> left,
	               AssetProvider<TextureRegion> right,
	               AssetProvider<TextureRegion> topLeft,
	               AssetProvider<TextureRegion> topRight,
	               AssetProvider<TextureRegion> bottomLeft,
	               AssetProvider<TextureRegion> bottomRight,
	               AssetProvider<TextureRegion> topLeftInside,
	               AssetProvider<TextureRegion> topRightInside,
	               AssetProvider<TextureRegion> bottomLeftInside,
	               AssetProvider<TextureRegion> bottomRightInside)
	{
		ensureNotNull(center, "center");
		ensureNotNull(top, "top");
		ensureNotNull(bottom, "bottom");
		ensureNotNull(left, "left");
		ensureNotNull(right, "right");
		ensureNotNull(topLeft, "topLeft");
		ensureNotNull(topRight, "topRight");
		ensureNotNull(bottomLeft, "bottomLeft");
		ensureNotNull(bottomRight, "bottomRight");
		ensureNotNull(topLeftInside, "topLeftInside");
		ensureNotNull(topRightInside, "topRightInside");
		ensureNotNull(bottomLeftInside, "bottomLeftInside");
		ensureNotNull(bottomRightInside, "bottomRightInside");

		this.center = center;
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
		this.topLeft = topLeft;
		this.topRight = topRight;
		this.bottomLeft = bottomLeft;
		this.bottomRight = bottomRight;
		this.topLeftInside = topLeftInside;
		this.topRightInside = topRightInside;
		this.bottomLeftInside = bottomLeftInside;
		this.bottomRightInside = bottomRightInside;
	}

	/**
	 * Loads every provider of this tileset in the specified {@link AssetSupplier}
	 *
	 * @param assets AssetSupplier to load into
	 */
	@Override
	public void load(AssetSupplier assets)
	{
		center.load(assets);
		top.load(assets);
		bottom.load(assets);
		left.load(assets);
		right.load(assets);
		topLeft.load(assets);
		topRight.load(assets);
		bottomLeft.load(assets);
		bottomRight.load(assets);
		topLeftInside.load(assets);
		topRightInside.load(assets);
		bottomLeftInside.load(assets);
		bottomRightInside.load(assets);
	}

	@Override
	public void finishLoading(AssetSupplier assets)
	{
		center.finishLoading(assets);
		top.finishLoading(assets);
		bottom.finishLoading(assets);
		left.finishLoading(assets);
		right.finishLoading(assets);
		topLeft.finishLoading(assets);
		topRight.finishLoading(assets);
		bottomLeft.finishLoading(assets);
		bottomRight.finishLoading(assets);
		topLeftInside.finishLoading(assets);
		topRightInside.finishLoading(assets);
		bottomLeftInside.finishLoading(assets);
		bottomRightInside.finishLoading(assets);
	}
}