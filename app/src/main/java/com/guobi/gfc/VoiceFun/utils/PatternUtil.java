package com.guobi.gfc.VoiceFun.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {

    /**
     * 是电话号码或者手机号码，不限制长度，只限制前面1位数，长途区号前面是0开头暂且，
     * 手机前面暂且是1开头用来查询归属地，其实电话只要前面4位数，手机只要前面7位数
     *
     * @param number
     * @return
     */
    public static boolean isPhoneNum(String number) {
        Pattern p = Pattern
                .compile("[0-9]*");
        Matcher m = p.matcher(number);
        //Matcher m1 = p1.matcher(mobiles);
        return m.matches();
    }
}
