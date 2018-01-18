package com.guobi.gfc.VoiceFun.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Patterns;
import android.webkit.URLUtil;

import com.guobi.gfc.gbmiscutils.config.GBManifestConfig;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import java.net.URLEncoder;
import java.util.Locale;


public final class BrowserUtils {


    // 使用正则表达式对当前用户搜索的内容进行判断
    // 如果是网址则直接打开该网址
    private static String isWebSite(String content) {
        if (content == null || content.length() <= 0)
            return null;
        if (Patterns.WEB_URL.matcher(content).matches()) {
            return URLUtil.guessUrl(content);
        }
        return null;
    }

    // 获取版本号
    private static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void startWinguoWebSearch(Context context,
                                            String key,
                                            double latitude,
                                            double longitude,
                                            String addr) {
        if (GBLogUtils.DEBUG) {
            GBLogUtils.DEBUG_DISPLAY("BrowserUtils", "@@@@startWinguoWebSearch with:" +
                    latitude + "," + longitude + ",addr:" + addr);
        }

        try {
            final StringBuffer url;
            //final boolean supportLBS=true;
            url = new StringBuffer("http://k.winguo.com/apikw?a=search");
            if (key != null && key.length() > 0) {
                url.append("&kw=").append(URLEncoder.encode(key));
            }

            // 在URL上添加统计信息
            String locale = Locale.getDefault().toString();
            url.append("&locale=").append(locale);
            url.append("&channel=").append(GBManifestConfig.getMetaDataValue(context, "UMENG_CHANNEL"));
            url.append("&pkgName=").append(context.getPackageName());
            url.append("&softversion=").append(getVersionName(context));
            // 添加位置信息
            url.append("&location=").append(latitude);
            url.append(",").append(longitude);

            startBrowser(context, url.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/*
	public static final void startInnerBrowser(Context context,String url){
		Intent intent = new Intent(context, BrowserActivity.class);
		intent.putExtra("TargetUrl", url);
		context.startActivity(intent);
	}
	*/

    public static final void startBrowser(Context context, String url) {
        if (GBLogUtils.DEBUG) {
            GBLogUtils.DEBUG_DISPLAY("BrowserUtils", "@@@@startBrowser:" + url);
        }
		
		/*
		if(sUseInnerBrowser){
			startInnerBrowser(context,url);
			return;
		}
		*/

        try {
            Intent intent = new Intent();
            //intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            //intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getGoogleSearchURL(String key) {
        String url = "http://www.google.com/search?";

        String language;
        if (Locale.getDefault().getLanguage().equals(Locale.CHINA.getLanguage())) {
            language = "&hl=zh-CN";
        } else {
            language = "&hl=en";
        }

        if (key != null && key.length() > 0) {
            url = url + "q=" + URLEncoder.encode(key) + language;
        }
        //return Uri.parse(url);
        return url;
    }

}
