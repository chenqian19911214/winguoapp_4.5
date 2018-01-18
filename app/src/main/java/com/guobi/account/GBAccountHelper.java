package com.guobi.account;


import android.content.Context;

import com.guobi.gfc.gbmiscutils.res.GBResourceUtils;

public final class GBAccountHelper {

    public static String getHelpStringLoginAccount(Context context) {
        return GBResourceUtils.getString(context, "gb_account_help_msg_login_account");
    }

    public static String getHelpStringRegAccount(Context context) {
        return GBResourceUtils.getString(context, "gb_account_help_msg_reg_account");
    }

    public static String getHelpStringPwd(Context context) {
        return GBResourceUtils.getString(context, "gb_account_help_msg_pwd");
    }

    public static String getHelpStringVCode(Context context) {
        return GBResourceUtils.getString(context, "gb_account_help_msg_vcode");
    }

    public static String getHelpStringResetpwdAccount(Context context) {
        return GBResourceUtils.getString(context, "gb_account_help_msg_resetpwd_account");
    }

    public static String getHelpStringResetpwdPhonenum(Context context) {
        return GBResourceUtils.getString(context, "gb_account_help_msg_resetpwd_phonenum");
    }

    public static String getHelpStringRegAgreementHead(Context context) {
        return GBResourceUtils.getString(context, "gb_account_help_msg_reg_agree_head");
    }

    /**
     * @param context
     * @param no      1-8共8条
     * @return
     */
    public static String getHelpStringRegAgreement(Context context, int no) {
        return GBResourceUtils.getString(context, "gb_account_help_msg_reg_agree_" + no);
    }


}
