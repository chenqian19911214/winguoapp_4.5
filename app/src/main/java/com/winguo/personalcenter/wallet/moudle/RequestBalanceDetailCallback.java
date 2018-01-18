package com.winguo.personalcenter.wallet.moudle;

import com.winguo.base.BaseRequestCallBack;
import com.winguo.personalcenter.wallet.bean.BalanceDetail;
import com.winguo.personalcenter.wallet.bean.CashCouponDetail;

/**
 * Created by admin on 2017/10/24.
 * 请求可提现明细回调
 */

public interface RequestBalanceDetailCallback extends BaseRequestCallBack {
    void requestBalanceDetailResult(BalanceDetail balanceDetail);
    void requestBalanceDetailErrorCode(int errorcode);
}
