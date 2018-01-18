package com.guobi.gfc.gbmiscutils.config;

import android.content.Context;
import android.util.DisplayMetrics;

public final class GBHWConfig {

	/**
	 * 按键点击超时，单位毫秒
	 * 从发生DOWN事件开始触发
	 * 若在此超时内完成一次快速滑动（FLING），且首次落点在一个按键上，
	 * 并且滑动距离没有超过SHORT_FLING_DISTANCE_MAX的话，则判定为一次点击
	 * 
	 * 该超时同时作为长按超时辅助，若系统没有发出LONGPRESS
	 * 则由此超时判定
	 */
	public static final int DOWN_EVENT_TIMEOUT = 500;//ms 
	
	/**
	 * @deprecated
	 * 短距离滑动的最大距离，小于此距离的FLING判定为点击
	 */
	public static final int SHORT_FLING_DISTANCE_MAX = 30;
	
	/**
	 * 自动长按间隔初始值
	 */
	public static final int AUTO_LONG_PRESS_DELAY = 350;//ms
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static float getShortFlingDistance(Context context){
		DisplayMetrics localDisplayMetrics =context.getResources().getDisplayMetrics();
		final int SCREEN_WIDTH = localDisplayMetrics.widthPixels;
		final int SCREEN_HEIGHT = localDisplayMetrics.heightPixels;
		return (SCREEN_WIDTH>SCREEN_HEIGHT)?(SCREEN_HEIGHT/8):(SCREEN_WIDTH/8);
	}
}
