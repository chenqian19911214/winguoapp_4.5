package com.winguo.confirmpay.modle;


import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.guobi.account.NameValuePair;
import com.guobi.account.URLCreator;
import com.guobi.account.WinguoAccountConfig;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountKey;
import com.guobi.account.WinguoEncryption;
import com.winguo.confirmpay.IConfirmPayCallBack;
import com.winguo.confirmpay.modle.bean.PayOrderBean;
import com.winguo.confirmpay.modle.bean.WXPayResultBean;
import com.winguo.net.IStringCallBack2;
import com.winguo.net.MyOkHttpUtils2;
import com.winguo.pay.modle.bean.BalancePayBean;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ThreadUtils;

import java.util.ArrayList;

/**
 * Created by Admin on 2017/1/23.
 * 支付订单（余额 微信 支付宝）
 */

public class PayOrderDataRequestNet {

    /**
     * 余额支付
     * @param context
     * @param type
     * @param orderNumber
     * @param pa
     * @param wallet
     * @param winguo
     * @param iConfirmPayCallBack
     */
    public void getBalancePayOrderData(Context context, int type,String orderNumber, String pa, int wallet, int winguo, final IConfirmPayCallBack iConfirmPayCallBack) {
        // 获取用户名
        WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(context);
        final String id = WinguoAccountDataMgr.getUserName(context);
        final String token = mKey.getToken();
        final String uuid = mKey.getUUID();

        // 创建Hash字符串
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("id=").append(id);
        strBuf.append("&token=").append(token);
        strBuf.append("&uuid=").append(uuid);
        strBuf.append("&pa=").append(pa);
        String hash = WinguoEncryption.commonEncryption(strBuf.toString(), mKey);

        // 生成URL
        final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_OnLinePay;

        ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
        valueList.add(new NameValuePair("hash", hash));// 加密的密码信息
        valueList.add(new NameValuePair("type", String.valueOf(type)));// 加密的密码信息
        valueList.add(new NameValuePair("paytype", String.valueOf(0)));//支付类型：0.支付宝支付，1.网银支付，2.货到付款（订单中只要有一个商品不支持货到付款支付方式，就不显示“货到付款”这种支付方式），3.连连支付
        valueList.add(new NameValuePair("orderNumber", orderNumber));// 订单号码
        valueList.add(new NameValuePair("wallet", String.valueOf(wallet)));// 余额 wallet 是否用问果余额支付：0.否，1.是
        valueList.add(new NameValuePair("winguo", String.valueOf(winguo)));// 余额 winguo 是否用果币支付：0.否，1.是
        //valueList.add(new NameValuePair("pay_method", String.valueOf(2)));// 支付方式:2.快捷支付（借记卡），3.快捷支付（信用卡）
        final String finalUrl = URLCreator.create(url, valueList);

        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(finalUrl, RequestCodeConstant.PAY_REQUEST_TAG, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(final String result) {
                        CommonUtil.printI("余额支付需要的网络数据:=", result);
                      //  {"message":{"status":"error","text":"支付密码验证失败。密码输入错误，支付密码初始密码为登录密码，请重试.","code":-5}}
                        if (result != null) {
                            //解析数据
                            final BalancePayBean balancePayBean = GsonUtil.json2Obj(result, BalancePayBean.class);
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iConfirmPayCallBack.onBackBalancePayOrder(balancePayBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iConfirmPayCallBack.onBackBalancePayOrder(null);
                    }

                });
            }
        });


    }

    /**
     * 微信 支付宝
     *
     * @param context
     * @param type
     * @param paytype
     * @param orderNumber
     * @param iConfirmPayCallBack
     */
    public void getPayOrderData(Context context, int type, int paytype, String orderNumber, IConfirmPayCallBack iConfirmPayCallBack) {
        if (SPUtils.contains(context, "accountName")) {//没有登录时不能加入购物车
            //获取用户id,因为使用map集合出现问题,直接拼接URL使用
            try {

                //使用微信和支付宝
                String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
                  /* String url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_cart)+
                            "?a=onlinePay&hash="+ hashUserId+"&type="+type+"&paytype="+paytype+"&orderNumber="+orderNumber;*/
                // 生成URL
                final String url = WinguoAccountConfig.getDOMAIN() + WinguoAccountConfig.GET_OnLinePay;

                ArrayList<NameValuePair> valueList = new ArrayList<NameValuePair>();
                valueList.add(new NameValuePair("hash", hashUserId));// 加密的密码信息
                valueList.add(new NameValuePair("type", String.valueOf(type)));// 加密的密码信息
                valueList.add(new NameValuePair("paytype", String.valueOf(paytype)));//支付类型：0.支付宝支付，1.网银支付，2.货到付款（订单中只要有一个商品不支持货到付款支付方式，就不显示“货到付款”这种支付方式），3.连连支付
                valueList.add(new NameValuePair("orderNumber", orderNumber));// 订单号码

                String finalUrl = URLCreator.create(url, valueList);
                CommonUtil.printI("finalUrl+++++++++++", finalUrl);
                if (paytype == 0) {
                    //支付宝支付
                    alipayRequest(finalUrl, iConfirmPayCallBack);
                } else if (paytype == 6) {
                    //微信支付
                    WXpayRequest(finalUrl, iConfirmPayCallBack);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //支付宝支付的网络请求
    private void alipayRequest(final String url, final IConfirmPayCallBack iConfirmPayCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.PAY_REQUEST_TAG, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        CommonUtil.printI("支付宝支付需要的网络数据:=", result);
                        if (result != null) {
                            //解析数据
                            final PayOrderBean payOrderBean = GsonUtil.json2Obj(result, PayOrderBean.class);
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iConfirmPayCallBack.onBackPayOrderData(payOrderBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iConfirmPayCallBack.onBackPayOrderData(null);
                    }

                });
            }
        });
    }

    /**
     * 网银支付
     *
     * @param context             上下文
     * @param price               支付价格
     * @param orderNumber         支付单号
     * @param iConfirmPayCallBack 回调
     *//*
    public void getUnionpayOrderData(Context context,String price , String orderNumber, IConfirmPayCallBack iConfirmPayCallBack){
        if (SPUtils.contains(context,"accountName")){//没有登录时不能加入购物车
            //获取用户id,因为使用map集合出现问题,直接拼接URL使用
            try {
                String hashUserId = WinguoAccountDataMgr.getHashCommon(context);
                String url = UrlConstant.BASE_URL + CommonUtil.getAppContext().getResources().getString(R.string.data_unionpay)+
                        "?hash="+ hashUserId+"&txnAmt="+price+"&orderId="+orderNumber;
                CommonUtil.printI("url+++++++++++",url);
                    //网银支付
                unionpayRequest(url,iConfirmPayCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
    //网银支付的网络请求
    /*private void unionpayRequest(final  String url, final IConfirmPayCallBack iConfirmPayCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.UNIONPAY_PAY_REQUEST_TAG, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(final String result) {
                        CommonUtil.printI("网银支付需要的网络数据:=", result);
                        if (result != null) {
                            //解析数据
                            final UnionpayOrderBean unionpayOrderBean = GsonUtil.json2Obj(result, UnionpayOrderBean.class);
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                  iConfirmPayCallBack.onBackUnionPayOrdertData(unionpayOrderBean);
                                }
                            });
                        }
                    }

                    @Override
                    public void failReturn() {
                        iConfirmPayCallBack.onBackUnionPayOrdertData(null);
                    }

                });
            }
        });
    }*/


    //微信支付的网络请求
    private void WXpayRequest(final String url, final IConfirmPayCallBack iConfirmPayCallBack) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpUtils2.post(url, RequestCodeConstant.WX_PAY_REQUEST_TAG, null, new IStringCallBack2() {
                    @Override
                    public void stringReturn(String result) {
                        Log.e("huangchengpai", "微信支付需要的网络数据---->result=" + result);
                        if (result != null) {
                            //解析数据
                            JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(result);
                            JsonObject root = element.getAsJsonObject();
                            String result_code = root.getAsJsonPrimitive("result_code").getAsString();
                            if ("SUCCESS".equals(result_code)) {
                                final WXPayResultBean wxPayResultBean = GsonUtil.json2Obj(result, WXPayResultBean.class);
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iConfirmPayCallBack.onBackWXPayOrderData(wxPayResultBean);
                                    }
                                });
                            } else if ("FAIL".equals(result_code)) {
                                iConfirmPayCallBack.onBackWXPayOrderData(null);
                            }
                        }
                    }

                    @Override
                    public void failReturn() {
                        iConfirmPayCallBack.onBackWXPayOrderData(null);
                    }

                });
            }
        });
    }
}
