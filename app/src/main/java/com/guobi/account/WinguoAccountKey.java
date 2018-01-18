package com.guobi.account;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

/**
 * 存放加密用的信息
 * Created by chenq on 2017/1/3.
 */
public final class WinguoAccountKey {

    private String mkey = null;
    private String mtoken = null;
    private String muuid = null;
    public String oriHash;

    public WinguoAccountKey() {
    }

    public WinguoAccountKey(String key, String token, String uuid) {
        mkey = key;
        mtoken = token;
        muuid = uuid;
    }

    public void setKey(String key) {
        mkey = key;
    }

    public void setToken(String token) {
        mtoken = token;
    }

    public void setUUID(String uuid) {
        muuid = uuid;
    }

    public String getKey() {
        return mkey;
    }

    public String getToken() {
        return mtoken;
    }

    public String getUUID() {
        return muuid;
    }

    public boolean verify() {
        if ((mkey == null) || (mtoken == null) || (muuid == null)) return false;
        return true;
    }

    public void dump() {
        if (GBLogUtils.DEBUG) {
            GBLogUtils.DEBUG_DISPLAY(this, "key:" + mkey);
            GBLogUtils.DEBUG_DISPLAY(this, "token:" + mtoken);
            GBLogUtils.DEBUG_DISPLAY(this, "uuid:" + muuid);
        }
    }

}
