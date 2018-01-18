package com.winguo.personalcenter.wallet.moudle;

import com.winguo.base.BaseRequestCallBack;
import com.winguo.personalcenter.wallet.bean.CashCouponDetail;

/**
 * Created by admin on 2017/10/24.
 * 请求现金券明细回调
 */

public interface RequestCashCouponDetailCallback extends BaseRequestCallBack {
    void requestCashCouponDetailResult(CashCouponDetail cashCouponDetail);
    void requestCashCouponDetailErrorCode(int errorcode);
}
