package com.normalpainter.util.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.floatToIntBits;
import static java.lang.Float.intBitsToFloat;

/**
 * Used to do the work of a DataInputStream and DataOutputStream without creating new objects
 * <p>
 * Created on 2016-12-15.
 *
 * @author Alexander Winter
 */
public class StreamUtil
{
	public static boolean readBoolean(InputStream stream) throws IOException
	{
		int val = stream.read();

		if(val < 0)
			throw new EOFException();

		return val != 0;
	}

	public static byte readByte(InputStream stream) throws IOException
	{
		int val = stream.read();

		if(val < 0)
			throw new EOFException();

		return (byte)(val);
	}

	public static int readUnsignedByte(InputStream stream) throws IOException
	{
		int val = stream.read();

		if(val < 0)
			throw new EOFException();

		return val;
	}

	public static short readShort(InputStream stream) throws IOException
	{
		int val1 = stream.read();
		int val2 = stream.read();

		if((val1 | val2) < 0)
			throw new EOFException();

		return (short)((val1 << 8) + val2);
	}

	public static int readUnsignedShort(InputStream stream) throws IOException
	{
		int val1 = stream.read();
		int val2 = stream.read();

		if((val1 | val2) < 0)
			throw new EOFException();

		return (val1 << 8) + val2;
	}

	public static char readChar(InputStream stream) throws IOException
	{
		int val1 = stream.read();
		int val2 = stream.read();

		if((val1 | val2) < 0)
			throw new EOFException();

		return (char)((val1 << 8) + val2);
	}

	public static int readInt24(InputStream stream) throws IOException
	{
		int val1 = stream.read();
		int val2 = stream.read();
		int val3 = stream.read();

		if((val1 | val2 | val3) < 0)
			throw new EOFException();

		return (val1 << 16) + (val2 << 8) + val3;
	}

	public static int readInt(InputStream stream) throws IOException
	{
		int val1 = stream.read();
		int val2 = stream.read();
		int val3 = stream.read();
		int val4 = stream.read();

		if((val1 | val2 | val3 | val4) < 0)
			throw new EOFException();

		return (val1 << 24) + (val2 << 16) + (val3 << 8) + val4;
	}

	public static int readInt(InputStream stream, int maxValue) throws IOException
	{
		if(maxValue < 1 << 8)
			return readByte(stream);

		if(maxValue < 1 << 16)
			return readShort(stream);

		if(maxValue < 1 << 24)
			return readInt24(stream);

		return readInt(stream);
	}

	public static float readFloat(InputStream stream) throws IOException
	{
		return intBitsToFloat(readInt(stream));
	}

	public static long readLong(InputStream stream) throws IOException
	{
		int val1 = stream.read();
		int val2 = stream.read();
		int val3 = stream.read();
		int val4 = stream.read();
		int val5 = stream.read();
		int val6 = stream.read();
		int val7 = stream.read();
		int val8 = stream.read();

		if((val1 | val2 | val3 | val4 | val5 | val6 | val7 | val8) < 0)
			throw new EOFException();

		return ((long)((byte)val1) << 56) +
				((long)(((byte)val2) & 255) << 48) +
				((long)(((byte)val3) & 255) << 40) +
				((long)(((byte)val4) & 255) << 32) +
				((long)(((byte)val5) & 255) << 24) +
				((((byte)val6) & 255) << 16) +
				((((byte)val7) & 255) << 8) +
				(((byte)val8) & 255);
	}

	public static double readDouble(InputStream stream) throws IOException
	{
		return longBitsToDouble(readLong(stream));
	}

	public static <T extends Enum<T>> T readEnum(InputStream stream, Class<T> type) throws IOException
	{
		int val = readInt(stream);
		if(val == -1)
			return null;
		return type.getEnumConstants()[val];
	}

	public static String readUTF(InputStream stream) throws IOException
	{
		return new DataInputStream(stream).readUTF();
	}

	public static void writeBoolean(OutputStream stream, boolean value) throws IOException
	{
		stream.write(value ? 1 : 0);
	}

	public static void writeByte(OutputStream stream, int value) throws IOException
	{
		stream.write(value);
	}

	public static void writeShort(OutputStream stream, int value) throws IOException
	{
		stream.write((value >>> 8) & 0xFF);
		stream.write(value & 0xFF);
	}

	public static void writeChar(OutputStream stream, int value) throws IOException
	{
		stream.write((value >>> 8) & 0xFF);
		stream.write(value & 0xFF);
	}

	public static void writeInt24(OutputStream stream, int value) throws IOException
	{
		stream.write((value >>> 16) & 0xFF);
		stream.write((value >>> 8) & 0xFF);
		stream.write(value & 0xFF);
	}

	public static void writeInt(OutputStream stream, int value) throws IOException
	{
		stream.write((value >>> 24) & 0xFF);
		stream.write((value >>> 16) & 0xFF);
		stream.write((value >>> 8) & 0xFF);
		stream.write(value & 0xFF);
	}

	public static void writeInt(OutputStream stream, int value, int maxValue) throws IOException
	{
		if(maxValue < 1 << 8)
			writeByte(stream, value);
		else if(maxValue < 1 << 16)
			writeShort(stream, value);
		else if(maxValue < 1 << 24)
			writeInt24(stream, value);
		else
			writeInt(stream, value);
	}

	public static void writeFloat(OutputStream stream, float value) throws IOException
	{
		writeInt(stream, floatToIntBits(value));
	}

	public static void writeLong(OutputStream stream, long value) throws IOException
	{
		stream.write((byte)(value >>> 56));
		stream.write((byte)(value >>> 48));
		stream.write((byte)(value >>> 40));
		stream.write((byte)(value >>> 32));
		stream.write((byte)(value >>> 24));
		stream.write((byte)(value >>> 16));
		stream.write((byte)(value >>> 8));
		stream.write((byte)(value));
	}

	public static void writeDouble(OutputStream stream, double value) throws IOException
	{
		writeLong(stream, doubleToLongBits(value));
	}

	public static void writeEnum(OutputStream stream, Enum e) throws IOException
	{
		writeInt(stream, e == null ? -1 : e.ordinal());
	}

	public static void writeUTF(OutputStream stream, String string) throws IOException
	{
		new DataOutputStream(stream).writeUTF(string);
	}
}
