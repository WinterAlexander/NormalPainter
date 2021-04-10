package net.jumpai.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * PrintStream that does nothing
 * <p>
 * Created on 2018-07-23.
 *
 * @author Alexander Winter
 */
public class NullPrintStream extends PrintStream
{
	public static NullPrintStream instance = new NullPrintStream();

	public NullPrintStream()
	{
		super(new OutputStream()
		{
			@Override
			public void write(int b) throws IOException
			{
				// do nothing
			}
		});
	}
}
