package net.jumpai.util.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * OutputStream that patches Oracle's java.net socket bug by preventing sent of
 * bytes when the socket is reading
 * <p>
 * Created on 2018-07-29.
 *
 * @author Alexander Winter
 */
public class OraclePatchOutputStream extends OutputStream
{
	private final OutputStream out;
	private volatile boolean halt = false;

	private final Object haltLock = new Object();

	public OraclePatchOutputStream(OutputStream out)
	{
		this.out = out;
	}

	@Override
	public void write(int b) throws IOException
	{
		while(halt)
		{
			synchronized(haltLock)
			{
				try
				{
					haltLock.wait();
				}
				catch(InterruptedException ex)
				{
					throw new RuntimeException(ex);
				}
			}
		}

		out.write(b);
	}

	public void setHalt(boolean halt)
	{
		this.halt = halt;

		if(!halt)
			synchronized(haltLock)
			{
				haltLock.notifyAll();
			}
	}
}
