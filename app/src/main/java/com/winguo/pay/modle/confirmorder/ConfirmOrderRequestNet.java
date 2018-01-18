package com.winguo.pay.modle.confirmorder;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.pay.ISeekUserInfoCallBack;
import com.winguo.pay.modle.bean.ConfirmOrderBean;
import com.winguo.pay.modle.bean.DataBean;
import com.winguo.pay.modle.bean.DetailBean;
import com.winguo.pay.modle.bean.GoodsItemsBean;
import com.winguo.pay.modle.bean.ShopInfoBean;
import com.winguo.pay.modle.bean.TempItemsBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

/**
 * Created by Admin on 2017/1/11.
 */

public class ConfirmOrderRequestNet {
    String url;
    public void getOrderData(Context context,String sku_ids, int is_prompt, ISeekUserInfoCallBack iSeekUserInfoCallBack){
        if (SPUtils.contains(context,"accountName")){

            //获取用户id,因为使用map集合出现问题,直接拼接URL使用
            try {
                String hashUserId =  WinguoAccountDataMgr.getHashCommon(context);
                url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_cart)+
                        "?a=confirm&hash="+ hashUserId+"&sku_ids="+sku_ids+"&is_prompt="+is_prompt;
                CommonUtil.printI("订单URL++++",url);
                request(iSeekUserInfoCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void request(final ISeekUserInfoCallBack iSeekUserInfoCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.CONFIRM_ORDER_REQUEST_TAG, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("确认订单信息网络数据:=", result);
                        if (result != null) {
                            //解析数据
                              GsonBuilder builder=new GsonBuilder();
                            builder.registerTypeAdapter(DetailBean.class,new DetailBeanDeserializer());
                            builder.registerTypeAdapter(ShopInfoBean.class,new ShopInfoDeserializer());
                            builder.registerTypeAdapter(TempItemsBean.class,new TempItemsBeanDeserializer());
                            builder.registerTypeAdapter(GoodsItemsBean.class,new GoodsItemsBeanDeserializer());
                            builder.registerTypeAdapter(DataBean.class,new DataBeanDeserializer());
                            Gson gson = builder.create();
                            final ConfirmOrderBean confirmOrderBean = gson.fromJson(result, ConfirmOrderBean.class);
                            CommonUtil.printI("确认订单信息解析后数据:=", result);
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iSeekUserInfoCallBack.onConfirmBackData(confirmOrderBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iSeekUserInfoCallBack.onConfirmBackData(null);
                    }

                });
            }
        });
    }

    ;
}
