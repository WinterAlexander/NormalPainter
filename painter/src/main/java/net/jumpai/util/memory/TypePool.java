package net.jumpai.util.memory;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;

/**
 * Non static version of Pools
 * <p>
 * Stores a map of {@link Pool}s (usually {@link ReflectionPool}s) by type for convenient access.
 * <p>
 * Created on 2017-01-17.
 *
 * @author Alexander Winter
 */
@SuppressWarnings("unchecked")
public class TypePool
{
	private final ObjectMap<Class, Pool> typePools;

	public TypePool()
	{
		typePools = new ObjectMap();
	}

	/** Returns a new or existing pool for the specified type, stored in a Class to {@link Pool} map. Note the max size is ignored
	 * if this is not the first time this pool has been requested. */
	public <T> Pool<T> get (Class<T> type, int max)
	{
		Pool pool = typePools.get(type);

		if (pool == null)
		{
			pool = new ReflectionPool(type, 4, max);
			typePools.put(type, pool);
		}
		return pool;
	}

	/** Returns a new or existing pool for the specified type, stored in a Class to {@link Pool} map. The max size of the pool used
	 * is 100. */
	public <T> Pool<T> get (Class<T> type)
	{
		return get(type, 100);
	}

	/** Sets an existing pool for the specified type, stored in a Class to {@link Pool} map. */
	public <T> void set(Class<T> type, Pool<T> pool)
	{
		typePools.put(type, pool);
	}

	/** Obtains an object from the {@link #get(Class) pool}. */
	public <T> T obtain(Class<T> type)
	{
		return get(type).obtain();
	}

	/** Frees an object from the {@link #get(Class) pool}. */
	public void free(Object object)
	{
		if(object == null)
			throw new IllegalArgumentException("Object cannot be null.");

		Pool pool = typePools.get(object.getClass());

		if(pool == null)
			return; // Ignore freeing an object that was never retained.

		pool.free(object);
	}

	/** Frees the specified objects from the {@link #get(Class) pool}. Null objects within the array are silently ignored. Objects
	 * don't need to be from the same pool. */
	public void freeAll(Array objects)
	{
		freeAll(objects, false);
	}

	/** Frees the specified objects from the {@link #get(Class) pool}. Null objects within the array are silently ignored.
	 * @param samePool If true, objects don't need to be from the same pool but the pool must be looked up for each object. */
	public void freeAll (Array objects, boolean samePool)
	{
		if (objects == null)
			throw new IllegalArgumentException("Objects cannot be null.");

		Pool pool = null;

		for(int i = 0, n = objects.size; i < n; i++)
		{
			Object object = objects.get(i);

			if(object == null)
				continue;

			if(pool == null)
			{
				pool = typePools.get(object.getClass());

				if (pool == null)
					continue; // Ignore freeing an object that was never retained.
			}
			pool.free(object);

			if(!samePool)
				pool = null;
		}
	}
}
