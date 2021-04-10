package net.jumpai;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An object that can be read or written to a stream (file, network or anything)
 * <p>
 * Created on 2016-12-15.
 *
 * @author Alexander Winter
 */
public interface CustomSerializable
{
	void readFrom(InputStream input) throws IOException;

	void writeTo(OutputStream output) throws IOException;
}
