package com.winguo.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.bean.ActivityTheme;
import com.winguo.bean.TodayShop;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 首页数据 获取帮助类
 * Created by admin on 2017/5/15.
 */

public class HomeDatahandle{

    public final static  int TODAY_SHOPP_FLAG_SUCCESS = 0X7894;
    public final static  int TODAY_SHOPP_FLAG_FALSE = 0X78946;
    public final static  int THEME_ACTIVITY_FLAG_SUCCESS = 0X15978;
    public final static  int THEME_ACTIVITY_FLAG_FALSE = 0X35789;

    public static void takeShopData(final Handler mHandler) {
        MyOkHttpUtils.post(UrlConstant.TODAY_SHOP,0,null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                try {
                    GBLogUtils.DEBUG_DISPLAY("dnw today shop: result",result);
                    JSONObject root = new JSONObject(result);
                    String items = root.getString("items");
                    List<TodayShop> todayShops = GsonUtil.json2List(items, TodayShop.class);
                    if (todayShops == null) {
                        mHandler.sendEmptyMessage(HomeDatahandle.TODAY_SHOPP_FLAG_FALSE);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = HomeDatahandle.TODAY_SHOPP_FLAG_SUCCESS;
                        msg.obj = todayShops;
                        mHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(HomeDatahandle.TODAY_SHOPP_FLAG_FALSE);
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                mHandler.sendEmptyMessage(HomeDatahandle.TODAY_SHOPP_FLAG_FALSE);
            }
        });
    }

    /**
     * 获取主题活动 数据
     * @param mHandler
     */
    public static void takeThemeData(final Handler mHandler) {
        CommonUtil.printI("dnw theme activity: final  ",UrlConstant.THEME_ACTIVITY);
        MyOkHttpUtils.post(UrlConstant.THEME_ACTIVITY,0,null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                CommonUtil.printI("dnw theme activity: result  ",result);
               try {

                    JSONObject root = new JSONObject(result);
                    String data = root.getString("Result");

                   ActivityTheme activityTheme = GsonUtil.json2Obj(data, ActivityTheme.class);
                    if (activityTheme == null) {
                        mHandler.sendEmptyMessage(HomeDatahandle.THEME_ACTIVITY_FLAG_FALSE);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = HomeDatahandle.THEME_ACTIVITY_FLAG_SUCCESS;
                        msg.obj = activityTheme;
                        mHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                   mHandler.sendEmptyMessage(HomeDatahandle.THEME_ACTIVITY_FLAG_FALSE);
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                mHandler.sendEmptyMessage(HomeDatahandle.THEME_ACTIVITY_FLAG_FALSE);
            }
        });
    }



}
