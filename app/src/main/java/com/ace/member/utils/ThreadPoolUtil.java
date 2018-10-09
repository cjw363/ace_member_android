package com.ace.member.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	private static final int MAX_POOL_SIZE = 2 * CPU_COUNT + 1;
	private static BlockingQueue<Runnable> sLinkedBlockingQueue;
	private static ThreadPoolExecutor sThreadPoolExecutor;
	private static ThreadPoolUtil sThreadPoolUtil = new ThreadPoolUtil();

	private ThreadPoolUtil() {
		sLinkedBlockingQueue = new LinkedBlockingQueue<>(128);
		sThreadPoolExecutor = initThreadPoolExecutor();
	}

	public static ThreadPoolUtil getInstance() {
		return sThreadPoolUtil;
	}

	private static ThreadPoolExecutor initThreadPoolExecutor() {
		return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 1, TimeUnit.SECONDS, sLinkedBlockingQueue);
	}

	public ThreadPoolExecutor getThreadPoolExecutor() {
		return sThreadPoolExecutor;
	}

	public void execute(Runnable runnable) {
		sThreadPoolExecutor.execute(runnable);
	}
}
