package com.winguo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import java.lang.reflect.Method;

/**
 * Created by admin on 2017/2/24.
 */

public class PermissionUtil {

    public static boolean checkPermissionAndRequest(Activity context, String permission,int requestCode) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                    GBLogUtils.DEBUG_DISPLAY("checkPermission", "shouldShowRequestPermissionRationale + true");
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
                    result =  false;
                }

            } else {

                result =  true;
            }

        } else {
            //低于6.0 版本 检测权限
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            } else {
                result = false;
            }

        }
        return result;
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

}
