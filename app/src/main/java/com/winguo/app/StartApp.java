package com.winguo.app;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountKey;
import com.guobi.gblocation.GBDLocation;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.guobi.gfc.gbmiscutils.thread.RunnableBus;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.winguo.MainActivity;
import com.winguo.R;
import com.winguo.cad.util.Utils;
import com.winguo.lbs.manuallocation.LocationService;
import com.winguo.utils.AssetsDatabaseManager;
import com.winguo.utils.Constants;
import com.winguo.utils.FileUtil;
import com.winguo.utils.LruCacheUtils;
import com.winguo.utils.SPUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;

/**
 * @author hcpai
 * @desc
 */

public class StartApp extends Application implements Thread.UncaughtExceptionHandler {

    /**
     * 公钥，要在第一次获取到时赋值
     */
    public static WinguoAccountKey mKey;
    /**
     * activity集合
     */
    private ArrayList<Activity> activitys = new ArrayList<>();
    /**
     * MyApplication实例
     */
    private static StartApp instance;
    /**
     * 分页加载一页显示的数量
     */
    public static int limit = 8;
    public LocationService locationService;
    public static IWXAPI mWxApi;

    /**
     * application在项目中其实本身已经是单例了,获取全局的context
     */
    public static Context getContext() {
        return instance;
    }

    /**
     * application在项目中其实本身已经是单例了
     */
    public static StartApp getInstance() {
        return instance;
    }

    //缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
    public static LruCacheUtils lruCache;

    private boolean isDebug;

