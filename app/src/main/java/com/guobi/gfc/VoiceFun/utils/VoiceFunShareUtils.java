package com.guobi.gfc.VoiceFun.utils;

public final class VoiceFunShareUtils {

    public static String sWinguoShareContent = "http://m.winguo.com/download/desktopapp";
    public static String sWinguoShareContentForMe;
    public static String sMallShareContent = "http://m.winguo.com";
    public static String sMallShareContentForMe;

    public static void setWinguoShareContent(String str) {
        sWinguoShareContent = str;
    }

    public static void setWinguoShareContentForMe(String str) {
        sWinguoShareContentForMe = str;
    }

    public static final void setMallShareContent(String str) {
        sMallShareContent = str;
    }

    public static final void setMallShareContentForMe(String str) {
        sMallShareContentForMe = str;
    }

    public static final String getWinguoShareContentFinal() {
        if (sWinguoShareContentForMe != null && sWinguoShareContentForMe.length() > 0) {
            return sWinguoShareContentForMe;
        }
        return sWinguoShareContent;
    }

    public static final String getMallShareContentFinal() {
        if (sMallShareContentForMe != null && sMallShareContentForMe.length() > 0) {
            return sMallShareContentForMe;
        }
        return sMallShareContent;
    }

    private VoiceFunShareUtils() {
    }
}
