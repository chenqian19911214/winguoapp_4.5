package com.guobi.gfc.VoiceFun.jcseg.core;

/**
 * Configuration for JCSeg
 * MAX_LENGTH maybe changed, in the property file.
 * see the
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public class Config {
    /**
     * maximum length for maximum match
     * i suggest 5 here
     * -coule be changed throught the jcseg.properties.
     */
    public static int MAX_LENGTH = 5;

    /**
     * maximum length for the chinese after the LATIN word.
     * use to match chinese and english mix word,
     * like 'B超,AA制...';
     * -coule be changed throught the jcseg.properties.
     */
    public static int MIX_CN_LENGTH = 2;

    /**
     * identify the chinese name?
     */
    public static boolean I_CN_NAME = false;

    /**
     * the max length for the adron of the chinese last name.
     * like 老陈 “老”
     * -coule be changed throught the jcseg.properties.
     */
    public static int MAX_CN_LNADRON = 1;

    /**
     * wether to load the pinying of the CJK_WORDS
     * -could be changed throught the jcseg.prperties.file
     */
    public static boolean LOAD_CJK_PINYIN = false;

    /**
     * append the pinying to the splited IWord
     */
    public static boolean APPEND_CJK_PINYIN = false;

    /**
     * wether to load the syn word of the CJK_WORDS
     * -could be changed throught the jcseg.prperties.file
     */
    public static boolean LOAD_CJK_SYN = false;

    /**
     * append the syn word to the splited IWord.
     */
    public static boolean APPEND_CJK_SYN = true;

    /**
     * wether to load the word's part of speech
     */
    public static boolean LOAD_CJK_POS = false;

    /**
     * simple algorithm segment
     */
    public static final int SIMPLE_MODE = 1;

    /**
     * complex algorithm segment
     */
    public static final int COMPLEX_MODE = 2;

    /**
     * the threshold of the single word that is a single word
     * when it and the last char of the name make up a word.
     * --could be changed throught the jcseg.properties.
     */
    public static int NAME_SINGLE_THRESHOLD = 1000000;

    /**
     * the maxinum length for the text bettween the pair punctution defined
     * in class ADictionary. <br />
     */
    public static int PPT_MAX_LENGTH = 15;

    /**
     * clear away the stopword. <br />
     */
    public static boolean CLEAR_STOPWORD = false;
}
