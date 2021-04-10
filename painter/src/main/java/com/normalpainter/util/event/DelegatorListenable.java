package com.normalpainter.util.event;

/**
 * Listenable that delegates the Listenable logic to another Listenable
 * <p>
 * Created on 2018-04-27.
 *
 * @author Alexander Winter
 */
public interface DelegatorListenable<L> extends Listenable<L>
{
	Listenable<L> getEventHandler();

	@Override
	default void addListener(L listener)
	{
		getEventHandler().addListener(listener);
	}

	@Override
	default void removeListener(L listener)
	{
		getEventHandler().removeListener(listener);
	}

	@Override
	default boolean hasListener(L listener)
	{
		return getEventHandler().hasListener(listener);
	}

	@Override
	default void clearListeners()
	{
		getEventHandler().clearListeners();
	}
}
