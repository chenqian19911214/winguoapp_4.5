package com.winguo.utils;

import android.os.Handler;


/**
 *
 * 1. 在子线程中执行
 * 2.  在主线程执行
 */
public class ThreadUtils {

    /**
     * 在子线程执行的方法
     * @param runnable
     */
    public static void runOnBackThread(Runnable runnable){

        ThreadPoolManager.getInstance().createNetThreadPool().execute(runnable);
    }

    private static Handler handler = new Handler();

    /**
     * 主线程执行
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable){
        handler.post(runnable);
    }

    /**
     * 在缓存子线程执行的方法
     * @param runnable
     */
    public static void runOnBackCacheThread(Runnable runnable){

        ThreadPoolManager.getInstance().createCacheThreadPool().submit(runnable);
    }


}
