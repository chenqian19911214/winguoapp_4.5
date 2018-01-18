package com.winguo.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/11/23.
 * 文件路径
 */

public class FileUtil {

    public static final String ROOT = "WinGuo";//sd卡的根目录

    /**
     * sd卡的根目录
     */
    public static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();

    /**
     * 手机的缓存根目录
     */
    public static String mDataRootPath = CommonUtil.getAppContext().getCacheDir().getPath();


    /**
     * 保存Image的目录名
     */
    public final static String FOLDER_NAME = "/tea/image";

    public FileUtil(Context context) {
        //  mDataRootPath = context.getCacheDir().getPath();
    }



    public static String getSdDir(Context context, String dir) {
        //  /mnt/sdcard/
        StringBuilder path = new StringBuilder();
        // /data/data/cache/cache
        path.append(context.getCacheDir().getAbsolutePath());// /data/data/cache
        path.append(File.separator); // /data/data/cache
        path.append(dir);  //data/data/cache/cache

        return path.toString();
    }

    public static String getDiskDir(Context context, String dir) {
        //  /mnt/sdcard/
        StringBuilder path = new StringBuilder();
        path.append(Environment.getExternalStorageDirectory().getAbsolutePath()); // /mnt/sdcard
        path.append(File.separator);
        path.append(ROOT);
        path.append(File.separator);
        path.append(dir);
        return path.toString();
    }

    /**
     * sd卡是否可用
     *
     * @return
     */
    public static boolean isSdcardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());//防止空指针
    }

    /**
     * 获取储存Image的目录
     *
     * @return
     */
    public String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mSdRootPath + FOLDER_NAME : mDataRootPath
                + FOLDER_NAME;
    }

    /**
     * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
     *
     * @param
     * @param bitmap
     * @throws
     */
    public void savaBitmap(String url, Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        String path = getStorageDirectory();
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        File file = new File(path + File.separator + getFileName(url));
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String saveImage(String name, Bitmap bmp) {

        /**
         *   sd = /storage/emulated/0
         *   mo = /storage/emulated/0
         * */
        String path ;
        boolean isSd =Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (isSd){
            path= mSdRootPath;
        }else {
            path=mDataRootPath;
        }
        File file = new File(path + File.separator, name);

        if (file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存资源文件
     *
     * @param context
     * @param assetsSrc
     * @return
     */
    public static boolean savaAssetImage(Context context, String assetsSrc) {
        String down = "";
        if (FileUtil.isSdcardAvailable()) {
            down = Environment.getExternalStorageDirectory() + "/" + assetsSrc;
        } else {
            down = context.getCacheDir() + "/" + assetsSrc;
        }
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            AssetManager am = context.getAssets();
            istream = am.open(assetsSrc);
            ostream = new FileOutputStream(down);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = istream.read(buffer)) > 0) {
                ostream.write(buffer, 0, length);
            }
            istream.close();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (istream != null)
                    istream.close();
                if (ostream != null)
                    ostream.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * 从手机或者sd卡获取Bitmap
     *
     * @param
     * @return
     */
    public Bitmap getBitmap(String url) {
        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + getFileName(url));
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean isFileExists(String fileName) {
        return new File(getStorageDirectory() + File.separator + getFileName(fileName)).exists();
    }

    /**
     * 获取文件的大小
     *
     * @param
     * @return
     */
    public long getFileSize(String url) {
        return new File(getStorageDirectory() + File.separator + getFileName(url)).length();
    }

    /**
     * 删除SD卡或者手机的缓存图片和目录
     */
    public void deleteFile() {
        File dirFile = new File(getStorageDirectory());
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    new File(dirFile, children[i]).delete();
                }
            }
        }
        dirFile.delete();
    }

    /**
     * @return String 返回类型
     * @throws
     * @Title: getFileName
     * @说 明: 根据url截取文件名
     * @参 数: @param url
     * @参 数: @return
     */
    public String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    /**
     * 取得文件大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) throws Exception {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        } else {
            f.createNewFile();
        }
        return s;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }


}
