package net.jumpai.util.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import net.jumpai.util.SystemUtil;
import net.jumpai.util.io.NullPrintStream;

import java.io.PrintStream;

/**
 * Utility class to interact with LibGDX
 * <p>
 * Created on 2017-11-08.
 *
 * @author Alexander Winter
 */
public class GdxUtil
{
	private GdxUtil() {}

	public static void postRunnable(Runnable runnable)
	{
		Gdx.app.postRunnable(runnable);
	}

	public static void postRunnable(Runnable runnable, long millis)
	{
		new Thread(() -> {
			SystemUtil.sleep(millis);
			postRunnable(runnable);
		}).start();
	}

	public static void registerInput(InputProcessor processor)
	{
		InputProcessor current = Gdx.input.getInputProcessor();

		if(current == null)
			Gdx.input.setInputProcessor(processor);

		else if(current instanceof InputMultiplexer)
			((InputMultiplexer)current).addProcessor(processor);

		else
			Gdx.input.setInputProcessor(new InputMultiplexer(current, processor));
	}

	public static void registerPriorityInput(InputProcessor processor)
	{
		InputProcessor current = Gdx.input.getInputProcessor();

		if(current == null)
			Gdx.input.setInputProcessor(processor);

		else if(current instanceof InputMultiplexer)
		{
			Array<InputProcessor> processors = new Array<>();
			processors.add(processor);
			processors.addAll(((InputMultiplexer)current).getProcessors());

			((InputMultiplexer)current).setProcessors(processors);
		}
		else
			Gdx.input.setInputProcessor(new InputMultiplexer(processor, current));
	}

	public static void unregisterInput(InputProcessor processor)
	{
		if(Gdx.input.getInputProcessor() == processor)
			Gdx.input.setInputProcessor(null);

		else if(Gdx.input.getInputProcessor() instanceof InputMultiplexer)
			((InputMultiplexer)Gdx.input.getInputProcessor()).removeProcessor(processor);
	}

	public static void clearInputProcessors()
	{
		if(Gdx.input.getInputProcessor() instanceof InputMultiplexer)
			((InputMultiplexer)Gdx.input.getInputProcessor()).clear();
		else
			Gdx.input.setInputProcessor(new InputMultiplexer());
	}

	public static boolean openURI(String uri)
	{
		PrintStream prevErr = System.err;
		try
		{
			System.setErr(NullPrintStream.instance);
		}
		catch(SecurityException ignored)
		{
			return Gdx.net.openURI(uri);
		}

		try
		{

			return Gdx.net.openURI(uri);
		}
		finally
		{
			System.setErr(prevErr);
		}
	}
}
