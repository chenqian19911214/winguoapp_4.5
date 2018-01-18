package com.winguo.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 验证工具 输入内容正则匹配
 * Created by admin on 2017/4/5.
 */

public class ValidateUtil {

    private ValidateUtil() {
        // 防止被实例化
    }

    /**
     * 字符串是否符合正则表达式的规则
     *
     * @param text 匹配文本
     * @param format 匹配规则
     * @return true 匹配成功 flase 匹配失败
     */
    private static boolean isMatches(String text, String format) {
        Pattern pattern = Pattern.compile(format);
        Matcher m = pattern.matcher(text);
        return m.matches();
    }

    /**
     * 匹配帐号类型是否正确（只能输入大小写字母和数字，最大不超过20个字符）
     *
     * @param str 帐号
     * @return true= 符合 false=不符合
     */
    public static boolean isPassword(String str) {
        String format = "[a-zA-Z0-9]{0,16}";
        return isMatches(str, format);
    }
    /**
     * 匹配帐号类型是否正确（只能输入大小写字母和数字，最大不超过20个字符）
     *
     * @param str 帐号
     * @return true= 符合 false=不符合
     */
    public static boolean isCommentAccount(String str) {
        String format = "[a-zA-Z0-9]{1,20}";
        return isMatches(str, format);
    }

    /**
     * 匹配账号类型 手机号()
     * @param str
     * @return
     */
    public static boolean isTelephone(String str) {
        String format = "1[3-9][0-9]{0,9}";
        return isMatches(str, format);
    }

    public static boolean isTelephone2(String str) {
        String format = "1[3-9][0-9]{9}";
        return isMatches(str, format);
    }

    /**
     * 手机号号段校验，
     第1位：1；
     第2位：{3、4、5、6、7、8}任意数字；
     第3—11位：0—9任意数字
     * @param value
     * @return
     */
    public static boolean isTelPhoneNumber(String value) {
        String str = "^1[3|4|5|6|7|8][0-9]\\d{8}$";
        if (value != null && value.length() == 11) {
            Pattern pattern = Pattern.compile(str);
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }
        return false;
    }
    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    /**
     * 验证输入的名字是否为“中文”或者是否包含“·”
     */
    public static boolean isLegalName(String name){
        if (name.contains("·") || name.contains("•")){
            if (name.matches("^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$")){
                return true;
            }else {
                return false;
            }
        }else {
            if (name.matches("^[\\u4e00-\\u9fa5]+$")){
                return true;
            }else {
                return false;
            }
        }
    }
    /**
     * 匹配金额是否符合要求（99999999.99）
     *
     * @param money 金额字符串
     * @return true= 符合 false=不符合
     */
    public static boolean isMoney(String money) {
        String regex = "(^[1-9][0-9]{0,7}(\\.[0-9]{0,2})?)|(^0(\\.[0-9]{0,2})?)";
        return isMatches(money, regex);
    }

    /**
     * 验证邮箱输入是否合法
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String str="^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(strEmail);
        Log.i(""+ValidateUtil.class.getSimpleName(),"------"+m.matches());
        return m.matches();
    }

}
