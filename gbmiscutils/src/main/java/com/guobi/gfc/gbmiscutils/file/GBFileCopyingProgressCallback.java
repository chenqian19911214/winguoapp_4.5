package com.guobi.gfc.gbmiscutils.file;

public abstract interface GBFileCopyingProgressCallback {

	/**
	 * 
	 * @param totalSize
	 */
	public abstract void onCopyBegin();
	
	/**
	 * 
	 * @param bytesWrote
	 */
	public abstract void onCopyProgress(long bytesWrote);
	
	/**
	 * 
	 */
	public abstract void onCopyEnd();
}
