package net.jumpai.util.error;

import net.jumpai.util.ReflectionUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Utility class to do operations with Exceptions or Throwables
 * <p>
 * Created on 2017-11-02.
 *
 * @author Alexander Winter
 */
public class ExceptionUtil
{
	public static void appendCause(Throwable exception, Throwable cause)
	{
		Throwable emptyCause = exception;

		while(true)
		{
			synchronized(emptyCause)
			{
				if(emptyCause.getCause() != null)
					emptyCause = emptyCause.getCause();
				else
				{
					try
					{
						emptyCause.initCause(cause);
					}
					catch(IllegalStateException illegalState)
					{
						// this means we've got an exception with a null cause
						// but with a cause it's tedious and the only way to fix
						// it is to use reflection to assign the cause

						ReflectionUtil.set(emptyCause, "cause", cause);
					}

					break;
				}
			}
		}
	}

	public static String printStackTrace(Throwable exception)
	{
		if(exception == null)
			return "null";

		StringWriter sw = new StringWriter();
		exception.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
