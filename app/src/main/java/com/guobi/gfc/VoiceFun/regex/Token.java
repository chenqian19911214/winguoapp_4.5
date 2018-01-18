package com.guobi.gfc.VoiceFun.regex;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 不允许空串的存在
 */
public class Token {
    String content;
    private String mTokens[], mIncompatibles[];
    private ArrayList<Token> mDependsToken = new ArrayList<Token>();
//	private short mIndex;

    private boolean mSpecial;

    public Token(String content) {
        if (content == null)
            throw new NullPointerException("token content is null");
        this.content = content;
        // | 划分子token，具体的
        separateSubToken(content);
    }

    Token(String content, boolean flag) {
        this(content);
        mSpecial = flag;
    }

    public void addDependsToken(Token token) {
        mDependsToken.add(token);
    }

    private void separateSubToken(String content) {
        mTokens = content.split("\\|");
        if (mTokens == null || mTokens.length <= 0)
            return;

        final int count = mTokens.length;
        mIncompatibles = new String[count];

        for (int i = 0; i < count; i++) {
            final String string = mTokens[i];

            if (string == null || string.isEmpty())
                return;

            int index1 = string.indexOf("(!");
            if (index1 > 0) {
                int index = string.indexOf(")", index1);
                if (index > 0 && index > (index1 + 2)) {
                    mIncompatibles[i] = string.substring(index1 + 2, index);
                    mTokens[i] = string.substring(0, index1);
                }
            }
        }
    }

    public int equals(String string, final int index) {
        final int count = string.length();
        int i = (short) index;
        int total = mTokens.length;
//		mTotal = (short)mTokens.length;

        boolean unmatches[] = new boolean[total];
        short[] poses = new short[total];
        short[] matchPoses = new short[total];
        Arrays.fill(matchPoses, (short) -1);
        int mathchMax = -1, pos = 0;

        outter:
        while (i < count) {
            char ch = string.charAt(i);

            int j = 0;
            for (String token : mTokens) {
                if (unmatches[j]) {
                    j++;
                    continue;
                } else if (i <= matchPoses[j]) {
                    j++;
                    continue;
                }
                final int length = token.length();
                if (length == 0) {
                    unmatches[j] = true;
                    total--;
                    if (mathchMax < 0) // 等于0的时候就已经匹配了
                        mathchMax = 0;
                } else {
                    int num = match(poses, token, j, i, string, length, ch);
                    if (num < 0) {
                        unmatches[j] = true;
                        total--;
                    } else if (num > 1) {
                        matchPoses[j] = (short) (i + num - 1);
                    } else {
                        matchPoses[j] = (short) i;
                    }

                    if (num >= 0 && poses[j] >= length) {//
                        unmatches[j] = true;
                        total--;
                        int n = num == 0 ? matchPoses[j] : matchPoses[j] + 1;
                        if (n > mathchMax) {
                            pos = j;
                            mathchMax = n;
                        }
//						mathchMax = n > mathchMax ? n : mathchMax;
                    }
                }

                j++;
                if (total <= 0) // 没有可匹配的项了
                    break outter;
            }
            i++;
        }

        if (mathchMax > 0 && mIncompatibles[pos] != null) { // 非法字符，比如联系张三，但是联系后面不能跟方式，
            boolean flag = string.substring(index + mathchMax).startsWith(mIncompatibles[pos]);
            if (flag)
                return -1;
        }

        return mathchMax;
    }

    private int match(short[] poses, String token, final int j, int i, String string, final int length, char ch) {
        int pos = poses[j];
        char c = token.charAt(pos);
        int num = -1;

        if (c == '%') { // 约定后面接数字，并且必须有数字，不再判断，判断浪费时间
            int n = (int) token.charAt(pos + 1) - 48;
            poses[j] += 2;

            Token depends = mDependsToken.get(n);
            num = depends.equals(string, i) - i;

//			if (depends.equals(string, i) > 0) { // 匹配
//				num = 1;
//			} else if (depends.equals(string, i) == 0) { // fxxk, 空的；看还有没有东西，不然字符，就会跑掉一个
//				if ((pos + 2) < length) {
//					num = match(poses, token, j, i, string, length, ch);
//				} else 
//					num = 0;
//			}

            if (num == 0) {
                if ((pos + 2) < length) {
                    num = match(poses, token, j, i, string, length, ch);
                }
//				else //不需要了，已经等于0
//					num = 0;
            }
        } else {
            if (c == ch) {
                num = 1;
            }

            poses[j]++;
        }

        return num;
    }
}
