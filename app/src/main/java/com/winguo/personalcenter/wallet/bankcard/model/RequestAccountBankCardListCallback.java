package com.winguo.personalcenter.wallet.bankcard.model;

import com.guobi.account.WinguoAccountBalance;
import com.guobi.account.WinguoAccountBankCard;
import com.winguo.base.BaseRequestCallBack;

import java.util.List;

/**
 * Created by admin on 2017/10/30.
 * 请求银行卡列表回调
 */

public interface RequestAccountBankCardListCallback extends BaseRequestCallBack{
    void requestAccountBankCardList(List<WinguoAccountBankCard> cards);
    void requestAccountBankCardListErrorCode(int errorcode);
}
