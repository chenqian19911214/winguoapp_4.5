package com.winguo.view;

import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by admin on 2017/3/21.
 */

public class CustomWebView extends WebView implements View.OnLongClickListener {
    private Context context;
    private LongClickCallBack mCallBack;
    public CustomWebView(Context context, LongClickCallBack mCallBack) {
        super(context);
        this.context = context;
        this.mCallBack = mCallBack;
        initSettings();
    }

    private void initSettings() {
        // 初始化设置
        WebSettings mSettings = this.getSettings();
        mSettings.setJavaScriptEnabled(true);//开启javascript
        mSettings.setDomStorageEnabled(true);//开启DOM
        mSettings.setDefaultTextEncodingName("utf-8");//设置字符编码
        //设置web页面
        mSettings.setAllowFileAccess(true);//设置支持文件流
        mSettings.setSupportZoom(true);// 支持缩放
        mSettings.setBuiltInZoomControls(true);// 支持缩放
        mSettings.setUseWideViewPort(true);// 调整到适合webview大小
        mSettings.setLoadWithOverviewMode(true);// 调整到适合webview大小
        mSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);// 屏幕自适应网页,如果没有这个，在低分辨率的手机上显示可能会异常
        mSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //提高网页加载速度，暂时阻塞图片加载，然后网页加载好了，在进行加载图片
        mSettings.setBlockNetworkImage(true);
        mSettings.setAppCacheEnabled(true);//开启缓存机制
        setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        // 长按事件监听（注意：需要实现LongClickCallBack接口并传入对象）
        final HitTestResult htr = getHitTestResult();//获取所点击的内容
        if (htr.getType() == HitTestResult.IMAGE_TYPE) {//判断被点击的类型为图片
            mCallBack.onLongClickCallBack(htr.getExtra());
        }
        return false;
    }


    /**
     * 长按事件回调接口，传递图片地址
     * @author LinZhang
     */
    public interface LongClickCallBack{
        /**用于传递图片地址*/
        void onLongClickCallBack(String imgUrl);
    }
}
