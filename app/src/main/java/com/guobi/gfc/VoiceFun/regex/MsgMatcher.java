package com.guobi.gfc.VoiceFun.regex;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Point;

/**
 * #将为替换符，数字代替，OR运算比较
 *
 * @author yeagle
 */
public class MsgMatcher {

    //	private int mFindPos[] = new int[2];
//	private String[] mRules;
    private ArrayList<Pattern> mPatterns = new ArrayList<Pattern>();
    private OnMatchListener mMatchListener;

    public MsgMatcher() {
    }

    public void addPattern(Pattern... patterns) {
        for (Pattern pattern : patterns) {
            mPatterns.add(pattern);
        }
    }

    public void setOnMatchListener(OnMatchListener listener) {
        this.mMatchListener = listener;
    }

    private String[] getRules() {
        final int size = mPatterns.size();
        String[] array = new String[size];

        for (int i = 0; i < size; i++) {
            array[i] = mPatterns.get(i).pattern;
        }

        return array;
    }

    public void match(String str) {
        ArrayList<Point> list = new ArrayList<Point>();

        if (mPatterns == null || str == null || str.isEmpty()) {
            if (mMatchListener != null) {
//				list.add(-1);
                mMatchListener.matchOver(null, list);
            }
            return;
        }

        final int size = str.length();

        int i = 0;
//		final int findPos[] = mFindPos;
//		final String rules[] = mPatterns;
        final String rules[] = getRules();

        final int len = rules.length;

        //匹配到第几个字符，到时候根据几个字符来截取
        /**
         * 最后一次分割的地方，用来回退,
         */
        final int matchPos[] = new int[len];
        Arrays.fill(matchPos, -1);

        // string匹配到那里了，
        final int matchChildPos[] = new int[len];
        final int pos[] = new int[len * 10];
        final int strLens[] = new int[len * 10];
        final int patternLens[] = new int[len * 10];
        final int status[] = new int[len];

        int no = -1;

//		outter:
        while (i < size) {
            char ch = str.charAt(i);

            int j = 0;

            for (; j < len; j++) {
                String string = rules[j];
                if (string == null)
                    continue;

                int n = matchPos[j];

                int num = matchChildPos[j];
                final int strLength = string.length();
                if (num >= strLength) {
                    status[j] = 3;
                    no = j;

                    continue; // break outter;
                }

                if (n >= 0) {
                    int p = pos[n + 10 * j];
                    int l = strLens[n + 10 * j];
                    if ((p + l) > i) {//像#那边匹配先就完毕了
//						patternLens[j]++;
                        continue;
                    }
                }

                char c = string.charAt(num);
                if (c == '$') { //标识下，直接去下面一个值
                    status[j] = 1; // 字符忽略,这个依旧ok
                    matchChildPos[j]++;

                    num = matchChildPos[j];
                    if (num >= strLength) {
                        status[j] = 3;
                        no = j;
                        continue; //outter
                    }
                    c = string.charAt(num);
                }

                boolean isNextMatch = false;

                switch (c) {
                    case '*':
                        if (num == strLength - 1) {
                            status[j] = 3;
                            no = j;
                            continue; //outter
                        }
                        isNextMatch = true;
                        if (status[j] == 1) { // 走过
                            status[j] = 2;
                            no = j;
                        }
                        break;
                    case '+': {
//					num = matchChildPos[j];
                        if (num == strLength - 1) {
                            status[j] = 3;
                            no = j;
                            continue; //outter
                        }
                        n = matchPos[j];
                        int childPos = i;

                        if (n < 0) {
                            childPos = -1;
                        } else {
                            childPos = pos[n + j * 10] + strLens[n + j * 10];
                        }

                        if (i > childPos) { // 说明过了一个了
                            isNextMatch = true;
                        }

                        if (status[j] == 1) {
                            status[j] = 2;
                            no = j;
                        }
                        // else 否则就跳过一个
                    }
                    break;
//				case '$': // 后面还有，字符部分可以完毕，
//					status[j] = 1; // 字符忽略,这个依旧ok
//					matchChildPos[j]++;
//					
//					break;
                    case '%': // 匹配则处理，不匹配则做一般字符处理
                        int temp = no;
                        no = replaceMethod(str, i, matchPos, matchChildPos, pos,
                                strLens, patternLens, status, no, j, string,
                                strLength, string.charAt(num + 1));
                        if (no != temp && no > -1)
                            continue; //outter
                        break;
                    default: // 字符
                        if (status[j] == 1) {
                            status[j] = 2; // 有解了，其他的串干脆忽略，后面的还是可以匹配的
                            no = j;
                        }

                        int mn;
                        if (ch == c) {
                            mn = 1;
                        } else
                            mn = -1;

                        temp = no;
                        no = replaceMethod(i, matchPos, matchChildPos, pos, strLens,
                                patternLens, status, no, j, string, strLength, mn, 1);

                        if (temp != no && no > -1)
                            continue; //outter
                        break;
                }

                if (isNextMatch) {
                    int next = num + 1;
                    if (next < strLength) {
                        char nextch = string.charAt(next);

                        if (nextch == ch) { // 匹配到了
                            matchOneChar(i, matchPos, matchChildPos, pos,
                                    strLens, patternLens, j, next, 1, 1);
                        } else if (nextch == '%') {
                            Pattern pattern = mPatterns.get(j);

                            char sch = string.charAt(next + 1);
                            int mn = pattern.match(str.substring(i), sch);

                            if (mn > 0) {
                                matchOneChar(i, matchPos, matchChildPos, pos,
                                        strLens, patternLens, j, next, mn, 3);
                            }
                        }
//						else { // 依旧叫匹配ok
//							
//						}
                    } else { // match over, success
                        status[j] = 3;
                        no = j;

                        continue; //outter
                    }
                }
            }

            i++;
        }

        i = 0;
        for (Pattern pattern : mPatterns) {
            pattern.reset();

            if (no >= 0 && i != no && (status[i] > status[no] || (status[i] == status[no]
                    && pattern.getWieght() > mPatterns.get(no).getWieght()))) {
                no = i; // 轮到后面，或者权限比这个大
            }
            i++;
        }

        if (no >= 0) { // find
            int lastNum = -1;

            Pattern pattern = mPatterns.get(no);
            int begin = -1;

            for (int j = no * 10; j < 10 * (no + 1); j++) {
                int num = pos[j];
                int length = strLens[j];
                if (num > lastNum) {
                    int end = pos[j + 1];
                    int start = num + length;

                    if (begin == -1)
                        begin = num;

                    if (end < start) {
                        end = begin;
                    }

                    Point point = new Point(start, end);
                    list.add(point);

                    lastNum = num;
                } else
                    break;
            }

            if (mMatchListener != null) {
                mMatchListener.matchOver(pattern, list);
            }
            return;
        }

        if (mMatchListener != null) {
//			list.add(-1);
            mMatchListener.matchOver(null, list);
        }
    }

