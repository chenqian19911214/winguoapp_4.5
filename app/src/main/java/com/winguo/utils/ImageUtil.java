package com.winguo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.app.Activity.RESULT_OK;

/**
 * 相册 相机 图片裁剪工具
 * Created by admin on 2016/12/13.
 */

public class ImageUtil {

    public static final String CROP_CACHE_FILE_NAME = "icon_my.jpg";
    public static final int REQUEST_GALLERY = 0Xa0;
    public static final int REQUEST_CAMERA = 0Xa1;
    public static final int RQ_GALLERY = 127;
    public static final int RQ_CAMERA = 128;
    private Context context;

    private ImageUtil(Context context) {
        this.context = context;
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), "tempImage.jpg");
    }

    private static ImageUtil instance;

    public static ImageUtil getInstance(Context context) {

        if (instance == null) {
            return new ImageUtil(context);
        } else {
            return instance;
        }
    }

    private Uri buildUri() {
        String path = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getPath() : context.getCacheDir().getPath();
        File file = new File(path + File.separator + CROP_CACHE_FILE_NAME);
        Uri urio = Uri.fromFile(file);
        return urio;
    }

    //获取缓存裁剪图片文件
    public File getCachedCropFile() {
        if (buildUri() == null)
            return null;
        return new File(buildUri().getPath());
    }

    public Intent buildGalleryIntent() {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_PICK);
        gallery.setType("image/*");
        return gallery;
    }


    //调用照相机返回图片文件
    private File tempFile;

    /**
     * 跳转到调用系统相机
     */
    public Intent buildCameraIntent() {
        //用于保存调用相机拍照后所生成的文件
        Uri contentUri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2);
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        //判断版本
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
        //   contentUri = FileProvider.getUriForFile(context, "com.hansion.chosehead", tempFile);
        //  } else {    //否则使用Uri.fromFile(file)方法获取Uri
        contentUri = Uri.fromFile(tempFile);
        //   }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        return intent;
    }

    private boolean hasSDcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回裁剪图片的Intent
     *
     * @param uri
     * @return Intent
     */
    public Intent buildCropIntent(Uri uri) {

        Log.i("", "uriuri" + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        return intent;
    }

    public void setHandleResultListener(CropHandler handler, int requestCode, int resultCode, Intent data) {

        Log.i("", "");
        if (handler == null)
            return;
        if (requestCode == Activity.RESULT_CANCELED) {
            handler.onCropCancel();
        } else if (resultCode == RESULT_OK) {
            Bitmap photo;
            switch (requestCode) {
                case RQ_CAMERA:
                    if (data == null || data.getExtras() == null) {
                        handler.onCropFailed("data must not be null");
                        return;
                    }
                    photo = data.getExtras().getParcelable("data");
                    FileUtil fileUtil = new FileUtil(handler.getContext());
                    fileUtil.saveImage(CROP_CACHE_FILE_NAME, photo);
                    handler.onPhotoCropped(photo, requestCode);
                    break;
                case RQ_GALLERY:
                    if (data == null || data.getExtras() == null) {
                        handler.onCropFailed("data must not be null");
                        return;
                    }
                    photo = data.getExtras().getParcelable("data");
                    try {
                        photo.compress(Bitmap.CompressFormat.JPEG, 30, new FileOutputStream(getCachedCropFile()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    handler.onPhotoCropped(photo, requestCode);
                    break;
                case REQUEST_CAMERA:
                    Uri  contentUri = Uri.fromFile(tempFile);
                    handler.getContext().startActivityForResult(buildCropIntent(contentUri), RQ_CAMERA);
                    break;
                case REQUEST_GALLERY:
                    if (data == null) {
                        handler.onCropFailed("data must not be null");
                        return;
                    }
                    Intent gallery = buildCropIntent(data.getData());
                    if (handler.getContext() != null) {
                        handler.getContext().startActivityForResult(gallery, RQ_GALLERY);
                    } else {
                        handler.onCropFailed("Context must not be null");
                    }
                    break;
            }
        }
    }

    public interface CropHandler {
        Activity getContext();

        void onPhotoCropped(Bitmap photo, int requstCode);

        void onCropCancel();

        void onCropFailed(String message);

    }
}
