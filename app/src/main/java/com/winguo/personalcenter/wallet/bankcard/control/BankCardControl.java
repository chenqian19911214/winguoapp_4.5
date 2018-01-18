package com.winguo.personalcenter.wallet.bankcard.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.winguo.R;
import com.winguo.base.BaseRequestCallBack;
import com.winguo.personalcenter.wallet.bankcard.model.IBankcard;
import com.winguo.personalcenter.wallet.bankcard.model.MyBankCardRequestNet;
import com.winguo.personalcenter.wallet.bankcard.model.RequestAccountBankCardDeleteCallback;
import com.winguo.personalcenter.wallet.bankcard.model.RequestAccountBankCardDetailCallback;
import com.winguo.personalcenter.wallet.bankcard.model.RequestAccountBankCardListCallback;
import com.winguo.personalcenter.wallet.bankcard.model.RequestBankListCallback;
import com.winguo.personalcenter.wallet.moudle.RequestBalanceWithdrawCashCallback;

/**
 * Created by admin on 2017/10/30.
 * 银行卡
 */

public class BankCardControl implements IBankcard {

    private BaseRequestCallBack requestCallBack;
    private final MyBankCardRequestNet requestNet;

    public BankCardControl(BaseRequestCallBack requestCallBack) {
        this.requestCallBack = requestCallBack;
        requestNet = new MyBankCardRequestNet();
    }

    /**
     * 获取账户绑定 银行卡列表
     * @param context
     */
    @Override
    public void getAccountBankCardList(Context context) {
        requestNet.requestBankCardList(context, (RequestAccountBankCardListCallback) requestCallBack);
    }

    /**
     * 获取指定银行卡详情
     * @param context
     * @param p_bid
     */
    @Override
    public void getAccountBankCardDetail(Context context, String p_bid) {
        requestNet.requestBankCardDetail(context,p_bid, (RequestAccountBankCardDetailCallback) requestCallBack);
    }

    @Override
    public void addBankCard(Context context) {

    }

    @Override
    public void getBankList(Context context) {
        requestNet.getBankList(context, (RequestBankListCallback) requestCallBack);
    }

    @Override
    public void delBankCard(Context context,String p_bid) {
        requestNet.deleteBankCard(context,p_bid, (RequestAccountBankCardDeleteCallback) requestCallBack);
    }

    @Override
    public void modifyBankCard(Context context) {

    }
    @Override
    public void getWinguoBalanceWithdraw(Context context, String amount, String p_bid, String paypd) {
        requestNet.requestBalanceWithdraw(context,amount,p_bid,paypd,(RequestBalanceWithdrawCashCallback)requestCallBack);
    }

    public int getBankIcon(@NonNull String bankName){
        int resid = 0;
        switch (bankName) {
            case "中国工商银行":
                resid = R.drawable.hybrid4_account_bank_icon_1;
                break;
            case "中国农业银行":
                resid = R.drawable.hybrid4_account_bank_icon_2;
                break;
            case "中国银行":
                resid = R.drawable.hybrid4_account_bank_icon_3;
                break;
            case "中国建设银行":
                resid = R.drawable.hybrid4_account_bank_icon_4;
                break;
            case "中国交通银行":
                resid = R.drawable.hybrid4_account_bank_icon_5;
                break;
            case "中国邮政储蓄银行":
                resid = R.drawable.hybrid4_account_bank_icon_6;
                break;
            case "中信银行":
                resid = R.drawable.hybrid4_account_bank_icon_7;
                break;
            case "光大银行":
                resid = R.drawable.hybrid4_account_bank_icon_8;
                break;
            case "华夏银行":
                resid = R.drawable.hybrid4_account_bank_icon_9;
                break;
            case "民生银行":
                resid = R.drawable.hybrid4_account_bank_icon_10;
                break;
            case "广东发展银行":
                resid = R.drawable.hybrid4_account_bank_icon_11;
                break;
            case "招商银行":
                resid = R.drawable.hybrid4_account_bank_icon_12;
                break;
            case "兴业银行":
                resid = R.drawable.hybrid4_account_bank_icon_13;
                break;
            case "平安银行":
                resid = R.drawable.hybrid4_account_bank_icon_14;
                break;
            case "浦发银行":
                resid = R.drawable.hybrid4_account_bank_icon_15;
                break;
            case "北京银行":
                resid = R.drawable.hybrid4_account_bank_icon_16;
                break;
            case "徽商银行":
                resid = R.drawable.hybrid4_account_bank_icon_17;
                break;
            case "江苏银行":
                resid = R.drawable.hybrid4_account_bank_icon_18;
                break;
            case "金华银行":
                resid = R.drawable.hybrid4_account_bank_icon_19;
                break;
            case "南京银行":
                resid = R.drawable.hybrid4_account_bank_icon_20;
                break;
            case "宁波银行":
                resid = R.drawable.hybrid4_account_bank_icon_21;
                break;
            case "上海银行":
                resid = R.drawable.hybrid4_account_bank_icon_22;
                break;
            case "温州银行":
                resid = R.drawable.hybrid4_account_bank_icon_23;
                break;

        }
        return resid;
    }
    //获取银行背景
    public int getBankIconBG(@NonNull String bankName){
        int resid = 0;
        switch (bankName) {
            case "中国工商银行":
                resid = R.drawable.my_bank_card_item_red_bg;
                break;
            case "中国农业银行":
                resid = R.drawable.my_bank_card_item_green_bg;
                break;
            case "中国银行":
                resid = R.drawable.my_bank_card_item_red_bg;
                break;
            case "中国建设银行":
                resid = R.drawable.my_bank_card_item_blue_bg;
                break;
            case "中国交通银行":
                resid = R.drawable.my_bank_card_item_blue_bg;
                break;
            case "中国邮政储蓄银行":
                resid = R.drawable.my_bank_card_item_green_bg;
                break;
            case "中信银行":
                resid = R.drawable.my_bank_card_item_red_bg;
                break;
            case "光大银行":
                resid = R.drawable.my_bank_card_item_yellow_bg;
                break;
            case "华夏银行":
                resid = R.drawable.my_bank_card_item_red_bg;
                break;
            case "民生银行":
                resid = R.drawable.my_bank_card_item_green_bg;
                break;
            case "广东发展银行":
                resid = R.drawable.my_bank_card_item_red_bg;
                break;
            case "招商银行":
                resid = R.drawable.my_bank_card_item_red_bg;
                break;
            case "兴业银行":
                resid = R.drawable.my_bank_card_item_blue_bg;
                break;
            case "平安银行":
                resid = R.drawable.my_bank_card_item_orange_bg;
                break;
            case "浦发银行":
                resid = R.drawable.my_bank_card_item_blue_bg;
                break;
            case "北京银行":
                resid = R.drawable.my_bank_card_item_red_bg;
                break;
            case "徽商银行":
                resid = R.drawable.my_bank_card_item_red_bg;
                break;
            case "江苏银行":
                resid = R.drawable.my_bank_card_item_blue_bg;
                break;
            case "金华银行":
                resid = R.drawable.my_bank_card_item_orange_bg;
                break;
            case "南京银行":
                resid = R.drawable.my_bank_card_item_red_bg;
                break;
            case "宁波银行":
                resid = R.drawable.my_bank_card_item_orange_bg;
                break;
            case "上海银行":
                resid = R.drawable.my_bank_card_item_yellow_bg;
                break;
            case "温州银行":
                resid = R.drawable.my_bank_card_item_yellow_bg;
                break;

        }
        return resid;
    }
}
