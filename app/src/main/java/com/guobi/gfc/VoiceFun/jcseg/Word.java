package com.guobi.gfc.VoiceFun.jcseg;

import com.guobi.gfc.VoiceFun.jcseg.core.IWord;

public class Word implements IWord {

    private static final long START_MASK = 0xffffff0000000000L;
    private static final long LENGTH_MASK = 0x000000ff00000000L;
    private static final long FRE_MASK = 0x00000000fffffff0L;
    private static final long TYPE_MASK = 0x000000000000000fL;

    //public String value;

    //public int start;
    //public byte length;
    //public int fre = 0;
    //public byte type;

    //public int position;
    //public String pinyin = null;
    //public String[] partspeech = null;
    //public String[] syn = null;

    public long v;

    private int length0;

    public Word(long v, int length0) {
        this.v = v;
        this.length0 = length0;
    }

    public int getStart() {
        return (int) ((v & START_MASK) >>> 40);
    }

    public void setStart(int start) {
        v = (v & (~START_MASK)) | (((long) start) << 40);
    }

    public int getLength() {
        return (int) ((v & LENGTH_MASK) >>> 32);
    }

    public void setLength(int start) {
        v = (v & (~LENGTH_MASK)) | (((long) start) << 32);
    }

    public int getFrequency() {
        return (int) ((v & FRE_MASK) >>> 4);
    }

    public void setFrequency(int start) {
        v = (v & (~FRE_MASK)) | (((long) Math.abs(start)) << 4);
    }

    public int getType() {
        return (int) ((v & TYPE_MASK) /*>>> 0*/);
    }

    public void setType(int start) {
        v = (v & (~TYPE_MASK)) | (((long) start) /*<< 0*/);
    }

    public String getValue() {

        //System.out.println("v: " + v);
        return new String(chars, getStart(), getLength());
    }

    public int getLength0() {
        return length0 == -1 ? getLength() : length0;
    }


    @Override
    public String toString() {
        return getValue();
    }

    public static void setPublicCharBuffer(char[] chars) {
        Word.chars = chars;
    }


    private static char[] chars;


    public static void main(String[] arg) {
        Word w = new Word(0, -1);

        w.setStart(670659);
        w.setLength(250);
        w.setFrequency(-500);
        w.setType(12);

        System.out.println(w.getStart());
        System.out.println(w.getLength());
        System.out.println(w.getFrequency());
        System.out.println(w.getType());
    }


}
