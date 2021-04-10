package com.normalpainter.util.error;

/**
 * A throwable with only purpose to be append to another exception to track the
 * full stacktrace of the original exception
 * <p>
 * Created on 2018-07-25.
 *
 * @author Alexander Winter
 */
public class TrackedStackTrace extends Throwable
{
	public TrackedStackTrace(String message)
	{
		super(message);
	}
}
