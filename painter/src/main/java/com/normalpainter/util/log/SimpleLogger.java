package com.normalpainter.util.log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An implementation of Logger using System.out
 * <p>
 * Created on 2016-12-18.
 *
 * @author Alexander Winter
 */
public class SimpleLogger implements Logger
{
	private LogLevel logLevel;
	private final static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	public SimpleLogger(LogLevel logLevel)
	{
		this.logLevel = logLevel;
	}

	public void log(LogLevel logLevel, String message, Throwable ex)
	{
		if(this.logLevel.ordinal() >= logLevel.ordinal())
		{
			System.out.println(timeFormat.format(new Date()) + " [" + logLevel.name() + "] " + message);
			if(ex != null)
				ex.printStackTrace(System.out);
		}
	}

	@Override
	public void debug(String message)
	{
		debug(message, null);
	}

	@Override
	public void debug(String message, Throwable ex)
	{
		log(LogLevel.DEBUG, message, ex);
	}

	@Override
	public void info(String message)
	{
		info(message, null);
	}

	@Override
	public void info(String message, Throwable ex)
	{
		log(LogLevel.INFO, message, ex);
	}

	@Override
	public void warn(String message)
	{
		warn(message, null);
	}

	@Override
	public void warn(String message, Throwable ex)
	{
		log(LogLevel.WARNING, message, ex);
	}

	@Override
	public void error(String message)
	{
		error(message, null);
	}

	@Override
	public void error(String message, Throwable ex)
	{
		log(LogLevel.ERROR, message, ex);
	}

	@Override
	public void setLogLevel(LogLevel logLevel)
	{
		this.logLevel = logLevel;
	}

	@Override
	public LogLevel getLogLevel()
	{
		return logLevel;
	}
}
