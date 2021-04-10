package com.normalpainter.util.async;

import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;
import com.normalpainter.util.error.StackTracker;
import com.normalpainter.util.error.Tracker;
import com.normalpainter.util.log.Logger;
import com.normalpainter.util.log.NullLogger;

import java.util.function.Consumer;

/**
 * Utility class to make async calls with a callback and handle exceptions
 *
 * @param <R>
 */
public class AsyncCaller<R>
{
	private static Logger logger = new NullLogger();

	private final Call<R> call;
	private Consumer<R> callback;
	private Consumer<Void> finallyCallback;
	private final OrderedMap<Class<? extends Exception>, Consumer<? extends Exception>> exCallbacks = new OrderedMap<>();
	private boolean called = false;

	private final Tracker tracker = StackTracker.cut("AsyncCaller");

	public AsyncCaller(Call<R> call)
	{
		this.call = call;
	}

	/**
	 * Sets the callback to be executed when async task is done without any errors
	 *
	 * @param callback callback to be executed
	 * @return the same AsyncCaller
	 */
	public AsyncCaller<R> then(Consumer<R> callback)
	{
		if(this.callback != null)
			throw new IllegalStateException("A callback was already set for this call");

		this.callback = callback;
		return this;
	}

	/**
	 * Sets the callback to be executed when async task is done without any errors
	 * <p>
	 * This method wraps the callback using specified CallbackWrapper
	 *
	 * @param callback callback to be executed
	 * @param wrapper wrapper of the callback
	 * @return the same AsyncCaller
	 */
	public AsyncCaller<R> then(Consumer<R> callback, CallbackWrapper wrapper)
	{
		this.callback = wrapper.wrap(callback);
		return this;
	}

	/**
	 * Adds a callback to a specific exeception type
	 * Any exception matching specific type exactly or via inheritance will be called
	 * You cannot add an exception callback to the same exception type twice, as the
	 * first one will be overwritten
	 *
	 * @param type type to match
	 * @param callback exception callback
	 * @return the same AsyncCaller
	 */
	public <T extends Exception> AsyncCaller<R> except(Class<T> type, Consumer<T> callback)
	{
		exCallbacks.put(type, callback);
		return this;
	}

	/**
	 * Adds a callback to a specific exception type
	 * Any exception matching specific type exactly or via inheritance will be called
	 * You cannot add an exception callback to the same exception type twice, as the
	 * first one will be overwritten
	 * <p>
	 * This method wraps the callback using specified CallbackWrapper
	 *
	 * @param type type to match
	 * @param callback exception callback
	 * @return the same AsyncCaller
	 */
	public <T extends Exception> AsyncCaller<R> except(Class<T> type, Consumer<T> callback, CallbackWrapper wrapper)
	{
		exCallbacks.put(type, wrapper.wrap(callback));
		return this;
	}

	/**
	 * Adds a callback to be always executed regardless of the aync call failing
	 *
	 * @param callback callback to always call
	 * @return the same AsyncCaller
	 */
	public AsyncCaller<R> always(Runnable callback)
	{
		this.finallyCallback = v -> callback.run();
		return this;
	}

	/**
	 * Adds a callback to be always executed regardless of the aync call failing
	 * <p>
	 * This method wraps the callback using specified CallbackWrapper
	 *
	 * @param callback callback to always call
	 * @return the same AsyncCaller
	 */
	public AsyncCaller<R> always(Runnable callback, CallbackWrapper wrapper)
	{
		this.finallyCallback = wrapper.wrap(v -> callback.run());
		return this;
	}

	/**
	 * Executes the async call
	 */
	public void execute()
	{
		called = true;
		new Thread(() -> {
			StackTracker.enter(tracker);
			try
			{
				R value = call.execute();
				if(callback != null)
					callback.accept(value);
			}
			catch(Exception ex)
			{
				StackTracker.appendFullStack(ex);
				dispatch(ex);
			}
			finally
			{
				if(finallyCallback != null)
					finallyCallback.accept(null);
				StackTracker.exit(tracker);
			}
		}, "AsyncCaller execution").start();
	}

	@Override
	protected void finalize()
	{
		//DEBUG
		if(!called)
			logger.error("AsyncCaller was destroyed without ever being executed !", tracker.get());
	}

	@SuppressWarnings("unchecked")
	private void dispatch(Exception exception)
	{
		for(Entry<Class<? extends Exception>, Consumer<? extends Exception>> entry : exCallbacks.entries())
		{
			if(entry.key.isInstance(exception))
			{
				((Consumer)entry.value).accept(exception);
				return;
			}
		}

		logger.error("Unhandled exception in AsyncCaller!", exception);
	}

