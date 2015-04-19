package com.singlevillage.meet.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by andygao on 2014-02-20.
 */
public class ThreadUtils {
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int KEEP_ALIVE = 1;

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
		}
	};

	private static final BlockingQueue<Runnable> sPoolWorkQueue =
			new LinkedBlockingQueue<Runnable>(128);

	/**
	 * An {@link java.util.concurrent.Executor} that can be used to execute tasks in parallel.
	 */
	public static final Executor THREAD_POOL_EXECUTOR
			= new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
			TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);


}
