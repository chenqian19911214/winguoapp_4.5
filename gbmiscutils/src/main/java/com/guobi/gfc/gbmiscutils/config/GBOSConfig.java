package com.guobi.gfc.gbmiscutils.config;

import android.os.Build;

//import com.guobi.gfc.GBMiscUtils.log.GBLogUtils;

public final class GBOSConfig {

	{
		//OS_BUILD_VERSION=android.os.Build.VERSION.SDK_INT;
		//GBLogUtils.DEBUG_DISPLAY("GBOSConfig init","OS_BUILD_VERSION:"+OS_BUILD_VERSION);
	}
	
	public static final int OS_BUILD_VERSION=android.os.Build.VERSION.SDK_INT;

	public static final boolean osVerGreaterThan4()
	{
		return OS_BUILD_VERSION>=14;
	}
	
	public static boolean hasFroyo() {
	        // Can use static final constants like FROYO, declared in later versions
	        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }
    
    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }
}
