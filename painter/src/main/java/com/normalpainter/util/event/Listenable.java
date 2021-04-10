package com.normalpainter.util.event;

/**
 * Represents an object that can have listener to listen to its events
 * <p>
 * Created on 2018-03-26.
 *
 * @author Alexander Winter
 */
public interface Listenable<L>
{
	/**
	 * Adds specified listener to this Listenable object to start listening to
	 * its events
	 * @param listener listener to add
	 */
	void addListener(L listener);

	/**
	 * Removes specified listener from this Listenable object to stop listening
	 * to its events
	 * @param listener listener to remove
	 */
	void removeListener(L listener);

	/**
	 * Checks if specified listener is currently listening to the events of this
	 * listenable.
	 * @param listener listener check if present
	 * @return true if listener is currently listening, otherwise false
	 */
	boolean hasListener(L listener);

	/**
	 * Clears this listenable from all its listeners.
	 */
	void clearListeners();
}