	public static void setLogger(Logger logger)
	{
		if(logger == null)
			logger = new NullLogger();

		AsyncCaller.logger = logger;
	}

	/**
	 * Async call to a function without any parameters that returns void
	 *
	 * @param function function with no params
	 * @return AsyncCaller of the function
	 */
	public static AsyncCaller<Void> async(CheckedVoidAction function)
	{
		return new AsyncCaller<>(() -> {
			function.call();
			return null;
		});
	}

	/**
	 * Async call to a function without any parameters
	 *
	 * @param function function with no params
	 * @param <R> return type of function
	 * @return AsyncCaller of the function
	 */
	public static <R> AsyncCaller<R> async(CheckedSupplier<R> function)
	{
		return new AsyncCaller<>(function::call);
	}

	/**
	 * Async call to a function with 1 parameter
	 *
	 * @param function function with 1 parameter
	 * @param param parameter to give to the function
	 * @param <P> param type
	 * @param <R> result type of the function
	 * @return AsyncCaller of the function
	 */
	public static <P, R> AsyncCaller<R> async(CheckedFunction<P, R> function, P param)
	{
		return new AsyncCaller<>(() -> function.call(param));
	}

	public static <P1, P2, R> AsyncCaller<R> async(CheckedBiFunction<P1, P2, R> function, P1 param1, P2 param2)
	{
		return new AsyncCaller<>(() -> function.call(param1, param2));
	}

	public static <P1, P2, P3, R> AsyncCaller<R> async(CheckedTriFunction<P1, P2, P3, R> function, P1 param1, P2 param2, P3 param3)
	{
		return new AsyncCaller<>(() -> function.call(param1, param2, param3));
	}

	public static <P1, P2, P3, P4, R> AsyncCaller<R> async(CheckedQuadriFunction<P1, P2, P3, P4, R> function, P1 param1, P2 param2, P3 param3, P4 param4)
	{
		return new AsyncCaller<>(() -> function.call(param1, param2, param3, param4));
	}

	public static <P> AsyncCaller<Void> async(CheckedVoidFunction<P> function, P param)
	{
		return new AsyncCaller<>(() -> {
			function.call(param);
			return null;
		});
	}

	public static <P1, P2> AsyncCaller<Void> async(CheckedBiVoidFunction<P1, P2> function, P1 param1, P2 param2)
	{
		return new AsyncCaller<>(() -> {
			function.call(param1, param2);
			return null;
		});
	}

	public static <P1, P2, P3> AsyncCaller<Void> async(CheckedTriVoidFunction<P1, P2, P3> function, P1 param1, P2 param2, P3 param3)
	{
		return new AsyncCaller<>(() -> {
			function.call(param1, param2, param3);
			return null;
		});
	}

	public static <P1, P2, P3, P4> AsyncCaller<Void> async(CheckedQuadriVoidFunction<P1, P2, P3, P4> function, P1 param1, P2 param2, P3 param3, P4 param4)
	{
		return new AsyncCaller<>(() -> {
			function.call(param1, param2, param3, param4);
			return null;
		});
	}

	/**
	 * Represents a function call
	 * <p>
	 * Created on 2017-08-26.
 *
 * @author Alexander Winter
	 */
	@FunctionalInterface
	public interface Call<R>
	{
		R execute() throws Exception;
	}

	@FunctionalInterface
	public interface CheckedSupplier<R>
	{
		R call() throws Exception;
	}

	@FunctionalInterface
	public interface CheckedFunction<P, R>
	{
		R call(P param1) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedBiFunction<P1, P2, R>
	{
		R call(P1 param1, P2 param2) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedTriFunction<P1, P2, P3, R>
	{
		R call(P1 param1, P2 param2, P3 param3) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedQuadriFunction<P1, P2, P3, P4, R>
	{
		R call(P1 param1, P2 param2, P3 param3, P4 param4) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedVoidAction
	{
		void call() throws Exception;
	}

	@FunctionalInterface
	public interface CheckedVoidFunction<P>
	{
		void call(P param1) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedBiVoidFunction<P1, P2>
	{
		void call(P1 param1, P2 param2) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedTriVoidFunction<P1, P2, P3>
	{
		void call(P1 param1, P2 param2, P3 param3) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedQuadriVoidFunction<P1, P2, P3, P4>
	{
		void call(P1 param1, P2 param2, P3 param3, P4 param4) throws Exception;
	}
}
