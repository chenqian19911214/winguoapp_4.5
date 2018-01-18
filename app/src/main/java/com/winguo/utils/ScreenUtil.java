package com.winguo.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.winguo.R;

import java.text.DecimalFormat;

/**
 * @author hcpai
 * @desc 屏幕工具类
 */
public class ScreenUtil {

    /**
     * dip转px
     *
     * @param activity
     * @param value
     * @return
     */
    public static int dipToPx(Activity activity, int value) {
        final DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (int) (dm.density * value + 0.5f);
    }

    /**
     * dip转px
     *
     * @param context
     * @param value
     * @return
     */
    public static int dipToPx(Context context, int value) {
        final WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return (int) (dm.density * value + 0.5f);
    }

    /**
     * 获得屏幕的高度
     */
    public static int getScreenHeight(Activity activity) {
        final DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获得屏幕的宽度
     */
    public static int getScreenWidth(Activity activity) {
        final DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static void setWebView(Context activity,WebView webView){
        WindowManager wm = (WindowManager)activity.getSystemService(activity.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
       /* if (width >= 1080) {
            webView.setInitialScale(140);
        } else if (width >= 720) {
            webView.setInitialScale(94);
        } else if (width >= 480) {
            webView.setInitialScale(62);
        }*/
        if(width > 650) {
            webView.setInitialScale(190);
        }else if(width > 520) {

           webView.setInitialScale(160);

        }else if(width > 450) {

           webView.setInitialScale(140);

        }else if(width > 300) {

           webView.setInitialScale(120);

        }else {
            webView.setInitialScale(100);
        }

    }


    /**
     * 是否隐藏状态栏
     *
     * @param enable
     */
    public static void showFullScreen(Activity activity, boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(lp);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = activity.getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attr);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 为了解决ListView在ScrollView中只能显示一行数据的问题
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

}
