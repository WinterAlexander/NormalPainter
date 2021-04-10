package com.normalpainter.util.event;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * PriorityListenable implementation with ObjectMap and enum values as priority
 * levels. Supports multiple listeners per levels.
 * <p>
 * Created on 2018-04-24.
 *
 * @author Alexander Winter
 */
public class PriorityListenableImpl<L, P extends Enum<P>> implements PriorityListenable<L, P>
{
	private final ObjectMap<P, Array<L>> listeners = new ObjectMap<>();
	private final P[] priorities;

	private final P defaultPriority;

	@SuppressWarnings("unchecked")
	public PriorityListenableImpl(P defaultPriority)
	{
		this.defaultPriority = defaultPriority;

		priorities = ((Class<P>)defaultPriority.getClass()).getEnumConstants();
		for(P priority : priorities)
			listeners.put(priority, new Array<>());
	}

	/**
	 * Triggers all listener with specified event, levels by levels and quit
	 * when a listener handles the event at a level. Will finish a level before
	 * quitting in the case of a handled event.
	 *
	 * @param event event to dispatch
	 * @return true if any listener handled the event, otherwise false
	 */
	protected boolean trigger(Function<L, Boolean> event)
	{
		for(P priority : priorities)
		{
			Array<L> level = listeners.get(priority);

			boolean handled = false;

			for(L listener : level)
				if(event.apply(listener))
					handled = true;

			if(handled)
				return true;
		}

		return false;
	}

	/**
	 * Triggers all listener with specified event, levels by levels until the
	 * event becomes handled. When that happen, the handledEvent is used instead
	 * to trigger the remaining levels.
	 *
	 * @param event event to dispatch
	 * @param handledEvent event to dispatch when handled
	 * @return true if any listener handled the event, otherwise false
	 */
	protected boolean trigger(Function<L, Boolean> event, Consumer<L> handledEvent)
	{
		boolean handled = false;

		for(P priority : priorities)
		{
			Array<L> level = listeners.get(priority);

			if(handled)
			{
				for(L listener : level)
					handledEvent.accept(listener);
				continue;
			}

			for(L listener : level)
				if(event.apply(listener))
					handled = true;
		}

		return handled;
	}

	@Override
	public void addListener(L listener)
	{
		addListener(listener, defaultPriority);
	}

	@Override
	public void addListener(L listener, P priority)
	{
		listeners.get(priority).add(listener);
	}

	@Override
	public void removeListener(L listener)
	{
		for(Array<L> array : listeners.values())
			array.removeValue(listener, true);
	}

	@Override
	public boolean hasListener(L listener)
	{
		for(Array<L> array : listeners.values())
			if(array.contains(listener, true))
				return true;
		return false;
	}

	@Override
	public void clearListeners()
	{
		for(Array<L> array : listeners.values())
			array.clear();
	}
}
