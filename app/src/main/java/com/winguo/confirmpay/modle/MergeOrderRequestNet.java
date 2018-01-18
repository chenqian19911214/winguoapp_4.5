package com.winguo.confirmpay.modle;


import android.content.Context;
import android.util.Log;

import com.guobi.account.WinguoAccountDataMgr;
import com.winguo.R;
import com.winguo.confirmpay.IConfirmPayCallBack;
import com.winguo.confirmpay.modle.bean.MergeOrderBean;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.pay.modle.bean.ReChargeBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;
import com.winguo.utils.UrlConstant;

/**
 * Created by Admin on 2017/1/14.
 */

public class MergeOrderRequestNet {
    public void getMergeOrderData(Context context, String order_ids, IConfirmPayCallBack iConfirmPayCallBack) {
        if (SPUtils.contains(context, "accountName")) {//没有登录时不能加入购物车
            //获取用户id,因为使用map集合出现问题,直接拼接URL使用
            try {
                String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
                String url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_cart) +
                        "?a=mergeOrder&hash=" + hashUserId + "&order_ids=" + order_ids;
                request(url, iConfirmPayCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void request(final String url, final IConfirmPayCallBack iConfirmPayCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.CONFIRM_PAY_REQUEST_TAG, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("合并订单网络数据:=", result);
                        if (result != null) {
                            //解析数据
                            final MergeOrderBean mergeOrderBean = GsonUtil.json2Obj(result, MergeOrderBean.class);
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iConfirmPayCallBack.onBackmergeOrderData(mergeOrderBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iConfirmPayCallBack.onBackmergeOrderData(null);
                    }
                });
            }
        });
    }

    //生成余额充值订单
    public void getRechargeOrder(Context context, int op, double amount, IConfirmPayCallBack iConfirmPayCallBack) {
        if (SPUtils.contains(context, "accountName")) {//没有登录时不能加入购物车
            //获取用户id,因为使用map集合出现问题,直接拼接URL使用
            try {
                String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
                String url = UrlConstant.BASE_URL + UrlConstant.RECHARGEORDER +
                        "?a=apprecharge&did=0&op=" + op + "&amount=" + amount + "&hash=" + hashUserId;
                Log.e("order==== ",""+url);
                requestData(url, iConfirmPayCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //请求数据
    private void requestData(final String url, final IConfirmPayCallBack iConfirmPayCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.RECHARGE_ORDER_TAG, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        if (result != null) {
                            ReChargeBean reChargeBean = GsonUtil.json2Obj(result, ReChargeBean.class);
                            if (reChargeBean.getCode() == 0) {
                                iConfirmPayCallBack.onBackRechargeOrder(reChargeBean);
                            }
                        }
                    }

                    @Override
                    public void failReturn() {
                        iConfirmPayCallBack.onBackRechargeOrder(null);
                    }
                });
            }
        });
    }
}
