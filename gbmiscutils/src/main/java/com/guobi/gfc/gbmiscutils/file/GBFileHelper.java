package com.guobi.gfc.gbmiscutils.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.StatFs;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;


public abstract class GBFileHelper {

	private final static String TAG="GBFileHelper";
	
	/**
	 * 
	 */
	protected GBFileHelper()
	{
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract String getRootDir();
	
	
	
	/**
	 * 
	 * @param dirName
	 * @return
	 */
	public final boolean makeDir(String dirName)
	{
		if(isEmptyString(dirName))
			return false;
		
		final String rootDir=this.getRootDir();
		if(isEmptyString(rootDir))
			return false;
		final String path = rootDir +File.separator + dirName;
		//chmod(path);
		File dir=new File(path);
		if(!dir.exists())
            return dir.mkdir();
        else
        	return false;
	}
	
	private static final void chmod(final String path){
		try {
			final String cmd = "chmod" + path + " " + "777"
					+ " && busybox chmod " + path + " " + "777";
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param fileAbsPath
	 * @return
	 */
	public static final boolean deleteFile(String fileAbsPath)
	{
		if(isEmptyString(fileAbsPath))
			return false;
		File path = new File(fileAbsPath);
		if(path.exists()&&path.isFile())
			return path.delete();
		else
			return false;
	}
	
	/**
	 * 
	 * @param fileAbsPath
	 * @return
	 */
	public static final boolean newFile(String fileAbsPath)
	{
		if(isEmptyString(fileAbsPath))
			return false;
		File path = new File(fileAbsPath);
		try {
			return path.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(GBLogUtils.DEBUG)
				e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param srcAbsPath
	 * @param destAbsPath
	 * @return
	 */
	public static final boolean copyFile(String srcAbsPath,String destAbsPath)
	{
		return copyFile(srcAbsPath,destAbsPath,4096,null);
	}
	
	/**
	 * 
	 * @param srcAbsPath
	 * @param destAbsPath
	 * @param chunkSize
	 * @param callBack
	 * @return
	 */
	public static final boolean copyFile(
			String srcAbsPath,String destAbsPath,
			int chunkSize, GBFileCopyingProgressCallback callBack)
	{
		if(isEmptyString(srcAbsPath))
			return false;
		if(isEmptyString(destAbsPath))
			return false;
		if(chunkSize<=0)
			return false;
		
		FileInputStream in=null;
		FileOutputStream out=null;
		
		try
		{
			in=new FileInputStream(srcAbsPath);
			File file=new File(destAbsPath);
			if(!file.exists())
			{
				if(!file.createNewFile())
					return false;
			}
			
			out=new FileOutputStream(file);
			int c;
			byte buffer[]=new byte[chunkSize];
			
			if(callBack!=null)
				callBack.onCopyBegin();
			
			while((c=in.read(buffer))!=-1){
				out.write(buffer,0,c);
				if(callBack!=null)
					callBack.onCopyProgress((long)c);
			}
			out.flush();
			
			if(callBack!=null)
				callBack.onCopyEnd();
			
			return true;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			if(GBLogUtils.DEBUG)
				e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(GBLogUtils.DEBUG)
				e.printStackTrace();
		}
		finally{
			
			try {
				if(in!=null)
					in.close();
				if(out!=null)
			       	out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if(GBLogUtils.DEBUG)
					e.printStackTrace();
			}
		}
        
		return false;
    }

	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public final String getFileAbsPath(String fileName)
	{
		return getFileAbsPath(null,fileName);
	}
	
	/**
	 * 
	 * @param dirPath
	 * @param fileName
	 * @return
	 */
	public final String getFileAbsPath(String dirPath,String fileName)
	{
		String path=this.getRootDir();
		if(isEmptyString(path))
			return null;
		if(!isEmptyString(dirPath))
			path += File.separator + dirPath;
		if(!isEmptyString(fileName))
			path += File.separator + fileName;
		return path;
	}
	
	/**
	 * 获取剩余空间
	 * @return
	 * In bytes
	 */
	public final long getAvailableSpace()
	{
		String path=this.getRootDir();
		if(isEmptyString(path))
			return -1;
		
		StatFs stat = new StatFs(path);
		final long blkSize = stat.getBlockSize();
		final long blkCount = stat.getAvailableBlocks();
		return blkSize*blkCount;
	}
	
	/**
	 * 
	 * @param fileAbsPath
	 * @param data
	 * @param bytesToWrite
	 * @return
	 */
	public static final boolean appendFile(String fileAbsPath,byte[] data,int bytesToWrite)
	{
		if(isEmptyString(fileAbsPath))
			return false;
		if(data==null||bytesToWrite<0)
			return false;
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileAbsPath,true);
			fos.write(data,0,bytesToWrite);
			fos.flush();
			return true;
		} catch (FileNotFoundException e) {
			if(GBLogUtils.DEBUG)
				e.printStackTrace();

		} catch (IOException e) {
			if(GBLogUtils.DEBUG)
				e.printStackTrace();
		}
		finally
		{
			try {
				if(fos!=null)
					fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if(GBLogUtils.DEBUG)
					e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param fileAbsPath
	 * @return
	 */
	public static final boolean isFileExists(String fileAbsPath)
	{
		if(isEmptyString(fileAbsPath))
			return false;
		File path = new File(fileAbsPath);
		return path.exists();
	}
	
	/**
	 * 
	 * @param fileAbsPath
	 * @return
	 */
	public static final byte[] readFile(String fileAbsPath)
	{
		if(!isFileExists(fileAbsPath))
			return null;
		
		FileInputStream in=null;
		
		try
		{
			File file=new File(fileAbsPath);
			if(file.isDirectory()||file.length()<=0)
				return null;
			
			in=new FileInputStream(fileAbsPath);
			byte[] buf = new byte[(int)file.length()];
			if(in.read(buf)!=file.length())
				return null;
					
			return buf;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			if(GBLogUtils.DEBUG)
				e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(GBLogUtils.DEBUG)
				e.printStackTrace();
		}
		finally{
			
			try {
				if(in!=null)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if(GBLogUtils.DEBUG)
					e.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param fileAbsPath
	 * @param buf
	 * @return
	 */
	public static final boolean writeFile(String fileAbsPath, byte[] buf)
	{
		if(buf==null||buf.length<=0)
			return false;
		
		deleteFile(fileAbsPath);
		if(!newFile(fileAbsPath))
			return false;
				
		return appendFile(fileAbsPath,buf,buf.length);
	}
	
	public static String loadString(GBFileHelper fileHelper,String fileName){
		String path = fileHelper.getFileAbsPath(fileName);
		if(path==null){
			return null;
		}
		byte[] buf = GBFileHelper.readFile(path);
		if(buf==null)
			return null;
		return new String(buf);
	}
	
	public static void saveString(GBFileHelper fileHelper,String fileName,String data){
		try {
			String path = fileHelper.getFileAbsPath(fileName);
			if(path==null)
				return;
			GBAppFileHelper.deleteFile(path);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream out =new FileOutputStream(file);
			BufferedOutputStream stream =new BufferedOutputStream(out);
			byte[] b=data.getBytes();
			stream.write(b);
			stream.flush();
			stream.close();
	        out.flush();  
	        out.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static final boolean isEmptyString(String str)
	{
		return str==null || str.length()<=0;
	}
}
