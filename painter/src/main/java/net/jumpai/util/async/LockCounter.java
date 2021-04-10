package net.jumpai.util.async;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link Lock} that counts how many threads are trying to get in the lock.
 * Very useful for debugging but shouldn't be used for production code.
 * <p>
 * Created on 2019-11-07.
 *
 * @author Alexander Winter
 */
public class LockCounter implements Lock
{
	private final Lock lock;
	private final ArrayList<Thread> threads = new ArrayList<>();

	public LockCounter(Lock lock)
	{
		ensureNotNull(lock, "lock");
		this.lock = lock;
	}

	@Override
	public void lock()
	{
		lock.lock();
		threads.add(Thread.currentThread());
	}

	@Override
	public void lockInterruptibly() throws InterruptedException
	{
		lock.lockInterruptibly();
		threads.add(Thread.currentThread());
	}

	@Override
	public boolean tryLock()
	{
		boolean res = lock.tryLock();

		if(res)
			threads.add(Thread.currentThread());

		return res;
	}

	@Override
	public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException
	{
		boolean res = lock.tryLock(l, timeUnit);

		if(res)
			threads.add(Thread.currentThread());

		return res;
	}

	@Override
	public void unlock()
	{
		lock.unlock();
		threads.remove(Thread.currentThread());
	}

	@Override
	public Condition newCondition()
	{
		return lock.newCondition();
	}

	public int getCount()
	{
		return threads.size();
	}
}
