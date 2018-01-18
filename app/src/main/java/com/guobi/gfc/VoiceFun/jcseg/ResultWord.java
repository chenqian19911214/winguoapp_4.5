package com.guobi.gfc.VoiceFun.jcseg;

import com.guobi.gfc.VoiceFun.jcseg.core.IWord;


/**
 * word class for jcseg has implements IWord interface
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public class ResultWord implements IWord {

    public String value;
    public int position;
    public int fre = 0;
    public int type;
    public int length0;


    public ResultWord() {

    }

    public ResultWord(IWord word) {
        this.value = word.getValue();
        this.fre = word.getFrequency();
        this.type = word.getType();
        this.length0 = word.getLength0();
    }

    public ResultWord(String value, int type) {
        this.value = value;
        this.type = type;
    }

    public ResultWord(String value, int fre, int type) {
        this.value = value;
        this.fre = fre;
        this.type = type;
    }


    public String getValue() {
        return value;
    }


    public int getLength() {
        return value.length();
    }


    public int getFrequency() {
        return fre;
    }


    public int getType() {
        return type;
    }


    public void setPosition(int pos) {
        position = pos;
    }

    /**
     * @see IWord#getPosition()
     */
    public int getPosition() {
        return position;
    }

    public int getLength0() {
        return length0 <= 0 ? getLength() : length0;
    }

    @Override
    public String toString() {
        return getValue();
    }

}
