package net.jumpai.util.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Actor meant to draw a drawable to the entire size of a parent, for the purpose
 * of having a background that supports clipping
 * <p>
 * Created on 2019-08-05.
 *
 * @author Alexander Winter
 */
public class FakeBackground extends Actor
{
	private final Drawable drawable;
	private final Actor parent;

	public FakeBackground(Drawable drawable, Actor parent)
	{
		ensureNotNull(drawable, "drawable");
		ensureNotNull(parent, "parent");

		this.drawable = drawable;
		this.parent = parent;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		drawable.draw(batch, 0, 0, parent.getWidth(), parent.getHeight());
	}
}
