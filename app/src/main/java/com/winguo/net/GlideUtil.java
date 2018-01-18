package com.winguo.net;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;


/**
 * Created by Administrator on 2016/11/23.
 * glide的封装类
 */

public class GlideUtil {

    private static GlideUtil glideUtil;
    private GlideUtil(){

    }
    /**
     * @return 返回一个GlideUtild的对象
     */
    public static GlideUtil getInstance(){
        if (glideUtil==null){
            synchronized (GlideUtil.class){
                if (glideUtil==null){
                    glideUtil=new GlideUtil();
                }
            }
        }
        return glideUtil;
    }
    /**
     * 加载图片到控件上
     * @param context
     * @param url 图片的路径
     * @param imageView 要加载到的控件
     */
    public void loadImage(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    /**
     * 加载图片到控件上
     * @param context
     * @param url 图片的路径
     * @param placeImage 站位图资源
     * @param errorImage 加载错误时图片资源
     * @param imageView 要加载到的控件
     */
    public void loadImage(Context context, String url, int placeImage,int errorImage,ImageView imageView){
        Glide.with(context)
                .load(url)
                .placeholder(placeImage)
                .error(errorImage)
                .into(imageView);
    }

    /**
     * @param context
     * @param url 图片的路径
     * @param width 自己指定的图片宽
     * @param hight 自己指定的图片高
     * @param imageView 要加载到的控件
     */
    public void loadImageCustomSize(Context context, String url,int width,int hight,ImageView imageView){
        Glide.with(context)
                .load(url)
                .override(width,hight)
                .into(imageView);
    }

    /**
     * @param context
     * @param url 图片的路径
     * @param placeImage 站位图资源
     * @param errorImage 加载错误时图片资源
     * @param width 自己指定的图片宽
     * @param hight 自己指定的图片高
     * @param imageView 要加载到的控件
     */
    public void loadImageCustomSize(Context context, String url, int placeImage,int errorImage,int width,int hight,ImageView imageView){
        Glide.with(context)
                .load(url)
                .placeholder(placeImage)
                .error(errorImage)
                .override(width,hight)
                .into(imageView);
    }
    /**
     *  设置加载优先级
     * @param context
     * @param url 图片的路径
     * @param placeImage 站位图资源
     * @param errorImage 加载错误时图片资源
     * @param imageView 要加载到的控件
     */
    public  void loadImagePriority(Context context, String url, int placeImage,int errorImage, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(placeImage)
                .error(errorImage)
                .priority(Priority.HIGH)
                .into(imageView);
    }
    /**
     *清理磁盘缓存
     */
    public  void clearDiskCache(Context context) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(context)
                .clearDiskCache();
    }
    /**
     *  清理内存缓存
     */
    public  void clearMemory(Context context) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(context)
                .clearMemory();
    }

    /**
     * 停止请求
     * @param context
     */
    public void pauseRequest(Context context){
        Glide.with(context).pauseRequests();
    }

    /**
     * 开始请求
     * @param context
     */
    public void resumeRequest(Context context){
        Glide.with(context).resumeRequests();
    }
}
