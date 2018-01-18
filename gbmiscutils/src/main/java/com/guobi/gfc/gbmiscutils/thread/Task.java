package com.guobi.gfc.gbmiscutils.thread;

public abstract class Task implements Runnable{
	private final TaskCanceller mTaskCanceller;
	
	
	protected Task(){
		mTaskCanceller = new TaskCanceller(this);
	}
		public final void cancel(){
    	mTaskCanceller.cancel();
    }
   	    
    public final boolean isCanceled(){
    	return mTaskCanceller.isCanceled();
    }
    
    public final TaskCanceller getCanceller(){
    	return mTaskCanceller;
    }
}
