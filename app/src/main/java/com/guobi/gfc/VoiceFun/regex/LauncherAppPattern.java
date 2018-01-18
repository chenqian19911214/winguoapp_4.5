package com.guobi.gfc.VoiceFun.regex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;

import com.guobi.gfc.VoiceFun.phrase.BasePhraseAnalyzer;
import com.guobi.gfc.VoiceFun.phrase.CallPhraseAnalyzer;
import com.guobi.gfc.VoiceFun.phrase.LauncherPhraseAnalyzer;
import com.guobi.gfc.VoiceFun.scene.CallActivity;
import com.guobi.gfc.VoiceFun.utils.AppUtils;
import com.guobi.gfc.VoiceFun.utils.ContactUtils;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.R;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.guobi.winguoapp.voice.VoiceFunActivity.DIAL_REQUESTCODE;

/**
 * Created by admin on 2017/3/8.
 */
@Deprecated
public class LauncherAppPattern extends Pattern {
    public static final int NORMAL_PRIORITY = 15;
    private static HashMap<Character, Token> mMaps = new HashMap<Character, Token>();
    private static final Token D;
    private static final Token Q;
    private static final Token Y;

    public static final LauncherAppPattern PATTERN1;
    public static final LauncherAppPattern PATTERN2;
    public static final LauncherAppPattern PATTERN3;

    static {

        D = new Token("打开"); // 拨给，拨打才是词组，播给不是词组，其他bo连个动词都不是，后面也会跟打和给，这么弱智的语音识别？
        Q = new Token("启动|开启");
        Y = new Token("运行");

        mMaps.put('D', D);
        mMaps.put('Q', Q);
        mMaps.put('Y', Y);


        PATTERN1 = new LauncherAppPattern("*%D#+", NORMAL_PRIORITY + 3);
        PATTERN2 = new LauncherAppPattern("*%Q#+", NORMAL_PRIORITY + 2);
        PATTERN3 = new LauncherAppPattern("*%Y#+", NORMAL_PRIORITY + 1);

    }

    public static final LauncherAppPattern DEFAULT_PATTERNS[] = {PATTERN1, PATTERN2, PATTERN3};


    private LauncherAppPattern(String pattern) {
        this(pattern, NORMAL_PRIORITY);
    }

    public LauncherAppPattern(String pattern, int weight) {
        super(pattern, weight);
    }

    @Override
    int match(String string, char ch) {
        Token token = mMaps.get(ch);
        if (token == null)
            return -1;

        return token.equals(string, 0);
    }

    @Override
    public void doAction(MsgMatcher.OnAnalyzeListener listener, ArrayList<Point> array, String rawContent, BasePhraseAnalyzer analyzer) {
        if (analyzer == null || !(analyzer instanceof LauncherPhraseAnalyzer)) {
            return;
        }
        LauncherPhraseAnalyzer appAnalyzer = (LauncherPhraseAnalyzer) analyzer;

        final int size = array.size();
        if (size < 1) {
//			analyzeFailed(context, rawContent);
            listener.analyzeFailed(rawContent);
            return;
        }
        Log.i("--doAction rawContent", rawContent);
        String x;
        int end = array.get(0).y;
        int start = array.get(0).x;

        if (end <= start)
            x = rawContent.substring(start);
        else
            x = rawContent.substring(start, end);

//		Thread thread = Thread.currentThread();
        if (listener != null && listener.isCanceled()) {
            return;
        }
        Log.i("--doaction analyse(x)", x);
        appAnalyzer.analyse(x);

        if (listener != null && listener.isCanceled()) {
            return;
        }

//		Intent intent = new Intent();
//		boolean flag = false;
        if (appAnalyzer.isTPhase()) {
            String num = appAnalyzer.getTPhaseNumber();//mAnalyzer.getPPhaseContact();
            listener.analyzeSusccess(num, null);
        } else if (appAnalyzer.isPPhase()) {
            AppUtils.AppResult contact = appAnalyzer.getPPhaseContact();

            if (contact.appName.getContent() == null) {
//				intent.setClass(context, CallActivity.class);
//				intent.putExtra("isContact", true);
//
//				String msg = context.getString(R.string.voicefun_prompt_contact_no_number, x);
//
//				intent.putExtra("msg", msg);
//				intent.putExtra("rawinput", rawContent);
//
//				flag = true;
                listener.analyzeSusccess(contact, x);
            }else {
//				intent.setClass(context, CallActivity.class);
//				intent.putExtra("isContact", true);
//				intent.putExtra("person_names", contact);
//				intent.putExtra("rawinput", rawContent);
//
//				flag = true;
                listener.analyzeSusccess(contact, null);
            }
        } else { // 未找到
            listener.findFailed(x);
            listener.analyzeSusccess(null, x);
        }
    }

    @Override
    public void startIntent(Activity activity, Object info, String content, String rawContent) {

        Intent intent = new Intent();
        if (info == null) {
            StringBuffer buffer = new StringBuffer(activity.getString(R.string.voicefun_prompt_cannot_find_contact));
            buffer.append(content);
            intent.setClass(activity, CallActivity.class);

            intent.putExtra("isContact", true);
            intent.putExtra("msg", buffer.toString());
            intent.putExtra("rawinput", rawContent);
        } else if (info instanceof String) {
//			intent.setAction(Intent.ACTION_CALL);
//			intent.setData(Uri.parse("tel:"+content));
          /*  content = (String) info;
            ContactUtils.Contact contact = new ContactUtils.Contact();
            contact.name = content;
            contact.addNumber(content);

            intent.setClass(activity, CallActivity.class);
            intent.putExtra("isContact", true);
            intent.putExtra("person_names", contact);
            intent.putExtra("rawinput", rawContent);*/
        } else if (info instanceof AppUtils.AppResult) {
            AppUtils.AppResult appResult = (AppUtils.AppResult) info;

            Log.i("LauncherAppPattern startIntent",appResult.appName.getContent());
            if (appResult.appName.getContent() == null) {
              /*  intent.setClass(activity, CallActivity.class);
                intent.putExtra("isContact", true);
                String msg = activity.getString(R.string.voicefun_prompt_contact_no_number, content);
                intent.putExtra("msg", msg);
                intent.putExtra("rawinput", rawContent);*/
            } else {
               /* intent.setClass(activity, CallActivity.class);
                intent.putExtra("isContact", true);
                intent.putExtra("person_names", contact);
                intent.putExtra("rawinput", rawContent);*/
            }
        }

        try {
            //activity.startActivityForResult(intent, DIAL_REQUESTCODE);
        } catch (Exception e) {
//			activity.findFailed(x);
           /* Intent data = new Intent();
            data.putExtra("result", "input");
            data.putExtra("rawinput", rawContent);
            activity.setResult(Activity.RESULT_OK);
            activity.finish();*/
            Log.i("LauncherAppPattern startIntent ","Exception"+e.getMessage());
        }
    }

    @Override
    public void trash() {

    }
}
