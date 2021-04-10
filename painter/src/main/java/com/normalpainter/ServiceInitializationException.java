package com.normalpainter;

/**
 * An exception to indicate that a service failed to initialize
 * <p>
 * Created on 2017-08-06.
 *
 * @author Alexander Winter
 */
public class ServiceInitializationException extends ServiceFailureException
{
	public ServiceInitializationException(String serviceName, String reason)
	{
		super("Service with name '" + serviceName + "' failed to initialize: " + reason);
	}

	public ServiceInitializationException(String serviceName, String message, Throwable cause)
	{
		super("Service with name '" + serviceName + "' failed to initialize: " + message, cause);
	}

	public ServiceInitializationException(String serviceName, Throwable cause)
	{
		super("Service with name '" + serviceName + "' failed to initialize.", cause);
	}
}
