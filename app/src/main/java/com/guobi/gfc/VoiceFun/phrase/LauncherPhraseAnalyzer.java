package com.guobi.gfc.VoiceFun.phrase;

import android.content.Context;
import android.util.Log;

import com.guobi.gfc.VoiceFun.utils.AppUtils;
import com.guobi.gfc.VoiceFun.utils.ContactUtils;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/3/9.
 */
@Deprecated
public class LauncherPhraseAnalyzer extends BasePhraseAnalyzer {
    public static final float Y_SCORE = 0.65f;

    private boolean isTPhase;
    private boolean isPPhase;

    private List<String> tPhaseNumberList;
    private List<AppUtils.AppResult> pPhaseAppList;


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
        int end = getAccurateOpenApp(phase, pList, flag, score);
        for (String contact : pList) {
            Log.i("--getAccurateAPP plist", pList.toString());
        }
        isPPhase = !isEmpty(pList);
        if (isPPhase) {
            pPhaseAppList = convert2APPList(pList);
            for (AppUtils.AppResult contact : pPhaseAppList) {
                Log.i("--convert2ContactList pPhaseAppList", "" + contact.appName.getContent());
            }
            return end;
        }

        return -1;
    }


    private void reset() {
        isTPhase = false;
        isPPhase = false;

        tPhaseNumberList = null;
        pPhaseAppList = null;

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

    public AppUtils.AppResult getPPhaseContact() {
        return isEmpty(pPhaseAppList) ? null : pPhaseAppList.get(0);
    }

    public LauncherPhraseAnalyzer(Context context) {
        super(context);
    }
}
