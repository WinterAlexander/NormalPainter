package net.jumpai.util.log;

import java.io.File;
import java.io.IOException;

import static net.jumpai.util.io.FileUtil.getDailyLogfileName;
import static net.jumpai.util.io.FileUtil.getLogFile;

/**
 * File logger that logs to a file with the current date.
 * <p>
 * Created on 2018-06-05.
 *
 * @author Cedric Martens
 */
public class CurrentDateFileLogger extends FileLogger
{
	public CurrentDateFileLogger(LogLevel logLevel, File baseDir)
	{
		super(logLevel, getLogFile(baseDir));
	}

	public CurrentDateFileLogger(LogLevel logLevel, File baseDir, boolean append)
	{
		super(logLevel, getLogFile(baseDir), append);
	}

	@Override
	protected void write(String line) throws IOException
	{
		if(!file.getName().equals(getDailyLogfileName()))
			file = getLogFile(file.getParentFile());
		super.write(line);
	}
}
