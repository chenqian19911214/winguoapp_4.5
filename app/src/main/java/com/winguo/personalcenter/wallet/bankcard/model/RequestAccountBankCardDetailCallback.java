package com.winguo.personalcenter.wallet.bankcard.model;

import com.guobi.account.WinguoAccountBankCard;
import com.winguo.base.BaseRequestCallBack;

import java.util.List;

/**
 * Created by admin on 2017/10/30.
 * 请求银行卡详情回调
 */

public interface RequestAccountBankCardDetailCallback extends BaseRequestCallBack{
    void requestAccountBankCardDetail(WinguoAccountBankCard card);
    void requestAccountBankCardDetailErrorCode(int errorcode);
}
