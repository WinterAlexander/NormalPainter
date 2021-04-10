package com.normalpainter.render.assetprovider;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.normalpainter.util.Validation;
import com.normalpainter.render.AssetSupplier;

/**
 * Provides different assets for a parallax or not parallax background
 * <p>
 * Created on 2020-12-27.
 *
 * @author Alexander Winter
 */
public class BackgroundProvider implements ProviderCollection
{
	private final AssetProvider<TextureRegion> staticLayer, background, foreground, clouds;
	private final AssetProvider<TextureRegion>[] midLayers;

	public BackgroundProvider(AssetProvider<TextureRegion> staticLayer,
	                          AssetProvider<TextureRegion> background,
	                          AssetProvider<TextureRegion> clouds,
	                          AssetProvider<TextureRegion> foreground,
	                          AssetProvider<TextureRegion>[] midLayers)
	{
		Validation.ensureNotNull(staticLayer, "staticLayer");
		Validation.ensureNotNull(background, "background");
		Validation.ensureNoneNull(midLayers, "midLayers");
		this.staticLayer = staticLayer;
		this.background = background;
		this.clouds = clouds;
		this.foreground = foreground;
		this.midLayers = midLayers;
	}

	@Override
	public void load(AssetSupplier assets)
	{
		staticLayer.load(assets);
		background.load(assets);
		if(clouds != null)
			clouds.load(assets);
		if(foreground != null)
			foreground.load(assets);
		for(AssetProvider<TextureRegion> midLayer : midLayers)
			midLayer.load(assets);
	}

	@Override
	public void finishLoading(AssetSupplier assets)
	{
		staticLayer.finishLoading(assets);
		background.finishLoading(assets);
		if(clouds != null)
			clouds.finishLoading(assets);
		if(foreground != null)
			foreground.finishLoading(assets);
		for(AssetProvider<TextureRegion> midLayer : midLayers)
			midLayer.finishLoading(assets);
	}

	public boolean hasForeground()
	{
		return foreground != null;
	}

	public boolean hasClouds()
	{
		return clouds != null;
	}

	public int getMidLayerCount()
	{
		return midLayers.length;
	}

	public AssetProvider<TextureRegion> getStaticLayer()
	{
		return staticLayer;
	}

	public AssetProvider<TextureRegion> getBackground()
	{
		return background;
	}

	public AssetProvider<TextureRegion> getForeground()
	{
		return foreground;
	}

	public AssetProvider<TextureRegion> getClouds()
	{
		return clouds;
	}

	public AssetProvider<TextureRegion>[] getMidLayers()
	{
		return midLayers;
	}
}
