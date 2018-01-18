package com.guobi.gfc.VoiceFun.jcseg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;

import com.guobi.gfc.VoiceFun.jcseg.core.Config;
import com.guobi.gfc.VoiceFun.jcseg.core.IChunk;
import com.guobi.gfc.VoiceFun.jcseg.core.IDictionary;
import com.guobi.gfc.VoiceFun.jcseg.core.ILexicon;
import com.guobi.gfc.VoiceFun.jcseg.core.ISegment;
import com.guobi.gfc.VoiceFun.jcseg.core.IWord;
import com.guobi.gfc.VoiceFun.jcseg.filter.CNNMFilter;
import com.guobi.gfc.VoiceFun.jcseg.filter.ENSCFilter;
import com.guobi.gfc.VoiceFun.jcseg.filter.PPTFilter;
import com.guobi.gfc.VoiceFun.jcseg.util.Stringer;

/**
 * abstract segment class, implemented ISegment interface <br />
 * implements all the common method that <br />
 * simple segment and Complex segment algorithm both share. <br />
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public abstract class ASegment implements ISegment {

    /**
     * jar home directory.
     */
    public static String JAR_HOME = System.getProperty("user.dir");

    /**
     * current position for the given stream.
     */
    protected int idx;
    protected PushbackReader reader = null;
    /*CJK word cache poll*/
    final protected LinkedList<ResultWord> wordPool = new LinkedList<ResultWord>();
    final protected Stringer isb;

	/*protected char[] buff;
	protected int b_len;
	protected int b_off;*/
    //protected LinkedList<String> cnNumeric = new LinkedList<String>();

    /**
     * the dictionary instance
     */
    protected IDictionary dic;

    public ASegment() throws Exception {
        this(null, false);
    }

    public ASegment(boolean train) throws Exception {
        this(null, train);
    }

    public ASegment(Reader input, boolean train) throws Exception {
        this(input, null, train);
    }

    public ASegment(Reader input, IDictionary dic, boolean train) throws Exception {
        if (dic == null)
            this.dic = DictionaryFactory.createDictionary("com.webssky.jcseg.Dictionary", train);
            //this.dic = DictionaryFactory.createTestDictionary();
        else
            this.dic = dic;
        //buff = new char[1024];
        isb = new Stringer(64);
        reset(input);
    }

    /**
     * stream/reader reset.
     *
     * @param input
     * @throws IOException
     */
    public void reset(Reader input) throws IOException {
        if (input != null)
            reader = new PushbackReader(new BufferedReader(input), 60);
        idx = -1;
        wordPool.clear();
        isb.clear();
        //b_off = 0;
        //b_len = 0;
    }

    /**
     * read the next char from the current position
     *
     * @throws IOException
     */
    protected int readNext() throws IOException {
		/*if ( b_off >= b_len ) {
			b_off = 0;
			b_len = reader.read(buff);
			if ( b_len == -1 ) return -1; 
		}
		idx++;
		return buff[b_off++];*/
        int c = reader.read();
        if (c != -1) idx++;
        return c;

    }

    /**
     * push back the data to the stream.
     *
     * @param data
     * @throws IOException
     */
    protected void pushBack(int data) throws IOException {
        //charPoll.add(data);
        reader.unread(data);
        idx--;
    }

    @Override
    public int getStreamPosition() {
        return idx + 1;
    }

    /**
     * @see ISegment#next()
     */
    @Override
    public ResultWord next() throws IOException {
        if (wordPool.size() > 0) return wordPool.removeFirst();
        int c, pos;
        while ((c = readNext()) != -1) {
            if (ENSCFilter.isWhitespace(c)) continue;
            pos = idx;
            //System.out.println((char)c);
            if (isCJKChar(c)) {
                final char[] chars = nextCJKSentence(c);
                final int len = chars.length;
                int cjkidx = 0;
                while (cjkidx < len) {
					/*
					 * find the next CJK word.
					 * the process will be different with the different algorithm
					 * @see getBestCJKChunk() from SimpleSeg or ComplexSeg. 
					 */
                    ResultWord w = null;
                    //check if there is chinese numeric
                    if (CNNMFilter.isCNNumeric(chars[cjkidx]) > -1
                            && cjkidx + 1 < len) {
                        //get the chinese numeric chars
                        String num = nextCNNumeric(chars, cjkidx);
                        if (cjkidx + 3 < len && chars[cjkidx + 1] == '分'
                                && chars[cjkidx + 2] == '之'
                                && CNNMFilter.isCNNumeric(chars[cjkidx + 3]) > -1) {
                            w = new ResultWord(num, IWord.T_CN_NUMERIC);
                            w.setPosition(pos + cjkidx);
                            wordPool.add(w);

                            String[] split = num.split("分之");
                            ResultWord wd = new ResultWord(CNNMFilter.cnNumericToArabic(split[1], true)
                                    + "/" + CNNMFilter.cnNumericToArabic(split[0], true),
                                    IWord.T_CN_NUMERIC);
                            wd.setPosition(w.getPosition());
                            wordPool.add(wd);
                        } else if (CNNMFilter.isCNNumeric(chars[cjkidx + 1]) > -1
                                || dic.match(ILexicon.CJK_UNITS, chars[cjkidx + 1])) {

                            final Stringer sb = new Stringer();
                            String temp = null;
                            sb.append(num);
                            boolean matched = false;
                            int j;

                            //find the word that made up with the numeric
                            //like: 五四运动
                            for (j = num.length();
                                 (cjkidx + j) < len && j < Config.MAX_LENGTH; j++) {
                                sb.append(chars[cjkidx + j]);
                                temp = sb.toString();
                                if (dic.match(ILexicon.CJK_WORDS, sb.getValue(), 0, sb.length())) {
                                    num = temp;
                                    matched = true;
                                }
                            }

                            ResultWord wd = null;
                            //find the numeric units
                            if (matched == false) {
                                //get the numeric'a arabic
                                String arbic = Integer.toString(CNNMFilter.cnNumericToArabic(num, true));

                                if ((cjkidx + num.length()) < len
                                        && dic.match(ILexicon.CJK_UNITS,
                                        chars[cjkidx + num.length()])) {
                                    char units = chars[cjkidx + num.length()];
                                    num += units;
                                    arbic += units;
                                }

                                wd = new ResultWord(arbic, IWord.T_CN_NUMERIC);
                                wd.setPosition(pos + cjkidx);
                            }

                            w = new ResultWord(num, IWord.T_CN_NUMERIC);
                            w.setPosition(pos + cjkidx);
                            wordPool.add(w);
                            if (wd != null) wordPool.add(wd);
                        }

                        if (w != null) {
                            cjkidx += w.getLength();
                            continue;
                        }
                    }

                    IChunk chunk = getBestCJKChunk(chars, cjkidx);
                    //System.out.println(chunk+"\n");
                    //w = new Word(chunk.getWords()[0].getValue(), IWord.T_CJK_WORD);
                    final IWord iw = chunk.getWords()[0];
                    w = new ResultWord(iw);
                    final String v = w.getValue();
					
					/*find the chinese name.*/
                    int T = -1;
                    if (Config.I_CN_NAME
                            && w.getLength() <= 2 && chunk.getWords().length > 1) {
                        Stringer sb = new Stringer();
                        sb.append(w.getValue());
                        String str = null;

                        //the w is a Chinese last name.
                        if (dic.match(ILexicon.CN_LNAME, v)
                                && (str = findCHName(chars, 0, chunk)) != null) {
                            T = IWord.T_CN_NAME;
                            sb.append(str);
                        }
                        //the w is Chinese last name adorn
                        else if (dic.match(ILexicon.CN_LNAME_ADORN, v)
                                && chunk.getWords()[1].getLength() <= 2
                                && dic.match(ILexicon.CN_LNAME, chunk.getWords()[1].getValue())) {
                            T = IWord.T_CN_NICKNAME;
                            sb.append(chunk.getWords()[1].getValue());
                        }
						/*
						 * the length of the w is 2:
						 * the last name and the first char make up a word
						 * for the double name. 
						 */
						/*else if ( w.getLength() > 1
								&& findCHName( w, chunk ))  {
							T = IWord.T_CN_NAME;
							sb.append(chunk.getWords()[1].getValue().charAt(0));
						}*/

                        if (T != -1)
                            w = new ResultWord(sb.toString(), T);
                    }

                    //check the stopwords(clear it when Config.CLEAR_STOPWORD is true)
                    if (T == -1 && Config.CLEAR_STOPWORD
                            && dic.match(ILexicon.STOP_WORD, w.getValue())) {
                        cjkidx += w.getLength();
                        continue;
                    }

                    w.setPosition(pos + cjkidx);
                    wordPool.add(w);
                    cjkidx += w.getLength0();


                }

                if (wordPool.size() == 0) continue;
                return wordPool.removeFirst();
            } else if (!ENSCFilter.isPunctuation(c) && isLetterOrDigit(c)) {
                ResultWord w = nextLetterOrDigit(c);
                w.setPosition(pos);
                return w;
            } else if (isLetterNumber(c)) {
                ResultWord w = new ResultWord(nextLetterNumber(c), IWord.T_OTHER_NUMBER);
                w.setPosition(pos);
                return w;
            } else if (isOtherNumber(c)) {
                ResultWord w = new ResultWord(nextOtherNumber(c), IWord.T_OTHER_NUMBER);
                return w;
            } else if (PPTFilter.isPairPunctuation((char) c)) {
                String text = getPairPunctuationText(c);
                if (text == null) continue;
                ResultWord w = new ResultWord(text, ILexicon.CJK_WORDS);
                return w;
            }
        }

        return null;
    }

    /**
     * check the specified char is CJK,Thai... char
     * true will be return if it is,
     * or return false.
     *
     * @param c
     * @return boolean
     */
    static boolean isCJKChar(int c) {
        final int type = Character.getType(c);
        if (Character.isLetter(c) || type == Character.OTHER_LETTER)
            return true;
        return false;
    }

    /**
     * check the specified char is a basic latin and russia and greece letter
     * true will be return if it is,
     * or return false.<br />
     * this method can recognize full-width char and letter.<br />
     *
     * @param c
     * @return boolean
     */
    static boolean isLetterOrDigit(int c) {
		/*int type = Character.getType(c);
		Character.UnicodeBlock cu = Character.UnicodeBlock.of(c);
		if ( ! Character.isWhitespace(c) && 
				(cu == Character.UnicodeBlock.BASIC_LATIN
				|| type == Character.DECIMAL_DIGIT_NUMBER
				|| type == Character.LOWERCASE_LETTER
				|| type == Character.UPPERCASE_LETTER
				|| type == Character.TITLECASE_LETTER
				|| type == Character.MODIFIER_LETTER)) 
			return true;
		return false;*/
        return (ENSCFilter.isHalfWidthChar(c) || ENSCFilter.isFullWidthChar(c));
    }

    /**
     * check the specified char is a digit or not.
     * true will return if it is ,
     * or return false
     * this method can recognize full-with char.
     *
     * @param c
     * @return boolean
     */
    static boolean isDigit(String str) {
        for (int j = 0, n = str.length(); j < n; j++) {
            char c = str.charAt(j);
            if (Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER)
                return false;
        }
        return true;
    }

    /**
     * check the specified char is Letter number like 'ⅠⅡ'
     * true will be return if it is,
     * or return false. <br />
     *
     * @param c
     * @return boolean
     */
    static boolean isLetterNumber(int c) {
        if (Character.getType(c) == Character.LETTER_NUMBER)
            return true;
        return false;
    }

    /**
     * check the specified char is other number like '①⑩⑽㈩'
     * true will be return if it is,
     * or return false. <br />
     *
     * @param c
     * @return boolean
     */
    static boolean isOtherNumber(int c) {
        if (Character.getType(c) == Character.OTHER_NUMBER)
            return true;
        return false;
    }

    /**
     * match the next CJK word in the dictionary. <br />
     *
     * @param chars
     * @param index
     * @return IWord[]
     */
    protected IWord[] getNextMatch(char[] chars, int index) {

        final ArrayList<IWord> mList = new ArrayList<IWord>(8);
        isb.clear();

        final char c = chars[index];
        isb.append(c);
        final String temp = isb.toString();
        IWord tword = dic.get(ILexicon.CJK_WORDS, c);
        if (tword != null) {
            mList.add(tword);
        }
        //String _key = null;
        for (int j = 1;
             j < Config.MAX_LENGTH && ((j + index) < chars.length); j++) {
            isb.append(chars[j + index]);
            //_key = isb.toString();
            tword = dic.get(ILexicon.CJK_WORDS, isb.getValue(), 0, isb.length());
            if (tword != null) {
                mList.add(tword);
            }
        }
		
		/*
		 * if match no words from the current position 
		 * to idx+Config.MAX_LENGTH, just return the Word with
		 * a value of temp as a unrecognited word. 
		 */
        if (mList.isEmpty()) {
            mList.add(new ResultWord(temp, ILexicon.UNMATCH_CJK_WORD));
        }
		
/*		for ( int j = 0; j < mList.size(); j++ ) {
			System.out.println(mList.get(j));
		}*/

        IWord[] words = new IWord[mList.size()];
        mList.toArray(words);
        //mList.clear();

        return words;
    }

    /**
     * find the chinese name from the position of the given word.
     *
     * @param chars
     * @param index
     * @param chunk
     * @return IWord
     */
    public String findCHName(char[] chars, int index, final IChunk chunk) {
        final Stringer isb = new Stringer();
        //isb.clear();
		/*there is only two IWords in the chunk. */
        if (chunk.getWords().length == 2) {
            final IWord w = chunk.getWords()[1];
            final String v = w.getValue();
            switch (w.getLength()) {
                case 1:
                    if (dic.match(ILexicon.CN_SNAME, v)) {
                        isb.append(v);
                        return isb.toString();
                    }
                    return null;
                case 2:
                case 3:
				/*
				 * there is only two IWords in the chunk.
				 * case 2:
				 * like: 这本书是陈高的, chunk: 陈_高的
				 * more: 瓜子和坚果,chunk: 和_坚果 (1.6.8前版本有歧义)
				 * case 3:
				 * 1.double name: the two chars and char after it make up a word.
				 * like: 这本书是陈美丽的, chunk: 陈_美丽的
				 * 2.single name: the char and the two chars after it make up a word. -ignore
				 */
                    final char d1 = v.charAt(0);
                    final char d2 = v.charAt(1);
                    if (dic.match(ILexicon.CN_DNAME_1, d1)
                            && dic.match(ILexicon.CN_DNAME_2, d2)) {
                        isb.append(d1);
                        isb.append(d2);
                        return isb.toString();
                    }
				/*
				 * the name char of the single name and the char after it
				 * 		make up a word. 
				 */
                    else if (dic.match(ILexicon.CN_SNAME, d1) &&
                            dic.get(ILexicon.CJK_WORDS, d2).getFrequency() >= Config.NAME_SINGLE_THRESHOLD) {
                        isb.append(d1);
                        return isb.toString();
                    }
                    return null;
            }
        }
		/*three IWords in the chunk */
        else {
            final IWord w1 = chunk.getWords()[1];
            final IWord w2 = chunk.getWords()[2];
            final String v1 = w1.getValue();
            final String v2 = w2.getValue();
            switch (w1.getLength()) {
                case 1:
				/*check if it is a double name first.*/
                    if (dic.match(ILexicon.CN_DNAME_1, v1)) {
                        if (w2.getLength() == 1) {
						/*real double name?*/
                            if (dic.match(ILexicon.CN_DNAME_2, v2)) {
                                isb.append(v1);
                                isb.append(v2);
                                return isb.toString();
                            }
						/*not a real double name, check if it is a single name.*/
                            else if (dic.match(ILexicon.CN_SNAME, v1)) {
                                isb.append(v1);
                                return isb.toString();
                            }
                        }
					/*
					 * double name:
					 * char 2 and the char after it make up a word.
					 * like: 陈志高兴奋极了, chunk:陈_志_高兴 (兴和后面成词)
					 * like: 陈志高的, chunk:陈_志_高的 ("的"的阕值Config.SINGLE_THRESHOLD)
					 * like: 陈高兴奋极了, chunk:陈_高_兴奋 (single name)
					 */
                        else {
                            final char d1 = v2.charAt(0);
                            final int index_ = index + chunk.getWords()[0].getLength() + 2;
                            final IWord[] ws = getNextMatch(chars, index_);
                            //System.out.println("index:"+index+":"+chars[index]+", "+ws[0]);
						/*is it a double name?*/
                            if (dic.match(ILexicon.CN_DNAME_2, d1) &&
                                    (ws.length > 1 || ws[0].getFrequency() >= Config.NAME_SINGLE_THRESHOLD)) {
                                isb.append(v1);
                                isb.append(d1);
                                return isb.toString();
                            }
						/*
						 * check if it is a single name
						 */
                            else if (dic.match(ILexicon.CN_SNAME, v1)) {
                                isb.append(v1);
                                return isb.toString();
                            }
                        }
                    }
				/*check if it is a single name.*/
                    else if (dic.match(ILexicon.CN_SNAME, v1)) {
                        isb.append(v1);
                        return isb.toString();
                    }
                    return null;
                case 2:
                    final char d1 = v1.charAt(0);
                    final char d2 = v1.charAt(1);
				/*
				 * it is a double name and char 1, char 2 make up a word.
				 * like: 陈美丽是对的, chunk: 陈_美丽_是
				 * more: 都成为高速公路, chunk:都_成为_高速公路 (1.6.8以前的有歧义)
				 */
                    if (dic.match(ILexicon.CN_DNAME_1, d1)
                            && dic.match(ILexicon.CN_DNAME_2, d2)) {
                        isb.append(v1);
                        return isb.toString();
                    }
				/*
				 * it is a single name, char 1 and the char after it make up a word.
				 */
                    else if (dic.match(ILexicon.CN_SNAME, d1) &&
                            dic.get(ILexicon.CJK_WORDS, d2).getFrequency() >= Config.NAME_SINGLE_THRESHOLD) {
                        isb.append(d1);
                        return isb.toString();
                    }
                    return null;
                case 3:
				/*
				 * singe name:  - ignore
				 *  mean the char and the two chars after it make up a word.
				 *  
				 * it is a double name.
				 * like: 陈美丽的人生， chunk: 陈_美丽的_人生
				 */
                    final char c1 = v1.charAt(0);
                    final char c2 = v1.charAt(1);
                    final IWord w3 = dic.get(ILexicon.CJK_WORDS, v1.charAt(2));
                    if (dic.match(ILexicon.CN_DNAME_1, c1)
                            && dic.match(ILexicon.CN_DNAME_2, c2)
                            && (w3 == null || w3.getFrequency() >= Config.NAME_SINGLE_THRESHOLD)) {
                        isb.append(c1);
                        isb.append(c2);
                        return isb.toString();
                    }
                    return null;
            }
        }
        return null;
    }

    /**
     * find the Chinese double name:
     * when the last name and the first char of the name make up a word.
     *
     * @param chunk the best chunk.
     * @return boolean
     */
    public boolean findCHName(IWord w, IChunk chunk) {
        final String value = w.getValue();
        final char s1 = value.charAt(0);
        final char s2 = value.charAt(1);

        if (dic.match(ILexicon.CN_LNAME, s1)
                && dic.match(ILexicon.CN_DNAME_1, s2)) {
            final IWord w2 = chunk.getWords()[1];
            final String v2 = w2.getValue();
            switch (w2.getLength()) {
                case 1:
                    if (dic.match(ILexicon.CN_DNAME_2, v2))
                        return true;
                case 2:
                    final char d1 = v2.charAt(0);
                    final IWord _w = dic.get(ILexicon.CJK_WORDS, v2.charAt(1));
                    //System.out.println(_w);
                    if (dic.match(ILexicon.CN_DNAME_2, d1)
                            && (_w == null || _w.getFrequency() >= Config.NAME_SINGLE_THRESHOLD))
                        return true;
            }
        }

        return false;
    }

    /**
     * load a CJK char list from the stream start from the current position.
     * till the char is not a CJK char.<br />
     *
     * @param c <br />
     * @return char[] <br />
     * @throws IOException
     */
    protected char[] nextCJKSentence(int c) throws IOException {
        isb.clear();
        int ch;
        isb.append((char) c);
        while ((ch = readNext()) != -1) {
            if (ENSCFilter.isWhitespace(ch)) break;
            if (!isCJKChar(ch)) {
                pushBack(ch);
                break;
            }
            isb.append((char) ch);
        }
        return isb.getValueCopy();
    }

    /**
     * find the letter or digit word from the current position.<br />
     * count until the char is whitespace or not letter_digit.
     *
     * @param c
     * @return IWord
     * @throws IOException
     */
    protected ResultWord nextLetterOrDigit(int c) throws IOException {
        isb.clear();
        if (ENSCFilter.isFullWidthChar(c)) c = c - 65248;
        if (ENSCFilter.isUpperCaseLetter(c)) c = c + 32;
        isb.append((char) c);
        int ch;
        boolean check = false;
        while ((ch = readNext()) != -1) {
            if (ENSCFilter.isWhitespace(ch)) {
                check = true;
                break;
            }
            if (ENSCFilter.isPunctuation(ch)
                    && !ENSCFilter.isENKeeepChar((char) ch)) break;
            if (!isLetterOrDigit(ch)) {
                pushBack(ch);
                check = true;
                break;
            }

            //turn the full-width char to half-width char.
            if (ENSCFilter.isFullWidthChar(ch))
                ch = ch - 65248;
            //turn the lower case letter to upper case.
            if (ENSCFilter.isUpperCaseLetter(ch))
                ch = ch + 32;

            isb.append((char) ch);
        }

        String __str = isb.toString();
        if (!dic.match(ILexicon.EN_PUN_WORDS, isb.getValue(), 0, isb.length())) {
            //delete the useless english punctuations.
            int t = isb.length() - 1;
            for (; t > 0 && isb.charAt(t) != '%'
                    && ENSCFilter.isPunctuation(isb.charAt(t)); t--) {
                isb.deleteCharAt(t);
            }
            if (t < isb.length() - 1) __str = isb.toString();
        }

        //System.out.println(sb.toString()+", "+ch);
        if ((check && !isCJKChar(ch)) || ch == -1)
            return new ResultWord(__str, IWord.T_BASIC_LATIN);

        ResultWord w = new ResultWord(__str, IWord.T_BASIC_LATIN);
		/*
		 * get chinese and english mix word
		 * like 'BB机,B超...'
		 */
        Stringer mixWord = new Stringer();
        mixWord.append(__str);
        String _temp = null;
        String _word = __str;
        int mc = 0, j = 0;                //the number of char that readed from the stream.
        ArrayList<Integer> chArr = new ArrayList<Integer>(Config.MIX_CN_LENGTH);
		/*
		 * Attension:
		 * make sure that (ch = readNext()) is after j < Config.MIX_CN_LENGTH.
		 * or it cause the miss of the next char. 
		 */
        for (; j < Config.MIX_CN_LENGTH && (ch = readNext()) != -1; j++) {
            if (ENSCFilter.isWhitespace(ch)) break;
            mixWord.append((char) ch);
            //System.out.print((char)ch+",");
            chArr.add(ch);
            _temp = mixWord.toString();
            //System.out.println((j+1)+": "+_temp);
            if (dic.match(ILexicon.MIXED_WORD, mixWord.getValue(), 0, mixWord.length())) {
                _word = _temp;
                mc = j + 1;
            }
        }

        if (mc > 0) {
            for (int i = j - 1; i >= mc; i--)
                pushBack(chArr.get(i).intValue());
            chArr.clear();
            w = new ResultWord(_word, IWord.T_MIXED_WORD);
        } else {
            for (int i = j - 1; i >= 0; i--)
                pushBack(chArr.get(i).intValue());
            chArr.clear();
            //check if there is a units for the digit.
            if (isDigit(_word)) {
                ch = readNext();
                if (dic.match(ILexicon.CJK_UNITS, (char) ch)) {
                    w = new ResultWord(new String(_word + ((char) ch)), IWord.T_MIXED_WORD);
                } else pushBack(ch);
            }
        }
        return w;
    }

    /**
     * find the next other letter from the current position.
     * find the letter number from the current position.
     * count until the char in the specified position is not
     * a letter number or whitespace. <br />
     *
     * @param c
     * @return String
     * @throws IOException
     */
    protected String nextLetterNumber(int c) throws IOException {
        final Stringer isb = new Stringer();
        isb.append((char) c);
        int ch;
        while ((ch = readNext()) != -1) {
            if (ENSCFilter.isWhitespace(ch)) break;
            if (!isLetterNumber(ch)) {
                pushBack(ch);
                break;
            }
            isb.append((char) ch);
        }
        return isb.toString();
    }

    /**
     * find the other number from the current position. <br />
     * count until the char in the specified position is not
     * a orther number or whitespace. <br />
     *
     * @param c
     * @return String
     * @throws IOException
     */
    protected String nextOtherNumber(int c) throws IOException {
        isb.clear();
        isb.append((char) c);
        int ch;
        while ((ch = readNext()) != -1) {
            if (ENSCFilter.isWhitespace(ch)) break;
            if (!isOtherNumber(ch)) {
                pushBack(ch);
                break;
            }
            isb.append((char) ch);
        }
        return isb.toString();
    }

    /**
     * find the chinese number from the current position. <br />
     * count until the char in the specified position is not
     * a orther number or whitespace. <br />
     *
     * @param chars char array of CJK items. <br />
     * @param index
     * @return String[]
     */
    protected String nextCNNumeric(char[] chars, int index) throws IOException {
        isb.clear();
        isb.append(chars[index]);
        for (int j = index + 1; j < chars.length; j++) {
            //System.out.println("cn:"+chars[j]);
            if (CNNMFilter.isCNNumeric(chars[j]) == -1) {
                //deal with “分之”
                if (j + 2 < chars.length
                        && chars[j] == '分' && chars[j + 1] == '之') {
                    isb.append(chars[j++]);
                    isb.append(chars[j]);
                    continue;
                } else
                    break;
            }
            isb.append(chars[j]);
        }
        return isb.toString();
    }

    /**
     * find pair punctuation of the given punctuation char.
     * the purpose is to get the text bettween them. <br />
     *
     * @param c
     * @throws IOException
     */
    protected String getPairPunctuationText(int c) throws IOException {
        isb.clear();
        char echar = PPTFilter.getPunctuationPair((char) c);
        boolean matched = false;
        int j, ch;
        ArrayList<Integer> chArr = new ArrayList<Integer>(Config.PPT_MAX_LENGTH);
        for (j = 0; j < Config.PPT_MAX_LENGTH; j++) {
            ch = readNext();
            if (ch == -1) break;
            if (ch == echar) {
                matched = true;
                break;
            }
            isb.append((char) ch);
            chArr.add(ch);
        }

        if (matched == false) {
            for (int i = j - 1; i >= 0; i--)
                pushBack(chArr.get(i).intValue());
            return null;
        }

        return isb.toString();
    }

    public void destroy() {
        dic.destroy();
        dic = null;
    }

    /**
     * an abstract method to gain a CJK word from the
     * current position.
     * simpleSeg and ComplexSeg is different to deal this,
     * so make it a abstract method here.
     *
     * @param chars
     * @param index
     * @return IChunk
     * @throws IOException
     */
    protected abstract IChunk getBestCJKChunk(char chars[], int index) throws IOException;

}
