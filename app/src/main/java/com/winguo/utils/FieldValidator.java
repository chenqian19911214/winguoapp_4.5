package com.winguo.utils;

/*
 * 数据格式验证类
 * 
 * */
public class FieldValidator {
    public static final String PASS_WORD_REGEXP = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
    ;
    private static final String EMAIL_VALIDATOR = "(?:[a-zA-Z0-9!#$%\\&'*+/=?\\^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%\\&'*+/=?\\^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static String[] arrayOfString = null;

    static {
        arrayOfString = new String[7];//增加新项时，注意修改这个数组的大小
        arrayOfString[0] = "regexp";//正则表达式验证
        arrayOfString[1] = "email";
        arrayOfString[2] = "confirmation";
        arrayOfString[3] = "credit_card";
        arrayOfString[4] = "credit_card_svn";
        arrayOfString[5] = "credit_card_ukss";
        arrayOfString[6] = "int_data";
    }

    private static final String[] VALIDATOR_TYPES = arrayOfString;

    public static final String DATA_TYPE_REGEXP = arrayOfString[0];
    public static final String DATA_TYPE_EMAIL = arrayOfString[1];
    public static final String DATA_TYPE_CONFIRMATION = arrayOfString[2];
    public static final String DATA_TYPE_CREDIT_CARD = arrayOfString[3];
    public static final String DATA_TYPE_CREDIT_CARD_SVN = arrayOfString[4];
    public static final String DATA_TYPE_CREDIT_CARD_UKSS = arrayOfString[5];
    public static final String DATA_TYPE_INT_DATA = arrayOfString[6];

    /*
     * 数据验证函数
      dataType		被验证的数据的类型标识，见VALIDATOR_TYPES数组
      paramString2	被验证的数据paramString3必须匹配的模式（本函数使用）,paramString2不为空时才验证（正则表达式验证）
      dataToValidator	被验证的数据
      paramString4	被验证的数据paramString3必须匹配的模式（外部验证接口paramCallbacksContainer使用）
      paramCallbacksContainer 外部验证接口，验证paramString3是否符合paramString4指定的模式
     * */
    public static boolean checkTextWithValidator(
            String dataType,        //被验证的数据的类型标识，见VALIDATOR_TYPES数组
            String paramString2, //被验证的数据dataToValidator必须匹配的模式（本函数使用）,paramString2不为空时才验证
            String dataToValidator, //被验证的数据
            String paramString4, //被验证的数据dataToValidator必须匹配的模式（外部验证接口paramCallbacksContainer使用）
            CallbacksContainer paramCallbacksContainer)//外部验证接口，验证dataToValidator是否符合paramString4指定的模式
    {
        int i = 0;
        boolean findType = false;
        for (i = 0; i < VALIDATOR_TYPES.length; i++) {//查找类型
            if (VALIDATOR_TYPES[i].equalsIgnoreCase(dataType.trim().toLowerCase())) {
                findType = true;
                break;
            }
        }
        boolean bool1 = false;
        if (findType) {
            switch (i) {
                case 0://regexp 正则表达式验证
                {
                    boolean bool3 = dataToValidator.matches(paramString2);
                    bool1 = bool3;
                }
                break;
                case 1://email 电子邮箱
                {
                    boolean bool2 = dataToValidator.matches(EMAIL_VALIDATOR);
                    bool1 = bool2;
                }
                break;
                case 2://confirmation 确认验证 （本项验证方式不确定）
                    if (paramCallbacksContainer != null) {
                        bool1 = paramCallbacksContainer.checkForIdentity(dataToValidator, paramString4);
                    }
                    break;
                case 3://credit_card（本项验证方式不确定）
                {
                    int[][] arrayOfInt = new int[2][];
                    int[] arrayOfInt1 = new int[10];
                    arrayOfInt1[1] = 1;
                    arrayOfInt1[2] = 2;
                    arrayOfInt1[3] = 3;
                    arrayOfInt1[4] = 4;
                    arrayOfInt1[5] = 5;
                    arrayOfInt1[6] = 6;
                    arrayOfInt1[7] = 7;
                    arrayOfInt1[8] = 8;
                    arrayOfInt1[9] = 9;
                    arrayOfInt[0] = arrayOfInt1;
                    int[] arrayOfInt2 = new int[10];
                    arrayOfInt2[1] = 2;
                    arrayOfInt2[2] = 4;
                    arrayOfInt2[3] = 6;
                    arrayOfInt2[4] = 8;
                    arrayOfInt2[5] = 1;
                    arrayOfInt2[6] = 3;
                    arrayOfInt2[7] = 5;
                    arrayOfInt2[8] = 7;
                    arrayOfInt2[9] = 9;
                    arrayOfInt[1] = arrayOfInt2;
                    int j = dataToValidator.length() - 1;
                    int k = 0;
                    int m = j;
                    for (int n = 0; ; n++) {
                        if (m < 0) {
                            if ((dataToValidator.length() <= 0) || (k % 10 != 0)) {
                                bool1 = false;
                            } else {
                                bool1 = true;
                            }
                            break;
                        }
                        k += arrayOfInt[(n & 0x1)][Character.digit(dataToValidator.charAt(m), 10)];
                        m--;
                    }
                }
                break;
                case 4://credit_card_svn
                    if (paramCallbacksContainer != null) {//使用外部接口验证
                        bool1 = paramCallbacksContainer.checkCreditCartSVN(dataToValidator, paramString4);
                    }
                    break;
                case 5://credit_card_ukss
                    if (paramCallbacksContainer != null) {//使用外部接口验证
                        bool1 = paramCallbacksContainer.checkCreditCardUKSS(dataToValidator);
                    }

                    break;
                case 6://整数验证
                    //Java基本类型中，最大整数是：2147483647
                    if (dataToValidator.length() > "2147483646".length()) {
                        bool1 = false;
                    } else {
                        if (dataToValidator.length() == "2147483646".length()) {
                            bool1 = true;
                            for (int k = 0; k < dataToValidator.length(); k++) {
                                int a = Integer.parseInt(dataToValidator.substring(k, k + 1));
                                int b = Integer.parseInt("2147483646".substring(k, k + 1));
                                if (a > b) {//只要高位有一位大，就超过最大整数
                                    bool1 = false;
                                    break;
                                }
                            }
                        } else {
                            bool1 = true;
                        }
                    }

                    break;
                default:
                    bool1 = false;
                    break;
            }
        }
        return bool1;
    }

