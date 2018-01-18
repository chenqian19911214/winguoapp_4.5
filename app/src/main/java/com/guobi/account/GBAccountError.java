package com.guobi.account;

import android.content.Context;

import com.guobi.gfc.gbmiscutils.res.GBResourceUtils;

/**
 * 公共错误编号
 * Created by chenq on 2017/1/3.
 */
public final class GBAccountError {

    public static final int UNKNOWN = 503;
    public static final int USER_NAME_NOT_FOUND = 504;
    public static final int INVALID_KEY = 505;
    public static final int HASH_FAILED = 506;
    public static final int NETWORK_FAILED = 507;
    public static final int INVALID_INPUT = 508;
    public static final int INVALID_PEER_DATA = 509;
    public static final int TASK_CANCELED = 510;
    public static final int CREATE_SESSION_FAILED = 511;
    public static final int NETWORK_CONN_FAILED = 512;
    public static final int WINGUOACCOUNTKEY_NULL = 513;
    public static final int REQUST_TIMEOUT = 514;

    public static final int SESSION_TIMEOUT = -101;


    public static String getErrorMsg(Context context, int code) {
        String msg = "";
        switch (code) {
            case USER_NAME_NOT_FOUND:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_need_login");
                break;
            case INVALID_KEY:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_invalid_key");
                break;
            case HASH_FAILED:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_hash_failed");
                break;
            case NETWORK_CONN_FAILED:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_network_connetion_failed");
                break;
            case NETWORK_FAILED:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_network_failed");
                break;
            case INVALID_INPUT:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_invalid_input");
                break;
            case INVALID_PEER_DATA:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_invalid_peer_data");
                break;
            case TASK_CANCELED:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_task_canceled");
                break;
            case CREATE_SESSION_FAILED:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_create_session_failed");
                break;

            case -100:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_no_usr");
                break;
            case SESSION_TIMEOUT:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_session_expires");
                break;

            case UNKNOWN:
                msg = GBResourceUtils.getString(context, "gb_account_err_msg_unknown");
                break;
            case REQUST_TIMEOUT:
                msg = GBResourceUtils.getString(context, "gb_account_err_request_timeout");
                break;

            default:
                return null;
        }
        return ErrorLog.combineErrorMsg(context, code, msg);
    }
}
