package com.winguo.personalcenter.wallet.control;

import android.content.Context;

import com.winguo.base.BaseRequestCallBack;
import com.winguo.personalcenter.wallet.moudle.IMyWallet;
import com.winguo.personalcenter.wallet.moudle.MyWalletRequestNet;
import com.winguo.personalcenter.wallet.moudle.RequestBalanceCallback;
import com.winguo.personalcenter.wallet.moudle.RequestBalanceDetailCallback;
import com.winguo.personalcenter.wallet.moudle.RequestBalanceWithdrawCashCallback;
import com.winguo.personalcenter.wallet.moudle.RequestCashCouponDetailCallback;
import com.winguo.personalcenter.wallet.moudle.RequestPrestoreDetailCallback;
import com.winguo.utils.WinguoAcccountManagerUtils;

/**
 * Created by admin on 2017/10/24.
 * 我的钱包
 */

public class MyWalletControl implements IMyWallet {

    private BaseRequestCallBack requestCallBack;
    private final MyWalletRequestNet requestNet;

    public MyWalletControl(BaseRequestCallBack requestCallBack) {
        this.requestCallBack = requestCallBack;
        requestNet = new MyWalletRequestNet();
    }

    /**
     * 获取余额
     * @param context
     */
    @Override
    public void getWinguoBalance(Context context) {
        requestNet.requestBalance(context,(RequestBalanceCallback)requestCallBack);
    }

    /**
     * 获取现金券明细
     * @param context
     */
    @Override
    public void getCashCouponDetail(Context context) {
        requestNet.requestCashCouponDetail(context, (RequestCashCouponDetailCallback) requestCallBack);
    }

    /**
     * 获取现金券 余额 及创客基金
     * @param context
     */
    @Override
    public void getBalanceAndCash(Context context) {
        requestNet.requestBalanceAndCash(context, (WinguoAcccountManagerUtils.IBalanceAndCashCallback) requestCallBack);
    }

    /**
     * 预存明细
     * @param context
     */
    @Override
    public void getPrestoreDetail(Context context) {
        requestNet.requestPrestoreDetail(context, (RequestPrestoreDetailCallback) requestCallBack);
    }

    /**
     * 可提现明细
     * @param context
     */
    @Override
    public void getBalanceDetail(Context context,int page,int flag,int type,int limit) {
        requestNet.requestBalanceDetail(context,page,flag,type,limit, (RequestBalanceDetailCallback) requestCallBack);
    }

}
