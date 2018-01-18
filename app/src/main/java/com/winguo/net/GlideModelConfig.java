package com.winguo.net;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpGlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.winguo.utils.FileUtil;


/**
 * Created by Administrator on 2016/11/23.
 * 对Glide缓存大小和缓存地址的设置
 */

public class GlideModelConfig extends OkHttpGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        //使用最大运行内存的1/8
        int mMemoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        //设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(mMemoryCacheSize));
        //设置磁盘缓存的大小
        int diskSize=1024*1024*50;
        //设置缓存的位置
        String downloadDirectoryPath ="";
        if(FileUtil.isSdcardAvailable()){
            //有sd卡,创建路径
            downloadDirectoryPath  = FileUtil.getSdDir(context,"icon");
        }else{
            downloadDirectoryPath =FileUtil.getDiskDir(context,"icon");
        }
        builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath,diskSize));


    }


}
