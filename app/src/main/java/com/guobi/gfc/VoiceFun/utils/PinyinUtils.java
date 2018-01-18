package com.guobi.gfc.VoiceFun.utils;

import android.text.TextUtils;

import com.guobi.common.wordpy.Wordpy;

import java.util.HashMap;


public class PinyinUtils {

    /**
     * 检查声母相似度
     *
     * @param name
     * @param voice
     * @return
     */
    private static float shengmu(String name, String voice) {
        float result = 0.0f;
        if (name.equals(voice)) {
            // 声母一样
            result = 1;
        } else {
            // 声母不一样， 检查模糊音
            int f = binarySearch(MH_SM_TABLE, name);
            if (f >= 0) {
                if (MH_SM_TABLE[f][1].equals(voice)) {
                    // 是模糊音
                    // 形如 l&n、 f&h的相似度得分
                    result = 0.8f;
                }
            }
        }

        return result;
    }

    /**
     * 检查韵母相似度
     *
     * @param name
     * @param voice
     * @return 相似度得分
     */
    private static float yunmu(String name, String voice) {
        float result = 0.0f;
        if (name.equals(voice)) {
            // 韵母一样
            result = 1;
        } else {
            // 韵母不一样，检查韵腹+韵尾是否一样, 或者是否模糊音
            int fname = binarySearch(YM_TABLE, name);
            int fvoice = binarySearch(YM_TABLE, voice);
            String tname = name;
            String tvoice = voice;
            if (fname >= 0) {
                tname = YM_TABLE[fname][2];
            }
            if (fvoice >= 0) {
                tvoice = YM_TABLE[fvoice][2];
            }
            if (tname.equals(tvoice)) {
                // 如官〔guan〕这个音节中，
                //〔g〕是声母，〔uan〕是韵母。韵母〔uan〕中，〔a〕是韵腹，〔u〕是韵头，〔n〕是韵尾
                // 这里检查韵腹+韵尾是否一样， 如果一样，说明也是押韵的
                // 形如 uang&ang、 uang&iang的相似度得分
                result = 0.7f;
            } else {
                // 韵腹韵尾不一样，检查是否韵母模糊音
                int f = binarySearch(MH_YM_TABLE, tname);
                if (f >= 0) {
                    if (MH_YM_TABLE[f][1].equals(tvoice)) {
                        // 是模糊音
                        if (name.startsWith(voice) || voice.startsWith(name)) {
                            // 形如 ang&an、 uan&uang的相似度得分
                            result = 0.93f;
                        } else {
                            //形如 uang&an、 uang&ian的相似度得分
                            result = 0.6f;
                        }
                    }
                }
            }

            if (result == 0.0f) {
                int f = binarySearch(MH_YM2_TABLE, name);
                if (f >= 0) {
                    if (MH_YM2_TABLE[f][1].equals(voice)) {
                        // 形如 ue&uan、üan&üe的相似度得分
                        result = 0.9f;
                    }
                } else {
                    // 韵腹韵尾不一样，也不是模糊音， 则判断韵腹是否一样
                    if (tname.charAt(0) == tvoice.charAt(0)) {
                        // 韵腹一样
                        // 形如 ang&a ao&a的相似度得分
                        result = 0.33f;
                    }
                }
            }

        }


        return result;
    }

    /**
     * 检查拼音相似度
     *
     * @param name
     * @param voice
     * @return 相似度得分
     */
    public static float pinyin(String name, String voice) {
        float result = 0;
        final String table[][] = PY_TABLE;

        // 查表拆分拼音的声母 韵母
        int fname = binarySearch(table, name);
        int fvoice = binarySearch(table, voice);
        if (fname >= 0 && fvoice >= 0) {
            // 分别计算声母和韵母的相似度得分
            float smScore = shengmu(table[fname][1], table[fvoice][1]);
            float ymScore = yunmu(table[fname][2], table[fvoice][2]);

            // 拼音相似度得分 = (声母相似度得分 * 25%) + (韵母相似度得分 * 75%)
            result = smScore * 0.25f + ymScore * 0.75f;
        }

        return result;
    }

