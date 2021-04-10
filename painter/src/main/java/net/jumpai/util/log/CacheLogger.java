package net.jumpai.util.log;

import com.badlogic.gdx.utils.Array;

import java.io.IOException;

/**
 * Implementation of Logger that keeps things into cache for later retrieval
 * <p>
 * Created on 2018-04-04.
 *
 * @author Alexander Winter
 */
public class CacheLogger extends AbstractLogger
{
	private final Array<String> lines = new Array<>();

	public CacheLogger(LogLevel logLevel)
	{
		super(logLevel);
	}

	@Override
	protected void write(String line) throws IOException
	{
		lines.add(line);
	}

	public Array<String> getLines()
	{
		return lines;
	}
}
