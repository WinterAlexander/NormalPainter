package net.jumpai.util.event;

/**
 * Listenable implementation with support for listener priority
 * <p>
 * Created on 2018-04-24.
 *
 * @author Alexander Winter
 */
public interface PriorityListenable<L, P> extends Listenable<L>
{
	/**
	 * Adds a listener with a specific priority
	 *
	 * @param listener listener to add
	 * @param priority priority of the listener
	 */
	void addListener(L listener, P priority);
}
