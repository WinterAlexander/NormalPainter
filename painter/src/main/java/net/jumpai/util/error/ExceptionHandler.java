package net.jumpai.util.error;

/**
 * Handles exceptions for methods where exception handling should not necessary stop its execution
 *
 * The best example of that would be an iterative method that doesn't know if should stop on first failure
 * or logs errors and keeps what is working
 * <p>
 * Created on 2017-08-28.
 *
 * @author Alexander Winter
 */
@FunctionalInterface
public interface ExceptionHandler<T extends Exception>
{
	/**
	 * Handles a single exception and lets the caller know if it should continue or not
	 * @param exception exception to handle
	 * @return true if the method should continue despite the exception, otherwise false
	 */
	boolean handle(T exception);
}
