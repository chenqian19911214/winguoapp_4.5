package com.winguo.home;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.home.bean.BannerBean;
import com.winguo.home.bean.ItemsBean;
import com.winguo.home.bean.MessageBean;
import com.winguo.net.IStringCallBack;
import com.winguo.net.MyOkHttpUtils;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.UrlConstant;

/**
 * Created by Admin on 2017/2/8.
 */

public class BannerRequestNet {


    public void getBannerData(final Handler handler) {


        MyOkHttpUtils.post(UrlConstant.BANNER_URL, RequestCodeConstant.REQUEST_BANNER_CODE, null, new IStringCallBack() {
            @Override
            public int stringReturn(String result) {
                try {

                    Log.i("chenqianokhtp","广告:"+result);
                    GBLogUtils.DEBUG_DISPLAY("广告位数据", result);
                    GsonBuilder builder = new GsonBuilder();

                    builder.registerTypeAdapter(MessageBean.class, new MessageBeanDeserializer());
                    builder.registerTypeAdapter(ItemsBean.class, new ItemsBeanDeserializer());
                    Gson gson = builder.create();
                    BannerBean bannerBean = gson.fromJson(result, BannerBean.class);

                    Log.i("chenqianokhtp","广告image:json:"+bannerBean.message.items.data.get(0).image);
                    Log.i("chenqianokhtp","广告url:json:"+bannerBean.message.items.data.get(0).url);

                    if (bannerBean == null) {
                        handler.sendEmptyMessage(RequestCodeConstant.REQUEST_BANNER_CODE_FALSE);
                    } else {
                        Message message = handler.obtainMessage();
                        message.obj = bannerBean;
                        message.what = RequestCodeConstant.BANNER_MAGESS;
                        handler.sendMessage(message);
                    }

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(RequestCodeConstant.REQUEST_BANNER_CODE_FALSE);
                }
                return 1;
            }

            @Override
            public void exceptionMessage(String message) {
                handler.sendEmptyMessage(RequestCodeConstant.REQUEST_BANNER_CODE_FALSE);
            }
        });

    }


}
