package com.guobi.gfc.gbmiscutils.res;

import com.guobi.gfc.gbmiscutils.config.GBOSConfig;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;


public final class GBDensityState {
	private static final String TAG="LauncherAppState";

	private final Context mContext;
	private final boolean mIsScreenLarge;
	private final float mScreenDensity;
	private final int mIconDpi;
	private static GBDensityState _instance;
		
	public static final GBDensityState createInstance(final Context context){
		if(_instance==null){
			_instance = new GBDensityState(context.getApplicationContext());
		}
		return _instance;
	}
	
	public static final GBDensityState getInstance(){
		return _instance;
	}
	
	public static final void destroyInstance(){
		if(_instance!=null){
			_instance.trash();
			_instance=null;
		}
	}
	
	
	private GBDensityState(final Context appContext){
		mContext=appContext;
				
		
		final Resources res = appContext.getResources(); 
		final DisplayMetrics dm = res.getDisplayMetrics();
		final int screenSize = res.getConfiguration().screenLayout &
				Configuration.SCREENLAYOUT_SIZE_MASK;
		mIsScreenLarge = screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
	            screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
		mScreenDensity = res.getDisplayMetrics().density;
		
		
		if(GBOSConfig.OS_BUILD_VERSION>=11){ // from Launcher3
			if(GBLogUtils.DEBUG){
				GBLogUtils.DEBUG_DISPLAY(TAG,"### initIconDpi with API11 way");
			}
			 
			ActivityManager activityManager =
				(ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
			mIconDpi = activityManager.getLauncherLargeIconDensity();
		}else{ // from Launcher 2
			if(GBLogUtils.DEBUG){
				GBLogUtils.DEBUG_DISPLAY(TAG,"### initIconDpi with older way");
			}
			 
			final int density = dm.densityDpi; 	
	        if (mIsScreenLarge) {
	        	if (density == DisplayMetrics.DENSITY_LOW) {
	                mIconDpi = DisplayMetrics.DENSITY_MEDIUM;
	            } else if (density == DisplayMetrics.DENSITY_MEDIUM) {
	                mIconDpi = DisplayMetrics.DENSITY_HIGH;
	            } else if (density == DisplayMetrics.DENSITY_HIGH) {
	                mIconDpi = DisplayMetrics.DENSITY_XHIGH;
	            } else if (density == DisplayMetrics.DENSITY_XHIGH) {
	                // We'll need to use a denser icon, or some sort of a mipmap
	                mIconDpi = DisplayMetrics.DENSITY_XHIGH;
	            }
	        	//?? which value here??? BUG from launcher2
	            else{
	            	mIconDpi = density;
	            }
	        } else {
	            mIconDpi = density;
	        }
		}
	}
	
	private final void trash(){
		
	}
	
	public final boolean isScreenLarge(){
		return mIsScreenLarge;
	}
	
	public final float getScreenDensity(){
		return mScreenDensity;
	}

	public final int getIconDpi(){
		return mIconDpi;
	}
}