    /**
     * 检查名字中单个字的相似度
     *
     * @param name
     * @param voice
     * @return 相似度得分
     */
    public static long ptime = 0;

    public static float zi(char name, char voice) {
        float result = 0;
        if (name == voice) {
            // 单个字一样
            result = 1;
        } else {
            // 字不一样，检查拼音
            long time0 = System.nanoTime();
            //String pyname = GBIMEV5Lite.getPinyinCode(name, 0);
            //String pyvoice = GBIMEV5Lite.getPinyinCode(voice, 0);
            String pyname = getPinyin(name);
            String pyvoice = getPinyin(voice);
            long time1 = System.nanoTime() - time0;
            ptime += time1;
            //System.out.println("seg: ptime: " + ptime +  " a:" + time1);
            if (pyname != null && pyvoice != null) {
                if (pyname.equals(pyvoice)) {
                    // 拼音一致
                    result = 0.95f;
                } else {
                    // 拼音不一致
                    float pyScore = pinyin(pyname, pyvoice);
                    result = 0.95f * pyScore;
                }
            }
        }
        return result;
    }

    /**
     * 检查名字的相似度
     *
     * @param name
     * @param voice
     * @return 相似度得分
     */
    public static float xingming(String name, String voice) {
        float result = 0;
        if (name.equals(voice)) {
            result = 1;
        } else {
            int lname = name.length();
            int lvoice = voice.length();
            int lenMin = Math.min(lname, lvoice);
            int lenMax = Math.max(lname, lvoice);
            float sum = 0;
            for (int i = 0; i < lenMin; i++) {
                char ziname = name.charAt(i);
                char zivoice = voice.charAt(i);
                float ziScore = zi(ziname, zivoice);
                if (ziScore <= 0.001f) {
                    // 名字中某个字相似度得分为0， 扣0.5分
                    ziScore = -0.5f;
                }
                sum += ziScore;
            }

            // 每少一个字扣0.4分
            sum -= (lenMax - lenMin) * 0.4f;

            // 名字相似度得分 = 每个字的平均得分
            result = sum / lenMax;
        }
        return result;
    }


    private static HashMap<Character, String> mPinyinMap = new HashMap<Character, String>();

    public static String getPinyin(char zi) {
        String pinyin = mPinyinMap.get(zi);
        if (pinyin != null) {
            return pinyin;
        } else {
            String z = String.valueOf(zi);
            Wordpy wordpy = new Wordpy();
            String pyname = wordpy.getPinyinCode(z, 0);
            if (TextUtils.isEmpty(pyname)) {
                int f = binarySearch(ABC_2_PY_TABLE, z.toUpperCase());
                if (f >= 0) {
                    pyname = ABC_2_PY_TABLE[f][1];
                    System.out.println("news_data_item: " + z + " : " + pyname);
                }
            }

            mPinyinMap.put(zi, pyname);
            return pyname;
        }

    }


