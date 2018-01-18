package com.winguo.personalcenter.wallet.bankcard.model;

import android.content.Context;

/**
 * Created by admin on 2017/10/30.
 * 银行卡 功能
 */

public interface IBankcard {

    void getAccountBankCardList(Context context);

    void getAccountBankCardDetail(Context context,String p_bid);

    void addBankCard(Context context);

    void getBankList(Context context);

    void delBankCard(Context context,String p_bid);

    void modifyBankCard(Context context);

    void getWinguoBalanceWithdraw(Context context,String amount,String p_bid,String paypd);
}
