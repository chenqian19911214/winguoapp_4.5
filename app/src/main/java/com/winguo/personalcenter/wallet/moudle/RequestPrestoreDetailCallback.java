package com.winguo.personalcenter.wallet.moudle;

import com.winguo.base.BaseRequestCallBack;
import com.winguo.personalcenter.wallet.bean.CashCouponDetail;
import com.winguo.personalcenter.wallet.bean.PrestoreDetail;

/**
 * Created by admin on 2017/10/24.
 * 请求预存明细回调
 */

public interface RequestPrestoreDetailCallback extends BaseRequestCallBack {
    void requestPrestoreDetailResult(PrestoreDetail prestoreDetail);
    void requestPrestoreDetailErrorCode(int errorcode);
}
