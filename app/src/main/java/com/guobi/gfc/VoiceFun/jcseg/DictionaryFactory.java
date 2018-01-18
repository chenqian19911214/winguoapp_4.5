package com.guobi.gfc.VoiceFun.jcseg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Properties;

import com.guobi.gfc.VoiceFun.jcseg.core.Config;
import com.guobi.gfc.VoiceFun.jcseg.core.IDictionary;
import com.guobi.gfc.VoiceFun.jcseg.core.ILexicon;
import com.guobi.gfc.VoiceFun.jcseg.core.IWord;
import com.guobi.gfc.VoiceFun.jcseg.core.LexiconException;
import com.guobi.gfc.VoiceFun.jcseg.util.Resource;

/**
 * Dictionary Factory to create singleton Dictionary object. a path of the class
 * that has implemented the IDictionary interface must be given first. <br />
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public class DictionaryFactory {

    /**
     * lexicon property file
     */
    public static final String LEX_PROPERTY_FILE = "jcseg.properties";

    private static final String DEFAULT_DICT_CLASS = "com.webssky.jcseg.Dictionary";

    /**
     * Dictionary singleton quote
     */
    private static IDictionary __instance = null;

    /**
     * lexicon Property object
     */
    private static Properties lexPro;


    /**
     * return the IDictionary instance
     *
     * @param __dicClass
     * @return IDictionary
     */
    public static IDictionary createDictionary(String __dicClass, InputStream dictStream, String dictFile, boolean train) throws Exception {
        if (__instance == null) {
            loadPropertes();

            if (train) {
                trainDictionary(__dicClass, dictFile);
            }

            System.out.print("loading dictionary for word segment ... ");
            //__instance = loadDictionary2(__dicClass, dictStream);

            IDictionary dic = new Dictionary();
            for (int i = 0; i < 200; i++) {
                dic.add(ILexicon.CJK_WORDS, "戴双芳", ILexicon.VOICE_XING_MING);
                dic.add(ILexicon.CJK_WORDS, "苏志光", ILexicon.VOICE_XING_MING);
                dic.add(ILexicon.CJK_WORDS, "谭玲慧", ILexicon.VOICE_XING_MING);
                dic.add(ILexicon.CJK_WORDS, "刘雄风", ILexicon.VOICE_XING_MING);
                dic.add(ILexicon.CJK_WORDS, "许泰赐", ILexicon.VOICE_XING_MING);
            }
            dic.add(0, "发短信", ILexicon.CJK_WORDS);
            dic.optimize();

            __instance = dic;
            System.out.println("done.");
        }

        return __instance;
    }

    public static IDictionary createDictionary(String __dicClass, boolean train) throws Exception {
        return createDictionary(__dicClass, Resource.getDictionaryStream(), Resource.getDictionaryFile(), train);
    }

    public static IDictionary createDictionary(boolean train) throws Exception {
        return createDictionary("com.webssky.jcseg.Dictionary", train);
    }

    public static IDictionary createDictionary() throws Exception {
        return createDictionary("com.webssky.jcseg.Dictionary", false);
    }

    public static IDictionary createTestDictionary() throws Exception {
        if (__instance == null) {
            loadPropertes();

			/*Dictionary dic = new Dictionary();
			for ( int i = 0; i < 200; i++ ) {
				dic.add(0, "戴双芳", ILexicon.VOICE_XING_MING);
				dic.add(0, "苏志光", ILexicon.VOICE_XING_MING);
				dic.add(0, "谭玲慧", ILexicon.VOICE_XING_MING);
				dic.add(0, "刘雄风", ILexicon.VOICE_XING_MING);
				dic.add(0, "许泰赐", ILexicon.VOICE_XING_MING);
				dic.add(0, "发短信", ILexicon.CJK_WORDS);
			}
			dic.optimize();
			
			__instance = dic;*/
        }

        return __instance;
    }


    /**
     * return the IDictionary instance
     *
     * @param __dicClass
     * @return IDictionary
     */
    public static IDictionary trainDictionary(String __dicClass, String dictFile) throws Exception {

        loadPropertes();

        __dicClass = (__dicClass == null) ? DEFAULT_DICT_CLASS : __dicClass;
        __instance = loadLexicon(__dicClass);

        saveDictionary2(__instance, dictFile);
        saveDictionaryAsText(__instance, dictFile + ".txt");

        return __instance;
    }


    public static void saveDictionary2(IDictionary adic, String fileName) throws Exception {
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
        Dictionary dic = (Dictionary) adic;

        char[] chars = dic.chars;
        int clen = chars.length;
        out.writeInt(clen);
        for (char c : chars) {
            out.writeChar(c);
        }

        long[][] dics = dic.dics;
        out.writeInt(dics.length);
        for (long[] dici : dics) {
            out.writeInt(dici.length);
            for (long d : dici) {
                out.writeLong(d);
            }
        }

        out.close();
    }

    public static void saveDictionaryAsText(IDictionary adic, String fileName) throws Exception {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName));
        Dictionary dic = (Dictionary) adic;

        long[][] dics = dic.dics;

        StringBuffer sb = new StringBuffer();
        for (long[] dici : dics) {
            sb.append(dici.length);
            sb.append('\n');
            for (long d : dici) {
                Word w = new Word(d, -1);
                sb.append(w.getValue());
                sb.append('\t');
                sb.append(w.getFrequency());
                sb.append('\n');
            }
        }

        out.write(sb.toString().getBytes());
        out.close();
    }

    public static IDictionary loadDictionary2(String __dicClass, InputStream infile) throws Exception {
        DataInputStream in = new DataInputStream(new BufferedInputStream(infile));

        Class<?> _class = Class.forName(__dicClass);
        Constructor<?> cons = _class.getConstructor();
        Dictionary dic = (Dictionary) cons.newInstance();

        int clen = in.readInt();
        char[] chars = new char[clen];
        for (int i = 0; i < clen; i++) {
            chars[i] = in.readChar();
        }

        dic.chars = chars;
        dic.charCount = clen;
        Word.setPublicCharBuffer(chars);

        int llen = in.readInt();
        //int llen = ILexicon.T_LEN;
        long[][] dics = new long[llen][];
        for (int i = 0; i < llen; i++) {
            int leni = in.readInt();
            dic.count[i] = leni;
            long[] dici = new long[leni];
            dics[i] = dici;
            for (int j = 0; j < leni; j++) {
                dici[j] = in.readLong();

                //Word w = new Word(dici[j]);
                //System.out.println(w.getValue() + " " + w.getFrequency());
            }
        }


        dic.dics = dics;

        in.close();

        return (IDictionary) dic;
    }

	/*public static void saveDictionary1(IDictionary dic, String fileName) throws Exception {
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(dic);
		fos.close();
	}

	public static IDictionary loadDictionary1(String fileName) throws Exception {
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream ois = new ObjectInputStream(fis);
		IDictionary dic = (IDictionary) ois.readObject();
		fis.close();

		return dic;
	}*/

    public static void loadPropertes() throws Exception {

        lexPro = new Properties();

		/*
		 * load the mapping from the property file. 1.load the the
		 * jcseg.properties located with the jar file. 2.load the
		 * jcseg.propertiess from the current directory.
		 */

        // System.out.println("j: home: " + propertyPath);

        lexPro.load(new BufferedInputStream(Resource.getPropertyStream()));

        if (lexPro.getProperty("jcseg.maxlen") != null)
            Config.MAX_LENGTH = Integer.parseInt(lexPro.getProperty("jcseg.maxlen"));
        if (lexPro.getProperty("jcseg.mixcnlen") != null)
            Config.MIX_CN_LENGTH = Integer.parseInt(lexPro.getProperty("jcseg.mixcnlen"));
        if (lexPro.getProperty("jcseg.icnname") != null && lexPro.getProperty("jcseg.icnname").equals("1"))
            Config.I_CN_NAME = true;
        if (lexPro.getProperty("jcseg.cnmaxlnadron") != null)
            Config.MAX_CN_LNADRON = Integer.parseInt(lexPro.getProperty("jcseg.cnmaxlnadron"));
        if (lexPro.getProperty("jcseg.nsthreshold") != null)
            Config.NAME_SINGLE_THRESHOLD = Integer.parseInt(lexPro.getProperty("jcseg.nsthreshold"));
        if (lexPro.getProperty("jcseg.pptmaxlen") != null)
            Config.PPT_MAX_LENGTH = Integer.parseInt(lexPro.getProperty("jcseg.pptmaxlen"));
        if (lexPro.getProperty("jcseg.loadpinyin") != null && lexPro.getProperty("jcseg.loadpinyin").equals("1"))
            Config.LOAD_CJK_PINYIN = true;
        if (lexPro.getProperty("jcseg.loadsyn") != null && lexPro.getProperty("jcseg.loadsyn").equals("1"))
            Config.LOAD_CJK_SYN = true;
        if (lexPro.getProperty("jcseg.loadpos") != null && lexPro.getProperty("jcseg.loadpos").equals("1"))
            Config.LOAD_CJK_POS = true;
        if (lexPro.getProperty("jcseg.clearstopword") != null && lexPro.getProperty("jcseg.clearstopword").equals("1"))
            Config.CLEAR_STOPWORD = true;
    }

    /**
     * load the lexcicon
     *
     * @param __dicClass ::the path of the class that has implemeted the IDictionary
     *                   interface.
     * @return IDictionary
     */
    private static IDictionary loadLexicon(String __dicClass) throws Exception {
        Class<?> _class = Class.forName(__dicClass);
        Constructor<?> cons = _class.getConstructor();
        IDictionary dic = (IDictionary) cons.newInstance();

        File[] files = getLexiconFiles();
        for (int j = 0; j < files.length; j++) {
            if (files[j].getName().endsWith("-t.lex")) {
                loadWordsFromScwsFile(dic, files[j], "UTF-8");
            } else {
                loadWordsFromFile(dic, files[j], "UTF-8");
            }
        }

        dic.optimize();

        System.out.println("done.");

        return dic;
    }

    /**
     * get all the lexicon file. this will parse the the lexicon.property file
     * first.
     *
     * @throws LexiconException
     * @throws IOException
     */
    private static File[] getLexiconFiles() throws Exception {

        File[] files = null;

        // the lexicon path
        String lexPath = Resource.getLexPath();

        System.out.println("lex path: " + lexPath);

        // the lexicon file prefix and suffix
        String suffix = "lex";
        String prefix = "lex";
        if (lexPro.getProperty("lexicon.suffix") != null)
            suffix = lexPro.getProperty("lexicon.suffix");
        if (lexPro.getProperty("lexicon.prefix") != null)
            prefix = lexPro.getProperty("lexicon.prefix");

        File path = new File(lexPath);
		
		/*
		 * load all the lexicon file under the lexicon path, that start with
		 * __prefix and end with __suffix.
		 */
        final String __suffix = suffix;
        final String __prefix = prefix;
        files = path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.startsWith(__prefix) && name.endsWith(__suffix));
            }
        });

        lexPro = null;

        return files;
    }

    /**
     * load all the words in the lexicon file, into the dictionary.
     *
     * @param IDictionary lex
     * @param File        file
     */
    private static void loadWordsFromFile(IDictionary dic, File file, String charset) throws Exception {

        System.out.println("loading " + file);

        InputStreamReader ir = new InputStreamReader(new FileInputStream(file), charset);
        BufferedReader br = new BufferedReader(ir);

        String line = null;
        boolean isFirstLine = true;
        int t = -1;
        int count = 0;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if ("".equals(line))
                continue;
            // swept the notes
            if (line.indexOf('#') == 0)
                continue;
            // the first line fo the lexicon file.
            if (isFirstLine == true) {

                String[] wd = line.split(" ");
                if (wd.length == 1) {
                    t = IDictionary.getIndex(line);
                } else {
                    t = IDictionary.getIndex(wd[0]);
                }

                isFirstLine = false;
                if (t >= 0) {
                    continue;
                }
            }

            // special lexicon
            if (line.indexOf('/') == -1) {
                if (line.length() <= Config.MAX_LENGTH) {
                    dic.add(t, line, IWord.T_CJK_WORD);
                }
            }
            // normal words lexicon file
            else {
                String[] wd = line.split("/");

                if (wd.length > 4) {
                    dic.add(t, wd[0], Integer.parseInt(wd[4]), IWord.T_CJK_WORD);
                } else {
                    dic.add(t, wd[0], IWord.T_CJK_WORD);
                }

            }

            count++;
        }

        System.out.println("word count: " + count + " t: " + t);

        ir.close();
        br.close();

    }

    private static void loadWordsFromScwsFile(IDictionary dic, File file, String charset) throws Exception {


        InputStreamReader ir = new InputStreamReader(new FileInputStream(file), charset);
        BufferedReader br = new BufferedReader(ir);

        int freCol = file.getName().charAt(file.getName().length() - 7) - 0x30;
        System.out.println("loading " + file + "   & fre col index: " + freCol);

        String line = null;
        boolean isFirstLine = true;
        int t = -1;
        int count = 0;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if ("".equals(line))
                continue;
            // swept the notes
            if (line.indexOf('#') == 0)
                continue;
            // the first line fo the lexicon file.
            if (isFirstLine == true) {

                String[] wd = line.split(" ");
                if (wd.length == 1) {
                    t = IDictionary.getIndex(line);
                } else {
                    t = IDictionary.getIndex(wd[0]);
                }

                isFirstLine = false;
                if (t >= 0) {
                    continue;
                }
            }

            // special lexicon
            if (line.indexOf('\t') == -1) {
                if (line.length() <= Config.MAX_LENGTH) {
                    dic.add(t, line, IWord.T_CJK_WORD);
                }
            }
            // normal words lexicon file
            else {
                final String[] wd = line.split("\t");
                final String word = wd[0];
                if (word.length() <= Config.MAX_LENGTH) {
                    if (wd.length >= 3) {
                        dic.add(t, word, Integer.parseInt(wd[freCol]), IWord.T_CJK_WORD);
                    } else {
                        dic.add(t, word, IWord.T_CJK_WORD);
                    }

                    //System.out.println(wd[0]);
                }
            }

            count++;
        }

        System.out.println("word count: " + count + " t: " + t);

        ir.close();
        br.close();

    }

	/*public static void main(String args[]) throws Exception {
		loadPropertes();
		trainDictionary("com.webssky.jcseg.Dictionary", Resource.getDictionaryFile0());
	}*/

}