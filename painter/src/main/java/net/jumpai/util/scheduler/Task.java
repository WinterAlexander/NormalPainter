package net.jumpai.util.scheduler;

/**
 * Represents a task to be executed in a scheduler
 * <p>
 * You can pass it a runnable OR redefine it's run method, both are fine
 * <p>
 * Created on 2016-12-16.
 *
 * @author Alexander Winter
 */
public class Task implements Runnable
{
	private Scheduler scheduler = null; //scheduler this task is scheduled on

	private final long delay; //delay of the task
	private long lastWork = -1L; //last time it got executed
	private final boolean repeating; //true if the task is meant to be repeated
	private final Runnable runnable; //content of the task, or null if task itself has been redefined

	public Task(long delay)
	{
		this(delay, false);
	}

	public Task(long delay, boolean repeating)
	{
		this(delay, repeating, null);
	}

	public Task(long delay, boolean repeating, Runnable runnable)
	{
		this.delay = delay;
		this.repeating = repeating;
		this.runnable = runnable;
	}

	public void register(Scheduler scheduler)
	{
		if(this.scheduler != null)
			this.scheduler.cancelTask(this);

		this.scheduler = scheduler;

		if(scheduler != null)
			setLastWork(scheduler.executionTime());
		else
			setLastWork(-1L);
	}

	@Override
	public void run()
	{
		if(runnable != null)
			runnable.run();
	}

	public Scheduler getScheduler()
	{
		return scheduler;
	}

	public long getDelay()
	{
		return this.delay;
	}

	public long getLastWork()
	{
		return lastWork;
	}

	public void setLastWork(long lastWork)
	{
		this.lastWork = lastWork;
	}

	public boolean isRepeating()
	{
		return repeating;
	}
}