    @Override
    public void onCreate() {
        super.onCreate();
        isDebug = false;
        WinguoAccountConfig.setDEBUG(isDebug);
        GBLogUtils.setIsShow(true);
        if (!SPUtils.contains(getApplicationContext(), "isDebug")) {
            SPUtils.put(getApplicationContext(), "isDebug", isDebug);
            SPUtils.put(getApplicationContext(), "isChange", false);
        } else {
            boolean flag = (boolean) SPUtils.get(getApplicationContext(), "isDebug", false);
            if (isDebug != flag) {
                SPUtils.put(getApplicationContext(), "isDebug", isDebug);
                SPUtils.put(getApplicationContext(), "isChange", true);
            } else {
                SPUtils.put(getApplicationContext(), "isChange", false);
            }
        }
        instance = this;
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=58607293");
        GBDLocation.createInstance(this);//百度定位
        ShareSDK.initSDK(this);//mob分享
        registToWX(); //微信第三方登录
        Utils.init(getContext());
        checkUpdateSet(); //bugly 检测更新
        Thread.setDefaultUncaughtExceptionHandler(this);//app出现异常 无响应强制杀死 重启应用
        RunnableBus.createInstance();//必须在账号管理之前创建实例对象
        GBAccountMgr.createInstance(this);//创建账号管理实例对象
        //GBAccountMgr.getInstance().startup();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //三级缓存设置
            lruCacheSet();
            //读取本地数据库 匹配电话归属地
            AssetsDatabaseManager.initManager(getApplicationContext());
            AssetsDatabaseManager manager = AssetsDatabaseManager.getManager();
            manager.getDatabase("telocation.db");

            //读取logo 到sdcard
            File logoDown = new File(Constants.logoSDPath);
            if (!logoDown.exists()) {
                FileUtil.savaAssetImage(getContext(), "logo.png");
            }
        }
        //友盟 集成统计测试
        // getDeviceInfo(getApplicationContext());
        // MobclickAgent.setDebugMode( true );


    }

    /**
     * 初始化微信授权登录
     */
    private void registToWX() {
        //Constants.APP_ID_WEIXIN_LOGIN是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID_WEIXIN_LOGIN, false);
        // 将该app注册到微信
        mWxApi.registerApp(Constants.APP_ID_WEIXIN_LOGIN);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    private static final String TAG = StartApp.class.getSimpleName();

    private void checkUpdateSet() {
        Beta.autoInit = true;
        Beta.enableHotfix = true;//开启热更新
        Beta.autoCheckUpgrade = true;
        //true表示初始化时自动检查升级;
        //false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;

        //延迟初始化
        Beta.initDelay = 1 * 1000;
        Beta.upgradeCheckPeriod = 60 * 1000;
        //设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);

        //设置通知栏大图标
        Beta.largeIconId = R.mipmap.logo;
        //largeIconId为项目中的图片资源;
        //设置状态栏小图标
        Beta.smallIconId = R.mipmap.logo;
        //smallIconId为项目中的图片资源id
        //设置更新弹窗默认展示的banner
        Beta.defaultBannerId = R.mipmap.logo;
        //defaultBannerId为项目中的图片资源Id;
        //当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading…“;
        //设置sd卡的Download为更新资源存储目录
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;

        //只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 如果不设置默认所有activity都可以显示弹窗。
        Beta.canShowUpgradeActs.add(MainActivity.class);
        //设置点击过确认的弹窗在App下次启动自动检查更新时会再次显示。
        Beta.showInterruptedStrategy = true;
        //设置自定义升级对话框UI布局
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
        /*upgrade_dialog为项目的布局资源。
        注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的
        正常使用：
        特性图片：beta_upgrade_banner，如：android:tag=”beta_upgrade_banner”
        标题：beta_title，如：android:tag=”beta_title”
        升级信息：beta_upgrade_info 如： android:tag=”beta_upgrade_info”
        更新属性：beta_upgrade_feature 如： android:tag=”beta_upgrade_feature”
        取消按钮：beta_cancel_button 如：android:tag=”beta_cancel_button”
        确定按钮：beta_confirm_button 如：android:tag=”beta_confirm_button”*/

        //设置自定义tip弹窗UI布局
        Beta.tipsDialogLayoutId = R.layout.tips_dialog;
        /*注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的
        正常使用：
        标题：beta_title，如：android:tag=”beta_title”
        提示信息：beta_tip_message 如： android:tag=”beta_tip_message”
        取消按钮：beta_cancel_button 如：android:tag=”beta_cancel_button”
        确定按钮：beta_confirm_button 如：android:tag=”beta_confirm_button”*/

        //如果你不想在通知栏显示下载进度，你可以将这个接口设置为false，默认值为true。
        Beta.enableNotification = true;
        //如果你想在Wifi网络下自动下载，可以将这个接口设置为true，默认值为false。
        Beta.autoDownloadOnWifi = false;
        //如果你使用我们默认弹窗是会显示apk信息的，如果你不想显示可以将这个接口设置为 false。
        Beta.canShowApkInfo = true;

        Bugly.init(getApplicationContext(), Constants.APP_KEY_BUGLY, false);
        //Bugly SDK初始化
        CrashReport.initCrashReport(getApplicationContext(), Constants.APP_KEY_BUGLY, false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        GBLogUtils.DEBUG_DISPLAY("application", "+++++++++++++++++++++++++++++++++++++++++++++++++++attachBaseContext");
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
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

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;

            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void lruCacheSet() {
        lruCache = new LruCacheUtils(getApplicationContext());
    }

    /**
     * 添加Activity到容器中
     *
     * @param activity 当前activity
     */
    public void addActivity(Activity activity) {
        activitys.add(activity);
    }

    /**
     * 删除容器中当前activity
     *
     * @param activity 当前activity
     */
    public void removeActivity(Activity activity) {
        activitys.remove(activity);
    }

    /**
     * 结束某一个activity
     *
     * @param activity 被结束的activity
     */
    public <T> void quit(Class<T> activity) {
        for (final Activity item : activitys) {
            if (item.getClass().equals(activity)) {
                item.finish();
                break;
            }
        }
    }

    /**
     * 保留某一个activity，结束掉其他的activity
     *
     * @param activity 要保留的activity
     */
    public <T> void quitOthers(Class<T> activity) {
        for (final Activity item : activitys) {
            if (!item.getClass().equals(activity)) {
            }
            item.finish();
        }
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        //如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用  用来保存统计数据。
        MobclickAgent.onKillProcess(getApplicationContext());
        System.out.println("uncaughtException");
        throwable.printStackTrace();

      /*  System.exit(0);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
              Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/

    }


}
