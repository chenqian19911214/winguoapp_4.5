package com.winguo.personalcenter.wallet.moudle;

import com.guobi.account.WinguoAccountBalance;
import com.winguo.base.BaseRequestCallBack;

/**
 * Created by admin on 2017/10/24.
 * 请求余额回调
 */

public interface RequestBalanceCallback extends BaseRequestCallBack {
    void requestBalanceResult(WinguoAccountBalance winguoAccountBalance);
    void requestBalanceErrorCode(int errorcode);
}
