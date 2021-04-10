package net.jumpai.util.log;

/**
 * Standart java implementation of Logger
 * <p>
 * Created on 2016-10-24.
 *
 * @author Alexander Winter
 */
public class JLogger implements Logger
{
	private java.util.logging.Logger jLogger;

	public JLogger(String name)
	{
		jLogger = java.util.logging.Logger.getLogger(name);
	}

	public JLogger(String name, String resourceBundleName)
	{
		jLogger = java.util.logging.Logger.getLogger(name, resourceBundleName);
	}

	@Override
	public void debug(String message)
	{
		jLogger.log(java.util.logging.Level.FINE, message);
	}

	@Override
	public void debug(String message, Throwable ex)
	{
		jLogger.log(java.util.logging.Level.FINE, message, ex);
	}

	@Override
	public void info(String message)
	{
		jLogger.log(java.util.logging.Level.INFO, message);
	}

	@Override
	public void info(String message, Throwable ex)
	{
		jLogger.log(java.util.logging.Level.INFO, message, ex);
	}

	@Override
	public void warn(String message)
	{
		jLogger.log(java.util.logging.Level.WARNING, message);
	}

	@Override
	public void warn(String message, Throwable ex)
	{
		jLogger.log(java.util.logging.Level.WARNING, message, ex);
	}

	@Override
	public void error(String message)
	{
		jLogger.log(java.util.logging.Level.SEVERE, message);
	}

	@Override
	public void error(String message, Throwable ex)
	{
		jLogger.log(java.util.logging.Level.SEVERE, message, ex);
	}

	@Override
	public void setLogLevel(LogLevel logLevel)
	{
		switch(logLevel)
		{
			case DEBUG:
				jLogger.setLevel(java.util.logging.Level.FINE);
				break;

			case INFO:
				jLogger.setLevel(java.util.logging.Level.INFO);
				break;

			case WARNING:
				jLogger.setLevel(java.util.logging.Level.WARNING);
				break;

			case ERROR:
				jLogger.setLevel(java.util.logging.Level.SEVERE);
				break;

			case NONE:
				jLogger.setLevel(java.util.logging.Level.OFF);
				break;
		}
	}

	@Override
	public LogLevel getLogLevel()
	{
		switch(jLogger.getLevel().intValue())
		{
			case Integer.MIN_VALUE:
			case 300:
			case 400:
			case 500:
				return LogLevel.DEBUG;

			case 700:
			case 800:
				return LogLevel.INFO;

			case 900: //warning
				return LogLevel.WARNING;

			case 1000:
				return LogLevel.ERROR;

			default:
				return LogLevel.NONE;
		}
	}

	public java.util.logging.Logger getInternalLogger()
	{
		return jLogger;
	}
}
