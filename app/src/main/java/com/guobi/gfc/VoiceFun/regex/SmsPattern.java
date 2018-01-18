package com.guobi.gfc.VoiceFun.regex;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;

import com.guobi.gfc.VoiceFun.phrase.BasePhraseAnalyzer;
import com.guobi.gfc.VoiceFun.phrase.SMSPhraseAnalyzer;
import com.guobi.gfc.VoiceFun.regex.MsgMatcher.OnAnalyzeListener;
import com.guobi.gfc.VoiceFun.scene.SendSmsActivity;
import com.guobi.gfc.VoiceFun.utils.ContactUtils;

import static com.guobi.winguoapp.voice.VoiceFunActivity.REQUESTCODE;

public class SmsPattern extends Pattern {
    private static final String TAG = SmsPattern.class.getSimpleName();
    public static final int MIN_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 5;
    public static final int MAX_PRIORITY = 10;

    private static final Token F;
    private static final Token B;

//	private static final Token V; // V 不允许存在，存在空串，因此存在V的地方需要2条语句代替

    private static final Token D;
    private static final Token N;
    private static final Token G;

    private static final Token L;
    private static final Token M;

    private static final Token J;
    private static final Token K;
    private static final Token H;

    private static final Token C;

    public static final SmsPattern PATTERN1;
    public static final SmsPattern PATTERN2;
    public static final SmsPattern PATTERN3;
    public static final SmsPattern PATTERN4;
    public static final SmsPattern PATTERN4_1;

    public static final SmsPattern PATTERN5;
    public static final SmsPattern PATTERN5_1;

    public static final SmsPattern PATTERN6;
    public static final SmsPattern PATTERN7;
    public static final SmsPattern PATTERN7_1;

    public static final SmsPattern PATTERN8;
    public static final SmsPattern PATTERN8_1;

    public static final SmsPattern PATTERN9;
    public static final SmsPattern PATTERN9_1;

    public static final SmsPattern PATTERN10;
    public static final SmsPattern PATTERN10_1;

    private int mCode;
    //	private SMSPhraseAnalyzer mAnalyzer;
    private static HashMap<Character, Token> mMaps = new HashMap<Character, Token>();
//	private Token mToken;

    private static final int DEALUT1 = 1;
    private static final int DEALUT2 = 2; // X+Y+(Z)
    private static final int DEALUT3 = 3;
    private static final int DEALUT4 = 4;

    static {
        F = new Token("发|发送|分享");
        B = new Token("把|将");

        D = new Token("条|个");
        N = new Token("一");
        G = new Token("短信|信息|消息|信");

        J = new Token("和|对|跟");
        K = new Token("说|讲");
        H = new Token("他|她|它|他们|它们|她们", true); // 太呕心了这种写法

//		V = new Token("%0|");
//		V.addDependsToken(F);

        L = new Token("%0|%1%0");
        L.addDependsToken(D);
        L.addDependsToken(N);

        M = new Token("%0|%1%0");
        M.addDependsToken(G);
        M.addDependsToken(L);

        C = new Token("%0%1%2|告诉%1|告诉%1%2|%2|内容是");
        C.addDependsToken(J);
        C.addDependsToken(H);
        C.addDependsToken(K);

        mMaps.put('F', F);
        mMaps.put('B', B);
        mMaps.put('D', D);
        mMaps.put('N', N);
        mMaps.put('G', G);
        mMaps.put('J', J);
        mMaps.put('K', K);
        mMaps.put('H', H);

//		mMaps.put('V', V);
        mMaps.put('L', L);
        mMaps.put('M', M);

        mMaps.put('C', C);

        PATTERN1 = new SmsPattern("*告诉$+%C#", DEALUT1); //(说|内容是)
        PATTERN2 = new SmsPattern("*%F#%M#给$+%C#", DEALUT1, NORMAL_PRIORITY + 2); //(说|内容是)
        PATTERN3 = new SmsPattern("*%F#+给$+%C#", DEALUT2, NORMAL_PRIORITY + 1); //(说|内容是)
//		
        PATTERN4 = new SmsPattern("*给+%F#%M#", DEALUT3, NORMAL_PRIORITY + 2); //(说|内容是)
        PATTERN4_1 = new SmsPattern("*给+%M#", DEALUT3, NORMAL_PRIORITY + 1); //(说|内容是)

        PATTERN5 = new SmsPattern("*给+%F#%M#%C#", DEALUT1, NORMAL_PRIORITY + 3); // 移到4_
        PATTERN5_1 = new SmsPattern("*给+%M#%C#", DEALUT1, NORMAL_PRIORITY + 2);  // 移到4_1

        PATTERN6 = new SmsPattern("*给+%F#$+%C#", DEALUT3, NORMAL_PRIORITY + 1);

        PATTERN7 = new SmsPattern("*%B#%M#%F#给$+%C#", DEALUT1, NORMAL_PRIORITY + 4);
        PATTERN7_1 = new SmsPattern("*%B#%M#给$+%C#", DEALUT1, NORMAL_PRIORITY + 3);

        PATTERN8 = new SmsPattern("*%B#+%F#给$+%C#", DEALUT2, NORMAL_PRIORITY + 3);
        PATTERN8_1 = new SmsPattern("*%B#+给$+%C#", DEALUT2, NORMAL_PRIORITY + 2);

        PATTERN9 = new SmsPattern("*%M#%F#给$+%C#", DEALUT1, NORMAL_PRIORITY + 2);
        PATTERN9_1 = new SmsPattern("*%M#给$+%C#", DEALUT1, NORMAL_PRIORITY + 1);

        PATTERN10 = new SmsPattern("*%F#给$+%C#", DEALUT4, NORMAL_PRIORITY + 1);
        PATTERN10_1 = new SmsPattern("*给$+%C#", DEALUT4);
    }

