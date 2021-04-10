package net.jumpai.util.async;

import net.jumpai.util.log.Logger;

import java.util.concurrent.Executor;

import static net.jumpai.util.async.AsyncCaller.async;
import static net.jumpai.util.async.GdxCallback.gdxWrapper;

/**
 * Executor that starts new threads everytime
 * <p>
 * Created on 2018-12-23.
 *
 * @author Alexander Winter
 */
public class AsyncExecutor implements Executor
{
	private final Logger logger;

	public AsyncExecutor(Logger logger)
	{
		this.logger = logger;
	}

	@Override
	public void execute(Runnable command)
	{
		async(command::run)
		.except(Exception.class,
				ex -> logger.error("Exception in AsyncExecutor", ex), gdxWrapper)
		.execute();
	}
}
