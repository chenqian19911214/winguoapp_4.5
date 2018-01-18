package com.guobi.account;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

/**
 * 果币信息
 * Created by chenq on 2017/1/3.
 */
public class WinguoAccountCurrency {

    public float avaliable;
    public float frozen;
    public float all;

    public final WinguoAccountCurrency copy() {
        final WinguoAccountCurrency c = new WinguoAccountCurrency();
        c.avaliable = this.avaliable;
        c.frozen = this.frozen;
        c.all = this.all;
        return c;
    }

    public final void dump() {
        if (GBLogUtils.DEBUG) {
            GBLogUtils.DEBUG_DISPLAY(this, "avaliable:		" + avaliable);
            GBLogUtils.DEBUG_DISPLAY(this, "frozen:	" + frozen);
            GBLogUtils.DEBUG_DISPLAY(this, "all:		" + all);
        }
    }

}
