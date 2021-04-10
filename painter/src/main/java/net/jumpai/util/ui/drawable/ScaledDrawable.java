package net.jumpai.util.ui.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * WrapperDrawable that scales its inner drawable
 * <p>
 * Created on 2019-12-31.
 *
 * @author Alexander Winter
 */
public class ScaledDrawable extends WrapperDrawable
{
	private final float scale;

	public ScaledDrawable(Drawable drawable, float scale)
	{
		super(drawable);
		this.scale = scale;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		super.draw(batch,
				x + (1 - scale) * width / 2f,
				y + (1 - scale) * height / 2f,
				width * scale,
				height * scale);
	}
}
