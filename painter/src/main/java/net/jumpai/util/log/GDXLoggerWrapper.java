package net.jumpai.util.log;

import com.badlogic.gdx.ApplicationLogger;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link ApplicationLogger} which feedsback into a regular Jumpaï
 * {@link Logger}
 * <p>
 * Created on 2021-01-15.
 *
 * @author Alexander Winter
 */
public class GDXLoggerWrapper implements ApplicationLogger
{
	private final Logger logger;

	public GDXLoggerWrapper(Logger logger)
	{
		ensureNotNull(logger, "logger");
		this.logger = logger;
	}

	@Override
	public void log(String tag, String message)
	{
		logger.info("[" + tag + "] " + message);
	}

	@Override
	public void log(String tag, String message, Throwable exception)
	{
		logger.info("[" + tag + "] " + message, exception);
	}

	@Override
	public void error(String tag, String message)
	{
		logger.error("[" + tag + "] " + message);
	}

	@Override
	public void error(String tag, String message, Throwable exception)
	{
		logger.error("[" + tag + "] " + message, exception);
	}

	@Override
	public void debug(String tag, String message)
	{
		logger.debug("[" + tag + "] " + message);
	}

	@Override
	public void debug(String tag, String message, Throwable exception)
	{
		logger.debug("[" + tag + "] " + message, exception);
	}
}
