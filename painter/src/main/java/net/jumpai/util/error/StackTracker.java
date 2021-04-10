package net.jumpai.util.error;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;

import static net.jumpai.util.error.ExceptionUtil.appendCause;

/**
 * Keeps your stacktraces complete!
 * <p>
 * Created on 2018-07-25.
 *
 * @author Alexander Winter
 */
public class StackTracker
{
	private static final ObjectMap<Thread, Queue<Tracker>> trackers = new ObjectMap<>();

	public static Tracker cut(String name)
	{
		TrackedStackTrace ex = new TrackedStackTrace("Exception in " + name);
		appendFullStack(ex);
		return new Tracker(ex);
	}

	public static synchronized void enter(Tracker tracker)
	{
		Thread thread = Thread.currentThread();

		if(!trackers.containsKey(thread))
			trackers.put(thread, new Queue<>());

		trackers.get(thread).addLast(tracker);
	}

	public static synchronized void exit(Tracker tracker)
	{
		Thread thread = Thread.currentThread();

		if(!trackers.containsKey(thread) || trackers.get(thread).size == 0)
			throw new IllegalStateException("Not tracked");

		if(trackers.get(thread).last() != tracker)
			throw new IllegalArgumentException("Invalid tracker");

		trackers.get(thread).removeLast();
	}

	public static synchronized void appendFullStack(Throwable ex)
	{
		Thread thread = Thread.currentThread();

		if(!trackers.containsKey(thread) || trackers.get(thread).size == 0)
			return; //not tracked

		appendCause(ex, trackers.get(thread).last().get());
	}

}
