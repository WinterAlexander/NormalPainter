package net.jumpai.util;

/**
 * Utility class for very broad operations
 * <p>
 * Created on 2018-10-25.
 *
 * @author Alexander Winter
 */
public class ObjectUtil
{
	private ObjectUtil() {}

	/**
	 * Returns the first non null object of the specified parameters, in order
	 * @param o1 object 1
	 * @param o2 object 2
	 * @return first non null object of parameters or null if none non null
	 */
	public static <T> T coalesce(T o1, T o2)
	{
		return o1 != null ? o1 : o2;
	}

	/**
	 * Returns the first non null object of the specified parameters, in order
	 *
	 * @param o1 object 1
	 * @param o2 object 2
	 * @param o3 object 3
	 * @return first non null object of parameters or null if none non null
	 */
	public static <T> T coalesce(T o1, T o2, T o3)
	{
		return o1 != null ? o1 : (o2 != null ? o2 : o3);
	}

	/**
	 * Returns the first non null object of the specified objects
	 *
	 * @param objs objects to find non null in
	 * @return first non null object of specified objects or null if none non null
	 */
	@SafeVarargs
	public static <T> T coalesce(T... objs)
	{
		for(T object : objs)
			if(object != null)
				return object;
		return null;
	}
}
