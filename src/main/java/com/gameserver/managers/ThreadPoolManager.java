package com.gameserver.managers;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.gameserver.Config;

/**
 * @author Ramy Ibrahim
 */
public final class ThreadPoolManager
{
	private static final Logger LOGGER = Logger.getLogger(ThreadPoolManager.class.getName());
	
	private static ScheduledThreadPoolExecutor[] SCHEDULED_POOLS = new ScheduledThreadPoolExecutor[Config.SCHEDULED_THREAD_POOL_COUNT];
	private static ThreadPoolExecutor[] INSTANT_POOLS = new ThreadPoolExecutor[Config.INSTANT_THREAD_POOL_COUNT];
	private static volatile int SCHEDULED_THREAD_RANDOMIZER = 0;
	private static volatile int INSTANT_THREAD_RANDOMIZER = 0;
	
	public static void init()
	{
		for (int i = 0; i < Config.SCHEDULED_THREAD_POOL_COUNT; i++)
		{
			SCHEDULED_POOLS[i] = new ScheduledThreadPoolExecutor(Config.THREADS_PER_SCHEDULED_THREAD_POOL);
			SCHEDULED_POOLS[i].prestartAllCoreThreads();
		}
		for (int i = 0; i < Config.INSTANT_THREAD_POOL_COUNT; i++)
		{
			INSTANT_POOLS[i] = new ThreadPoolExecutor(Config.THREADS_PER_INSTANT_THREAD_POOL, Config.THREADS_PER_INSTANT_THREAD_POOL, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100000));
			INSTANT_POOLS[i].prestartAllCoreThreads();
		}
		
		scheduleAtFixedRate(() ->
		{
			for (ScheduledThreadPoolExecutor threadPool : SCHEDULED_POOLS)
			{
				threadPool.purge();
			}
			for (ThreadPoolExecutor threadPool : INSTANT_POOLS)
			{
				threadPool.purge();
			}
		}, 600000, 600000);
		
		LOGGER.info("Initialized " + Config.SCHEDULED_THREAD_POOL_COUNT + " scheduled pool executors with " + (Config.SCHEDULED_THREAD_POOL_COUNT * Config.THREADS_PER_SCHEDULED_THREAD_POOL) + " total threads.");
		LOGGER.info("Initialized " + Config.INSTANT_THREAD_POOL_COUNT + " instant pool executors with " + (Config.INSTANT_THREAD_POOL_COUNT * Config.THREADS_PER_INSTANT_THREAD_POOL) + " total threads.");
	}
	
	/**
	 * Creates and executes a one-shot action that becomes enabled after the given delay.
	 * @param runnable : the task to execute.
	 * @param delay : the time from now to delay execution.
	 * @return a ScheduledFuture representing pending completion of the task and whose get() method will return null upon completion.
	 */
	public static ScheduledFuture<?> schedule(Runnable runnable, long delay)
	{
		try
		{
			return SCHEDULED_POOLS[SCHEDULED_THREAD_RANDOMIZER++ % Config.SCHEDULED_THREAD_POOL_COUNT].schedule(new TaskWrapper(runnable), delay, TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Creates and executes a periodic action that becomes enabled first after the given initial delay.
	 * @param runnable : the task to execute.
	 * @param initialDelay : the time to delay first execution.
	 * @param period : the period between successive executions.
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation.
	 */
	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period)
	{
		try
		{
			return SCHEDULED_POOLS[SCHEDULED_THREAD_RANDOMIZER++ % Config.SCHEDULED_THREAD_POOL_COUNT].scheduleAtFixedRate(new TaskWrapper(runnable), initialDelay, period, TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Executes the given task sometime in the future.
	 * @param runnable : the task to execute.
	 */
	public static void execute(Runnable runnable)
	{
		try
		{
			INSTANT_POOLS[INSTANT_THREAD_RANDOMIZER++ % Config.INSTANT_THREAD_POOL_COUNT].execute(new TaskWrapper(runnable));
		}
		catch (Exception e)
		{
		}
	}
	
	public static void shutdown()
	{
		try
		{
			LOGGER.info("ThreadPoolManager: Shutting down.");
			for (ScheduledThreadPoolExecutor threadPool : SCHEDULED_POOLS)
			{
				threadPool.shutdownNow();
			}
			for (ThreadPoolExecutor threadPool : INSTANT_POOLS)
			{
				threadPool.shutdownNow();
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	static class TaskWrapper implements Runnable
	{
		private final Runnable _runnable;
		
		public TaskWrapper(Runnable runnable)
		{
			_runnable = runnable;
		}
		
		@Override
		public void run()
		{
			try
			{
				_runnable.run();
			}
			catch (Throwable t)
			{
				final Thread thread = Thread.currentThread();
				final UncaughtExceptionHandler h = thread.getUncaughtExceptionHandler();
				if (h != null)
				{
					h.uncaughtException(thread, t);
				}
			}
		}
	}
}