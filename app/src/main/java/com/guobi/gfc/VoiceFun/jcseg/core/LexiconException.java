package com.guobi.gfc.VoiceFun.jcseg.core;

/**
 * JCSeg Dictionary configuration exception class
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public class LexiconException extends Exception {

    private static final long serialVersionUID = 3794928123652720865L;

    public LexiconException(String info) {
        super(info);
    }

    public LexiconException(Throwable res) {
        super(res);
    }

    public LexiconException(String info, Throwable res) {
        super(info, res);
    }

}
