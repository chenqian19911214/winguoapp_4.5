package com.guobi.account;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户 数据
 * Created by chenq on 2017/1/3.
 */

public class GBAccountInfo {
    /**
     * 当前账户状态，见GBAccountStatus定义
     */
    public int status = GBAccountStatus.not_startup;

    /**
     * 登录名，用户上次登录使用的用户名，并不一定是真正的账户名
     */
    public String accountName = "";

    /**
     * 这个头像数据是上次用户登录或刷新后保存的头像数据
     * 如果用户没有登录，并且这个数据不为空的话，可以使用这个图片显示头像
     * 若用户登录成功，优先使用winguoGeneral.icoData，因为这个是最新的
     */
    public byte[] accountIco;

    /**
     * 问果帐号通用信息
     * 如果未登录，该信息为null
     */
    public WinguoAccountGeneral winguoGeneral;

    /**
     * 问果余额信息
     * 如果未登录，该信息为null
     */
    public WinguoAccountBalance winguoBalance;

    /**
     * 果币信息
     * 如果未登录，该信息为null
     */
    public WinguoAccountCurrency winguoCurrency;

    public List<WinguoAccountBank> banks = new ArrayList<>();
    public List<WinguoAccountBankCard> cards = new ArrayList<>();


    public final GBAccountInfo copy() {
        GBAccountInfo c = new GBAccountInfo();
        c.status = this.status;
        c.accountName = this.accountName;
        if (this.winguoGeneral != null) {
            c.winguoGeneral = this.winguoGeneral.copy();
        }
        if (this.winguoBalance != null) {
            c.winguoBalance = this.winguoBalance.copy();
        }
        if (this.winguoCurrency != null) {
            c.winguoCurrency = this.winguoCurrency.copy();
        }
        if (this.cards != null) {
            c.cards.addAll(this.cards);
        }
        if (this.banks != null) {
            c.banks.clear();
            c.banks.addAll(this.banks);
        }
        return c;
    }

    public final void dump() {
        if (GBLogUtils.DEBUG) {
            switch (status) {
                case GBAccountStatus.not_startup:
                    GBLogUtils.DEBUG_DISPLAY(this, "status: " + "not_startup");
                    break;
                case GBAccountStatus.initializing:
                    GBLogUtils.DEBUG_DISPLAY(this, "status: " + "initializing");
                    break;
                case GBAccountStatus.tourist:
                    GBLogUtils.DEBUG_DISPLAY(this, "status: " + "tourist");
                    break;
                case GBAccountStatus.usr_not_logged_in:
                    GBLogUtils.DEBUG_DISPLAY(this, "status: " + "usr_not_logged_in");
                    break;
                case GBAccountStatus.usr_logged:
                    GBLogUtils.DEBUG_DISPLAY(this, "status: " + "usr_logged");
                    break;
            }

            GBLogUtils.DEBUG_DISPLAY(this, "accountName: " + accountName);
            if (winguoGeneral != null) winguoGeneral.dump();
            if (winguoBalance != null) winguoBalance.dump();
            if (winguoCurrency != null) winguoCurrency.dump();
        }
    }
}
