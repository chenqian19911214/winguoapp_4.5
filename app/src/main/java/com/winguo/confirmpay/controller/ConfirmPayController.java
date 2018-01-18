package com.winguo.confirmpay.controller;


import android.content.Context;

import com.winguo.confirmpay.IConfirmPayCallBack;
import com.winguo.confirmpay.modle.bean.MergeOrderBean;
import com.winguo.confirmpay.modle.MergeOrderRequestNet;
import com.winguo.confirmpay.modle.bean.PayOrderBean;
import com.winguo.confirmpay.modle.PayOrderDataRequestNet;
import com.winguo.confirmpay.modle.bean.PayResultBean;
import com.winguo.confirmpay.modle.PayResultDataRequestNet;
import com.winguo.confirmpay.modle.bean.WXPayResultBean;
import com.winguo.confirmpay.view.IConfirmPayView;
import com.winguo.pay.modle.bean.BalancePayBean;
import com.winguo.pay.modle.bean.ReChargeBean;

/**
 * Created by Admin on 2017/1/14.
 * 订单支付
 */

public class ConfirmPayController implements IConfirmPayCallBack {

    private IConfirmPayView iConfirmPayView;
    private final MergeOrderRequestNet mergeOrderRequestNet;
    private final PayOrderDataRequestNet payOrderDataRequestNet;
    private final PayResultDataRequestNet payResultDataRequestNet;

    public ConfirmPayController(IConfirmPayView iConfirmPayView) {

        this.iConfirmPayView = iConfirmPayView;
        mergeOrderRequestNet = new MergeOrderRequestNet();
        payOrderDataRequestNet = new PayOrderDataRequestNet();
        payResultDataRequestNet = new PayResultDataRequestNet();
    }

    /**
     * 获取合并订单的数据
     *
     * @param order_ids
     */
    public void getMergeOrderData(Context context, String order_ids) {
        mergeOrderRequestNet.getMergeOrderData(context, order_ids, this);
    }

    /**
     * 获取支付宝支付数据
     *
     * @param type
     * @param paytype
     * @param orderNumber
     */
    public void getPayOrderData(Context context, int type, int paytype, String orderNumber) {
        payOrderDataRequestNet.getPayOrderData(context, type, paytype, orderNumber,this);
    }

    /**
     * 余额支付
     * @param context
     * @param type
     * @param orderNumber
     * @param pa  支付密码
     */
    public void getBalancePayOrderData(Context context, int type, String orderNumber,String pa) {
        payOrderDataRequestNet.getBalancePayOrderData(context, type, orderNumber,pa,1,0, this);
    }

    /**
     * @param context 上下文
     * @param price   价格
     * @param orderId 支付单号
     */
    //public void getUnionpayOrderData(Context context, String price, String orderId) {
    //    payOrderDataRequestNet.getUnionpayOrderData(context, price, orderId, this);
    //}

    /**
     * 支付结果数据
     *
     * @param orderNumber
     */
    public void getPayResultData(Context context, String orderNumber) {
        payResultDataRequestNet.getPayOrderData(context, orderNumber, this);
    }

    @Override
    public void onBackmergeOrderData(MergeOrderBean mergeOrderBean) {
        iConfirmPayView.mergeOrderData(mergeOrderBean);
    }

    @Override
    public void onBackPayOrderData(PayOrderBean payOrderBean) {
        iConfirmPayView.payOrderData(payOrderBean);
    }

    @Override
    public void onBackPayResultData(PayResultBean payResultBean) {
        iConfirmPayView.payResultData(payResultBean);
    }

    //@Override
    //public void onBackUnionPayOrdertData(UnionpayOrderBean unionpayOrderBean) {
    //    iConfirmPayView.unionPayOrderData(unionpayOrderBean);
    //}


    //调起微信支付
    @Override
    public void onBackWXPayOrderData(WXPayResultBean wxPayResultBean) {
        iConfirmPayView.wxPayOrderData(wxPayResultBean);
    }

    //余额充值:生成支付订单
    public void getRechargeOrder(Context context, int op, double amount) {
        mergeOrderRequestNet.getRechargeOrder(context, op, amount, this);
    }

    //余额充值:生成支付订单结果
    @Override
    public void onBackRechargeOrder(ReChargeBean reChargeBean) {
        iConfirmPayView.getRechargeOrder(reChargeBean);
    }

    //余额支付订单
    @Override
    public void onBackBalancePayOrder(BalancePayBean balancePayBean) {
        iConfirmPayView.balancePayOrderData(balancePayBean);
    }

}
