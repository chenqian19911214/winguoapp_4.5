package com.guobi.account;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具与参数类
 * Created by chenq on 2017/1/3.
 */
public final class NetworkUtils {
    public static final int CONNECTION_TIMEOUT = 10000;//链接等待时长

    /**
     * 判断是否连通网络
     *
     * @return 是否连通网络
     */
    public static synchronized boolean isConnectNet(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo Mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo Wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (Mobile.getState().equals(NetworkInfo.State.DISCONNECTED) &&
                Wifi.getState().equals(NetworkInfo.State.DISCONNECTED))
            return false;
        return true;
    }




}
