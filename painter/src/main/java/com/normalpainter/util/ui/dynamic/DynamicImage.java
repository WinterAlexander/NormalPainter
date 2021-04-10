package com.normalpainter.util.ui.dynamic;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import java.util.function.Supplier;

/**
 * {@link Image} that retrieves its content from a source
 * <p>
 * Created on 2017-11-04.
 *
 * @author Alexander Winter
 */
public class DynamicImage extends Image implements DynamicUI
{
	private Supplier<Drawable> source;

	public DynamicImage(Skin skin, Supplier<String> drawableName)
	{
		super(skin, drawableName.get());
		this.source = () -> skin.getDrawable(drawableName.get());
	}

	public DynamicImage(Supplier<Drawable> drawable)
	{
		super(drawable.get());
		this.source = drawable;
	}

	public DynamicImage(Supplier<Drawable> drawable, Scaling scaling)
	{
		super(drawable.get(), scaling);
		this.source = drawable;
	}

	public DynamicImage(Supplier<Drawable> drawable, Scaling scaling, int align)
	{
		super(drawable.get(), scaling, align);
		this.source = drawable;
	}

	public static DynamicImage fromNinePatch(Supplier<NinePatch> patch)
	{
		return new DynamicImage(() -> {
			NinePatch val = patch.get();
			if(val == null)
				return null;
			return new NinePatchDrawable(val);
		});
	}

	public static DynamicImage fromTextureRegion(Supplier<TextureRegion> region)
	{
		return new DynamicImage(() -> {
			TextureRegion tex = region.get();
			if(tex == null)
				return null;
			return new TextureRegionDrawable(tex);
		});
	}

	@Override
	public void update()
	{
		setDrawable(source.get());
	}
}
