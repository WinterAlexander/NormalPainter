package net.jumpai.util.scheduler;

import com.badlogic.gdx.utils.Array;
import net.jumpai.util.async.CallbackWrapper;
import net.jumpai.util.log.Logger;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static java.lang.System.currentTimeMillis;
import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Represents a scheduler executing tasks at a defined time
 * <p>
 * Created on 2016-12-16.
 *
 * @see Task
 * @author Alexander Winter
 */
public class Scheduler implements CallbackWrapper
{
	private final Logger logger;

	private final Array<Task> tasks = new Array<>(false, 16), tmpTasks = new Array<>(false, 16);
	private final Array<Task> toRemove = new Array<>(false, 16);

	private long pauseLength = 0, lastPause;
	private volatile boolean running = false, updating = false;


	/**
	 * Creating a new scheduler stopped by default with a logger.
	 * @param logger logger, can be null
	 */
	public Scheduler(Logger logger)
	{
		ensureNotNull(logger, "logger");

		this.logger = logger;

		lastPause = currentTimeMillis();
	}

	/**
	 * Mark the scheduler as started. You need to receive the update method
	 * in a loop or call the loop method to actually receive the tasks.
	 * Update won't work if the scheduler isn't started
	 */
	public void start()
	{
		running = true;
		pauseLength += currentTimeMillis() - lastPause;
	}

	/**
	 * Stops the scheduler
	 */
	public void stop()
	{
		if(!running)
		{
			running = false;
			lastPause = currentTimeMillis();
		}
	}

	public void loop(BooleanSupplier condition)
	{
		if(!isRunning())
			start();

		while(condition.getAsBoolean())
		{
			long toWait = getWaitingDelay();
			if(toWait > 0)
			{
				try
				{
					synchronized(this)
					{
						if(toWait == Long.MAX_VALUE)
							wait(0);
						else
							wait(toWait);
					}
				}
				catch(InterruptedException ex)
				{
					logger.error("Unexpected exception occurred while waiting in Scheduler lopp", ex);
				}
			}
			update();
		}
	}

	public void update()
	{
		if(!running)
			return;

		updating = true;
		tmpTasks.size = 0;

		synchronized(this)
		{
			tmpTasks.addAll(tasks);
		}

		for(Task task : tmpTasks)
		{
			try
			{
				if(task == null)
					break; //it means we reached the end of valid data in the array.

				if(task.getScheduler() != this)
				{
					toRemove.add(task);
					continue;
				}

				if(task.getDelay() == 0)
				{
					task.run();
					if(!task.isRepeating())
						toRemove.add(task);
					continue;
				}

				int turns = (int)((executionTime() - task.getLastWork()) / task.getDelay());
				if(!task.isRepeating() && turns >= 1)
				{
					task.run();
					toRemove.add(task);
					continue;
				}

				for(int i = 0; i < turns; i++)
					task.run();

				task.setLastWork(task.getLastWork() + task.getDelay() * turns);
			}
			catch(Exception ex)
			{
				toRemove.add(task);
				logger.error("Error in scheduler with task " + task.toString(), ex);
			}
		}

		synchronized(this)
		{
			tasks.removeAll(toRemove, true);
		}

		toRemove.clear();

		updating = false;
	}

	/**
	 * Retrieves the time until the next time the scheduler should be updated
	 *
	 * @return time between now and next update
	 */
	public synchronized long getWaitingDelay()
	{
		long minDelay = Long.MAX_VALUE;

		for(Task task : tasks)
		{
			if(task == null)
				continue;

			long delay = task.getLastWork() + task.getDelay() - executionTime();
			if(delay < minDelay)
				minDelay = delay;
		}

		return minDelay;
	}

	/**
	 * Gets the time that has passed since the start of this scheduler. Pauses are substracted from this time.
	 * @return time in milliseconds
	 */
	public long executionTime()
	{
		if(!isRunning())
			return lastPause - pauseLength;
		return currentTimeMillis() - pauseLength;
	}

	@Override
	public <T> Consumer<T> wrap(Consumer<T> consumer)
	{
		return t -> addTask(new Task(0, false) {
			@Override
			public void run() {
				consumer.accept(t);
			}
		});
	}

	public void addTask(Runnable runnable, int delay)
	{
		addTask(runnable, delay, false);
	}

	public void addTask(Runnable runnable, int delay, boolean repeat)
	{
		addTask(new Task(delay, repeat, runnable));
	}

	/**
	 * @param task task to register, must NOT be null
	 */
	public void addTask(Task task)
	{
		ensureNotNull(task, "task");

		task.register(this);
		synchronized(this)
		{
			tasks.add(task);

			if(!updating)
				notify();
		}
	}

	public synchronized void cancelTask(Task task)
	{
		tasks.removeValue(task, true);
	}

	public synchronized void clear()
	{
		tasks.size = 0;
	}

	public synchronized boolean contains(Task task)
	{
		return tasks.contains(task, true);
	}

	public boolean isRunning()
	{
		return running;
	}
}
