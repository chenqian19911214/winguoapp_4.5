package com.guobi.gfc.VoiceFun.regex;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;

import com.guobi.gfc.VoiceFun.phrase.BasePhraseAnalyzer;
import com.guobi.gfc.VoiceFun.phrase.CallPhraseAnalyzer;
import com.guobi.gfc.VoiceFun.regex.MsgMatcher.OnAnalyzeListener;
import com.guobi.gfc.VoiceFun.scene.CallActivity;
import com.guobi.gfc.VoiceFun.utils.ContactUtils.Contact;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.R;

import static com.guobi.winguoapp.voice.VoiceFunActivity.DIAL_REQUESTCODE;

public class CallPattern extends Pattern {
    // private int mCode;

    public static final int MIN_PRIORITY = 11;
    public static final int NORMAL_PRIORITY = 15;
    public static final int MAX_PRIORITY = 20;

    private static final Token D;
    private static final Token N;
    private static final Token J;
    private static final Token H;
    private static final Token L;

    public static final CallPattern PATTERN1;
    public static final CallPattern PATTERN2;
    public static final CallPattern PATTERN3;
    public static final CallPattern PATTERN4;
    public static final CallPattern PATTERN5;

    private static HashMap<Character, Token> mMaps = new HashMap<Character, Token>();
//	private CallPhraseAnalyzer mAnalyzer;

    // private static final int DEALUT1 = 1;
    // private static final int DEALUT2 = 2; // X+Y+(Z)
    // private static final int DEALUT3 = 3;
    // private static final int DEALUT4 = 4;

    static {
        D = new Token("打|拨|拨打|播"); // 拨给，拨打才是词组，播给不是词组，其他bo连个动词都不是，后面也会跟打和给，这么弱智的语音识别？
        N = new Token("电话|号");
        J = new Token("接|接通|拨|拨打|联系(!方式)");
        H = new Token("和|对|跟");
        L = new Token("通话|对话|说话|联系(!方式)");

        mMaps.put('D', D);
        mMaps.put('N', N);
        mMaps.put('J', J);
        mMaps.put('H', H);
        mMaps.put('L', L);

        PATTERN1 = new CallPattern("*%D#%N#给+", NORMAL_PRIORITY + 3);
        PATTERN2 = new CallPattern("*%D#给+", NORMAL_PRIORITY + 2);
        PATTERN3 = new CallPattern("*给+%D#%N#", NORMAL_PRIORITY + 1);
        PATTERN4 = new CallPattern("*%J#+");
        PATTERN5 = new CallPattern("*%H#+%L#");
    }

    public static final CallPattern DEFAULT_PATTERNS[] = {PATTERN1, PATTERN2, PATTERN3,
            PATTERN4, PATTERN5};

    private CallPattern(String pattern) {
        this(pattern, NORMAL_PRIORITY);
    }

    private CallPattern(String pattern, int weight) {
        super(pattern, weight);
        // this.mCode = code;
    }

    @Override
    int match(String string, char ch) {
        Token token = mMaps.get(ch);
        if (token == null)
            return -1;

        return token.equals(string, 0);
    }

    @Override
    public void doAction(OnAnalyzeListener listener, ArrayList<Point> array,
                         String rawContent, BasePhraseAnalyzer analyzer) {
        if (analyzer == null || !(analyzer instanceof CallPhraseAnalyzer)) {
            return;
        }
        CallPhraseAnalyzer callAnalyzer = (CallPhraseAnalyzer) analyzer;

        final int size = array.size();
        if (size < 1) {
//			analyzeFailed(context, rawContent);
            listener.analyzeFailed(rawContent);
            return;
        }
        GBLogUtils.DEBUG_DISPLAY("--doAction rawContent", rawContent);
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
        GBLogUtils.DEBUG_DISPLAY("--doaction analyse(x)", x);
        callAnalyzer.analyse(x);

        if (listener != null && listener.isCanceled()) {
            return;
        }

//		Intent intent = new Intent();
//		boolean flag = false;
        if (callAnalyzer.isTPhase()) {
            String num = callAnalyzer.getTPhaseNumber();//mAnalyzer.getPPhaseContact();
            listener.analyzeSusccess(num, null);
        } else if (callAnalyzer.isPPhase()) {
            Contact contact = callAnalyzer.getPPhaseContact();

            if (contact.numbers == null) {
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
            } else if (contact.numbers.size() == 1) {
//				intent.setAction(Intent.ACTION_DIAL);
//				intent.setData(Uri.parse("tel:"+contact.numbers.get(0)));
                listener.analyzeSusccess(contact, null);
            } else {
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

//			flag = true;
            // analyzeFailed(context, x);
//			intent.setAction(Intent.ACTION_DIAL);
            // intent.addCategory("android.intent。category.DEFAULT");
            // intent.setData(Uri.parse("tel:"+number));

            // 方法内部会自动为Inter 添加类别:android.intent。category.DEFAULT
        }
//		if (flag) {
//			context.startActivityForResult(intent, DIAL_REQUESTCODE);
//		} else {
//			
//		}
    }

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
            content = (String) info;
            Contact contact = new Contact();
            contact.name = content;
            contact.addNumber(content);

            intent.setClass(activity, CallActivity.class);
            intent.putExtra("isContact", true);
            intent.putExtra("person_names", contact);
            intent.putExtra("rawinput", rawContent);
        } else if (info instanceof Contact) {
            Contact contact = (Contact) info;
            if (contact.numbers == null) {
                intent.setClass(activity, CallActivity.class);
                intent.putExtra("isContact", true);

                String msg = activity.getString(R.string.voicefun_prompt_contact_no_number, content);

                intent.putExtra("msg", msg);
                intent.putExtra("rawinput", rawContent);
            } else if (contact.numbers.size() == 1) {
                //TODO
//				intent.setAction(Intent.ACTION_CALL);
//				intent.setData(Uri.parse("tel:"+contact.numbers.get(0)));

                intent.setClass(activity, CallActivity.class);
                intent.putExtra("isContact", true);
                intent.putExtra("person_names", contact);
                intent.putExtra("rawinput", rawContent);
            } else {
                intent.setClass(activity, CallActivity.class);
                intent.putExtra("isContact", true);
                intent.putExtra("person_names", contact);
                intent.putExtra("rawinput", rawContent);
            }
        }

        try {
            activity.startActivityForResult(intent, DIAL_REQUESTCODE);
        } catch (Exception e) {
//			activity.findFailed(x);
            Intent data = new Intent();
            data.putExtra("result", "input");
            data.putExtra("rawinput", rawContent);
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        }
    }

    @Override
    public void trash() {
        mMaps.clear();
    }

    private void analyzeFailed(Activity activity, String content) {
        // Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra("result", "input");
        intent.putExtra("rawinput", content);
        activity.setResult(Activity.RESULT_CANCELED, intent);

        activity.finish();
    }


}
