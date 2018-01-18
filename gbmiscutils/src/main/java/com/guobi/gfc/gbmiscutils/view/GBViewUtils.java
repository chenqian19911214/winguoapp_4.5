package com.guobi.gfc.gbmiscutils.view;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import android.graphics.Rect;
import android.view.View;

public final  class GBViewUtils
{
	public static void dumpMeasureSpec(String prefix, int spec)
	{
		GBLogUtils.DEBUG_DISPLAY(prefix,View.MeasureSpec.toString(spec));
	}

	public static void dumpViewGlobalRect(View view)
	{
		Rect rc = new Rect();
		
		/// !!! fuck below doesn't works, always return local cood
		view.getGlobalVisibleRect(rc);
		GBLogUtils.DEBUG_DISPLAY(view,
				"Dumping View's location in parent:" 
				+"left:" + rc.left 
				+",top:" + rc.top 
				+",right:" + rc.right 
				+",bottom:" + rc.bottom 
				); 
		
		/// Below function will return global cood on screen 
		/// Top status bar's height is computed in.
		int location[] = new int[2];
		view.getLocationOnScreen(location);
		GBLogUtils.DEBUG_DISPLAY(view,
				"Dumping View's location on whole screen:" 
				+"left:" + location[0]//rc.left 
				+",top:" + location[1]//rc.top 
				//+",right:" + rc.right 
				//+",bottom:" + rc.bottom 
				); 
		
		
		view.getLocationInWindow(location);
		GBLogUtils.DEBUG_DISPLAY(view,
				"Dumping View's location in its window:" 
				+"left:" + location[0]//rc.left 
				+",top:" + location[1]//rc.top 
				//+",right:" + rc.right 
				//+",bottom:" + rc.bottom 
				); 
	}

	/**
	 * 
	 * @param view
	 * @return
	 */
	public static Rect getGlobalRectOnScreen(View view)
	{
		int location[] = new int[2];
		view.getLocationOnScreen(location);
		Rect rc = new Rect(	location[0],//left
							location[1],//top
							location[0] + view.getWidth(),//right
							location[1] + view.getHeight()//bottom
							);
		return rc;
	}
	
	private GBViewUtils(){}
}
