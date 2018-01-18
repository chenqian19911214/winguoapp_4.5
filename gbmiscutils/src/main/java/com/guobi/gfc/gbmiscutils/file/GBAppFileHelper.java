package com.guobi.gfc.gbmiscutils.file;

import android.content.Context;

public final class GBAppFileHelper extends GBFileHelper{

	/**
	 * 
	 * @param appContext
	 * @param onExternalStorage
	 */
	public GBAppFileHelper(Context appContext)
	{
		mAppFilesRootDirPath=appContext.getFilesDir().getAbsolutePath();
	}
	

	@Override
	public final String getRootDir() {
		// TODO Auto-generated method stub
		return mAppFilesRootDirPath;
	}
	
	private final String mAppFilesRootDirPath;
}
