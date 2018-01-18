package com.guobi.gfc.VoiceFun.regex;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Point;

import com.guobi.gfc.VoiceFun.phrase.BasePhraseAnalyzer;
import com.guobi.gfc.VoiceFun.regex.MsgMatcher.OnAnalyzeListener;

public abstract class Pattern {
    protected final String pattern;
    protected int mRepPostion;

//	public static final int SMS_MIN_PRIORITY = 1;
//	public static final int NORMAL_PRIORITY = 5;
//	public static final int MAX_PRIORITY = 10;

    private final int mWeight;

    private ArrayList<Operation> mTypeList;
    private ArrayList<Integer> mTypeSuffixList;
    private OperationFactory factory = new OperationFactory();

//	public Pattern(String pattern){
//		this(pattern, 0);
//	}

    public Pattern(String pattern, int weight) {
        mTypeList = new ArrayList<Operation>();
        mTypeSuffixList = new ArrayList<Integer>();

//		StringBuffer buffer = new StringBuffer(pattern);
//		handlePattern(buffer);
        mRepPostion = 0;

        this.pattern = pattern; // buffer.toString();

//		if (weight > MAX_PRIORITY || weight < SMS_MIN_PRIORITY)
//			weight = NORMAL_PRIORITY;

        this.mWeight = weight;
    }

    abstract int match(String string, char ch);

    private int match(String string) {
        final int size = mTypeList.size();
        if (size <= mRepPostion)
            return -1;

        Operation type = mTypeList.get(mRepPostion);
        int num = type.operate(string);
        if (num > -1)
            mRepPostion++;

        return num;
    }

    public int getWieght() {
        return this.mWeight;
    }

    public void reset() {
        mRepPostion = 0;
    }

    private void handlePattern(StringBuffer buffer) {
        String pattern = buffer.toString();
        if (pattern.isEmpty())
            return;

        final int count = pattern.length();
        int leftPos = -1;
        int lackNum = 0;

        //取第一个左括号与最前面那个右括号匹配
        for (int i = 0; i < count; i++) {
            char ch = pattern.charAt(i);

            if (leftPos < 0 && ch == '(') {
                leftPos = i;
            } else if (leftPos >= 0 && ch == ')') {
                Operation type = factory.createType(pattern.substring(leftPos + 1, i));

                if (type != null) {
                    mTypeList.add(type);
                    //替换成#
                    buffer.replace(leftPos - lackNum, i + 1 - lackNum, "#");
                    mTypeSuffixList.add(leftPos - lackNum);

                    lackNum += (i - leftPos);
                }

                leftPos = -1;
            }
        }
    }

    public void posAutoDecrease(int low, int high) {
        if (mRepPostion <= 0)
            return;

        for (int i = mRepPostion - 1; i >= 0; i--) {
            int num = mTypeSuffixList.get(i);

            if (num >= low && num <= high) {
                mRepPostion--;
            }
        }
    }

    public abstract void doAction(OnAnalyzeListener listener, ArrayList<Point> array,
                                  String rawContent, BasePhraseAnalyzer analyzer);

    public abstract void startIntent(Activity activity, Object info, String content, String rawContent);

    public abstract void trash();

}
