package com.guobi.gfc.gbmiscutils.file;

import java.io.File;
import android.os.Environment;

public final class GBExternalFileHelper extends GBFileHelper{

	/**
	 * 
	 */
	public GBExternalFileHelper()
	{
	}
	

	
	@Override
	public final String getRootDir()
	{
		return getRootDirInner();
	}
	
	public final static boolean isSDCardInserted(){
		return getRootDirInner()!=null;
	}
	
	private final static String getRootDirInner(){
		final String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED))
		{
			File path = Environment.getExternalStorageDirectory();
			if(path!=null)
				return path.getPath();
		}
		return null;
	}
}
