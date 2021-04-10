package net.jumpai;

/**
 * A generic exception indicating the service has failed to fulfill its contract
 * <p>
 * Created on 2017-08-06.
 *
 * @author Alexander Winter
 */
public class ServiceFailureException extends RuntimeException
{
	public ServiceFailureException() {}

	public ServiceFailureException(String message)
	{
		super(message);
	}

	public ServiceFailureException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ServiceFailureException(Throwable cause)
	{
		super(cause);
	}

	public ServiceFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
