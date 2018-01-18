package com.winguo.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.BuildConfig;
import com.winguo.R;
import com.winguo.personalcenter.setting.ExplainActivity;
import com.winguo.personalcenter.setting.FeedBackActivity;
import com.winguo.personalcenter.setting.HelpVoiceActivity;
import com.winguo.activity.NewCartActivity;
import com.winguo.login.LoginActivity;
import com.winguo.login.register.RegisterActivity;
import com.winguo.view.GBDialog;
import com.winguo.view.IDialog;
import com.winguo.view.SelfDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Intent 跳转工具类
 */
public class Intents {

    public static void helpWin(Context context){
        Intent it = new Intent(context, HelpVoiceActivity.class);
        it.putExtra("title","您可以这么说");
        context.startActivity(it);
    }
    public static void feedBack(Context context,Boolean flag){

        if (SPUtils.contains(context, "accountName")) {
            context.startActivity(new Intent(context, FeedBackActivity.class));
        } else {
            //ToastUtil.showToast(context,"请先登录^_^");
            Intent it =  new Intent(context, LoginActivity.class);
            it.putExtra("putExtra",Constants.LOGIN_WAY_FEEDBACK);
            context.startActivity(it);
        }

    }

    public static void startCart(boolean isLogin,Context mContext) {
        Intent cartIntent = null;
        if (isLogin) {
            cartIntent = new Intent(mContext, NewCartActivity.class);
        } else {
            // Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
            cartIntent = new Intent(mContext,LoginActivity.class);
            cartIntent.putExtra("putExtra", Constants.LOGIN_WAY_CART);
        }
        mContext.startActivity(cartIntent);
    }

