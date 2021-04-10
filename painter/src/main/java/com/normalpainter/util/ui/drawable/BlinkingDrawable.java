package com.normalpainter.util.ui.drawable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * {@link Drawable} that blinks at a specified frequency
 * <p>
 * Created on 2019-01-21.
 *
 * @author Alexander Winter
 */
public class BlinkingDrawable extends WrapperDrawable
{
	public float visibleDuration, invisibleDuration;

	public BlinkingDrawable(Drawable drawable, float visibleDuration, float invisibleDuration)
	{
		super(drawable);

		this.visibleDuration = visibleDuration;
		this.invisibleDuration = invisibleDuration;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		float progress = Gdx.graphics.getFrameId() / 60f
				% (visibleDuration + invisibleDuration);

		if(progress > visibleDuration)
			return;

		super.draw(batch, x, y, width, height);
	}
}
