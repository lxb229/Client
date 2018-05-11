package com.guse.platform.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadPoolUtil {
	
	/**
	 * 创建一个线程池
	 * @param size 线程数
	 * @param threadName 线程名
	 * @param daemon 是否是守护线程
	 * @return
	 */
	public static final ExecutorService createThreadPool(int size, final String threadName, final boolean daemon) {
	    ExecutorService es = Executors.newFixedThreadPool(size, new ThreadFactory() {
	      int i = 0;

	      @Override
	      public Thread newThread(Runnable r) {
	        Thread t;
	        if (null == threadName || "".equals(threadName.trim())) {
	          t = new Thread(r, "ThreadPool-" + (i++));
	        } else {
	          t = new Thread(r, threadName + "-" + (i++));
	        }
	        if (daemon) {
	          t.setDaemon(daemon);
	        }
	        return t;
	      }
	    });
	    return es;
	  }

}