    public static final SmsPattern DEFAULT_PATTERNS[] = {PATTERN1, PATTERN2, PATTERN3,
            PATTERN4, PATTERN4_1, PATTERN5, PATTERN5_1, PATTERN6, PATTERN7, PATTERN7_1, PATTERN8,
            PATTERN8_1, PATTERN9, PATTERN9_1, PATTERN10, PATTERN10_1}; //

    private SmsPattern(String pattern, int code) {
        this(pattern, code, NORMAL_PRIORITY);
    }

    private SmsPattern(String pattern, int code, int weight) {
        super(pattern, weight);
        this.mCode = code;
    }

    @Override
    public final void doAction(OnAnalyzeListener context, ArrayList<Point> array, String content, BasePhraseAnalyzer analyzer) {
        // 字段
//		final String pattern = this.pattern; // 那个模式的
        analyze(context, array, null, content, analyzer);
    }

    public final void doAction(OnAnalyzeListener context, ArrayList<Point> array, StringBuffer res, String content, BasePhraseAnalyzer analyzer) {
        analyze(context, array, res, content, analyzer);
    }

    private void analyze(OnAnalyzeListener listener, ArrayList<Point> array, StringBuffer res, String content, BasePhraseAnalyzer analyzer) {
        if (analyzer == null || !(analyzer instanceof SMSPhraseAnalyzer)) {
            return;
        }
        SMSPhraseAnalyzer smsAnalyzer = (SMSPhraseAnalyzer) analyzer;

        final int size = array.size();
        if (size < 1) {
            listener.analyzeFailed(content);
            return;
        }

        String x = null;
//		String x2 = null;
        String y = null;
        String z = null;
        String w = null;

        Point index1, index2, index3;
        index1 = array.get(0);

        switch (mCode) {
            case DEALUT1: //
                if (size > 1) {  //说后面不需要分析，直接作为内容
                    index2 = array.get(1);

                    y = content.substring(index1.x, index1.y);

//				if (content.charAt(index2.x - 1) == '说')
                    z = content.substring(index2.x);
//				else
//					x1 = content.substring(index2.x);
                } else {
                    w = content.substring(index1.x);
                }
                break;

            case DEALUT2:
                if (size < 2) {// 也是失败
                    listener.analyzeFailed(content);
                    return;
                }
                index2 = array.get(1);
                x = content.substring(index1.x, index1.y);

                if (size > 2) {
                    index3 = array.get(2);

                    y = content.substring(index2.x, index2.y);

//				if (content.charAt(index3.x - 1) == '说')
                    z = content.substring(index3.x);
//				else
//					x2 = content.substring(index3.x);
                } else {
                    w = content.substring(index2.x);
                }
                break;

            case DEALUT3:
                if (size < 2) {// 也是失败
                    listener.analyzeFailed(content);
                    return;
                }
                index2 = array.get(1);
                y = content.substring(index1.x, index1.y);

                if (size > 2) {
//				x2 = array.get(2);
                    index3 = array.get(2);
                    x = content.substring(index2.x, index2.y);

//				if (content.charAt(index3.x - 1) == '说')
                    z = content.substring(index3.x);
//				else
//					x2 = content.substring(index3.x); 
                } else
                    x = content.substring(index2.x);
                break;
            case DEALUT4:
                if (size > 1) {
                    index2 = array.get(1);
                    y = content.substring(index1.x, index1.y);
                    z = content.substring(index2.x);

                    if (index2.y > 0)
                        x = content.substring(0, index2.y);
                } else {
                    w = content.substring(index1.x);
                    if (index1.y > 0)
                        x = content.substring(0, index1.y);
                }

                break;
//		case DEALUT5:
//			if (size < 2) {// 也是失败
//				analyzeFailed(context);
//				return;
//			}
//			
//			index2 = array.get(1);
//			x1 = content.substring(index1.x, index1.y);
//			
//			if (size > 2) {
//				index3 = array.get(2);
//				y = content.substring(index2.x, index2.y);
//				
//				if (content.charAt(index3.x - 1) == '说')
//					z = content.substring(index3.x);
//				else
//					x2 = content.substring(index3.x); 
//			} else 
//				w = content.substring(index2.x);
//			
//			break;
            default:
                break;
        }

        List<ContactUtils.Contact> contactList = null;
        List<String> strList = null;

        StringBuffer buffer = new StringBuffer();
//		boolean isXExist = false;

        if (y != null) {
            if (listener.isCanceled()) {
                return;
            }

            smsAnalyzer.analyseY(y);
            if (res != null) {
                res.append("y analysis begin:\n");
            }

            if (smsAnalyzer.isTPhase()) {
                strList = smsAnalyzer.getTPhaseNumberList();

                if (res != null) {
                    res.append("isTPhase：\n");
                }
            } else if (smsAnalyzer.isPPhase()) {
                contactList = smsAnalyzer.getPPhaseContactList();

                if (res != null) {
                    res.append("isPPhase\n");
                }
            } else {
                listener.findFailed(y);
                if (res != null) {
                    res.append("y analysis failed\n");
                }
            }
        }

//		xAnalysis(x1, buffer); // res, 
//		Thread thread = Thread.currentThread();
        if (listener.isCanceled()) {
            return;
        }
        xAnalysis(x, buffer, smsAnalyzer); // res,

        if (listener.isCanceled()) {
            return;
        }

        if (w != null) {
            smsAnalyzer.analyseW(w);
            if (listener.isCanceled()) {
                return;
            }

            if (smsAnalyzer.isTPhase()) {
                strList = smsAnalyzer.getTPhaseNumberList();

                if (res != null) {
                    res.append("W ana isTPhase：\n");
                }
            } else if (smsAnalyzer.isPPhase()) {
                contactList = smsAnalyzer.getPPhaseContactList();

                if (res != null) {
                    res.append("W ana isPPhase\n");
                }
            } else {
                if (res != null) {
                    res.append("W analysis failed\n");
                }
            }

            String xRes = smsAnalyzer.getXPhaseResult();
            if (xRes != null && !xRes.isEmpty()) {
                if (buffer.length() > 0)
                    buffer.append(',');

                buffer.append(xRes);
            }
        }

        if (z != null && (!z.isEmpty())) {
            if (buffer.length() > 0) {
                buffer.append(',');
            }

            buffer.append(z);
        }

        if (strList == null || strList.isEmpty()) {
            if (contactList == null || contactList.isEmpty()) {
                listener.analyzeSusccess(null, buffer.toString());
            } else {
//				intent = getIntentContact(context, buffer.toString(), 
//						(ArrayList<ContactUtils.Contact>)contactList);
//				context.startActivityForResult(intent, REQUESTCODE);
                listener.analyzeSusccess(contactList, buffer.toString());
            }
        } else {
            listener.analyzeSusccess(strList, buffer.toString());
        }
    }