    private void matchOneChar(int i, final int[] matchPos,
                              final int[] matchChildPos, final int[] pos, final int[] strLens,
                              final int[] patternLens, int j, int next, int matchNum, int offset) {
        int n = matchPos[j];
        int childPos = i;

        if (n < 0) {
            childPos = -1;
        } else {
            childPos = pos[n + j * 10] + strLens[n + j * 10];
        }

        if (i > childPos) { // 说明过了一个了
            matchChildPos[j] = (next + offset); // 到next了
            n = matchPos[j]; //
            n++;
            matchPos[j] = n;

            pos[n + j * 10] = i;
//								lens[n+j*10]++;
            patternLens[n + j * 10] += offset;
            strLens[n + j * 10] += matchNum;
        } else {
//								lens[n+j*10]++;
            patternLens[n + j * 10] += offset;
            strLens[n + j * 10] += matchNum;
            matchChildPos[j] = (next + offset);  // + 1
        }
    }

    private int replaceMethod(String str, int i, final int[] matchPos,
                              final int[] matchChildPos, final int[] pos, final int[] strLens,
                              final int[] patternLens, final int[] status, int no, int j,
                              String string, final int strLength, char ch) {
        Pattern pattern = mPatterns.get(j);
        int mathchNum = pattern.match(str.substring(i), ch);

        no = replaceMethod(i, matchPos, matchChildPos, pos, strLens,
                patternLens, status, no, j, string, strLength, mathchNum, 3);

        return no;
    }

    private int replaceMethod(int i, final int[] matchPos,
                              final int[] matchChildPos, final int[] pos, final int[] strLens,
                              final int[] patternLens, final int[] status, int no, int j,
                              String string, final int strLength, int mathchNum, int offset) {
        if (mathchNum > 0) {
            matchChildPos[j] += offset;

            int n = matchPos[j];
            if (n < 0) { // 第一次的
                n++;
                matchPos[j] = n;

                pos[n + j * 10] = i;
//							lens[n+j*10] ++;
                patternLens[n + j * 10] += offset;
                strLens[n + j * 10] += mathchNum;
            } else {
//							lens[n+j*10] ++;
                patternLens[n + j * 10] += offset;
                strLens[n + j * 10] += mathchNum;
            }

            int n1 = matchChildPos[j];
            if (n1 >= strLength) {
                status[j] = 3;
                no = j;
//							break outter;
            } else if (n1 == strLength - 1 && string.charAt(n1) == '*') {
                status[j] = 3;
                no = j;
//							break outter;
            }
        } else if (mathchNum == 0) {

        } else { //没命中肯定回退
            int n = matchPos[j];
            if (n >= 0) {
                final int total = patternLens[n + j * 10]; //pattern 回退pattern的数量
                if (total > 0) {
                    patternLens[n + j * 10] = 0;
                    strLens[n + j * 10] = 0;
                    pos[n + j * 10] = 0;

                    matchPos[j]--;

//					Pattern pattern = mPatterns.get(j);
//					pattern.posAutoDecrease(matchChildPos[j]-total, matchChildPos[j]);
                    matchChildPos[j] -= total;

                    n = matchChildPos[j];
//					if (n >= 0 && string.charAt(n) == '#') { // 没碰到'*'，'#'匹配全部的字符，都该继续减的
//						matchChildPos[j]--;
//					}
                    while (n >= 0 && string.charAt(n) != '*' && string.charAt(n) != '+') {// 退到通用的
                        matchChildPos[j]--;
                        n--;
                    }
                }
            }
        }
        return no;
    }

    public static interface OnMatchListener {
        public void matchOver(Pattern pattern, ArrayList<Point> msg);
    }

    public static interface OnAnalyzeListener {
        public void analyzeSusccess(Object info, String content);

        public void analyzeFailed(String content);

        public void findFailed(String name);

        public boolean isCanceled();
    }
}
