package com.guobi.gfc.gbmiscutils.thread;


/**
 * 
 * @author Tan LingHui
 *
 */
public final class TaskCanceller {
	private final Task mTask;
	private boolean mIsCanceled;
	public TaskCanceller(Task t){
		mTask=t;
	}
	public final boolean isCanceled(){
		synchronized (mTask){
			return mIsCanceled;
		}
	}
	public final void cancel(){
		synchronized (mTask){
			mIsCanceled=true;
			mTask.notify(); // 唤醒正在等待的任务
		}
	}
}
