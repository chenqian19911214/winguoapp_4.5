package com.winguo.personalcenter.wallet.bankcard.model;

import com.guobi.account.WinguoAccountBankCard;
import com.winguo.base.BaseRequestCallBack;

/**
 * Created by admin on 2017/10/30.
 * 请求银行卡详情回调
 */

public interface RequestAccountBankCardDeleteCallback extends BaseRequestCallBack{
    void requestAccountBankCardDelete(String result);
    void requestAccountBankCardDeleteErrorCode(int errorcode);
}
