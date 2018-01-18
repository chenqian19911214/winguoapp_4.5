package com.guobi.gfc.VoiceFun.phrase;

import java.util.ArrayList;
import java.util.List;

import com.guobi.gfc.VoiceFun.utils.ContactUtils;
import com.guobi.gfc.VoiceFun.utils.ContactUtils.Contact;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;


import android.content.Context;


public class CallPhraseAnalyzer extends BasePhraseAnalyzer {

    public static final float Y_SCORE = 0.65f;

    private boolean isTPhase;
    private boolean isPPhase;

    private List<String> tPhaseNumberList;
    private List<Contact> pPhaseContactList;


    public int analyse(String phase) {
        reset();
        return analyse(phase, true, ContactUtils.ANY, Y_SCORE);
    }

    private int analyse(String phase, boolean only, int flag, float score) {
        List<String> tList = checkT(phase, only);
        isTPhase = !isEmpty(tList);
        if (isTPhase) {
            tPhaseNumberList = tList;
            return mEnd;
        }

        List<String> pList = new ArrayList<String>();
        int end = getAccurateContact(phase, pList, flag, score);
        for (String contact : pList) {
            GBLogUtils.DEBUG_DISPLAY("--getAccurateContact plist", pList.toString());
        }
        isPPhase = !isEmpty(pList);
        if (isPPhase) {
            pPhaseContactList = convert2ContactList(pList);
            for (Contact contact : pPhaseContactList) {
                GBLogUtils.DEBUG_DISPLAY("--convert2ContactList pPhaseContactList", "" + contact.name);
            }
            return end;
        }

        return -1;
    }


    private void reset() {
        isTPhase = false;
        isPPhase = false;

        tPhaseNumberList = null;
        pPhaseContactList = null;

        mEnd = -1;
    }


    public boolean isTPhase() {
        return isTPhase;
    }

    public boolean isPPhase() {
        return isPPhase;
    }

    public String getTPhaseNumber() {
        return isEmpty(tPhaseNumberList) ? null : tPhaseNumberList.get(0);
    }

    public Contact getPPhaseContact() {
        return isEmpty(pPhaseContactList) ? null : pPhaseContactList.get(0);
    }

    public CallPhraseAnalyzer(Context context) {
        super(context);
    }

}