    private void xAnalysis(String x1, StringBuffer buffer, SMSPhraseAnalyzer smsAnalyzer) {
        if (x1 != null && (!x1.isEmpty())) {
            if (buffer.length() > 0)
                buffer.append(',');

            smsAnalyzer.analyseX(x1);

            String result = smsAnalyzer.getXPhaseResult();
            if (result != null && !result.isEmpty()) {
                buffer.append(result);
            }
//			else {
//				buffer.append(x1);
//			}
        }
    }

    private Intent getIntentContact(Context context, String content, ArrayList<ContactUtils.Contact> names) {
        Intent intent = new Intent(context, SendSmsActivity.class);
        intent.putExtra("msg_content", content);
        intent.putExtra("isContact", true);
//		intent.putStringArrayListExtra("person_names", (ArrayList<String>)names);
        intent.putParcelableArrayListExtra("person_names", names);

        return intent;
    }

    private Intent getIntent(Context context, String content, List<String> names) {
        Intent intent = new Intent(context, SendSmsActivity.class);
        intent.putExtra("msg_content", content);
        intent.putExtra("isContact", false);
        intent.putStringArrayListExtra("person_names", (ArrayList<String>) names);

        return intent;
    }

    private void analyzeFailed(Activity activity) {
        Intent intent = new Intent();
        intent.putExtra("result", "failed");
        activity.setResult(Activity.RESULT_CANCELED, intent);

        activity.finish();
    }

    @Override
    public void trash() {
//		mAnalyzer = null;
    }

    @Override
    int match(String string, char ch) {
        Token token = mMaps.get(ch);
        if (token == null)
            return -1;

        return token.equals(string, 0);
    }

    @Override
    public void startIntent(Activity activity, Object info, String content, String rawContent) {
        Intent intent = null;

        if (info == null) {
            intent = getIntentContact(activity, content,
                    null);
        } else if (info instanceof ArrayList<?>) {
            Object obj = ((ArrayList) info).get(0);

            if (obj instanceof ContactUtils.Contact) {
                intent = getIntentContact(activity, content, (ArrayList<ContactUtils.Contact>) info);
            } else if (obj instanceof String) {
                intent = getIntent(activity, content, (ArrayList<String>) info);
            }
        }

        if (intent != null) {
            intent.putExtra("rawinput", rawContent);
            activity.startActivityForResult(intent, REQUESTCODE);
            activity.finish();
        }
    }

}
