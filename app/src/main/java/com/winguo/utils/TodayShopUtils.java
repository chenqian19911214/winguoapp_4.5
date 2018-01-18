package com.winguo.utils;

import android.os.Handler;
import android.os.Message;

import com.winguo.bean.TodayShop;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by admin on 2017/3/20.
 */
@Deprecated
public class TodayShopUtils {

    public final static  int TODAY_SHOPP_FLAG_SUCCESS = 0X7894;

    public final static  int TODAY_SHOPP_FLAG_FALSE = 0X78946;

    public static void takeShopData(final Handler mHandler) {

        MyOkHttpUtils.post(UrlConstant.TODAY_SHOP,0,null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {

                try {
                    CommonUtil.printI("dnw today shop: result",result);
                    JSONObject root = new JSONObject(result);
                    String items = root.getString("items");

                    List<TodayShop> todayShops = GsonUtil.json2List(items, TodayShop.class);
                    if (todayShops == null) {
                        mHandler.sendEmptyMessage(TodayShopUtils.TODAY_SHOPP_FLAG_FALSE);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = TodayShopUtils.TODAY_SHOPP_FLAG_SUCCESS;
                        msg.obj = todayShops;
                        mHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            @Override
            public void exceptionMessage(String message) {
                mHandler.sendEmptyMessage(TodayShopUtils.TODAY_SHOPP_FLAG_FALSE);
            }
        });
    }




}
