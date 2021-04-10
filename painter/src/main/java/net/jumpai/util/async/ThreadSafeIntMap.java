package net.jumpai.util.async;

import com.badlogic.gdx.utils.IntMap;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * Wrapper around a {@link IntMap} for mapping of keys with elements that do not
 * support remapping of existing keys but allow all of its methods to be
 * executed over many threads effectively using a specified
 * {@link ReadWriteLock}
 * <p>
 * Created on 2019-11-07.
 *
 * @author Alexander Winter
 */
public class ThreadSafeIntMap<V>
{
	private final IntMap<V> map;
	private final ReadWriteLock lock;

	public ThreadSafeIntMap(IntMap<V> map, ReadWriteLock lock)
	{
		ensureNotNull(map, "map");
		ensureNotNull(lock, "lock");
		this.map = map;
		this.lock = lock;
	}

	public V getOrInit(int key, V initValue)
	{
		return getOrInit(key, () -> initValue);
	}

	public V getOrInit(int key, Supplier<V> initValue)
	{
		lock.readLock().lock();

		if(map.containsKey(key))
		{
			V val = map.get(key);
			lock.readLock().unlock();
			return val;
		}

		lock.readLock().unlock();
		lock.writeLock().lock();

		if(map.containsKey(key))
		{
			V val = map.get(key);
			lock.writeLock().unlock();
			return val;
		}

		V val = initValue.get();
		map.put(key, val);
		lock.writeLock().unlock();
		return val;
	}

	public V getOrNull(int key)
	{
		lock.readLock().lock();
		V val = map.get(key);
		lock.readLock().unlock();
		return val;
	}

	public void clear()
	{
		lock.writeLock().lock();
		map.clear();
		lock.writeLock().unlock();
	}
}
