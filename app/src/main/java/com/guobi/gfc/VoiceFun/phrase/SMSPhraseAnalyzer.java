package com.guobi.gfc.VoiceFun.phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.guobi.gfc.VoiceFun.utils.ContactUtils;
import com.guobi.gfc.VoiceFun.utils.ContactUtils.Contact;
import com.guobi.gfc.VoiceFun.utils.VoiceFunShareUtils;

import android.content.Context;
import android.text.TextUtils;

public class SMSPhraseAnalyzer extends BasePhraseAnalyzer {

    public static final float X_O_SCORE = 0.68f;
    public static final float X_P_SCORE = 0.900f;
    public static final float Y_SCORE = 0.68f;
    public static final float W_SCORE = 0.80f;
    public static final float W_SCORE_2 = 0.900f;

    private boolean isTPhase;
    private boolean isPPhase;

    private String xPhaseResult;

    private List<String> tPhaseNumberList;
    private List<Contact> pPhaseContactList;

    private Pattern mChecker;
    private Pattern oChecker;
    private Pattern qChecker;

    private Matcher mMatcher;


    public void analyseX(String phase) {
        reset();
        analyseX(phase, true);
    }

    private void analyseX(String phase, boolean only) {

        int endM = checkM(phase, false);
        boolean isMPhase = endM >= 0;
        if (isMPhase) {
            xPhaseResult = phase.substring(endM);
            return;
        }

        String qString = checkQ(phase, false);
        boolean isQPhase = !TextUtils.isEmpty(qString);
        if (isQPhase) {
            xPhaseResult = qString + phase.substring(mMatcher.end());
            return;
        }

        List<String> tList = checkT(phase, true);
        boolean isTPhase = !isEmpty(tList);
        if (isTPhase) {
            xPhaseResult = list2TString(tList);
            return;
        }

        List<String> oList = checkO(phase, false, X_O_SCORE);
        List<Contact> ocList = convert2ContactList(oList);
        boolean isOPhase = !isEmpty(ocList);
        if (isOPhase) {
            xPhaseResult = list2OString(ocList);
            return;
        }

        if (only) {
            List<String> pList = new ArrayList<String>();
            getAccurateContact(phase, pList, ContactUtils.LIKE, X_P_SCORE);
            List<Contact> pcList = convert2ContactList(pList);
            boolean isPPhase = !isEmpty(pcList);
            if (isPPhase) {
                xPhaseResult = list2OString(pcList);
                return;
            }
        }

        xPhaseResult = phase;
    }

    public int analyseY(String phase) {
        reset();
        return analyseY(phase, true, ContactUtils.ANY, Y_SCORE);
    }

    private int analyseY(String phase, boolean only, int flag, float score) {
        List<String> tList = checkT(phase, only);
        isTPhase = !isEmpty(tList);
        if (isTPhase) {
            tPhaseNumberList = tList;
            return mEnd;
        }

        List<String> pList = new ArrayList<String>();
        int end = getAccurateContact(phase, pList, flag, score);
        isPPhase = !isEmpty(pList);
        if (isPPhase) {
            pPhaseContactList = convert2ContactList(pList);
            return end;
        }

        return -1;
    }

    public boolean analyseW(String phase) {
        reset();

        int flag = ContactUtils.FIRST_GROUP;
        String subPhase = phase;

        Matcher m = qChecker.matcher(phase);
        if (m.find()) {
            subPhase = phase.substring(0, m.start());
        } else if (oChecker.matcher(phase).find()) {
            flag = ContactUtils.FIRST;
        }

        int end = analyseY(subPhase, false, flag, W_SCORE);
        boolean hasP = end > 0;
        /*if ( hasP )*/
        {
            String x = phase.substring(end == -1 ? 0 : end);
            analyseX(x, false);
        }

        return hasP;
    }

    private void reset() {
        isTPhase = false;
        isPPhase = false;

        xPhaseResult = null;

        tPhaseNumberList = null;
        pPhaseContactList = null;

        mEnd = -1;
    }

    private int checkM(String s, boolean accurate) {
        Matcher m = mMatcher = mChecker.matcher(s);
        boolean isM = accurate ? m.matches() : m.find();
        if (isM) {
            if (m.start() == m.end()) {
                System.out.println("test: M: empty M");
                return -1;
            } else {
                mEnd = m.end();
                return mEnd;
            }
        } else {
            return -1;
        }
    }

    private String checkQ(String s, boolean accurate) {
        Matcher m = mMatcher = qChecker.matcher(s);
        boolean isQ = accurate ? m.matches() : m.find();
        if (isQ) {
            printGroup(m);
            mEnd = m.end();
            if (m.group(3) != null) {
                return VoiceFunShareUtils.getWinguoShareContentFinal();
            } else if (m.group(4) != null) {
                return VoiceFunShareUtils.getMallShareContentFinal();
            }
        }

        return null;
    }

    private List<String> checkO(String s, boolean accurate, float score) {

        Matcher m = mMatcher = oChecker.matcher(s);

        List<String> contacts0 = matchGroup(m, 2, s, accurate);
        List<String> contacts = checkOList(contacts0, score);

        return contacts;
    }

    private List<String> checkOList(List<String> list, float score) {
        List<String> result = new ArrayList<String>();
        int end = -1;
        for (String persons : list) {
            List<String> names = new ArrayList<String>();
            end = getAccurateContact(persons, names, ContactUtils.ANY, score);
            if (end > 0) {
                result.addAll(names);
            } else {
                //return null;
                result.add(persons);
            }
        }

        return result;
    }

    private String list2OString(List<Contact> list) {
        StringBuffer sb = new StringBuffer();
        for (Contact contact : list) {
            sb.append(contact.toString());
            sb.append("  ");
        }

        int len = sb.length();
        if (len >= 2) {
            sb.delete(len - 2, len);
        }
        return sb.toString();
    }


    public boolean isTPhase() {
        return isTPhase;
    }

    public boolean isPPhase() {
        return isPPhase;
    }

    public String getXPhaseResult() {
        return xPhaseResult;
    }

    public List<String> getTPhaseNumberList() {
        return tPhaseNumberList;
    }

    public List<Contact> getPPhaseContactList() {
        return pPhaseContactList;
    }

    public SMSPhraseAnalyzer(Context context) {
        super(context);

        String pattern;

        pattern = "(((一|1|){0,1}(条|个)){0,1}(短信|信息|消息|信)){0,1}((内容|正文)(是|为)){0,1}";
        mChecker = Pattern.compile(pattern);

        pattern = "((.{1,}?)(的){0,1}((电话|手机)(号码|号)|电话|手机|号码|联系方式))(和|与|跟|以及|还有| |　|,|，|、){0,1}";
        oChecker = Pattern.compile(pattern);

        pattern = "(我的){0,1}((问过桌面|问果桌面|桌面|winguo|问过|问果)|(网店|商城|商场|网站|网页))";
        qChecker = Pattern.compile(pattern);
    }

}
