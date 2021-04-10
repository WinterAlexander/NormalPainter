package com.normalpainter.util.ui.scrollpane;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.normalpainter.util.ui.dynamic.DynamicUI;
import com.normalpainter.util.ui.listener.HoverAdapter;

/**
 * ScrollPane with methods to change background, style of scroll bars and get
 * scrollbar width
 * <p>
 * Created on 2017-11-02.
 *
 * @author Alexander Winter
 */
public class BetterScrollPane extends ScrollPane implements DynamicUI
{
	private final Skin skin;

	{
		addListener(new HoverAdapter(() -> getStage().setScrollFocus(this)));
	}

	public BetterScrollPane(Actor widget, Skin skin)
	{
		super(widget, skin);
		this.skin = skin;
	}

	public BetterScrollPane(Actor widget, Skin skin, String styleName)
	{
		super(widget, skin, styleName);
		this.skin = skin;
	}

	public float getVScrollWidth()
	{
		Drawable vScrollKnob = getStyle().vScrollKnob;

		float scrollbarWidth = 0;

		if(vScrollKnob != null)
			scrollbarWidth = vScrollKnob.getMinWidth();

		if(getStyle().vScroll != null)
			scrollbarWidth = Math.max(scrollbarWidth, getStyle().vScroll.getMinWidth());

		return scrollbarWidth;
	}

	public float getHScrollHeight()
	{
		Drawable hScrollKnob = getStyle().hScrollKnob;

		float scrollbarHeight = 0;

		if(hScrollKnob != null)
			scrollbarHeight = hScrollKnob.getMinHeight();

		if(getStyle().hScroll != null)
			scrollbarHeight = Math.max(scrollbarHeight, getStyle().hScroll.getMinHeight());

		return scrollbarHeight;
	}

	@Override
	public void update()
	{
		for(Actor actor : getChildren())
			if(actor instanceof DynamicUI)
				((DynamicUI)actor).update();
	}

	/**
	 * Sets the background drawable from the skin and adjusts the table's padding to match the background.
	 *
	 * @see #setBackground(Drawable)
	 */
	public void setBackground(String drawableName)
	{
		if(skin == null)
			throw new IllegalStateException("Table must have a skin set to use this method.");
		setBackground(skin.getDrawable(drawableName));
	}

	/**
	 * @param background May be null to clear the background.
	 */
	public void setBackground(Drawable background)
	{
		this.getStyle().background = background;
	}

	public Skin getSkin()
	{
		return this.skin;
	}
}
