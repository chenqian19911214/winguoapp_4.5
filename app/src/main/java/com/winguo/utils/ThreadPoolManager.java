package com.winguo.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理器(单例)
 */
public class ThreadPoolManager {

    private ThreadPoolProxy poolProxy;
    private ThreadPoolProxy shortPooProxy;
    private ExecutorService cacheThreadPool;

    private ThreadPoolManager() {
    }

    //懒汉式  饿汉式(线程安全的)
    private static ThreadPoolManager instance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    /***
     * cpu * 2 + 1
     * 1. 请求网络
     *
     *
     * @return
     */
    public ThreadPoolProxy createNetThreadPool() {
        if (poolProxy == null)
            poolProxy = new ThreadPoolProxy(9, 9, 5000);
        return poolProxy;
    }
    public ExecutorService createCacheThreadPool() {
        if (cacheThreadPool == null)
            cacheThreadPool = Executors.newCachedThreadPool();
        return cacheThreadPool;
    }

    /**
     * 读写文件
     * @return
     */
    public ThreadPoolProxy createFileThreadPool() {
        if (shortPooProxy == null)
            shortPooProxy = new ThreadPoolProxy(9, 9, 5000);
        return shortPooProxy;
    }

    /**
     * 配置线程池
     */
    public class ThreadPoolProxy {
        ThreadPoolExecutor threadPool;//线程池对象
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        /**
         * 执行任务
         */
        public void execute(Runnable runnable) {
            if (threadPool == null) {

                //corePoolSize: 初始化几个线程
                // maximumPoolSize: 最大线程数量(额外最大数量)
                threadPool = new ThreadPoolExecutor(corePoolSize,
                        maximumPoolSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>());
            }
            threadPool.execute(runnable);
        }

        public void cancel(Runnable runnable) {
            if (threadPool != null && !threadPool.isTerminated() && !threadPool.isShutdown()) {
                threadPool.remove(runnable);
            }
        }

    }
}
