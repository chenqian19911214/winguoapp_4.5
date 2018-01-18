package com.guobi.gfc.VoiceFun.jcseg;

import java.util.Arrays;

import com.guobi.gfc.VoiceFun.jcseg.core.IDictionary;
import com.guobi.gfc.VoiceFun.jcseg.core.ILexicon;
import com.guobi.gfc.VoiceFun.jcseg.core.IWord;
import com.guobi.gfc.VoiceFun.jcseg.util.TimSort;
import com.guobi.gfc.VoiceFun.utils.PinyinUtils;

/**
 * Dictionary class. <br />
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public class Dictionary extends IDictionary {


    private static final long serialVersionUID = 2686379609469434402L;


    public char chars[];
    public int charCount;

    public long[][] dics;
    public int count[];


    private static final char[] char2find = new char[8];


    public Dictionary() {
        super();

        dics = new long[ILexicon.T_LEN][8];
        count = new int[ILexicon.T_LEN];

        chars = new char[8];
    }

    /**
     * @see IDictionary#match(int, Stringer)
     */
    @Override
    public boolean match(int t, String key) {
        System.out.println("test: seg: match:" + key);
        final char[] chars = key.toCharArray();
        final int i = binarySearch0(dics[t], chars, 0, key.length());

        return i >= 0;
    }


    @Override
    public boolean match(int t, char key) {
        System.out.println("test: seg: match: c: " + key);

        char2find[0] = key;
        final int i = binarySearch0(dics[t], char2find, 0, 1);
        return i >= 0;
    }

    @Override
    public boolean match(int t, char[] key, int start, int len) {
        System.out.println("test: seg: match: cs: " + new String(key, start, len) + " s: " + start + " l: " + len);

        final int i = binarySearch0(dics[t], key, start, len);
        return i >= 0;
    }


    @Override
    public IWord get(int t, String key) {
        //System.out.println("test: seg: get:" + key);

        final char[] chars = key.toCharArray();
        final int i = binarySearch0(dics[t], chars, 0, key.length());
        return i >= 0 ? (new Word(dics[t][i], key.length())) : null;
    }


    @Override
    public IWord get(int t, char key) {
        //System.out.println("test: : get: c: " + key);

        char2find[0] = key;
        final int i = binarySearch0(dics[t], char2find, 0, 1);
        return i >= 0 ? (new Word(dics[t][i], 1)) : null;
    }

    @Override
    public IWord get(int t, char[] key, int start, int len) {
        //System.out.println("test: : get: cs: " + new String(key, start, len) + " s: " + start + " l: " + len);

        final int i = binarySearch0(dics[t], key, start, len);
        return i >= 0 ? (new Word(dics[t][i], len)) : null;
    }

    /**
     * @see IDictionary#add(int, Stringer, int)
     */
    @Override
    public void add(int t, String word, int type) {
        add(t, word, 0, type);
    }

    /**
     * @see IDictionary#add(int, Stringer, int, int)
     */
    @Override
    public void add(int t, String word, int fre, int type) {

        int start = charCount;
        int len = word.length();

        ensureCharCapacity(charCount + len);
        word.getChars(0, len, chars, charCount);
        charCount += len;

        ensureDicCapacity(t, count[t] + 1);
        dics[t][count[t]++] = word2long(start, len, fre, type);

    }


    /**
     * @see IDictionary#remove(int, Stringer)
     */
    @Override
    public void remove(int t, String key) {

    }

    /**
     * @see IDictionary#size(int)
     */
    @Override
    public int size(int t) {
        return count[t];
    }

    private void ensureDicCapacity(int t, int cap) {
        int currCap = dics[t].length;
        if (currCap < cap) {
            int newCap = currCap * 2;
            dics[t] = Arrays.copyOf(dics[t], newCap);

            //System.out.println("lex " + t + " old size: " + currCap +  " new size: " + dics[t].length);
        }
    }

    private void ensureCharCapacity(int cap) {
        int currCap = chars.length;
        if (currCap < cap) {
            int newCap = currCap * 2;
            chars = Arrays.copyOf(chars, newCap);

            //System.out.println("chars old size: " + currCap +  " + new size: " + chars.length);
        }
    }

    public void optimize() {

        chars = Arrays.copyOf(chars, charCount);
        Word.setPublicCharBuffer(chars);

        int wordCount = 0;
        int dupCount = 0;

        for (int tk = 0; tk < dics.length; tk++) {
            dics[tk] = Arrays.copyOf(dics[tk], count[tk]);

            wordCount += count[tk];
            System.out.println("lex " + tk + " word count: " + count[tk]);
            TimSort.sort(dics[tk], wordComparator);

            for (int i = 0, n = dics[tk].length - 1; i < n; i++) {
                if (new Word(dics[tk][i], -1).getValue().equals(new Word(dics[tk][i + 1], -1).getValue())) {
                    dupCount++;
                }
            }
        }

        System.out.println("Total chars count: " + charCount + "  Total word count: " + wordCount + "  Dup word count: " + dupCount);
    }

    private WordComparator wordComparator = new WordComparator();

    class WordComparator implements TimSort.LongComparator {
        @Override
        public int compare(long o1, long o2) {
            return new Word(o1, -1).getValue().toString().compareTo(new Word(o2, -1).getValue().toString());
        }
    }

    private long word2long(int start, int length, int fre, int type) {
        Word w = new Word(0, -1);

        w.setStart(start);
        w.setLength(length);
        w.setFrequency(fre);
        w.setType(type);

        return w.v;
    }

    private int binarySearch00(long[] a, char[] v2, int start2, int len2) {

        int low = 0;
        int high = a.length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            long v = a[mid];

            //int cmp = compare(v, v2, start2, len2);
            //compare(long v1, char[] v2, int start2, int len2) 

            int cmp = 0;
            {
                w.v = v;

                final int start1 = w.getStart();
                final int len1 = w.getLength();
                final int lim = (len1 <= len2) ? len1 : len2;
                final char v1[] = chars;

                int k = 0;
                while (k < lim) {
                    char c1 = v1[k + start1];
                    char c2 = v2[k + start2];
                    if (c1 != c2) {
                        cmp = c1 - c2;
                        break;
                    }
                    k++;
                }
                if (k >= lim) {
                    cmp = len1 - len2;
                }
            }

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }

        return -(low + 1);  // key not found.
    }


    private int binarySearch0(long[] a, char[] v2, int start2, int len2) {
        int f = binarySearch00(a, v2, start2, len2);
        if (f < 0) {
            float bestScore = 0;
            int bestIndex = -1;
            final long[] b = dics[ILexicon.CJK_WORDS];
            for (int i = 0, n = b.length; i < n; i++) {
                float score = xingming(b[i], v2, start2, len2);
                if (score > bestScore) {
                    bestScore = score;
                    bestIndex = i;
                }
            }

            return bestScore > score ? bestIndex : -1;
        } else {
            return f;
        }
    }

    private float xingming(long v, char[] v2, int start2, int len2) {
        float result = 0.0f;

        w.v = v;

        final int start1 = w.getStart();
        final int len1 = w.getLength();
        final char v1[] = chars;

        final int lenDiff = len1 - len2;
        if (((lenDiff == 0) || (lenDiff >= 1 && lenDiff <= 2 && len2 >= 2))
                && w.getType() == ILexicon.VOICE_XING_MING) {
            float sum = 0;
            for (int i = 0; i < len2; i++) {
                char ziname = v1[i + start1 + lenDiff];
                char zivoice = v2[i + start2];
                float ziScore = PinyinUtils.zi(ziname, zivoice);
                if (ziScore <= 0.0001f) {
                    //ziScore = -0.5f;
                    return 0;
                }
                sum += ziScore;
            }

            if (lenDiff == 0) {
                result = sum / len2;
            } else {
                result = sum / len2 * 0.9f;
            }
        }

		/*if ( result > 0.33f ) {
            System.out.println("test: " + w.getValue() + ":" + new String(v2, start2, len2) + " score:　" + result);
		}*/

        return result;
    }

    private float score = 0.6f;

    public void setScore(float score) {
        this.score = score;
    }
    
   /* public int compare(long v, char[] v2, int start2, int len2) {
    	
    	w.v = v;
    	
    	int start1 = w.getStart();
    	int len1 = w.getLength();
        int lim = Math.min(len1, len2);
        char v1[] = chars;
        //char v2[] = key;

        int k = 0;
        while (k < lim) {
            char c1 = v1[k + start1];
            char c2 = v2[k + start2];
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }
        return len1 - len2;
	}*/


    final Word w = new Word(0, -1);


    @Override
    public void destroy() {
        chars = null;
        dics = null;
    }

}
