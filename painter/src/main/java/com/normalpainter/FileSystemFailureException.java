package com.normalpainter;

/**
 * Thrown when the file system used by implementation fails to operate properly or yields inconsistent results
 * <p>
 * Created on 2017-08-06.
 *
 * @author Alexander Winter
 */
public class FileSystemFailureException extends ServiceFailureException
{
	public FileSystemFailureException() {}

	public FileSystemFailureException(Throwable cause)
	{
		super(cause);
	}

	public FileSystemFailureException(String message)
	{
		super(message);
	}

	public FileSystemFailureException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
