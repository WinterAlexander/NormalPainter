package com.normalpainter.util.ui.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * {@link NinePatchDrawable} which auto scales the patch when trying to draw
 * smaller than its possible size rather than draw overlapping parts
 * <p>
 * Created on 2020-03-07.
 *
 * @author Alexander Winter
 */
public class ScaleFixNinePatchDrawable extends NinePatchDrawable
{
	public ScaleFixNinePatchDrawable() {}

	public ScaleFixNinePatchDrawable(NinePatch patch)
	{
		super(patch);
	}

	public ScaleFixNinePatchDrawable(NinePatchDrawable drawable)
	{
		super(drawable);
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		float drawW = width, drawH = height;
		boolean noScaleX = false;

		if(width < getPatch().getTotalWidth())
			drawW = getPatch().getTotalWidth();
		else
			noScaleX = true;

		if(height < getPatch().getTotalHeight())
			drawH = getPatch().getTotalHeight();
		else if(noScaleX)
		{
			super.draw(batch, x, y, width, height);
			return;
		}

		draw(batch, x, y,
				getPatch().getMiddleWidth(),
				getPatch().getMiddleHeight(),
				drawW, drawH,
				width / drawW, height / drawH, 0f);
	}
}
