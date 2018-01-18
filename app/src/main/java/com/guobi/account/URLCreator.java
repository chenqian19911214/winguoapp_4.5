package com.guobi.account;

import android.util.Log;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import java.util.ArrayList;

/**
 * URL创建器
 * Created by chenq on 2017/1/3.
 */
public class URLCreator {

    /**
     * 创建一个URL
     * 例子：
     * http://count.com/index.php?m=admin
     * &c=index&a=login
     */
    public static String create(String url, ArrayList<NameValuePair> valueList) {
        if ((url == null) || (valueList == null)) return "";
        StringBuffer returnStr = new StringBuffer(url);
        for (int i = 0; i < valueList.size(); i++) {
            returnStr.append("&").append(valueList.get(i).getkey());
            returnStr.append("=").append(valueList.get(i).getvalue());
        }
        GBLogUtils.DEBUG_DISPLAY("valueList", "valueListvalueListurl = " + returnStr.toString());
        return returnStr.toString();
    }

    /**
     * 创建一个URL，第一个参数不带&符号
     * 例子：
     * http://api.winguo.com/data/login?
     * ctype=1&hash=MuP77PkueJ
     */
    public static String createNoAnd(String url, ArrayList<NameValuePair> valueList) {
        if ((url == null) || (valueList == null)) return "";
        StringBuffer returnStr = new StringBuffer(url);
        for (int i = 0; i < valueList.size(); i++) {
            if (i != 0) returnStr.append("&");
            returnStr.append(valueList.get(i).getkey());
            returnStr.append("=").append(valueList.get(i).getvalue());
        }
        GBLogUtils.DEBUG_DISPLAY("url", "url   :" + returnStr.toString());
        return returnStr.toString();
    }

    public static final byte[] keyWinguoBytes2 = {0x69, 0x6E, 0x71, 0x32};
    public static final byte[] keyWinguoBytes3 = {0x33, 0x33, 0x65, 0x72};

    public static final byte[] key17WOBytes2 = {0x6b, 0x69, 0x77, 0x6e};
    public static final byte[] key17WOBytes3 = {0x67, 0x31, 0x38, 0x34};

    public static final byte[] key17WONewBytes2 = {0x47, 0x4D, 0x56, 0x58};
    public static final byte[] key17WONewBytes3 = {0x71, 0x35, 0x48, 0x37};
}
