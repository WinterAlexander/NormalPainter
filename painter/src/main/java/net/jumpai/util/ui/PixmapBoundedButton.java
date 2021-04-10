package net.jumpai.util.ui;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import static java.lang.Math.round;

/**
 * Button that only reacts to mouse events over the pixels of a pixmap
 * <p>
 * Created on 2019-06-10.
 *
 * @author Alexander Winter
 */
public class PixmapBoundedButton extends Button
{
	private final Pixmap pixmap;
	private final boolean disposePixmap;

	public PixmapBoundedButton(Pixmap pixmap, boolean disposePixmap, Skin skin)
	{
		super(skin);

		this.pixmap = pixmap;
		this.disposePixmap = disposePixmap;
	}

	public PixmapBoundedButton(Pixmap pixmap, boolean disposePixmap, Skin skin, String styleName)
	{
		super(skin, styleName);

		this.pixmap = pixmap;
		this.disposePixmap = disposePixmap;
	}

	public PixmapBoundedButton(Pixmap pixmap, boolean disposePixmap, Actor child, Skin skin, String styleName)
	{
		super(child, skin, styleName);

		this.pixmap = pixmap;
		this.disposePixmap = disposePixmap;
	}

	public PixmapBoundedButton(Pixmap pixmap, boolean disposePixmap, Actor child, ButtonStyle style)
	{
		super(child, style);

		this.pixmap = pixmap;
		this.disposePixmap = disposePixmap;
	}

	public PixmapBoundedButton(Pixmap pixmap, boolean disposePixmap, ButtonStyle style)
	{
		super(style);

		this.pixmap = pixmap;
		this.disposePixmap = disposePixmap;
	}

	public PixmapBoundedButton(Pixmap pixmap, boolean disposePixmap)
	{
		this.pixmap = pixmap;
		this.disposePixmap = disposePixmap;
	}

	public PixmapBoundedButton(Pixmap pixmap, boolean disposePixmap, Drawable up)
	{
		super(up);

		this.pixmap = pixmap;
		this.disposePixmap = disposePixmap;
	}

	public PixmapBoundedButton(Pixmap pixmap, boolean disposePixmap, Drawable up, Drawable down)
	{
		super(up, down);

		this.pixmap = pixmap;
		this.disposePixmap = disposePixmap;
	}

	public PixmapBoundedButton(Pixmap pixmap, boolean disposePixmap, Drawable up, Drawable down, Drawable checked)
	{
		super(up, down, checked);

		this.pixmap = pixmap;
		this.disposePixmap = disposePixmap;
	}

	public PixmapBoundedButton(Pixmap pixmap, boolean disposePixmap, Actor child, Skin skin)
	{
		super(child, skin);

		this.pixmap = pixmap;
		this.disposePixmap = disposePixmap;
	}

	@Override
	public Actor hit(float x, float y, boolean touchable)
	{
		Actor actor = super.hit(x, y, touchable);

		if(actor == null)
			return null;

		int pixel = pixmap.getPixel(
				round(x / getWidth() * pixmap.getWidth()),
				round((getHeight() - y) / getHeight() * pixmap.getHeight()));

		if((pixel & 0x000000FF) == 0)
			return null;

		return actor;
	}

	@Override
	protected void finalize() throws Throwable
	{
		if(disposePixmap)
			pixmap.dispose();

		super.finalize();
	}
}
