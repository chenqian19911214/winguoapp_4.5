package com.guobi.gfc.gbmiscutils.thread;

import android.os.Handler;
import android.os.HandlerThread;

public final class RunnableBus {
	private static final String TAG=RunnableBus.class.getSimpleName();
	private static RunnableBus _instance;
	private final HandlerThread mWorkerThread;
	private final Handler mWorkerHandler;
	private final DeferredHandler mMainHandler;

	
	public static final RunnableBus createInstance(){
		if(_instance==null){
			_instance = new RunnableBus();
		}
		return _instance;
	}
	
	public static final RunnableBus getInstance(){
		return _instance;
	}
	
	public static final void destroyInstance(){
		if(_instance!=null){
			_instance.trash();
			_instance=null;
		}
	}
	
	/**
	 * 把一个R投递到工作线程队列执行
	 * @param r
	 */
	public final void postWorker(final Runnable r){
		mWorkerHandler.post(r);
	}
	
	/**
	 * 
	 * @param r
	 * @param ms
	 */
	public final void postWorkerDelay(final Runnable r,long ms){
		mWorkerHandler.postDelayed(r,ms);
	}
	
	/**
	 * 把一个R投递到主线程的队列运行
	 * @param r
	 */
	public final void postMain(final Runnable r){
		mMainHandler.post(r);
	}
	
	/**
	 * 当主线程空闲时，这个R就会被执行
	 * 当然这个R也是运行在主线程
	 * @param r
	 */
	public final void postMainIdle(final Runnable r){
		mMainHandler.postIdle(r);
	}
	
	/**
	 * 
	 * @param r
	 */
	public final void removeWorker(final Runnable r){
		mWorkerHandler.removeCallbacks(r);
	}
	
	/**
	 * 
	 * @param r
	 */
	public final void removeMain(final Runnable r){
		mMainHandler.cancelRunnable(r);
	}
	
	private RunnableBus(){
		mWorkerThread = new HandlerThread(TAG);
		mWorkerThread.setPriority(Thread.MIN_PRIORITY);
		mWorkerThread.start();
		mWorkerHandler = new Handler(mWorkerThread.getLooper());
		mMainHandler = new DeferredHandler();
	}
	
	private final void trash(){
		mWorkerHandler.removeCallbacksAndMessages(null);
		mWorkerThread.quit();
		mMainHandler.cancel();
	}
}
