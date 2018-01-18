package com.winguo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.winguo.net.MyOkHttpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * 三级缓存bitmap
 * Created by admin on 2017/1/9.
 */

public class LruCacheUtils {

    private static final int LOAD_SUCCESS = 1;
    //缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
    private LruCache<String, Bitmap> lruCache;
    // 文件操作工具类
    private FileUtil utils;
    //线程池
    private ThreadPoolExecutor executor;

    public LruCacheUtils(Context context) {
        super();
        // 开启线程池 最小线程数
        executor = new ThreadPoolExecutor(1, 4, 2, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        // 获取系统分配给应用程序的最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int maxSize = maxMemory / 8;
        lruCache = new LruCache<String, Bitmap>(maxSize) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 测量Bitmap的大小 默认返回图片数量
                return value.getRowBytes() * value.getHeight();
            }

        };

        utils = new FileUtil(context);
    }

    /**
     *
     * @Title: downLoader
     * @说 明: 加载图片,在调用此方法之前，应该为Imageview预设一个tag，防止出现图片错位的情况
     * @参 数: @param url
     * @参 数: @param loaderlistener
     * @参 数: @return
     * @return Bitmap 返回类型
     * @throws
     */
    public void downLoaderBitmap(final ImageView imageView, final ImageLoaderlistener loaderlistener) {
        final String url = (String) imageView.getTag();
        Log.i("MyFamilyFragment  ","-----down"+url);
        if (url != null) {
            final Bitmap bitmap = showCacheBitmap(url);
            Log.i("MyFamilyFragment  ","-----down  bitmap"+bitmap);

            if (bitmap != null) {
                loaderlistener.onImageLoader(bitmap, imageView);
            } else {
                Log.i("MyFamilyFragment  ","-----down  bitmap   null");
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        loaderlistener.onImageLoader((Bitmap) msg.obj, imageView);
                    }
                };

                executor.execute(new Runnable() {

                    @Override
                    public void run() {
                        Bitmap bitmap = getBitmapFormUrl(url);
                        if (bitmap != null) {
                            Message msg = handler.obtainMessage();
                            msg.obj = bitmap;
                            msg.what = LOAD_SUCCESS;
                            handler.sendMessage(msg);
                            try {
                                utils.savaBitmap(url, bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            lruCache.put(url, bitmap);
                        } else {
                            loaderlistener.onImageLoader(null, imageView);
                        }
                    }
                });
            }
        }
    }

    /**
     *
     * @Title: showCacheBitmap
     * @说 明: 获取bitmap对象 : 内存中没有就去sd卡中去找
     * @参 数: @param url 图片地址
     * @参 数: @return
     * @return Bitmap 返回类型 图片
     * @throws
     */
    public Bitmap showCacheBitmap(String url) {
        Bitmap bitmap = getMemoryBitmap(url);
        if (bitmap != null) {
            return bitmap;
        } else if (utils.isFileExists(url) && utils.getFileSize(url) > 0) {
            bitmap = utils.getBitmap(url);
            if (bitmap == null) {
                return null;
            }
            lruCache.put(url, bitmap);
            return bitmap;
        }
        return null;
    }

    /**
     *
     * @Title: getMemoryBitmap
     * @说 明:获取内存中的图片
     * @参 数: @param url
     * @参 数: @return
     * @return Bitmap 返回类型
     * @throws
     */
    private Bitmap getMemoryBitmap(String url) {
        return lruCache.get(url);
    }

    public interface ImageLoaderlistener {
        public void onImageLoader(Bitmap bitmap, ImageView imageView);
    }

    /**
     *
     * @Title: cancelTask
     * @说 明:停止所有下载线程
     * @参 数:
     * @return void 返回类型
     * @throws
     */
    public void cancelTask() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    public static Bitmap getBitmapFormUrl(String url) {

        Bitmap bitmap = null;
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(3000,TimeUnit.SECONDS);
            OkHttpClient client = builder.build();
            Request request = new Request.Builder().url(url).build();
            ResponseBody body = client.newCall(request).execute().body();
            InputStream in = body.byteStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
