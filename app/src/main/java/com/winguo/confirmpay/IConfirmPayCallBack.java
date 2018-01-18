package com.winguo.confirmpay;


import com.winguo.confirmpay.modle.bean.MergeOrderBean;
import com.winguo.confirmpay.modle.bean.PayOrderBean;
import com.winguo.confirmpay.modle.bean.PayResultBean;
import com.winguo.confirmpay.modle.bean.WXPayResultBean;
import com.winguo.pay.modle.bean.BalancePayBean;
import com.winguo.pay.modle.bean.ReChargeBean;

/**
 * Created by Admin on 2017/1/14.
 */

public interface IConfirmPayCallBack {
    void onBackmergeOrderData(MergeOrderBean mergeOrderBean);

    //支付宝支付的数据
    void onBackPayOrderData(PayOrderBean payOrderBean);

    //支付的结果
    void onBackPayResultData(PayResultBean payResultBean);


    //微信支付的数据
    void onBackWXPayOrderData(WXPayResultBean wxPayResultBean);


    //网银支付的数据
    //void onBackUnionPayOrdertData(UnionpayOrderBean unionpayOrderBean);

    //余额充值
    void onBackRechargeOrder(ReChargeBean reChargeBean);

    void onBackBalancePayOrder(BalancePayBean balancePayBean);

}