    // 检测权限是否打开
    public static void checkPermissions(Activity context,String[] requestedPermissions ,int requestCode) {

        if (Build.VERSION.SDK_INT >= 23) {
            // 不去获取app所有权限检测 String[] requestedPermissions = Intents.getAppPermissions(activity);
            // String[] requestedPermissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.SEND_SMS};
            //检测权限是否打开(打电话 发短信 定位 录音 写外部存储卡) 下列权限一个没打开，直接提示
            boolean isOpen = true;
            List<String> temp = new ArrayList<>();
            for (int i = 0; i < requestedPermissions.length; i++) {
                String permisson = requestedPermissions[i];
                switch (permisson) {
                    case Manifest.permission.READ_CONTACTS:
                        if (checkPermission(context, permisson) != PackageManager.PERMISSION_GRANTED) {
                            isOpen = false;
                            temp.add(Manifest.permission.READ_CONTACTS);
                        }
                        break;
                    case Manifest.permission.CALL_PHONE:
                        if (checkPermission(context, permisson) != PackageManager.PERMISSION_GRANTED) {
                            isOpen = false;
                            temp.add(Manifest.permission.CALL_PHONE);
                        }
                        break;
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        if (checkPermission(context, permisson) != PackageManager.PERMISSION_GRANTED) {
                            isOpen = false;
                            temp.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                        break;
                    case Manifest.permission.ACCESS_COARSE_LOCATION:
                        if (checkPermission(context, permisson) != PackageManager.PERMISSION_GRANTED) {
                            isOpen = false;
                            temp.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                        }
                        break;
                    case Manifest.permission.READ_SMS:
                        if (checkPermission(context, permisson) != PackageManager.PERMISSION_GRANTED) {
                            isOpen = false;
                            temp.add(Manifest.permission.READ_SMS);
                        }
                        break;
                    case Manifest.permission.RECORD_AUDIO:
                        if (checkPermission(context, permisson) != PackageManager.PERMISSION_GRANTED) {
                            isOpen = false;
                            temp.add(Manifest.permission.RECORD_AUDIO);
                        }
                        break;
                }

            }
            if (!isOpen) {
                String[] requestPermission = new String[temp.size()];
                temp.toArray(requestPermission);
                context.requestPermissions(requestPermission,requestCode);
            }

        } else {
            //如果低于23  android 6.0

        }

    }


    /**
     * 多个权限提示
     * @param context
     */
    public static void createDialogShow(final Context context,String mess) {
        ToastUtil.showToast(context,mess);
        final IDialog.Builder builder = new IDialog.Builder(context);
        builder.setTitle("为了更好的为您服务，需要获取以下权限:");
        builder.setHelpButton("系统权限使用说明", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ToastUtil.showToast(context, "系统权限使用说明");
                //跳转系统权限说明
                Intent permissions = new Intent(context, ExplainActivity.class);
                permissions.putExtra("assetUrl",Constants.assetPermissionURL);
                context.startActivity(permissions);
            }
        });
        builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ToastUtil.showToast(context, "知道了");
            }
        });
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ToastUtil.showToast(context, "去设置");
                //跳转设置权限界面 根据不同手机权限设置不同  默认打开设置界面
                OpenPermissionsSetting(context);

            }
        });

        builder.create().show();
    }

    public static int checkPermission(Context context,String permission){
        int flag = ContextCompat.checkSelfPermission(context, permission);
        return flag;
    }

    public static void noCameraPermissionStatus(final Context context, String mess, final String on_start_canmera){
        final SelfDialog dialog=new SelfDialog(context,false,context.getString(R.string.camera_no_work),
                mess,
                context.getString(R.string.camera_no_work_text2),
                context.getString(R.string.start_camera),
                on_start_canmera);
        dialog.show();
        dialog.setOnClickPositiveButton(new SelfDialog.OnClickPositiveButton() {
            @Override
            public void onClickPositiveButton() {
                dialog.dismiss();
                OpenPermissionsSetting(context);
            }
        });
        dialog.setOnClickNegativeButton(new SelfDialog.OnClickNegativeButton() {
            @Override
            public void onClickNegativeButton() {
                dialog.dismiss();
                if (on_start_canmera.equals("不扫描，直接注册")) {
                    context.startActivity(new Intent(context, RegisterActivity.class));
                    ((Activity) context).finish();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.do_not_image), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void OpenPermissionsSetting(Context context) {
        String product = Build.MANUFACTURER; //手机制造商
        switch (product) {
            case Constants.BUILDE_PRODUCT_MIUI:
                gotoMiuiPermission(context);
                break;
            case Constants.BUILDE_PRODUCT_MEIZU:
                gotoMeizuPermission(context);
                break;
            case Constants.BUILDE_PRODUCT_HUAWEI:
                gotoHuaweiPermission(context);
                break;
            default:
                context.startActivity(getAppDetailSettingIntent(context));
                break;
        }
    }


    public static void noPermissionStatus(final Context context,String mess){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(mess+",是否打开权限设置界面？");
        builder.setTitle("权限状态提示：");

        builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                OpenPermissionsSetting(context);
            }
        });
        builder.create().show();
    }

    public static synchronized GBDialog noNetStatus(final Context context){

        ToastUtil.showToast(context, "当前处于无网络状态");
        GBDialog.Builder builder = new GBDialog.Builder(context);
        builder.setMessage("当前处于无网络状态，是否打开打开网络设置界面？");
        builder.setTitle("网络状态提示：");
        builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                context.startActivity(getWifiDetailSettingIntent(context));
            }
        });
       return builder.create();
    }
    /**
     * 跳转wifi详情设置页面
     * @return
     */
    public static Intent getWifiDetailSettingIntent(Context context) {

        Intent intent = null;
        // 先判断当前系统版本
        if (Build.VERSION.SDK_INT > 10) {
            intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName(
                    "com.android.settings",
                    "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        return intent;
    }


    /**
     * 获取本app 所有权限
     * @param activity
     * @return
     */
    public static String[] getAppPermissions(Activity activity)  {

        try {

            PackageManager packageManager = activity.getPackageManager();
            ApplicationInfo applicationInfo = activity.getApplicationInfo();

            PackageInfo packageInfo = null;
            packageInfo = packageManager.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            for (int i = 0; i < requestedPermissions.length; i++) {
                GBLogUtils.DEBUG_DISPLAY("permissions", "--------" + requestedPermissions[i].toString());
            }
            return requestedPermissions;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 跳转魅族权限管理
     */
    private static void gotoMeizuPermission(Context context) {
        Intent it = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        it.addCategory(Intent.CATEGORY_DEFAULT);
        it.putExtra("packageName", BuildConfig.APPLICATION_ID);
        try {
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            gotoHuaweiPermission(context);
        }
    }

    /**
     * 跳转小米权限管理
     */
    private static void gotoMiuiPermission(Context context) {

        Intent it = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName comp = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        it.setComponent(comp);
        it.putExtra("extra_pkgname", context.getPackageName());
        try {
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            gotoMeizuPermission(context);
        }
    }

    /**
     * 跳转华为权限管理
     */
    private static void gotoHuaweiPermission(Context context) {

        Intent it = new Intent();
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
        it.setComponent(comp);
        try {
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivity(getAppDetailSettingIntent(context));
        }

    }


    /**
     * 跳转应用详情设置页面
     *
     * @return
     */
    public static Intent getAppDetailSettingIntent(Context context) {

        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }

}
