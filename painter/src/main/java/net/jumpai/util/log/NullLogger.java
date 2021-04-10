package net.jumpai.util.log;

/**
 * A silent implementation of my Logger
 *
 * Created on 2016-12-19.
 *
 * @author Alexander Winter
 */
public class NullLogger implements Logger
{
	public static final NullLogger INSTANCE = new NullLogger();

	@Override
	public void debug(String message) {}

	@Override
	public void debug(String message, Throwable ex) {}

	@Override
	public void info(String message) {}

	@Override
	public void info(String message, Throwable ex) {}

	@Override
	public void warn(String message) {}

	@Override
	public void warn(String message, Throwable ex) {}

	@Override
	public void error(String message) {}

	@Override
	public void error(String message, Throwable ex) {}

	@Override
	public void setLogLevel(LogLevel logLevel) {}

	@Override
	public LogLevel getLogLevel()
	{
		return null;
	}
}
