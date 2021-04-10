package net.jumpai.util.log;

/**
 * Logger that logs to multiple existing loggers
 * <p>
 * Created on 2017-06-07.
 *
 * @author Alexander Winter
 */
public class LogMultiplexer implements Logger
{
	private Logger[] loggers;

	public LogMultiplexer(Logger... loggers)
	{
		if(loggers.length < 1)
			throw new IllegalArgumentException("Must have at least one logger in log multiplexer");

		this.loggers = loggers;
	}

	@Override
	public void debug(String message)
	{
		for(Logger logger : loggers)
			logger.debug(message);
	}

	@Override
	public void debug(String message, Throwable ex)
	{
		for(Logger logger : loggers)
			logger.debug(message, ex);
	}

	@Override
	public void info(String message)
	{
		for(Logger logger : loggers)
			logger.info(message);
	}

	@Override
	public void info(String message, Throwable ex)
	{
		for(Logger logger : loggers)
			logger.info(message, ex);
	}

	@Override
	public void warn(String message)
	{
		for(Logger logger : loggers)
			logger.warn(message);
	}

	@Override
	public void warn(String message, Throwable ex)
	{
		for(Logger logger : loggers)
			logger.warn(message, ex);
	}

	@Override
	public void error(String message)
	{
		for(Logger logger : loggers)
			logger.error(message);
	}

	@Override
	public void error(String message, Throwable ex)
	{
		for(Logger logger : loggers)
			logger.error(message, ex);
	}

	@Override
	public void setLogLevel(LogLevel logLevel)
	{
		for(Logger logger : loggers)
			logger.setLogLevel(logLevel);
	}

	@Override
	public LogLevel getLogLevel()
	{
		LogLevel logLevel = loggers[0].getLogLevel();

		for(Logger logger : loggers)
			if(logger.getLogLevel().ordinal() > logLevel.ordinal())
				logLevel = logger.getLogLevel();

		return logLevel;
	}

	public Logger[] getLoggers()
	{
		return loggers;
	}
}
