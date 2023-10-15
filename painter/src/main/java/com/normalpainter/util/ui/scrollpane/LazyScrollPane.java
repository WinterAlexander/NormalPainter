package com.normalpainter.util.ui.scrollpane;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.winteralexander.gdx.utils.event.Listenable;
import com.winteralexander.gdx.utils.event.ListenableDelegator;
import com.winteralexander.gdx.utils.event.ListenableImpl;

/**
 * {@link ScrollPane} that loads its content lazyly. Scrolling down triggers
 * the load. For vertical use only
 * <p>
 * Created on 2019-09-05.
 *
 * @author Alexander Winter
 */
public class LazyScrollPane
		extends BetterScrollPane
		implements ListenableDelegator<LazyScrollPaneListener>
{
	private final ListenableImpl<LazyScrollPaneListener> eventHandler = new ListenableImpl<>();

	protected float reloadScrollHeight = 150f;
	protected float loadScrollHeight = 400f;

	private boolean wasReloading = false;

	public LazyScrollPane(Actor widget, Skin skin)
	{
		this(widget, skin, "default");
	}

	public LazyScrollPane(Actor widget, Skin skin, String styleName)
	{
		super(widget, skin, styleName);

		setFadeScrollBars(false);
		setScrollingDisabled(true, false);
		setForceScroll(false, true);
		setupOverscroll(reloadScrollHeight + 10f, 50f, 200f);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if(delta == 0f)
			return;

		validate();

		if(getScrollY() < -reloadScrollHeight && !wasReloading)
		{
			eventHandler.trigger(LazyScrollPaneListener::reload);
			wasReloading = true;
		}
		else if(getActor().getHeight() - getScrollY() - getScrollHeight() < loadScrollHeight)
			eventHandler.trigger(LazyScrollPaneListener::loadMore);

		if(getScrollY() > -10f)
			wasReloading = false;
	}

	@Override
	public Listenable<LazyScrollPaneListener> getEventHandler()
	{
		return eventHandler;
	}
}
