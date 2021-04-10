package com.normalpainter.util.ui.scrollpane;

/**
 * Listens to the events of a {@link LazyScrollPane}
 * <p>
 * Created on 2019-09-05.
 *
 * @author Alexander Winter
 */
public interface LazyScrollPaneListener
{
	/**
	 * Called when the user attempts to load more by scrolling down
	 */
	void loadMore();

	/**
	 * Called when the user attemps to reload by scrolling up
	 */
	void reload();
}
