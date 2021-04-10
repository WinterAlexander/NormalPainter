package com.normalpainter.util.error;

/**
 * StackTrace wrapper for StackTracker
 * <p>
 * Created on 2018-07-25.
 *
 * @author Alexander Winter
 */
public class Tracker
{
	private final TrackedStackTrace ex;

	public Tracker(TrackedStackTrace ex)
	{
		this.ex = ex;
	}

	public TrackedStackTrace get()
	{
		return ex;
	}
}
