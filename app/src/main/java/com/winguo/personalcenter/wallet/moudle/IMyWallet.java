package com.winguo.personalcenter.wallet.moudle;

import android.content.Context;

/**
 * Created by admin on 2017/10/24.
 * 我的钱包实现
 */

public interface IMyWallet {
    /**
     * 获取账户余额
     * @param context
     */
    void getWinguoBalance(Context context);

    /**
     * 获取现金券明细
     * @param context
     */
    void getCashCouponDetail(Context context);
    /**
     * 获取现金券 余额 及创客基金
     * @param context
     */
    void getBalanceAndCash(Context context);

    /**
     * 预存金额交易明细
     * @param context
     */
    void getPrestoreDetail(Context context);

    /**
     * 可提现交易明细
     * @param context
     */
    void getBalanceDetail(Context context,int page,int flag,int type,int limit);
}
