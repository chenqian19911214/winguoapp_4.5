package com.guobi.gfc.VoiceFun.jcseg.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * a class to deal with the english stop char
 * like the english punctuation ';' <br />
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public class ENSCFilter {

    private static final Character[] EN_KEEP_CHARS = {
            '@', '$', '%', '^', '&', '-', ':', '.', '/', '\'', '#', '+'};

    private static Map<Character, Character> enKeepChar = null;

    static {
        enKeepChar = new HashMap<Character, Character>(
                (int) (EN_KEEP_CHARS.length / 1.7) + 1, 0.85f);
        //set the keep char's keep status
        for (int j = 0; j < EN_KEEP_CHARS.length; j++)
            enKeepChar.put(EN_KEEP_CHARS[j], EN_KEEP_CHARS[j]);
    }

    /**
     * check the given char is english stop punctuation.
     *
     * @param c
     * @return boolean
     */
    public static boolean isENKeeepChar(char c) {
        return enKeepChar.containsKey(c);
    }

    public static boolean isUpperCaseLetter(int u) {
        return (u >= 65 && u <= 90);
    }

    public static boolean isLowerCaseLetter(int u) {
        return (u >= 97 && u <= 122);
    }

    public static int toLowerCase(int u) {
        return (u + 32);
    }

    public static int toUpperCase(int u) {
        return (u - 32);
    }


    /**
     * check the given char is a half-width char or not.
     * <ul>
     * <li>32 		-&gt; whitespace</li>
     * <li>33-47	-&gt; punctuations</li>
     * <li>48-57	-&gt; 0-9</li>
     * <li>58-64	-&gt; punctuations</li>
     * <li>65-90	-&gt; A-Z</li>
     * <li>91-96	-&gt; 97-122</li>
     * <li>97-122	-&gt; a-z</li>
     * <li>123-126	-&gt; punctuations</li>
     * </ul>
     *
     * @param int
     * @return boolean
     */
    public static boolean isHalfWidthChar(int c) {
        return (c >= 32 && c <= 126);
    }

    /**
     * check the given char is half-width punctuation.<br />
     *
     * @param c
     * @return boolean
     */
    public static boolean isPunctuation(int c) {
        if (c > 65280) c = c - 65248;
        return ((c > 32 && c < 48)
                || (c > 57 && c < 65)
                || (c > 90 && c < 97)
                || (c > 122 && c < 127));
    }

    /**
     * check the given string is a whitespace. <br />
     *
     * @param c
     * @return boolean;
     */
    public static boolean isWhitespace(int c) {
        return (c == 32 || c == 12288);
    }

    /**
     * check the given char is a full-width char.  <br />
     * full-width char - 65248 = half-width char. (except for the whitespace). <br />
     *
     * @param c
     * @return boolean
     */
    public static boolean isFullWidthChar(int c) {
        //full width space, \u0020->32, \u3000->12288
        if (c == 12288) return true;
        return (c > 65280 && c <= 65406);
    }

    /**
     * a static method to replace the full-width char to the half-width char
     * in a given string. <br />
     * (65281-65374 for full-width char) <br />
     *
     * @param str
     * @return String the new String after the replace.
     */
    public static String fwsTohws(String str) {
        char[] chars = str.toCharArray();
        for (int j = 0; j < chars.length; j++) {
            if (chars[j] == '\u3000')
                chars[j] = '\u0020';
            else if (chars[j] > '\uFF00' && chars[j] < '\uFF5F')
                chars[j] = (char) (chars[j] - 65248);
        }
        return new String(chars);
    }

    /**
     * a static method to replace the half-width char to the full-width char.
     * in a given string. <br />
     *
     * @param str
     * @return String the new String after the replace.
     */
    public static String hwsTofws(String str) {
        char[] chars = str.toCharArray();
        for (int j = 0; j < chars.length; j++) {
            if (chars[j] == '\u0020')
                chars[j] = '\u3000';
            else if (chars[j] < '\177')
                chars[j] = (char) (chars[j] + 65248);
        }
        return new String(chars);
    }
}
