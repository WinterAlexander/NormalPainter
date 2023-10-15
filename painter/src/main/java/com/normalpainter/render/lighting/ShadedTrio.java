package com.normalpainter.render.lighting;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.winteralexander.gdx.utils.Validation;

/**
 * A bundle of {@link TextureRegion}s required for rendering an asset with full
 * shading. It has the flat, normal and preshaded textures.
 * <p>
 * Created on 2020-11-30.
 *
 * @author Alexander Winter
 */
public class ShadedTrio
{
	public final TextureRegion flat, normal, preshaded;

	public ShadedTrio(TextureRegion flat, TextureRegion normal, TextureRegion preshaded)
	{
		Validation.ensureNotNull(flat, "flat");
		Validation.ensureNotNull(normal, "normal");

		this.flat = flat;
		this.normal = normal;
		this.preshaded = preshaded;
	}

	public TextureRegion getUnlit()
	{
		return preshaded == null ? flat : preshaded;
	}
}
