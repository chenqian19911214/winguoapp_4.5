package com.guobi.gfc.VoiceFun.phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.example.localsearch.LocalAppFinder;
import com.guobi.gfc.VoiceFun.utils.AppUtils;
import com.guobi.gfc.VoiceFun.utils.ContactUtils;
import com.guobi.gfc.VoiceFun.utils.ContactUtils.Contact;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;


import android.content.Context;
import android.util.Log;

public class BasePhraseAnalyzer {


    protected Pattern tChecker;

    protected Context context;

    protected int mEnd;


    protected List<String> checkT(String s, boolean accurate) {

        Matcher m = tChecker.matcher(s);

        List<String> nums0 = matchGroup(m, 1, s, accurate);
        List<String> nums = checkTList(nums0);

        return nums;
    }

    protected List<String> matchGroup(Matcher m, int group, String phase, boolean accurate) {
        int lastGroupEnd = 0;
        List<String> names = new ArrayList<String>();
        while (m.find()) {
            printGroup(m);

            if (m.start() != lastGroupEnd) {
                names.clear();
                break;
            }
            lastGroupEnd = m.end();

            String name = m.group(group);
            names.add(name);

            mEnd = m.end();
        }
        if (accurate) {
            if (lastGroupEnd != phase.length()) {
                names.clear();
            }
        }
        return names;
    }

    protected void printGroup(Matcher m) {
        System.out.println("test: find...........");
        int n = m.groupCount();
        for (int i = 0; i <= n; i++) {
            System.out.println("test: " + i + "：" + m.group(i) + " start: " + m.start(i) + " end: " + m.end(i));
        }
    }

    protected List<String> checkTList(List<String> list) {

        for (int i = 0, n = list.size(); i < n; i++) {
            String num0 = list.get(i);
            String num = getAccurateNumber(num0);
            if (num != null) {
                list.set(i, num);
            } else {
                return null;
            }
        }

        return list;
    }

    protected String getAccurateNumber(String num) {
        final String zi = "零一二三四五六七八九";

        StringBuffer sb = new StringBuffer();

        for (int i = 0, n = num.length(); i < n; i++) {
            char c = num.charAt(i);
            int f = zi.indexOf(c);
            if (f >= 0) {
                sb.append((char) ('0' + f));
            } else if (c >= '0' && c <= '9') {
                sb.append(c);
            } else if (c == '幺') {
                sb.append('1');
            } else {
                return null;
            }
        }

        return sb.toString();
    }

    protected String list2TString(List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(s);
            sb.append(", ");
        }

        int len = sb.length();
        if (len >= 2) {
            sb.delete(len - 2, len);
        }
        return sb.toString();
    }


    protected int getAccurateContact(String persons, List<String> result, int accurate, float score) {
        try {
            GBLogUtils.DEBUG_DISPLAY("--getAccurateContact", persons);
            return ContactUtils.getSuggestContactsBySpeech(context, persons, result, accurate, score);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    protected int getAccurateOpenApp(String persons, List<String> result, int accurate, float score) {
        try {
            Log.i("--getAccurateContact", persons);
            return AppUtils.getSuggestAppsBySpeech(context, persons, result, accurate, score);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    protected List<Contact> convert2ContactList(List<String> list) {
        List<Contact> result = new ArrayList<Contact>();
        for (String name : list) {
            List<Contact> contacts = ContactUtils.findContact(context, name);
            result.addAll(contacts);
        }
        return result;
    }

    protected List<AppUtils.AppResult> convert2APPList(List<String> list) {
        List<AppUtils.AppResult> result = new ArrayList<AppUtils.AppResult>();
        for (String name : list) {
            List<AppUtils.AppResult> contacts = AppUtils.findApp(context, name);
            result.addAll(contacts);
        }
        return result;
    }

    protected <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }


    public BasePhraseAnalyzer(Context context) {
        String pattern;

        pattern = "((幺|一|二|三|四|五|六|七|八|九|零|0|1|2|3|4|5|6|7|8|9){3,})(和|与|跟|以及|还有| |　|,|，|、){0,1}";
        tChecker = Pattern.compile(pattern);


        this.context = context;
    }

}
