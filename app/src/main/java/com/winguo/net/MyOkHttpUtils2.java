package com.winguo.net;


import android.util.Log;

import com.winguo.net.IStringCallBack2;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.XmlUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by on 2016/9/22.
 */
public class MyOkHttpUtils2 {

    /**
     * post请求
     *
     * @param url        请求url
     * @param tag        请求标记
     * @param map        请求参数，存入map集合中
     * @param stringBack 请求回调
     */
    public static void post(String url, final int tag, HashMap<String, String> map, final IStringCallBack2 stringBack) {
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .tag(tag)
                .build()
                .connTimeOut(5000)
                .execute(new StringCallback() {
                    @Override
                    public boolean validateReponse(Response response, int id) {
                        if (!response.isSuccessful()) {
                            stringBack.failReturn();
                        }
                        return super.validateReponse(response, id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        if(e!=null){
                            CommonUtil.printD("请求失败：=", e.toString());//D/请求失败：=: java.net.SocketTimeoutException
                            stringBack.failReturn();
                        }

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        boolean json = isJson(response);
                        if (json && response != null) {
                            stringBack.stringReturn(response);
                        } else {
                            response = XmlUtil.xmlToJson(response);
                            stringBack.stringReturn(response);
                        }

                    }

                });
    }


    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String value) {
        try {
            new JSONObject(value);

        } catch (JSONException e) {
            try {
                new JSONArray(value);
            } catch (JSONException e1) {
                return false;
            }
        }
        return true;
    }

}
