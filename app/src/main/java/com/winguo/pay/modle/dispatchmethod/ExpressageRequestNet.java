package com.winguo.pay.modle.dispatchmethod;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.pay.modle.bean.ExpressMethodBean;
import com.winguo.pay.modle.bean.ExpressageBean;
import com.winguo.pay.modle.bean.ItemBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

/**
 * Created by Admin on 2017/1/10.
 */

public class ExpressageRequestNet {
    public static final int EXPRESSAGE_METHOD_MESSAGE=0;


    public void getDispatchMethodData(Context context, int temp_id, int shop_id, int recid, Handler handler) {
        if (SPUtils.contains(context,"accountName")) {//没有登录时不能加入购物车

            //获取用户id,因为使用map集合出现问题,直接拼接URL使用
            try {
                String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
               String url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_cart) +
                        "?a=getExpress&hash=" + hashUserId +"&temp_id=" + temp_id + "&shop_id=" + shop_id+"&recid="+recid;
                CommonUtil.printI("请求配送方式的URL",url);
                request(url, temp_id, handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private  void request(final String url, final  int temp_id, final Handler handler ) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.DISPATCH_METHOD_REQUEST_TAG, null, new IStringCallBack2() {


                    @Override
                    public void stringReturn(String result) {
                        ExpressMethodBean expressMethodBean=null;
                        CommonUtil.printI("配送方式网络数据:=", result);
                        if (result != null) {
                            GsonBuilder builder=new GsonBuilder();
                            builder.registerTypeAdapter(ExpressageBean.class,new ExpressageBeanDeserializer());
                            builder.registerTypeAdapter(ItemBean.class,new ItemBeanDeserializer());
                            Gson gson = builder.create();
                            expressMethodBean = gson.fromJson(result, ExpressMethodBean.class);
                            expressMethodBean.temp_id=temp_id;
                        }
                        Message message = handler.obtainMessage();
                        message.what=EXPRESSAGE_METHOD_MESSAGE;
                        message.obj=expressMethodBean;
                        handler.sendMessage(message);
                        CommonUtil.printI("配送方式网络数据每一次获取:=", expressMethodBean.toString());
                    }

                    @Override
                    public void failReturn() {
                        Message message = handler.obtainMessage();
                        message.what=EXPRESSAGE_METHOD_MESSAGE;
                        message.obj=null;
                        handler.sendMessage(message);
                    }

                });
            }
        });
    }
}
