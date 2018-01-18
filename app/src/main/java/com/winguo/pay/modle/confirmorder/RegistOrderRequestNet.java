package com.winguo.pay.modle.confirmorder;


import android.content.Context;

import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.pay.IConfirmOrderCallBack;
import com.winguo.pay.modle.bean.OrderResultBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

/**
 * Created by Admin on 2017/1/13.
 */

public class RegistOrderRequestNet {
    public void getAddressData(Context context,String sku_ids, String type, String shop_ids, String ct, String temp_ids,
                               String memo, String recid, int is_prompt,boolean is_use, IConfirmOrderCallBack iConfirmOrderCallBack){
        if (SPUtils.contains(context,"accountName")){//没有登录时不能加入购物车

            //获取用户id,因为使用map集合出现问题,直接拼接URL使用
            try {
                String hashUserId =  WinguoAccountDataMgr.getHashCommon(context);
                String url = null;
                if (is_use) {  //是否使用现金券
                     url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_cart) +
                            "?a=regist&hash=" + hashUserId + "&sku_ids=" + sku_ids + "&type=" + type + "&shop_ids=" + shop_ids + "&ct=" + ct +
                            "&temp_ids=" + temp_ids + "&memo=" + memo + "&recid=" + recid + "&is_prompt=" + is_prompt+"&cash_coupon=1";
                } else {
                     url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_cart) +
                            "?a=regist&hash=" + hashUserId + "&sku_ids=" + sku_ids + "&type=" + type + "&shop_ids=" + shop_ids + "&ct=" + ct +
                            "&temp_ids=" + temp_ids + "&memo=" + memo + "&recid=" + recid + "&is_prompt=" + is_prompt;
                }
                request(url, iConfirmOrderCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void request(final String url, final IConfirmOrderCallBack iConfirmOrderCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.CONFIRM_PAY_REQUEST_TAG, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("下订单网络数据:=", result);
                        if (result != null) {
                            //解析数据
                            final OrderResultBean orderResultBean = GsonUtil.json2Obj(result, OrderResultBean.class);
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iConfirmOrderCallBack.completeOrderCallBack(orderResultBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iConfirmOrderCallBack.completeOrderCallBack(null);
                    }

                });
            }
        });
    }
}
