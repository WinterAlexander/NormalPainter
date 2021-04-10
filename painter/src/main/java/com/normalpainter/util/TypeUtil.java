package com.normalpainter.util;

import java.lang.reflect.Array;

/**
 * Utility class to deal with java types
 * <p>
 * Created on 2017-09-05.
 *
 * @author Alexander Winter
 */
public class TypeUtil
{
	private TypeUtil() {}

	@SuppressWarnings("unchecked")
	public static <T> T defaultValue(Class<T> clazz)
	{
		return (T) Array.get(Array.newInstance(clazz, 1), 0);
	}

	public static Class<?> boxedType(Class<?> clazz)
	{
		if(clazz == int.class)
			return Integer.class;

		if(clazz == float.class)
			return Float.class;

		if(clazz == long.class)
			return Long.class;

		if(clazz == short.class)
			return Short.class;

		if(clazz == byte.class)
			return Byte.class;

		if(clazz == double.class)
			return Double.class;

		if(clazz == char.class)
			return Character.class;

		if(clazz == boolean.class)
			return Boolean.class;

		throw new IllegalArgumentException("clazz must be a primitive class");
	}

	public static boolean isPrimitiveBox(Class<?> clazz)
	{
		return clazz == Integer.class
			|| clazz == Float.class
			|| clazz == Long.class
			|| clazz == Short.class
			|| clazz == Byte.class
			|| clazz == Double.class
			|| clazz == Character.class
			|| clazz == Boolean.class;
	}
}
