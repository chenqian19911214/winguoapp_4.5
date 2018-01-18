package com.guobi.gfc.gbmiscutils.intent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.guobi.gfc.gbmiscutils.config.GBManifestConfig;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class GBIntentUtils {

	public static final Intent createIntentByAction(Context context,String action)
	{
		try {
					
			String intentName = GBManifestConfig.getMetaDataValue(context, action);
			if(intentName==null || intentName.length()<=0)
				return null;
			
			Class<?> clazz = Class.forName(intentName).newInstance().getClass();
			if(clazz==null)
				return null;
			
			return new Intent(context, clazz);
				
		} catch (Exception e) {
			//ignore
		}
		return null;
	}
	
	/**
	 * 这种方法比较猥琐，难道没有其他方法？
	 * @param context
	 * @param pkgName
	 * @return
	 */
	public static boolean isApkInstalled(Context context,String pkgName){
		try{
			context.getPackageManager().getPackageInfo(pkgName,0);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	public static final Intent getInstallIntent(String path){
		chmodPath(path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(
				//Uri.parse(path)
				Uri.fromFile(new File(path))
				,"application/vnd.android.package-archive");
		return intent;
	}
	
	public static final void startLaunchActivityForPackage(Context context, String pkgName ){
		try{
			context.startActivity(context.getPackageManager().getLaunchIntentForPackage(pkgName));	
		}catch(Exception e){
			
		}
	}
	
	public static final void startInstallApk(Context context,String path){
		try{
			chmodPath(path); 
			context.startActivity(getInstallIntent(path));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void chmodPath(String path){
		if (path.startsWith("/data")) {
			int index = path.lastIndexOf("/");
			String dirPath = path.substring(0, index);
			String[] args0 = { "chmod", "705", dirPath };  
			String result = exec(args0);
			String[] args1 = { "chmod", "777", path};  
			result = exec(args1);
		}
	}
	
	
	
	public static String exec(String[] args) {  
	    String result = "";  
	    ProcessBuilder processBuilder = new ProcessBuilder(args);  
	    Process process = null;  
	    InputStream errIs = null;  
	    InputStream inIs = null;  
	    try {  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        int read = -1;  
	        process = processBuilder.start();  
	        errIs = process.getErrorStream();  
	        while ((read = errIs.read()) != -1) {  
	            baos.write(read);  
	        }  
	        baos.write('\n');  
	        inIs = process.getInputStream();  
	        while ((read = inIs.read()) != -1) {  
	            baos.write(read);  
	        }  
	        byte[] data = baos.toByteArray();  
	        result = new String(data);  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (errIs != null) {  
	                errIs.close();  
	            }  
	            if (inIs != null) {  
	                inIs.close();  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        if (process != null) {  
	            process.destroy();  
	        }  
	    }  
	    return result;  
	}
}
