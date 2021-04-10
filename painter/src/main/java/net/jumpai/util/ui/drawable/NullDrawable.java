package net.jumpai.util.ui.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

/**
 * Drawable that is always transparent
 * <p>
 * Created on 2018-04-08.
 *
 * @author Alexander Winter
 */
public class NullDrawable extends BaseDrawable
{
	@Override
	public void draw(Batch batch, float x, float y, float width, float height) {}
}