    public static abstract interface CallbacksContainer {
        public abstract boolean checkCreditCardUKSS(String paramString);

        public abstract boolean checkCreditCartSVN(String paramString1, String paramString2);

        public abstract boolean checkForIdentity(String paramString1, String paramString2);
    }

    public static final int DATA_DIGIT = 0x00000001;//可以有0-9十个数字
    public static final int DATA_LOWER_CHAR = 0x00000002;//小写字母
    public static final int DATA_UPPER_CHAR = 0x00000004;//大写字母
    public static final int DATA_SYMBOL = 0x00000008;//符号
    public static final int DATA_USER_ALLOW_CHARS = 0x00000010;//用户自定义
    public static final int DATA_PHONE_EXT_CHAR = 0x00000020;//电话其他符号

    public static final String STR_DIGIT = "0123456789";
    public static final String STR_LOWER_CHAR = "abcdefghijklmnopqrstuvwxyz";
    public static final String STR_UPPER_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String STR_SYMBOL = "~!@#$%^&*()_+-=[]{};'\\:\"|,./<>?";
    public static final String STR_PHONE_EXT_CHAR = "().,PW+- *#$";

    public static boolean checkByMaskDefine(String strToCheck, int mask, String userAllowChars) {
        String allChars = "";
        if ((mask & DATA_DIGIT) == DATA_DIGIT) {
            allChars = allChars + STR_DIGIT;
        }
        if ((mask & DATA_LOWER_CHAR) == DATA_LOWER_CHAR) {
            allChars = allChars + STR_LOWER_CHAR;
        }
        if ((mask & DATA_UPPER_CHAR) == DATA_UPPER_CHAR) {
            allChars = allChars + STR_UPPER_CHAR;
        }
        if ((mask & DATA_SYMBOL) == DATA_SYMBOL) {
            allChars = allChars + STR_SYMBOL;
        }
        if (((mask & DATA_USER_ALLOW_CHARS) == DATA_USER_ALLOW_CHARS) && userAllowChars != null) {
            allChars = allChars + userAllowChars;
        }
        if ((mask & DATA_PHONE_EXT_CHAR) == DATA_PHONE_EXT_CHAR) {
            allChars = allChars + STR_PHONE_EXT_CHAR;
        }
        if (strToCheck != null) {
            for (int i = 0; i < strToCheck.length(); i++) {
                String ch = strToCheck.substring(i, i + 1);
                int pos = allChars.indexOf(ch);
                //只要有一个字符不在允许的集合里，就说明字符串不符合要求
                if (pos <= -1) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    public static boolean checkNoAllowChar(String strToCheck, String userNoAllowChars) {
        if (strToCheck != null && userNoAllowChars != null) {
            for (int i = 0; i < strToCheck.length(); i++) {
                String ch = strToCheck.substring(i, i + 1);
                int pos = userNoAllowChars.indexOf(ch);
                if (pos >= 0) {
                    return false;
                }
            }
        } else {
            return true;
        }
        return true;
    }

}