package net.jumpai.util.ui.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Tints a drawable with a specified color
 * <p>
 * Created on 2017-11-15.
 *
 * @author Alexander Winter
 */
public class TintedDrawable extends WrapperDrawable
{
	public Color color;

	public TintedDrawable()
	{
		this(new NullDrawable(), Color.WHITE);
	}

	public TintedDrawable(Drawable drawable, Color color)
	{
		super(drawable);
		this.color = color;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		float prevColor = batch.getPackedColor();
		batch.setColor(batch.getColor().mul(color));

		drawable.draw(batch, x, y, width, height);

		batch.setPackedColor(prevColor);
	}

	public static TintedDrawable tinted(Drawable drawable, Color color)
	{
		return new TintedDrawable(drawable, color);
	}

	public static TintedDrawable transparent(Drawable drawable, float alpha)
	{
		return new TintedDrawable(drawable, new Color(1f, 1f, 1f, alpha));
	}
}
