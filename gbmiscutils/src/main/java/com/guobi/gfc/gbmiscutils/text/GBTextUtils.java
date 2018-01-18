package com.guobi.gfc.gbmiscutils.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public final  class GBTextUtils
{
	/**
	 * 
	 * @param text
	 * @param ftSize
	 * @param face
	 * @return
	 */
	public static Rect getTextBounds(	String text,
										float ftSize,
										Typeface face)
	{
		if(text==null || text.length()<=0 || ftSize<=0)
			return new Rect(0,0,0,0);
		
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(ftSize);
		if(face!=null)
			textPaint.setTypeface(face);
		
		Rect rc = new Rect();
		textPaint.getTextBounds(text,0,text.length(),rc);
		return rc;
	}
	
	/**
	 * 
	 * @param canvas
	 * @param text
	 * @param left
	 * @param top
	 * @param color
	 * @param ftSize
	 * @param face
	 * @return
	 */
	public static int drawText( Canvas		canvas,
								String		text,
								float		left,
								float		top,
								int			color,
								float		ftSize,
								Typeface	face	)
	{
		if(canvas==null)
			return 0;
		if(text==null || text.length()<=0 || ftSize<=0)
			return 0;
		
		Paint textPaint = new Paint(); 
		textPaint.setSubpixelText(true);
        textPaint.setAntiAlias(true);
        textPaint.setColor(color);
		textPaint.setTextSize(ftSize);
		if(face!=null)
			textPaint.setTypeface(face);
		
		Rect rcText = new Rect();
		textPaint.getTextBounds(text,0,text.length(),rcText);
		
		canvas.drawText(text, 
						left-rcText.left,
						top-rcText.top,
						textPaint);
		
		return rcText.width();
	}
	
	/**
	 * 
	 * @param canvas
	 * @param text
	 * @param left
	 * @param top
	 * @param textPaint
	 */
	public static void drawText( 	Canvas		canvas,
									String		text,
									float		left,
									float		top,
									Paint 		textPaint )
	{
		if(canvas==null)
			return;
		if(text==null || text.length()<=0)
			return;
			
		Rect rcText = new Rect();
		textPaint.getTextBounds(text,0,text.length(),rcText);
		
		canvas.drawText(
			text, 
			left-rcText.left,
			top-rcText.top,
			textPaint
		);
	}
	
	
	/**
	 * 
	 * @param canvas
	 * @param text
	 * @param left
	 * @param top
	 * @param color
	 * @param ftSize
	 * @param face
	 */
	public static void drawTextAtOri( 	Canvas		canvas,
										String		text,
										float		left,
										float		top,
										int			color,
										float		ftSize,
										Typeface	face	)
	{
		if(canvas==null)
			return;
		if(text==null || text.length()<=0 || ftSize<=0)
			return;
		
		Paint textPaint = new Paint(); 
		textPaint.setSubpixelText(true);
		textPaint.setAntiAlias(true);
		textPaint.setColor(color);
		textPaint.setTextSize(ftSize);
		if(face!=null)
			textPaint.setTypeface(face);
				
		canvas.drawText(text, 
						left,
						top,
						textPaint);
	}
	
	/*
	public static void drawTextAtOri( 	Canvas		canvas,
										String		text,
										float		left,
										float		top,
										Paint 		textPaint )
	{
		if(canvas==null)
			return;
		if(text==null || text.length()<=0)
			return;
		canvas.drawText(text,left,top,textPaint);
	}
	*/
	
	private GBTextUtils(){}
}
