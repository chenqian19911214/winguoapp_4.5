package com.guobi.gfc.gbmiscutils.log;

import android.util.Log;

import java.lang.ref.WeakReference;

public final class GBLogUtils {

	public static final boolean DEBUG = true;
	public static final boolean INFO = false;
	public static final boolean WARN = false;
	public static final boolean ERROR = false;
	private static boolean  IS_SHOW = true;
	public static void setIsShow(boolean isShow) {
		IS_SHOW = isShow;
	}
	public static void DEBUG_DISPLAY(String tag, String msg){
		if (IS_SHOW)
			Log.d(tag, msg);
	}
	
	public static void DEBUG_DISPLAY(Object obj, String msg){
		if (IS_SHOW)
			DEBUG_DISPLAY(getWeakRef(obj).getClass().getSimpleName(), msg);
	}

	public static void INFO_DISPLAY(Object obj, String msg){
		if (IS_SHOW)
			INFO_DISPLAY(getWeakRef(obj).getClass().getSimpleName(), msg);
	}

	public static void INFO_DISPLAY(String tag, String msg){
		if (IS_SHOW)
			Log.i(tag, msg);
	}

	public static void WARN_DISPLAY(String tag, String msg){
		if (IS_SHOW)
			Log.w(tag, msg);
	}
	
	public static void WARN_DISPLAY(String tag, String msg,Throwable e){
		if (IS_SHOW)
			Log.w(tag, msg,e);
	}
	
	public static void WARN_DISPLAY(Object obj, String msg){
		if (IS_SHOW)
			WARN_DISPLAY(getWeakRef(obj).getClass().getSimpleName(), msg);
	}
	
	public static void WARN_DISPLAY(Object obj, String msg,Throwable e){
		if (IS_SHOW)
			WARN_DISPLAY(getWeakRef(obj).getClass().getSimpleName(), msg,e);
	}
	
	public static void ERROR_DISPLAY(Object obj, String msg){
		if (IS_SHOW)
			ERROR_DISPLAY(getWeakRef(obj).getClass().getSimpleName(), msg);
	}

	public static void ERROR_DISPLAY(String tag, String msg){
		if (IS_SHOW)
			Log.e(tag, msg);
	}

	private final static Object getWeakRef(Object obj)
	{ 
		WeakReference<Object> wr = new WeakReference<Object>(obj);
		return wr.get();
	}
}
