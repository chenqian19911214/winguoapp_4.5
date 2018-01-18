package com.winguo.confirmpay.view;


import com.winguo.confirmpay.modle.bean.MergeOrderBean;
import com.winguo.confirmpay.modle.bean.PayOrderBean;
import com.winguo.confirmpay.modle.bean.PayResultBean;
import com.winguo.confirmpay.modle.bean.WXPayResultBean;
import com.winguo.pay.modle.bean.BalancePayBean;
import com.winguo.pay.modle.bean.ReChargeBean;

/**
 * Created by Admin on 2017/1/14.
 */

public interface IConfirmPayView {
    void mergeOrderData(MergeOrderBean mergeOrderBean);

    void payOrderData(PayOrderBean payOrderBean);

    void payResultData(PayResultBean payResultBean);

    void wxPayOrderData(WXPayResultBean wxPayResultBean);

    void balancePayOrderData(BalancePayBean balancePayBean);

    //void unionPayOrderData(UnionpayOrderBean unionpayOrderBean);

    void getRechargeOrder(ReChargeBean reChargeBean);
}
