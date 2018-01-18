package com.guobi.account;

import android.content.Context;

/**
 * Created by chenq on 2017/1/3.
 */
public class WinguoAccountDataMgr {

    private static final String URI_WINGUO_USERNAME = "URI_WINGUO_USERNAME";
    private static final String URI_WINGUO_HASH_LOGIN = "URI_WINGUO_LOGINHASH";
    private static final String URI_WINGUO_HASH_COMMON = "URI_WINGUO_COMMONHASH";
    private static final String URI_WINGUO_MD5 = "URI_WINGUO_MD5";
    private static final String URI_WINGUO_KEY = "URI_WINGUO_KEY";
    private static final String URI_WINGUO_TOKEN = "URI_WINGUO_TOEKN";
    private static final String URI_WINGUO_UUID = "URI_WINGUO_UUID";
    private static final String URI_USE_DEFAULT_PWD = "URI_USE_DEFAULT_PWD";
    private static final String URI_SESSION_URL = "URI_SESSION_URL";
    private static final String URI_PIZZA = "URI_PIZZA";
    private static final String URI_WINGUO_ICO = "URI_WINGUO_ICO";

    private WinguoAccountDataMgr() {
    }

    public static final void clear(Context context, boolean clearUsrName) {
        if (clearUsrName) {
            removeUserName(context);
            removeMD5(context);
        }
        removePizza(context);
        removeHashLogin(context);
        removeUseDeaultPWD(context);
        removeHashCommon(context);
        removeKEY(context);
        removeUUID(context);
        removeTOKEN(context);
        removeSessionUrl(context);
    }


    public static final void setUserName(Context context, String username) {
        GBAccountDataProviderHelper.setValueByUri(context, URI_WINGUO_USERNAME, username);
    }

    public static final String getUserName(Context context) {
        return GBAccountDataProviderHelper.getValueByUri(context, URI_WINGUO_USERNAME);
    }

    public static final void removeUserName(Context context) {
        GBAccountDataProviderHelper.removeByUri(context, URI_WINGUO_USERNAME);
    }


    public static final void setHashLogin(Context context, String hash) {
        GBAccountDataProviderHelper.setValueByUri(context, URI_WINGUO_HASH_LOGIN, hash);
    }

    public static final String getHashLogin(Context context) {
        return GBAccountDataProviderHelper.getValueByUri(context, URI_WINGUO_HASH_LOGIN);
    }

    public static final void removeHashLogin(Context context) {
        GBAccountDataProviderHelper.removeByUri(context, URI_WINGUO_HASH_LOGIN);
    }

    public static final void setHashCommon(Context context, String hash) {
        GBAccountDataProviderHelper.setValueByUri(context, URI_WINGUO_HASH_COMMON, hash);
    }

    public static final String getHashCommon(Context context) {
        return GBAccountDataProviderHelper.getValueByUri(context, URI_WINGUO_HASH_COMMON);
    }

    public static final void removeHashCommon(Context context) {
        GBAccountDataProviderHelper.removeByUri(context, URI_WINGUO_HASH_COMMON);
    }

    public static final void setMD5(Context context, String md5) {
        GBAccountDataProviderHelper.setValueByUri(context, URI_WINGUO_MD5, md5);
    }

    public static final String getMD5(Context context) {
        return GBAccountDataProviderHelper.getValueByUri(context, URI_WINGUO_MD5);
    }

    public static final void removeMD5(Context context) {
        GBAccountDataProviderHelper.removeByUri(context, URI_WINGUO_MD5);
    }

    public static final void setKEY(Context context, String key) {
        GBAccountDataProviderHelper.setValueByUri(context, URI_WINGUO_KEY, key);
    }

    public static final String getKEY(Context context) {
        return GBAccountDataProviderHelper.getValueByUri(context, URI_WINGUO_KEY);
    }

    public static final void removeKEY(Context context) {
        GBAccountDataProviderHelper.removeByUri(context, URI_WINGUO_KEY);
    }

    public static final void setTOKEN(Context context, String token) {
        GBAccountDataProviderHelper.setValueByUri(context, URI_WINGUO_TOKEN, token);
    }

    public static final String getTOKEN(Context context) {
        return GBAccountDataProviderHelper.getValueByUri(context, URI_WINGUO_TOKEN);
    }

    public static final void removeTOKEN(Context context) {
        GBAccountDataProviderHelper.removeByUri(context, URI_WINGUO_TOKEN);
    }

    public static final void setUUID(Context context, String uuid) {
        GBAccountDataProviderHelper.setValueByUri(context, URI_WINGUO_UUID, uuid);
    }

    public static final String getUUID(Context context) {
        return GBAccountDataProviderHelper.getValueByUri(context, URI_WINGUO_UUID);
    }

    public static final void removeUUID(Context context) {
        GBAccountDataProviderHelper.removeByUri(context, URI_WINGUO_UUID);
    }


    public static final void setUseDeaultPWD(Context context, boolean on) {
        GBAccountDataProviderHelper.setValueByUri(context, URI_USE_DEFAULT_PWD, on ? "true" : "false");
    }

    public static final boolean getUseDeaultPWD(Context context) {
        String val = GBAccountDataProviderHelper.getValueByUri(context, URI_USE_DEFAULT_PWD);
        if (val != null && val.equals("true")) {
            return true;
        }
        return false;
    }

    public static final void removeUseDeaultPWD(Context context) {
        GBAccountDataProviderHelper.removeByUri(context, URI_USE_DEFAULT_PWD);
    }

    public static final void setSessionUrl(Context context, String url) {
        GBAccountDataProviderHelper.setValueByUri(context, URI_SESSION_URL, url);
    }

    public static final String getSessionUrl(Context context) {
        return GBAccountDataProviderHelper.getValueByUri(context, URI_SESSION_URL);
    }

    public static final void removeSessionUrl(Context context) {
        GBAccountDataProviderHelper.removeByUri(context, URI_SESSION_URL);
    }


    public static final void setPizza(Context context, String pizza) {
        GBAccountDataProviderHelper.setDataByUri(
                context,
                URI_PIZZA,
                WinguoEncryption.encryptString(pizza, getMD5(context))
        );
    }

    public static final String getPizza(Context context) {
        return WinguoEncryption.decryptString(
                GBAccountDataProviderHelper.getDataByUri(context, URI_PIZZA),
                getMD5(context)
        );
    }

    public static final void removePizza(Context context) {
        GBAccountDataProviderHelper.removeByUri(context, URI_PIZZA);
    }


    public static final void setUserIco(Context context, byte[] data) {
        GBAccountDataProviderHelper.setDataByUri(context, URI_WINGUO_ICO, data);
    }

    public static final byte[] getUserIco(Context context) {
        return GBAccountDataProviderHelper.getDataByUri(context, URI_WINGUO_ICO);
    }

    public static final void removeUserIco(Context context) {
        GBAccountDataProviderHelper.removeByUri(context, URI_WINGUO_ICO);
    }


    public static final WinguoAccountKey getWinguoAccountKey(Context context) {
        return new WinguoAccountKey(
                WinguoAccountDataMgr.getKEY(context),
                WinguoAccountDataMgr.getTOKEN(context),
                WinguoAccountDataMgr.getUUID(context)
        );
    }
}
