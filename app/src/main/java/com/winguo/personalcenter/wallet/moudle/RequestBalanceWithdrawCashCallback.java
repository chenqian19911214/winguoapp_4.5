package com.winguo.personalcenter.wallet.moudle;

import com.winguo.base.BaseRequestCallBack;

/**
 * 请求余额提现
 */

public interface RequestBalanceWithdrawCashCallback extends BaseRequestCallBack {
    void requestBalanceWithdrawCashResult(String result);
    void requestBalanceWithdrawCashErrorCode(int errorcode);
}
