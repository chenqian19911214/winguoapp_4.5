package com.guobi.gfc.gbmiscutils.res;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import com.guobi.gfc.gbmiscutils.config.GBOSConfig;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public final class GBResourceUtils
{
	
	public static XmlPullParser getXmlFromFile(Context context, String filePath) {
		// TODO Auto-generated method stub
		FileInputStream in = null;
		try {
			in = new FileInputStream(filePath);
			XmlPullParserFactory pullParserFactory = XmlPullParserFactory
					.newInstance();
			XmlPullParser pullParser = pullParserFactory.newPullParser();

			if (GBOSConfig.hasIceCreamSandwich()) {
				pullParser.setInput(new BOMInputStream(in, false,
						ByteOrderMark.UTF_8), "UTF-8");
			} else {
				byte[] bs = new byte[in.available()];
				in.read(bs);
				String str = new String(bs, "UTF-8");
				pullParser.setInput(new StringReader(str));
			}
			return pullParser;
		} catch (Exception e) {
			if (GBLogUtils.DEBUG) {
				e.printStackTrace();
			}
		} catch (OutOfMemoryError e) {
			if (GBLogUtils.DEBUG) {
				e.printStackTrace();
			}
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if (GBLogUtils.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	/////////////////////////////////////////////////////////////////////
	// static version
	/////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param resources
	 * @param pkgName
	 * @param resType
	 * @param resName
	 * @return
	 */
	public static int getResID(Resources resources,String pkgName,String resType,String resName) 
	{
		if(isValidParam(resName)==false)
			return -1;
			
		try{
			final int resID = resources.getIdentifier(
				resName,
				resType,
				pkgName
			);
			
			return resID;
		}
		catch(Exception ex)
		{
		}
		return -1;
	}
	
	/**
	 * 
	 * @param context
	 * @param resType
	 * @param resName
	 * @return
	 */
	public static int getResID(Context context,String resType,String resName) 
	{
		return getResID(
			context.getResources(),
			context.getPackageName(),
			resType,
			resName
		);
	}
	
	/**
	 * 
	 * @param resources
	 * @param pkgName
	 * @param resName
	 * @return
	 */
	public static boolean getColorAble(Resources resources,String pkgName,String resName) {
		return (getResID(resources,pkgName,"color",resName)>0);
	}
	
	
	/**
	 * 
	 * @param context
	 * @param resName
	 * @return
	 */
	public static boolean getColorAble(Context context,String resName) {
		
		return getColorAble(
			context.getResources(),
			context.getPackageName(),
			resName
		);
	}
	
	/**
	 * 
	 * @param resources
	 * @param pkgName
	 * @param resName
	 * @return
	 */
	public static int getColor(Resources resources,String pkgName,String resName) {
		
		final int resID = getResID(resources,pkgName,"color",resName);
		if(resID<=0)
			return Color.BLACK;
		
		try
		{
			return resources.getColor(resID);
		}
		catch(Exception ex)
		{
		}
		catch (OutOfMemoryError err){
			
		}
		return Color.BLACK;
	}
	
	/**
	 * 
	 * @param context
	 * @param resName
	 * @return
	 */
	public static int getColor(Context context,String resName) {
		
		return getColor(
			context.getResources(),
			context.getPackageName(),
			resName
		);
	}
	
	/**
	 * 
	 * @param resources
	 * @param pkgName
	 * @param resName
	 * @return
	 */
	public static String getString(Resources resources,String pkgName,String resName)
	{
		final int resID = getResID(resources,pkgName,"string",resName);
		if(resID<=0)
			return null;
		
		try
		{
			return resources.getString(resID);
		}
		catch(Exception ex)
		{
		}
		catch (OutOfMemoryError err){
			
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param context
	 * @param resName
	 * @return
	 */
	public static String getString(Context context,String resName)
	{
		return getString(
			context.getResources(),
			context.getPackageName(),
			resName
		);
	}
	
	/**
	 *
	 * @param context
	 * @param resID
	 * @return
	 */
	public static String getString(Context context,int resID)
	{
		if(resID<=0)
			return null;
		
		// TODO Auto-generated method stub
		try{
			return context.getResources().getString(resID);
		}
		catch(Exception ex)
		{
		}
		catch (OutOfMemoryError err){
			
		}
		return null;
	}
	
	/**
	 * 
	 * @param resources
	 * @param pkgName
	 * @param resName
	 * @return
	 */
	public static Bitmap getBitmap(Resources resources,String pkgName,String resName) {
		
		final int resID = getResID(resources,pkgName,"drawable",resName);
		if(resID<=0)
			return null;
		try{
			return BitmapFactory.decodeResource(resources,resID);
		}
		catch(Exception ex){
        	
        }
        catch (OutOfMemoryError err){
			
		}
        return null;
	}
	
	/**
	 * @param context
	 * @param resName
	 * @return
	 */
	public static Bitmap getBitmap(Context context,String resName) {
		
		return getBitmap(
			context.getResources(),
			context.getPackageName(),
			resName
		);
	}
	
	/**
	 * 
	 * @param resources
	 * @param resID
	 * @return
	 */
	public static Bitmap getBitmapEx(Resources resources,int resID) {
		
		if(resources==null)
			return null;
		if(resID<=0)
			return null;
		
		BitmapFactory.Options opt = new BitmapFactory.Options();  
        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
        opt.inPurgeable = true;  
        opt.inInputShareable = true;  
        try
        {
          	InputStream is = resources.openRawResource(resID);  
          	return BitmapFactory.decodeStream(is,null,opt);  
        }
        catch(Exception ex){
        	
        }
        catch (OutOfMemoryError err){
			
		}
        return null;
    }
	
	/**
	 * 
	 * @param resources
	 * @param pkgName
	 * @param resName
	 * @return
	 */
	public static Bitmap getBitmapEx(Resources resources,String pkgName,String resName) {
		
		final int resID = getResID(resources,pkgName,"drawable",resName);
		if(resID<=0)
			return null;
		return getBitmapEx(resources,resID);
	}
	
	/**
	 * 
	 * @param context
	 * @param resName
	 * @return
	 */
	public static Bitmap getBitmapEx(Context context,String resName) {
		
		return getBitmapEx(
			context.getResources(),
			context.getPackageName(),
			resName
		);
	}
	
	/**
	 * 
	 * @param resources
	 * @param pkgName
	 * @param resName
	 * @return
	 */
	public static Drawable getDrawable(Resources resources,String pkgName,String resName) {
		
		final int resID = getResID(resources,pkgName,"drawable",resName);
		if(resID<=0)
			return null;
				
		try{
			return resources.getDrawable(resID);
		}
		catch(Exception ex){
		}
		catch (OutOfMemoryError err){
			
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param context
	 * @param resName
	 * @return
	 */
	public static Drawable getDrawable(Context context,String resName) {
		
		return getDrawable(
			context.getResources(),
			context.getPackageName(),
			resName
		);
	}
	
	/**
	 * 
	 * @param resources
	 * @param pkgName
	 * @param resName
	 * @return
	 */
	public static byte[] getRawData(Resources resources,String pkgName,String resName)
	{
		final int resID = getResID(resources,pkgName,"raw",resName);
		if(resID<=0)
			return null;
		return readRawData(resources,resID);
	}
	
	/**
	 * 
	 * @param resources
	 * @param pkgName
	 * @param resName
	 * @return
	 */
	public static final XmlResourceParser getXml(Resources resources,String pkgName,String resName)
	{
		final int resID = getResID(resources,pkgName,"xml",resName);
		if(resID<=0)
			return null;
		try
		{
			return resources.getXml(resID);
		}
		catch(Exception ex){
		}
		catch (OutOfMemoryError err){
			
		}
		return null;
	}
	
	/**
	 * 
	 * @param context
	 * @param resName
	 * @return
	 */
	public static byte[] getRawData(Context context,String resName)
	{
		return getRawData(
			context.getResources(),
			context.getPackageName(),
			resName
		);
	}
	
	/**
	 * 
	 * @param resources
	 * @param pkgName
	 * @param resName
	 * @param defVal
	 * @return
	 */
	public static float getDimension(
			Resources resources,
			String pkgName,
			String resName,
			float defVal)
	{
		
		if(isValidParam(resName)==false)
			return defVal;
		
		// TODO Auto-generated method stub
		
		try{
			final int resID = resources.getIdentifier(
				resName,
				"dimen",
				pkgName
			);
			
			if(resID<=0)
				return defVal;
			
			return resources.getDimension(resID);
		}
		catch(Exception ex)
		{
		
		}
		return defVal;
	}
	
	/**
	 * 
	 * @param context
	 * @param
	 * @param resName
	 * @param defVal
	 * @return
	 */
	public static float getDimension(
			Context context,
			String resName,
			float defVal)
	{
		
		return getDimension(
				context.getResources(),
				context.getPackageName(),
				resName,
				defVal
		);
	}
	
	/**
	 * 
	 * @param context
	 * @param tag
	 * @param resName
	 * @param defVal
	 * @return
	 */
	public static int getDimensionPixel(
			Resources resources,
			String pkgName,
			String resName,
			int defVal)
	{
		
		if(isValidParam(resName)==false)
			return defVal;
				
	
		try{
			final int resID = resources.getIdentifier(
				resName,
				"dimen",
				pkgName
			);
			
			if(resID<=0)
				return defVal;
			
			return resources.getDimensionPixelSize(resID);
		}
		catch(Exception ex)
		{
		
		}
		return defVal;
	}
	
	/**
	 * 
	 * @param context
	 * @param tag
	 * @param resName
	 * @param defVal
	 * @return
	 */
	public static int getDimensionPixel(
			Context context,
			String resName,
			int defVal)
	{
		
		return getDimensionPixel(
			context.getResources(),
			context.getPackageName(),
			resName,
			defVal
		);
	}
	
	/////////////////////////////////////////////////////////////////////
	// 
	/////////////////////////////////////////////////////////////////////
	
	
	private static final boolean isValidParam(String resName)
	{
		if(resName==null||resName.length()<=0)
			return false;
		return true;
	}
	
	
	private static byte[] readRawData(Resources res, int id) {
		try {

			InputStream is = res.openRawResource(id);
			if (is == null) {
				return null;
			}

			int length = is.available();
			if (length <= 0) {
				return null;
			}

			byte[] buf = new byte[length];

			if (is.read(buf, 0, length) == -1) {
				return null;
			}

			is.close();

			return buf;

		} catch (Exception ex) {
			if(GBLogUtils.DEBUG)
				ex.printStackTrace();
		}
		catch (OutOfMemoryError err){
			
		}

		return null;
	}
	
	/////////////////////////////////////////////////////////////////////
	// 
	/////////////////////////////////////////////////////////////////////
	
	private GBResourceUtils(){}
}
