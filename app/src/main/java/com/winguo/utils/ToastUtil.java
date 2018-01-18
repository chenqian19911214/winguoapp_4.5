package com.winguo.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * 子线程和主线程中安全的运行
 */
public class ToastUtil {
    private static Toast toast;

    /**
     * 静态toast
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        if (toast == null) { // 1. 创建前 2.消失后toast为null
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }


    public  static  void show(final Activity activity, final String text){
        if("main".equals(Thread.currentThread().getName())) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        }else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
