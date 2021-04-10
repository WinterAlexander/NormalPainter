package com.normalpainter.util;

import com.badlogic.gdx.utils.Array;

import java.util.Collection;

/**
 * Utility class to help classes validate their inputs
 * <p>
 * Created on 2017-08-22.
 *
 * @author Alexander Winter
 */
public class Validation
{
	private Validation() {}

	/**
	 * Ensures specified integer is positive, meaning it is greater or equal than 0.
	 * Generates a meaningful exception if the condition isn't met.
	 *
	 * @param value value to check for validity
	 * @param name name of the parameter to validate
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
	public static int ensurePositive(int value, String name)
	{
		if(value < 0)
			throw new IllegalArgumentException(name + " must be a positive integer");
		return value;
	}

	/**
	 * Ensures specified integer is strictly positive, meaning it is greater than 0.
	 * Generates a meaningful exception if the condition isn't met.
	 *
	 * @param value value to check for validity
	 * @param name name of the parameter to validate
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
	public static int ensureStrictlyPositive(int value, String name)
	{
		if(value <= 0)
			throw new IllegalArgumentException(name + " must be a strictly positive integer");
		return value;
	}

	/**
	 * Ensures specified floating point is positive, meaning it is greater or equal than 0.
	 * Generates a meaningful exception if the condition isn't met.
	 *
	 * @param value value to check for validity
	 * @param name name of the parameter to validate
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
	public static float ensurePositive(float value, String name)
	{
		if(value < 0)
			throw new IllegalArgumentException(name + " must be a positive integer");
		return value;
	}

	/**
	 * Ensures specified floating point is stricly positive, meaning it is greater than 0.
	 * Generates a meaningful exception if the condition isn't met.
	 *
	 * @param value value to check for validity
	 * @param name name of the parameter to validate
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
	public static float ensureStrictlyPositive(float value, String name)
	{
		if(value <= 0)
			throw new IllegalArgumentException(name + " must be a strictly positive integer");
		return value;
	}

	/**
	 * Ensures specified long integer is positive, meaning it is greater or equal
	 * than 0. Generates a meaningful exception if the condition isn't met.
	 *
	 * @param value value to check for validity
	 * @param name name of the parameter to validate
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
	public static long ensurePositive(long value, String name)
	{
		if(value < 0)
			throw new IllegalArgumentException(name + " must be a positive long integer");
		return value;
	}

	/**
	 * Ensures specified long integer is stricly positive, meaning it is greater
	 * than 0. Generates a meaningful exception if the condition isn't met.
	 *
	 * @param value value to check for validity
	 * @param name name of the parameter to validate
	 *
	 * @throws IllegalArgumentException if validation fails
	 */
	public static long ensureStrictlyPositive(long value, String name)
	{
		if(value <= 0)
			throw new IllegalArgumentException(name + " must be a strictly positive long integer");
		return value;
	}

	/**
	 * Ensures specified integer is in specified range. Minimum is inclusive
	 * and maximum is exclusive. Generates a meaningful exception if the
	 * condition isn't met.
	 *
	 * @param value value to check
	 * @param min minimum, inclusive
	 * @param max maximum, exclusive
	 * @param name name of the parameter for exception
	 *
	 * @return parameter passed as value, if in range
	 */
	public static int ensureInRange(int value, int min, int max, String name)
	{
		if(value < min || value >= max)
			throw new IllegalArgumentException(name + " must be an integer between [" + min + ", " + max + "[");
		return value;
	}

	/**
	 * Ensures the specified floating point is in the specified range. Minimum
	 * and maximum are inclusive. Generates a meaningful exception if the
	 * condition isn't met.
	 *
	 * @param value value to check
	 * @param min minimum, inclusive
	 * @param max maximum, inclusive
	 * @param name name of the parameter for exception
	 *
	 * @return parameter passed as value, if in range
	 */
	public static float ensureInRange(float value, float min, float max, String name)
	{
		if(value < min || value > max)
			throw new IllegalArgumentException(name + " must be a floating point between [" + min + ", " + max + "]");
		return value;
	}

	/**
	 * Ensures that the value specified as parameter is in the range of
	 * the array also specified as parameter. Generates a meaningful exception
	 * using the parameter specified name is the confition isn't met.
	 *
	 * @param value value to check
	 * @param array array to ensure in bounds of
	 * @param name name of the value
	 */
	public static int ensureInBounds(int value, Object[] array, String name)
	{
		ensureInRange(value, 0, array.length, name);
		return value;
	}

	/**
	 * Ensures the specified Object is not null. Generates a meaningful exception
	 * if the condition isn't met.
	 *
	 * @param value value to check ensure is not null
	 * @param name name of the object for exception
	 */
	public static <T> T ensureNotNull(T value, String name)
	{
		if(value == null)
			throw new IllegalArgumentException(name + " cannot be null");
		return value;
	}

	public static <T> T[] ensureNoneNull(T[] array, String name)
	{
		ensureNotNull(array, name);
		for(T val : array)
			if(val == null)
				throw new IllegalArgumentException("Value of array " + name + " cannot be null");
		return array;
	}

	public static <T> T[] ensureNotEmpty(T[] array, String name)
	{
		ensureNotNull(array, name);
		if(array.length == 0)
			throw new IllegalArgumentException("Array " + name + " cannot be empty");
		return array;
	}

	public static <T extends Collection> T ensureNotEmpty(T collection, String name)
	{
		ensureNotNull(collection, name);
		if(collection.size() == 0)
			throw new IllegalArgumentException("Collection (" + collection.getClass().getName() + ") " + name + " cannot be empty");
		return collection;
	}

	public static <T extends Array> T ensureNotEmpty(T array, String name)
	{
		ensureNotNull(array, name);
		if(array.size == 0)
			throw new IllegalArgumentException("Collection (" + array.getClass().getName() + ") " + name + " cannot be empty");
		return array;
	}

	public static void ensureMatching(Class<?>[] types, Object[] objects)
	{
		if(objects.length != types.length)
			throw new IllegalArgumentException("Amount of objects mismatches. Expected " + types.length + " but only given " + objects.length);

		for(int i = 0; i < types.length; i++)
		{
			if(types[i].isAssignableFrom(objects[i].getClass()))
				continue;

			if(types[i].isPrimitive() && TypeUtil.boxedType(types[i]) == objects[i].getClass())
				continue;

			throw new IllegalArgumentException("Type of objects mismatches at index " + i);
		}
	}
}
