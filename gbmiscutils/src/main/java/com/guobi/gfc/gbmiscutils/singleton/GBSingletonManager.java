package com.guobi.gfc.gbmiscutils.singleton;

import java.util.LinkedList;
import java.util.List;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

public final class GBSingletonManager {
	
	public static final String TAG="GBSingletonManager";

	public static final void register(GBSingleton instance)
	{
		if(instance==null)
			return;
		if(mInstanceList.contains(instance))
			return;
		mInstanceList.add(instance);
	}
	
	public static final void trash()
	{
		if(GBLogUtils.DEBUG)
			GBLogUtils.DEBUG_DISPLAY(TAG,"trash begin...");
		
		for(GBSingleton instance:mInstanceList)
			instance.trash();
				
		if(GBLogUtils.DEBUG)
			GBLogUtils.DEBUG_DISPLAY(TAG,"trash end.");
	}
	
	public static final void term()
	{
		if(GBLogUtils.DEBUG)
			GBLogUtils.DEBUG_DISPLAY(TAG,"term begin...");
		
		mInstanceList.clear();
		
		if(GBLogUtils.DEBUG)
			GBLogUtils.DEBUG_DISPLAY(TAG,"term end.");
	}
	
	private GBSingletonManager(){}

	private static final List<GBSingleton> mInstanceList = new LinkedList<GBSingleton>();
}
