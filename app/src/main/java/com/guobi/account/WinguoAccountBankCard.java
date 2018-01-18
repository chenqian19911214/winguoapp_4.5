package com.guobi.account;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

/**
 * Created by chenq on 2017/1/3.
 */
public final class WinguoAccountBankCard {
    /**
     * 流水号
     */
    public String p_bid;

    /**
     * 银行ID，需要获取详情之后才有这个字段
     */
    public String bid;

    /**
     * 银行名称
     */
    public String bankName;
    /**
     * 支行ID
     */
    public String bankBranchesID;
    /**
     * 支行名称
     */
    public String bankBranchesName;
    /**
     * 省份ID
     */
    public String provinceID;
    /**
     * 省份名
     */
    public String provinceName;
    /**
     * 城市ID
     */
    public String cityID;
    /**
     * 城市名
     */
    public String cityName;
    /**
     * 卡尾号
     */
    public String cardNumberTail;

    /**
     * 卡全号，需要获取详情之后才有这个字段
     */
    public String cardNumberFull;

    /**
     * 是否为默认银行卡
     */
    public boolean isDefault;

    /**
     * 账户名
     */
    public String accountName;

    public final WinguoAccountBankCard copy() {
        final WinguoAccountBankCard c = new WinguoAccountBankCard();
        c.p_bid = this.p_bid;
        c.bid = this.bid;
        c.bankName = this.bankName;
        c.bankBranchesID = this.bankBranchesID;
        c.bankBranchesName = this.bankBranchesName;
        c.provinceID = this.provinceID;
        c.provinceName = this.provinceName;
        c.cityID = this.cityID;
        c.cityName = this.cityName;
        c.cardNumberTail = this.cardNumberTail;
        c.cardNumberFull = this.cardNumberFull;
        c.isDefault = this.isDefault;
        c.accountName = this.accountName;
        return c;
    }

    public final void dump() {
        if (GBLogUtils.DEBUG) {
            GBLogUtils.DEBUG_DISPLAY(this, "p_bid:		" + p_bid);
            GBLogUtils.DEBUG_DISPLAY(this, "bid:		" + bid);
            GBLogUtils.DEBUG_DISPLAY(this, "bankName:	" + bankName);
            GBLogUtils.DEBUG_DISPLAY(this, "bankBranchesID:	" + bankBranchesID);
            GBLogUtils.DEBUG_DISPLAY(this, "bankBranchesName:	" + bankBranchesName);
            GBLogUtils.DEBUG_DISPLAY(this, "provinceID:	" + provinceID);
            GBLogUtils.DEBUG_DISPLAY(this, "provinceName:	" + provinceName);
            GBLogUtils.DEBUG_DISPLAY(this, "cityID:	" + cityID);
            GBLogUtils.DEBUG_DISPLAY(this, "cityName:	" + cityName);
            GBLogUtils.DEBUG_DISPLAY(this, "cardTailNumber:		" + cardNumberTail);
            GBLogUtils.DEBUG_DISPLAY(this, "cardFullNumber:		" + cardNumberFull);
            GBLogUtils.DEBUG_DISPLAY(this, "isDefault:	" + isDefault);
            GBLogUtils.DEBUG_DISPLAY(this, "accountName:	" + accountName);
        }
    }
}
