package net.jumpai.util.io;

import com.badlogic.gdx.utils.Array;
import net.jumpai.CustomSerializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static net.jumpai.util.io.StreamUtil.*;

/**
 * Serialization utility class
 * <p>
 * Created on 2018-06-03.
 *
 * @author Cedric Martens
 * @author Alexander Winter
 */
public class SerializationUtil
{
	private static final byte INT_T = 0;
	private static final byte FLOAT_T = 1;
	private static final byte LONG_T = 2;
	private static final byte DOUBLE_T = 3;
	private static final byte SHORT_T = 4;
	private static final byte BYTE_T = 5;
	private static final byte NULL_T = 6;

	public static <T extends CustomSerializable> T readSerializable(InputStream inputStream, Class<T> type) throws IOException
	{
		T newInstance;
		try
		{
			newInstance = type.getDeclaredConstructor().newInstance();
		}
		catch(ReflectiveOperationException e)
		{
			throw new IOException("Failed to create an instance of " + type.getSimpleName(), e);
		}

		newInstance.readFrom(inputStream);
		return newInstance;
	}

	public static <T extends CustomSerializable> void readSmallArray(InputStream stream, Class<T> type, Array<T> out) throws IOException
	{
		out.clear();
		int size = readShort(stream);
		out.ensureCapacity(size);
		while(size --> 0)
			out.add(readSerializable(stream, type));
	}

	public static void writeSmallArray(OutputStream stream, Array<? extends CustomSerializable> array) throws IOException
	{
		writeShort(stream, array.size);
		for(int i = 0; i < array.size; i++)
			array.get(i).writeTo(stream);
	}

	public static <T extends CustomSerializable> void readArray(InputStream stream, Class<T> type, Array<T> out) throws IOException
	{
		out.clear();
		int size = readInt(stream);
		out.ensureCapacity(size);
		while(size --> 0)
			out.add(readSerializable(stream, type));
	}

	public static void writeArray(OutputStream stream, Array<? extends CustomSerializable> array) throws IOException
	{
		writeInt(stream, array.size);
		for(int i = 0; i < array.size; i++)
			array.get(i).writeTo(stream);
	}

	public static void readBuffered(InputStream stream, CustomSerializable serializable) throws IOException
	{
		int size = readInt(stream);

		if(size < 0)
			throw new IOException("Invalid size of buffer: " + size);

		byte[] data = new byte[size];

		int totalRead = 0;
		do
		{
			int read = stream.read(data, totalRead, size - totalRead);

			if(read < 0)
				throw new EOFException();

			totalRead += read;
		}
		while(totalRead < size);

		serializable.readFrom(new ByteArrayInputStream(data));
	}

	public static void writeBuffered(OutputStream stream, CustomSerializable serializable) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializable.writeTo(baos);
		writeInt(stream, baos.size());
		baos.writeTo(stream);
	}


	@SuppressWarnings("unchecked")
	public static <T> T readPrimitive(InputStream stream, Class<T> type) throws IOException
	{
		if(type == int.class || type == Integer.class)
			return (T)Integer.valueOf(readInt(stream));

		if(type == float.class || type == Float.class)
			return (T)Float.valueOf(readFloat(stream));

		if(type == long.class || type == Long.class)
			return (T)Long.valueOf(readLong(stream));

		if(type == short.class || type == Short.class)
			return (T)Short.valueOf(readShort(stream));

		if(type == byte.class || type == Byte.class)
			return (T)Byte.valueOf(readByte(stream));

		if(type == double.class || type == Double.class)
			return (T)Double.valueOf(readDouble(stream));

		if(type == char.class || type == Character.class)
			return (T)Character.valueOf(readChar(stream));

		if(type == boolean.class || type == Boolean.class)
			return (T)Boolean.valueOf(readBoolean(stream));

		throw new IllegalArgumentException("Specified type must be primitive");
	}

	public static void writePrimitive(OutputStream outputStream, Object primitive) throws IOException
	{
		if(primitive instanceof Integer)
			writeInt(outputStream, (Integer)primitive);

		else if(primitive instanceof Float)
			writeFloat(outputStream, (Float)primitive);

		else if(primitive instanceof Long)
			writeLong(outputStream, (Long)primitive);

		else if(primitive instanceof Short)
			writeShort(outputStream, (Short)primitive);

		else if(primitive instanceof Byte)
			writeByte(outputStream, (Byte)primitive);

		else if(primitive instanceof Double)
			writeDouble(outputStream, (Double)primitive);

		else if(primitive instanceof Character)
			writeChar(outputStream, (Character)primitive);

		else if(primitive instanceof Boolean)
			writeBoolean(outputStream, (Boolean)primitive);

		else
			throw new IllegalArgumentException("Specified type isn't primitive: " + primitive);
	}

	/**
	 * Reads a number and its type from stream
	 * @return number read from stream
	 */
	public static Number readNumber(InputStream stream) throws IOException
	{
		int type = readByte(stream);
		switch(type)
		{
			case INT_T:
				return readInt(stream);

			case FLOAT_T:
				return readFloat(stream);

			case LONG_T:
				return readLong(stream);

			case DOUBLE_T:
				return readDouble(stream);

			case SHORT_T:
				return readShort(stream);

			case BYTE_T:
				return readByte(stream);

			case NULL_T:
				return null;

			default:
				throw new IOException("Invalid primitive number type: " + type);
		}
	}

	/**
	 * Writes a number and its type into the stream
	 * @param number to write into stream
	 */
	public static void writeNumber(OutputStream stream, Number number) throws IOException
	{
		if(number == null)
		{
			writeByte(stream, NULL_T);
			return;
		}

		if(number.getClass() == Integer.class)
		{
			writeByte(stream, INT_T);
			writeInt(stream, number.intValue());
		}
		else if(number.getClass() == Float.class)
		{
			writeByte(stream, FLOAT_T);
			writeFloat(stream, number.floatValue());
		}
		else if(number.getClass() == Long.class)
		{
			writeByte(stream, LONG_T);
			writeLong(stream, number.longValue());
		}
		else if(number.getClass() == Double.class)
		{
			writeByte(stream, DOUBLE_T);
			writeDouble(stream, number.doubleValue());
		}
		else if(number.getClass() == Short.class)
		{
			writeByte(stream, SHORT_T);
			writeShort(stream, number.shortValue());
		}
		else if(number.getClass() == Byte.class)
		{
			writeByte(stream, BYTE_T);
			writeByte(stream, number.byteValue());
		}
		else
			throw new IllegalArgumentException("Unknown number type: " + number.getClass());
	}
}
