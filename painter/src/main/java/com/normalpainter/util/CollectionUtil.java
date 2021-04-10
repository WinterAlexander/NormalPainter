package com.normalpainter.util;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.Predicate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * Utility class for collections
 * <p>
 * Created on 2016-12-20.
 *
 * @author Alexander Winter
 */
public class CollectionUtil
{
	private static final Random DEFAULT_RANDOM = new Random();

	public static int sizeOf(Iterable<?> iterable)
	{
		int count = 0;
		for(Object ignored : iterable)
			count++;
		return count;
	}

	public static int sizeOf(Iterator<?> iterator)
	{
		int count = 0;
		for(; iterator.hasNext(); iterator.next())
			count++;
		return count;
	}

	public static <T> T last(T[] array)
	{
		return array[array.length - 1];
	}

	public static <T> T last(Array<T> array)
	{
		return array.get(array.size - 1);
	}

	/**
	 * Returns a random element in the specified array. NOT THREAD SAFE. Use
	 * {@link #random(Random, T[])} for thread safe version
	 *
	 * @param array array of elements to pick from
	 * @param <T> type of elements
	 * @return a random element in the array
	 */
	public static <T> T random(T[] array)
	{
		return random(DEFAULT_RANDOM, array);
	}

	public static <T> T random(Random random, T[] array)
	{
		return array[random.nextInt(array.length)];
	}

	public static <T> int indexOf(T[] array, T element)
	{
		for(int i = 0; i < array.length; i++)
			if(Objects.equals(element, array[i]))
				return i;

		return -1;
	}

	public static <T> boolean any(Iterable<T> array, Predicate<T> predicate)
	{
		for(T element : array)
			if(predicate.evaluate(element))
				return true;

		return false;
	}

	public static <T> boolean all(Iterable<T> array, Predicate<T> predicate)
	{
		for(T element : array)
			if(!predicate.evaluate(element))
				return false;

		return true;
	}

	public static <T> boolean any(T[] array, Predicate<T> predicate)
	{
		for(T element : array)
			if(predicate.evaluate(element))
				return true;

		return false;
	}

	public static <T> boolean all(T[] array, Predicate<T> predicate)
	{
		for(T element : array)
			if(!predicate.evaluate(element))
				return false;

		return true;
	}

	public static <T> Array<T> toArray(Iterable<T> iterable)
	{
		return toArray(iterable.iterator());
	}

	public static <T> Array<T> toArray(Iterator<T> iterator)
	{
		Array<T> array = new Array<>();
		while(iterator.hasNext())
			array.add(iterator.next());
		return array;
	}

	@SafeVarargs
	public static <T> T[] mergeWithArray(T[] arrayA, T... arrayB)
	{
		return mergeArrays(arrayA, arrayB);
	}

	@SafeVarargs
	public static <T> T[] mergeWithArray(Class<?> type, T[] arrayA, T... arrayB)
	{
		return mergeArrays(type, arrayA, arrayB);
	}

	@SafeVarargs
	public static <T> T[] mergeArrays(T[]... arrays)
	{
		return mergeArrays(arrays[0].getClass().getComponentType(), arrays);
	}

	@SafeVarargs
	public static <T> T[] mergeArrays(Class<?> type, T[]... arrays)
	{
		if(arrays.length == 0)
			throw new IllegalArgumentException("array of array mustn't be empty");

		int totalLength = 0;

		for(T[] array : arrays)
			totalLength += array.length;

		@SuppressWarnings("unchecked")
		T[] result = (T[]) java.lang.reflect.Array.newInstance(type, totalLength);

		int index = 0;
		for(T[] array : arrays)
		{
			System.arraycopy(array, 0, result, index, array.length);
			index += array.length;
		}

		return result;
	}

	@SafeVarargs
	public static <T extends Enum<T>> T[] valuesExcept(Class<T> enumClass, T... values)
	{
		Array<T> constants  = new Array<>(enumClass.getEnumConstants());
		constants.removeAll(new Array<>(values), false);
		return constants.toArray(enumClass);
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] except(Class<T> type, T[] array, T element)
	{
		int index = indexOf(array, element);
		if(index == -1)
			return array;

		T[] cpy = (T[])java.lang.reflect.Array.newInstance(type, array.length - 1);

		for(int i = 0; i < array.length; i++)
			if(i != index)
				cpy[i > index ? i - 1 : i] = array[i];

		return cpy;
	}

	public static <T> void sort(Array<T> array, Comparator<T> comparator)
	{
		Arrays.sort(array.items, 0, array.size, comparator);
	}

	public static <K, V> void putAll(IdentityMap<K, V> into, IdentityMap<? extends K, ? extends V> content)
	{
		into.ensureCapacity(content.size);
		for(Entry<? extends K, ? extends V> entry : content)
			into.put(entry.key, entry.value);
	}

	public static Object[] toObjectArray(int[] intArray)
	{
		Object[] res = new Object[intArray.length];

		for(int i = 0; i < intArray.length; i++)
			res[i] = intArray[i];

		return res;
	}

	public static <K, V> ObjectMap<K, V> buildMap(K[] keys, Function<K, V> builder)
	{
		ObjectMap<K, V> map = new ObjectMap<>(keys.length);
		for(K key : keys)
			map.put(key, builder.apply(key));
		return map;
	}

	public static <K, V> ObjectMap<K, V> buildMap(K[] keys, V[] values)
	{
		if(keys.length != values.length)
			throw new IllegalArgumentException("Number of keys and values mismatch");

		ObjectMap<K, V> map = new ObjectMap<>(keys.length);
		for(int i = 0; i < keys.length; i++)
			map.put(keys[i], values[i]);

		return map;
	}
}
