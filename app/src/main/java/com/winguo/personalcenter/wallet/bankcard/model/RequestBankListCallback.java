package com.winguo.personalcenter.wallet.bankcard.model;

import com.guobi.account.WinguoAccountBank;
import com.winguo.base.BaseRequestCallBack;

import java.util.List;

/**
 * 请求银行列表回调
 */

public interface RequestBankListCallback extends BaseRequestCallBack{
    void requestBankList(List<WinguoAccountBank> banks);
    void requestBankListErrorCode(int errorcode);
}
