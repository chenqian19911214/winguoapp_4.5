package com.guobi.account;

import java.io.Serializable;

/**
 * Created by chenq on 2017/1/3.
 */
public class NameValuePair implements Serializable{

    private String mkey;
    private String mvalue;

    public NameValuePair(String key, String value) {
        mkey = key;
        mvalue = value;
    }

    public void setkey(String key) {
        mkey = key;
    }

    public void setvalue(String value) {
        mvalue = value;
    }

    public String getkey() {
        return mkey;
    }

    public String getvalue() {
        return mvalue;
    }
}
