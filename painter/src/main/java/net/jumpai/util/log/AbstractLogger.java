package net.jumpai.util.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger that keeps it's log level and forwards all logs into a single method
 * <p>
 * Created on 2018-04-04.
 *
 * @author Alexander Winter
 */
public abstract class AbstractLogger implements Logger
{
	private final static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	protected LogLevel logLevel;

	public AbstractLogger(LogLevel logLevel)
	{
		this.logLevel = logLevel;
	}

	protected abstract void write(String line) throws IOException;

	protected synchronized void log(LogLevel logLevel, String message, Throwable ex)
	{
		if(this.logLevel.ordinal() >= logLevel.ordinal())
		{
			try
			{
				String line = timeFormat.format(new Date()) + " [" + logLevel.name() + "] " + message;
				if(ex != null)
				{
					StringWriter full = new StringWriter();

					full.write(line);
					full.write(System.lineSeparator());
					ex.printStackTrace(new PrintWriter(full));

					write(full.toString());
				}
				else
					write(line);
			}
			catch(IOException logEx)
			{
				System.err.println("Logging error:");
				logEx.printStackTrace(System.err);
				System.err.println("Was tring to log " + logLevel.name() + " message: " + message);
				if(ex != null)
					ex.printStackTrace(System.err);
			}
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
	public LogLevel getLogLevel()
	{
		return logLevel;
	}

	@Override
	public void setLogLevel(LogLevel logLevel)
	{
		this.logLevel = logLevel;
	}
}
