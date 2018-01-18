package com.guobi.account;

import android.content.Context;

import com.guobi.gfc.gbmiscutils.res.GBResourceUtils;

/**
 * 错误信息
 * Created by chenq on 2017/1/3.
 */
public final class ErrorLog {
    public static String combineErrorMsg(Context context, int code,
                                         String msgBody) {
        return GBResourceUtils.getString(context, "gb_account_err_code_prefix") + code + "  " + msgBody;
    }
}
