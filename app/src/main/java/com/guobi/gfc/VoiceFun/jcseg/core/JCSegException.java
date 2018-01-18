package com.guobi.gfc.VoiceFun.jcseg.core;

/**
 * JCSeg exception class
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public class JCSegException extends Exception {

    private static final long serialVersionUID = 4495714680349884838L;

    public JCSegException(String info) {
        super(info);
    }

    public JCSegException(Throwable res) {
        super(res);
    }

    public JCSegException(String info, Throwable res) {
        super(info, res);
    }

}
