package com.winguo.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.winguo.utils.Constants;
import com.winguo.utils.ToastUtil;

/**
 * Created by admin on 2017/1/11.
 */

public class NetWorkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (activeInfo == null) {
            //无网络 打开网络设置界面
            Intent it = new Intent(Constants.NO_NET_STATU);
            context.sendOrderedBroadcast(it,null);
        } else {

            if (mobileInfo.isConnected()) {
                ToastUtil.showToast(context, "正在使用手机网络");
                Intent mobileOpen = new Intent(Constants.NET_STATU_MOBILE);
                context.sendOrderedBroadcast(mobileOpen,null);
            }
            if (wifiInfo.isConnected()) {
                ToastUtil.showToast(context, "正在使用wifi网络");
                Intent wifiOpen = new Intent(Constants.NET_STATU_WIFI);
                context.sendOrderedBroadcast(wifiOpen,null);
            }

        }

    }


}
