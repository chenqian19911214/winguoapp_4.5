package com.guobi.gfc.gbmiscutils.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

//import com.guobi.gfc.GBMiscUtils.log.GBLogUtils;

public final class GBManifestConfig {

	public static final String getMetaDataValue(Context context,String name)
	{
		try {
			ApplicationInfo appInfo = 
					context.getPackageManager().getApplicationInfo(
							context.getPackageName(),PackageManager.GET_META_DATA);
			
			if (appInfo == null || appInfo.metaData == null)
				return null;
			return appInfo.metaData.getString(name);
						
		} catch (Exception e) {
			//ignore
		}
		return null;
	}
	
	public static final int getMetaDataValueInt(Context context,String name)
	{
		try {
			ApplicationInfo appInfo = 
					context.getPackageManager().getApplicationInfo(
							context.getPackageName(),PackageManager.GET_META_DATA);
			
			if (appInfo == null || appInfo.metaData == null)
				return -1;
			return appInfo.metaData.getInt(name);
						
		} catch (Exception e) {
			//ignore
			e.printStackTrace();
		}
		return -1;
	}
}