    private static int binarySearch(String[][] a, String key) {
        int low = 0;
        int high = a.length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            String[] midVal = a[mid];
            int cmp = compare(midVal, key);
            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid;
        }
        return -(low + 1);
    }


    private static int compare(String[] arg0, String arg1) {
        return arg0[0].compareTo(arg1);
    }


    public static void test() {

        long time0 = System.currentTimeMillis();

        System.out.println("news_data_item: " + shengmu("b", "c"));
        System.out.println("news_data_item: " + shengmu("b", "b"));
        System.out.println("news_data_item: " + shengmu("c", "ch"));
        System.out.println("news_data_item: " + shengmu("ch", "c"));
        System.out.println("news_data_item: " + shengmu("zh", "z"));
        System.out.println("news_data_item: " + shengmu("l", "n"));
        System.out.println("news_data_item: " + shengmu("n", "l"));
        System.out.println("news_data_item: " + shengmu("f", "h"));

        System.out.println("news_data_item: " + yunmu("a", "a"));
        System.out.println("news_data_item: " + yunmu("ao", "ao"));
        System.out.println("news_data_item: " + yunmu("ao", "a"));
        System.out.println("news_data_item: " + yunmu("ao", "o"));
        System.out.println("news_data_item: " + yunmu("iu", "iu"));
        System.out.println("news_data_item: " + yunmu("iou", "iou"));
        System.out.println("news_data_item: " + yunmu("iou", "ou"));
        System.out.println("news_data_item: " + yunmu("iou", "i"));
        System.out.println("news_data_item: " + yunmu("ou", "iou"));

        System.out.println("news_data_item: " + yunmu("eng", "eng"));
        System.out.println("news_data_item: " + yunmu("eng", "ueng"));
        System.out.println("news_data_item: " + yunmu("eng", "uen"));
        System.out.println("news_data_item: " + yunmu("en", "uen"));
        System.out.println("news_data_item: " + yunmu("ueng", "eng"));
        System.out.println("news_data_item: " + yunmu("eng", "en"));
        System.out.println("news_data_item: " + yunmu("en", "eng"));
        System.out.println("news_data_item: " + yunmu("uen", "ueng"));
        System.out.println("news_data_item: " + yunmu("ueng", "eng"));
        System.out.println("news_data_item: " + yunmu("ueng", "ng"));
        System.out.println("news_data_item: " + yunmu("ueng", "eng"));
        System.out.println("news_data_item: " + yunmu("ueng", "uen"));
        System.out.println("news_data_item: " + yunmu("ueng", "en"));

        System.out.println("news_data_item: " + yunmu("ia", "a"));
        System.out.println("news_data_item: " + yunmu("ia", "ian"));
        System.out.println("news_data_item: " + yunmu("uang", "ian"));
        System.out.println("news_data_item: " + yunmu("uang", "ia"));
        System.out.println("news_data_item: " + yunmu("uang", "ian"));
        System.out.println("news_data_item: " + yunmu("uang", "iang"));
        System.out.println("news_data_item: " + yunmu("uang", "ueng"));

        System.out.println("news_data_item: " + yunmu("a", "o"));
        System.out.println("news_data_item: " + yunmu("a", "ao"));
        System.out.println("news_data_item: " + yunmu("o", "ao"));
        System.out.println("news_data_item: " + yunmu("e", "ei"));
        System.out.println("news_data_item: " + yunmu("uang", "a"));
        System.out.println("news_data_item: " + yunmu("uang", "an"));
        System.out.println("news_data_item: " + yunmu("ao", "a"));
        System.out.println("news_data_item: " + yunmu("a", "uang"));
        System.out.println("news_data_item: " + yunmu("a", "ua"));
        System.out.println("news_data_item: " + yunmu("ua", "a"));
        System.out.println("news_data_item: " + yunmu("ua", "ia"));

        System.out.println("news_data_item: " + pinyin("wa", "ta"));
        System.out.println("news_data_item: " + pinyin("fa", "ta"));
        System.out.println("news_data_item: " + pinyin("wen", "tun"));
        System.out.println("news_data_item: " + pinyin("you", "qiu"));

        System.out.println("news_data_item: " + pinyin("ta", "ta"));
        System.out.println("news_data_item: " + pinyin("ta", "tan"));
        System.out.println("news_data_item: " + pinyin("tang", "tan"));
        System.out.println("news_data_item: " + pinyin("tuan", "tan"));
        System.out.println("news_data_item: " + pinyin("tuan", "huan"));
        System.out.println("news_data_item: " + pinyin("tuan", "huang"));
        System.out.println("news_data_item: " + pinyin("tan", "huang"));
        System.out.println("news_data_item: " + pinyin("hua", "fa"));
        System.out.println("news_data_item: " + pinyin("hua", "fang"));
        System.out.println("news_data_item: " + pinyin("nu", "lu"));

        System.out.println("news_data_item: " + zi('戴', '戴'));
        System.out.println("news_data_item: " + zi('戴', '带'));
        System.out.println("news_data_item: " + zi('戴', '坏'));
        System.out.println("news_data_item: " + zi('戴', '帅'));
        System.out.println("news_data_item: " + zi('戴', '赛'));
        System.out.println("news_data_item: " + zi('唐', '谭'));

        System.out.println("news_data_item: " + xingming("戴双芳", "戴双芳"));
        System.out.println("news_data_item: " + xingming("戴双芳", "戴双方"));
        System.out.println("news_data_item: " + xingming("戴双芳", "带双方"));
        System.out.println("news_data_item: " + xingming("戴双芳", "带双凡"));
        System.out.println("news_data_item: " + xingming("戴双芳", "带沙发"));
        System.out.println("news_data_item: " + xingming("戴双芳", "晒沙发"));
        System.out.println("news_data_item: " + xingming("戴双芳", "坏双方"));
        System.out.println("news_data_item: " + xingming("戴双芳", "戴双"));
        System.out.println("news_data_item: " + xingming("戴双芳", "戴双好"));
        System.out.println("news_data_item: " + xingming("戴双芳", "戴双一"));

		/*for ( int i = 0; i < 300; i++ ) {
			xingming("戴双芳", "帅沙发");
		}*/

        System.out.println("news_data_item: cost time: " + (System.currentTimeMillis() - time0));
    }


    private static final String[][] MH_SM_TABLE = new String[][]{
            {"c", "ch"},
            {"ch", "c"},
            {"f", "h"},
            {"h", "f"},
            {"l", "n"},
            {"n", "l"},
            {"s", "sh"},
            {"sh", "s"},
            {"z", "zh"},
            {"zh", "z"},
    };

    private static final String[][] MH_YM_TABLE = new String[][]{
            {"an", "ang"},
            {"ang", "an"},
            {"en", "eng"},
            {"eng", "en"},
            {"in", "ing"},
            {"ing", "in"},
    };

    private static final String[][] MH_YM2_TABLE = new String[][]{
            {"ian", "ie"},
            {"ie", "ian"},
            {"üan", "üe"},
            {"üe", "üan"}
    };

    private static final String[][] YM_TABLE = new String[][]{
            {"ia", "i", "a"},
            {"ian", "i", "an"},
            {"iang", "i", "ang"},
            {"iao", "i", "ao"},
            {"ie", "i", "e"},
            {"iong", "i", "ong"},
            {"iou", "i", "ou"},
            {"ua", "u", "a"},
            {"uai", "u", "ai"},
            {"uan", "u", "an"},
            {"uang", "u", "ang"},
            {"uei", "u", "ei"},
            {"uen", "u", "en"},
            {"ueng", "u", "eng"},
            {"uo", "u", "o"},
            {"üan", "ü", "an"},
            {"üe", "ü", "e"},
    };

    private static final String[][] PY_TABLE = new String[][]{
            {"a", "@", "a"},
            {"ai", "@", "ai"},
            {"an", "@", "an"},
            {"ang", "@", "ang"},
            {"ao", "@", "ao"},
            {"ba", "b", "a"},
            {"bai", "b", "ai"},
            {"ban", "b", "an"},
            {"bang", "b", "ang"},
            {"bao", "b", "ao"},
            {"bei", "b", "ei"},
            {"ben", "b", "en"},
            {"beng", "b", "eng"},
            {"bi", "b", "i"},
            {"bian", "b", "ian"},
            {"biao", "b", "iao"},
            {"bie", "b", "ie"},
            {"bin", "b", "in"},
            {"bing", "b", "ing"},
            {"bo", "b", "o"},
            {"bu", "b", "u"},
            {"ca", "c", "a"},
            {"cai", "c", "ai"},
            {"can", "c", "an"},
            {"cang", "c", "ang"},
            {"cao", "c", "ao"},
            {"ce", "c", "e"},
            {"cen", "c", "en"},
            {"ceng", "c", "eng"},
            {"cha", "ch", "a"},
            {"chai", "ch", "ai"},
            {"chan", "ch", "an"},
            {"chang", "ch", "ang"},
            {"chao", "ch", "ao"},
            {"che", "ch", "e"},
            {"chen", "ch", "en"},
            {"cheng", "ch", "eng"},
            {"chi", "ch", "i"},
            {"chong", "ch", "ong"},
            {"chou", "ch", "ou"},
            {"chu", "ch", "u"},
            {"chuai", "ch", "uai"},
            {"chuan", "ch", "uan"},
            {"chuang", "ch", "uang"},
            {"chui", "ch", "uei"},
            {"chun", "ch", "uen"},
            {"chuo", "ch", "uo"},
            {"ci", "c", "i"},
            {"cong", "c", "ong"},
            {"cou", "c", "ou"},
            {"cu", "c", "u"},
            {"cuan", "c", "uan"},
            {"cui", "c", "uei"},
            {"cun", "c", "uen"},
            {"cuo", "c", "uo"},
            {"da", "d", "a"},
            {"dai", "d", "ai"},
            {"dan", "d", "an"},
            {"dang", "d", "ang"},
            {"dao", "d", "ao"},
            {"de", "d", "e"},
            {"dei", "d", "ei"},
            {"deng", "d", "eng"},
            {"di", "d", "i"},
            {"dia", "d", "ia"},
            {"dian", "d", "ian"},
            {"diao", "d", "iao"},
            {"die", "d", "ie"},
            {"ding", "d", "ing"},
            {"diu", "d", "iou"},
            {"dong", "d", "ong"},
            {"dou", "d", "ou"},
            {"du", "d", "u"},
            {"duan", "d", "uan"},
            {"dui", "d", "uei"},
            {"dun", "d", "uen"},
            {"duo", "d", "uo"},
            {"e", "@", "e"},
            {"en", "@", "en"},
            {"er", "@", "er"},
            {"fa", "f", "a"},
            {"fan", "f", "an"},
            {"fang", "f", "ang"},
            {"fei", "f", "ei"},
            {"fen", "f", "en"},
            {"feng", "f", "eng"},
            {"fo", "f", "o"},
            {"fou", "f", "ou"},
            {"fu", "f", "u"},
            {"ga", "g", "a"},
            {"gai", "g", "ai"},
            {"gan", "g", "an"},
            {"gang", "g", "ang"},
            {"gao", "g", "ao"},
            {"ge", "g", "e"},
            {"gei", "g", "ei"},
            {"gen", "g", "en"},
            {"geng", "g", "eng"},
            {"gong", "g", "ong"},
            {"gou", "g", "ou"},
            {"gu", "g", "u"},
            {"gua", "g", "ua"},
            {"guai", "g", "uai"},
            {"guan", "g", "uan"},
            {"guang", "g", "uang"},
            {"gui", "g", "uei"},
            {"gun", "g", "uen"},
            {"guo", "g", "uo"},
            {"ha", "h", "a"},
            {"hai", "h", "ai"},
            {"han", "h", "an"},
            {"hang", "h", "ang"},
            {"hao", "h", "ao"},
            {"he", "h", "e"},
            {"hei", "h", "ei"},
            {"hen", "h", "en"},
            {"heng", "h", "eng"},
            {"hng", "h", "ng"},
            {"hong", "h", "ong"},
            {"hou", "h", "ou"},
            {"hu", "h", "u"},
            {"hua", "h", "ua"},
            {"huai", "h", "uai"},
            {"huan", "h", "uan"},
            {"huang", "h", "uang"},
            {"hui", "h", "uei"},
            {"hun", "h", "uen"},
            {"huo", "h", "uo"},
            {"ji", "j", "i"},
            {"jia", "j", "ia"},
            {"jian", "j", "ian"},
            {"jiang", "j", "iang"},
            {"jiao", "j", "iao"},
            {"jie", "j", "ie"},
            {"jin", "j", "in"},
            {"jing", "j", "ing"},
            {"jiong", "j", "iong"},
            {"jiu", "j", "iou"},
            {"ju", "j", "ü"},
            {"juan", "j", "üan"},
            {"jue", "j", "üe"},
            {"jun", "j", "ün"},
            {"ka", "k", "a"},
            {"kai", "k", "ai"},
            {"kan", "k", "an"},
            {"kang", "k", "ang"},
            {"kao", "k", "ao"},
            {"ke", "k", "e"},
            {"ken", "k", "en"},
            {"keng", "k", "eng"},
            {"kong", "k", "ong"},
            {"kou", "k", "ou"},
            {"ku", "k", "u"},
            {"kua", "k", "ua"},
            {"kuai", "k", "uai"},
            {"kuan", "k", "uan"},
            {"kuang", "k", "uang"},
            {"kui", "k", "uei"},
            {"kun", "k", "uen"},
            {"kuo", "k", "uo"},
            {"la", "l", "a"},
            {"lai", "l", "ai"},
            {"lan", "l", "an"},
            {"lang", "l", "ang"},
            {"lao", "l", "ao"},
            {"le", "l", "e"},
            {"lei", "l", "ei"},
            {"leng", "l", "eng"},
            {"li", "l", "i"},
            {"lia", "l", "ia"},
            {"lian", "l", "ian"},
            {"liang", "l", "iang"},
            {"liao", "l", "iao"},
            {"lie", "l", "ie"},
            {"lin", "l", "in"},
            {"ling", "l", "ing"},
            {"liu", "l", "iou"},
            {"long", "l", "ong"},
            {"lou", "l", "ou"},
            {"lu", "l", "u"},
            {"luan", "l", "uan"},
            {"lue", "l", "üe"},
            {"lun", "l", "uen"},
            {"luo", "l", "uo"},
            {"lv", "l", "ü"},
            {"lve", "l", "üe"},
            {"m", "m", "@"},
            {"ma", "m", "a"},
            {"mai", "m", "ai"},
            {"man", "m", "an"},
            {"mang", "m", "ang"},
            {"mao", "m", "ao"},
            {"me", "m", "e"},
            {"mei", "m", "ei"},
            {"men", "m", "en"},
            {"meng", "m", "eng"},
            {"mi", "m", "i"},
            {"mian", "m", "ian"},
            {"miao", "m", "iao"},
            {"mie", "m", "ie"},
            {"min", "m", "in"},
            {"ming", "m", "ing"},
            {"miu", "m", "iou"},
            {"mo", "m", "o"},
            {"mou", "m", "ou"},
            {"mu", "m", "u"},
            {"n", "n", "@"},
            {"na", "n", "a"},
            {"nai", "n", "ai"},
            {"nan", "n", "an"},
            {"nang", "n", "ang"},
            {"nao", "n", "ao"},
            {"ne", "n", "e"},
            {"nei", "n", "ei"},
            {"nen", "n", "en"},
            {"neng", "n", "eng"},
            {"ng", "n", "g"},
            {"ni", "n", "i"},
            {"nian", "n", "ian"},
            {"niang", "n", "iang"},
            {"niao", "n", "iao"},
            {"nie", "n", "ie"},
            {"nin", "n", "in"},
            {"ning", "n", "ing"},
            {"niu", "n", "iou"},
            {"nong", "n", "ong"},
            {"nou", "n", "ou"},
            {"nu", "n", "u"},
            {"nuan", "n", "uan"},
            {"nue", "n", "üe"},
            {"nuo", "n", "uo"},
            {"nv", "n", "ü"},
            {"nve", "n", "üe"},
            {"o", "@", "o"},
            {"ou", "@", "ou"},
            {"pa", "p", "a"},
            {"pai", "p", "ai"},
            {"pan", "p", "an"},
            {"pang", "p", "ang"},
            {"pao", "p", "ao"},
            {"pei", "p", "ei"},
            {"pen", "p", "en"},
            {"peng", "p", "eng"},
            {"pi", "p", "i"},
            {"pian", "p", "ian"},
            {"piao", "p", "iao"},
            {"pie", "p", "ie"},
            {"pin", "p", "in"},
            {"ping", "p", "ing"},
            {"po", "p", "o"},
            {"pou", "p", "ou"},
            {"pu", "p", "u"},
            {"qi", "q", "i"},
            {"qia", "q", "ia"},
            {"qian", "q", "ian"},
            {"qiang", "q", "iang"},
            {"qiao", "q", "iao"},
            {"qie", "q", "ie"},
            {"qin", "q", "in"},
            {"qing", "q", "ing"},
            {"qiong", "q", "iong"},
            {"qiu", "q", "iou"},
            {"qu", "q", "ü"},
            {"quan", "q", "üan"},
            {"que", "q", "üe"},
            {"qun", "q", "ün"},
            {"ran", "r", "an"},
            {"rang", "r", "ang"},
            {"rao", "r", "ao"},
            {"re", "r", "e"},
            {"ren", "r", "en"},
            {"reng", "r", "eng"},
            {"ri", "r", "i"},
            {"rong", "r", "ong"},
            {"rou", "r", "ou"},
            {"ru", "r", "u"},
            {"ruan", "r", "uan"},
            {"rui", "r", "uei"},
            {"run", "r", "uen"},
            {"ruo", "r", "uo"},
            {"sa", "s", "a"},
            {"sai", "s", "ai"},
            {"san", "s", "an"},
            {"sang", "s", "ang"},
            {"sao", "s", "ao"},
            {"se", "s", "e"},
            {"sen", "s", "en"},
            {"seng", "s", "eng"},
            {"sha", "sh", "a"},
            {"shai", "sh", "ai"},
            {"shan", "sh", "an"},
            {"shang", "sh", "ang"},
            {"shao", "sh", "ao"},
            {"she", "sh", "e"},
            {"shei", "sh", "ei"},
            {"shen", "sh", "en"},
            {"sheng", "sh", "eng"},
            {"shi", "sh", "i"},
            {"shou", "sh", "ou"},
            {"shu", "sh", "u"},
            {"shua", "sh", "ua"},
            {"shuai", "sh", "uai"},
            {"shuan", "sh", "uan"},
            {"shuang", "sh", "uang"},
            {"shui", "sh", "uei"},
            {"shun", "sh", "uen"},
            {"shuo", "sh", "uo"},
            {"si", "s", "i"},
            {"song", "s", "ong"},
            {"sou", "s", "ou"},
            {"su", "s", "u"},
            {"suan", "s", "uan"},
            {"sui", "s", "uei"},
            {"sun", "s", "uen"},
            {"suo", "s", "uo"},
            {"ta", "t", "a"},
            {"tai", "t", "ai"},
            {"tan", "t", "an"},
            {"tang", "t", "ang"},
            {"tao", "t", "ao"},
            {"te", "t", "e"},
            {"tei", "t", "ei"},
            {"teng", "t", "eng"},
            {"ti", "t", "i"},
            {"tian", "t", "ian"},
            {"tiao", "t", "iao"},
            {"tie", "t", "ie"},
            {"ting", "t", "ing"},
            {"tong", "t", "ong"},
            {"tou", "t", "ou"},
            {"tu", "t", "u"},
            {"tuan", "t", "uan"},
            {"tui", "t", "uei"},
            {"tun", "t", "uen"},
            {"tuo", "t", "uo"},
            {"wa", "@", "ua"},
            {"wai", "@", "uai"},
            {"wan", "@", "uan"},
            {"wang", "@", "uang"},
            {"wei", "@", "ei"},
            {"wen", "@", "uen"},
            {"weng", "@", "ueng"},
            {"wo", "@", "uo"},
            {"wu", "@", "u"},
            {"xi", "x", "i"},
            {"xia", "x", "ia"},
            {"xian", "x", "ian"},
            {"xiang", "x", "iang"},
            {"xiao", "x", "iao"},
            {"xie", "x", "ie"},
            {"xin", "x", "in"},
            {"xing", "x", "ing"},
            {"xiong", "x", "iong"},
            {"xiu", "x", "iou"},
            {"xu", "x", "ü"},
            {"xuan", "x", "üan"},
            {"xue", "x", "üe"},
            {"xun", "x", "ün"},
            {"ya", "@", "ia"},
            {"yan", "@", "ian"},
            {"yang", "@", "iang"},
            {"yao", "@", "iao"},
            {"ye", "@", "ie"},
            {"yi", "@", "i"},
            {"yin", "@", "in"},
            {"ying", "@", "ing"},
            {"yo", "@", "o"},
            {"yong", "@", "iong"},
            {"you", "@", "iou"},
            {"yu", "@", "ü"},
            {"yuan", "@", "üan"},
            {"yue", "@", "üe"},
            {"yun", "@", "ün"},
            {"za", "z", "a"},
            {"zai", "z", "ai"},
            {"zan", "z", "an"},
            {"zang", "z", "ang"},
            {"zao", "z", "ao"},
            {"ze", "z", "e"},
            {"zei", "z", "ei"},
            {"zen", "z", "en"},
            {"zeng", "z", "eng"},
            {"zha", "zh", "a"},
            {"zhai", "zh", "ai"},
            {"zhan", "zh", "an"},
            {"zhang", "zh", "ang"},
            {"zhao", "zh", "ao"},
            {"zhe", "zh", "e"},
            {"zhei", "zh", "ei"},
            {"zhen", "zh", "en"},
            {"zheng", "zh", "eng"},
            {"zhi", "zh", "i"},
            {"zhong", "zh", "ong"},
            {"zhou", "zh", "ou"},
            {"zhu", "zh", "u"},
            {"zhua", "zh", "ua"},
            {"zhuai", "zh", "uai"},
            {"zhuan", "zh", "uan"},
            {"zhuang", "zh", "uang"},
            {"zhui", "zh", "uei"},
            {"zhun", "zh", "uen"},
            {"zhuo", "zh", "uo"},
            {"zi", "z", "i"},
            {"zong", "z", "ong"},
            {"zou", "z", "ou"},
            {"zu", "z", "u"},
            {"zuan", "z", "uan"},
            {"zui", "z", "uei"},
            {"zun", "z", "uen"},
            {"zuo", "z", "uo"},
    };

    private static final String[][] ABC_2_PY_TABLE = new String[][]{
            {"A", "en"},
            {"B", "bi"},
            {"C", "xi"},
            {"D", "di"},
            {"E", "yi"},
            {"F", "fu"},
            {"G", "ji"},
            {"H", "qu"},
            {"I", "ai"},
            {"J", "jie"},
            {"K", "ke"},
            {"L", "o"},
            {"M", "en"},
            {"N", "en"},
            {"O", "ou"},
            {"P", "pi"},
            {"Q", "kou"},
            {"R", "a"},
            {"S", "si"},
            {"T", "ti"},
            {"U", "you"},
            {"V", "wei"},
            {"Y", "wai"},
            {"Z", "ze"},
    };


}
