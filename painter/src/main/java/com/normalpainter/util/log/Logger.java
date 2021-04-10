package com.normalpainter.util.log;

/**
 * A model for an object to log ouput.
 * Used by classes that should be reused amount multiple projects.
 * <p>
 * Created on 2016-10-24.
 *
 * @author Alexander Winter
 */
public interface Logger
{
	void debug(String message);
	void debug(String message, Throwable ex);

	void info(String message);
	void info(String message, Throwable ex);

	void warn(String message);
	void warn(String message, Throwable ex);

	void error(String message);
	void error(String message, Throwable ex);

	void setLogLevel(LogLevel logLevel);
	LogLevel getLogLevel();

	enum LogLevel
	{
		NONE, ERROR, WARNING, INFO, DEBUG
	}
}
