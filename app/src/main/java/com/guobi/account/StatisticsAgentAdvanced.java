package com.guobi.account;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import java.util.Random;

import static com.guobi.account.MD5.get32MD5Str;

/**
 * Created by chenq on 2017/1/3.
 */
public class StatisticsAgentAdvanced {


    /**
     * 获取用户ID
     */
    public static synchronized final String getDeviceId(Context context) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            try {

                String deviceId = "";
                String wifiMac, IMEI, SN, androidId;
                // WIFIMAC
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifi.getConnectionInfo();
                wifiMac = info.getMacAddress();
                if ((wifiMac != null) && (!wifiMac.equals(""))) deviceId += "W" + wifiMac;
                // IMEI
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                IMEI = tm.getDeviceId();
                if ((IMEI != null) && (!(IMEI.equals("") || IMEI.equals("zeros") || (IMEI.equals("asterisks")))))
                    deviceId += "I" + IMEI;
                // SN
                SN = tm.getSimSerialNumber();
                if ((SN != null) && (!SN.equals(""))) deviceId += "S" + SN;
                // ANDROID_ID
                androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                if ((androidId != null) && (!(androidId.equals("") || (androidId.equals("9774d56d682e549c")))))
                    deviceId += "A" + androidId;
                if (!deviceId.equals("")) return get32MD5Str(deviceId);
                // 如果上面都没有,则随机生成一个ID

                return get32MD5Str(getRandomId());

            } catch (Exception e) {
                e.printStackTrace();
                return get32MD5Str(getRandomId());
            }
        } else {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((Activity)context).requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},1);
            }*/
            return get32MD5Str(getRandomId());
        }

    }

    public static String getRandomId() {
        Random ran = new Random(System.currentTimeMillis());
        return "R" + ran.nextInt(10000) + "" + ran.nextInt(10000) + "" + ran.nextInt(10000) + "" + ran.nextInt(10000) + "" + ran.nextInt(10000);
    }
}
