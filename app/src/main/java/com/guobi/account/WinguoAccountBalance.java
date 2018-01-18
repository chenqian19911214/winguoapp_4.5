package com.guobi.account;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import java.io.Serializable;

/**
 * 余额信息
 * Created by chenq on 2017/1/3.
 */
public class WinguoAccountBalance implements Serializable {

    /**
     * 当前余额
     */
    public double balance;
    /**
     * （最迟）提现到账时间
     */
    public String incomeTime;
    /**
     * 提现到帐时间描述
     */
    public String incomeDescription;
    /**
     * 当日可提现次数
     */
    public int withdrawalTimes;

    public float unable_balance;
    /**
     * 分销订单数
     */
    public int shareOrder;
    /**
     * 推荐厂商数
     */
    public int shareMaker;
    /**
     * 推荐开店数
     */
    public int shareCustomer;


    public final WinguoAccountBalance copy() {
        final WinguoAccountBalance c = new WinguoAccountBalance();
        c.balance = this.balance;
        c.incomeDescription = this.incomeDescription;
        c.incomeTime = this.incomeTime;
        c.withdrawalTimes = this.withdrawalTimes;
        c.unable_balance = this.unable_balance;
        return c;
    }

    public final void dump() {
        if (GBLogUtils.DEBUG) {
            GBLogUtils.DEBUG_DISPLAY(this, "balance:		" + balance);
            GBLogUtils.DEBUG_DISPLAY(this, "incomeTime:	" + incomeTime);
            GBLogUtils.DEBUG_DISPLAY(this, "incomeDescription:		" + incomeDescription);
            GBLogUtils.DEBUG_DISPLAY(this, "withdrawalTimes:	" + withdrawalTimes);
            GBLogUtils.DEBUG_DISPLAY(this, "unable_balance:	" + unable_balance);
        }
    }
}
