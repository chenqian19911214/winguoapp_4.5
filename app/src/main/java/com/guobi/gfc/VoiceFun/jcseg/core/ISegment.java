package com.guobi.gfc.VoiceFun.jcseg.core;

import java.io.IOException;
import java.io.Reader;

import com.guobi.gfc.VoiceFun.jcseg.ResultWord;

/**
 * JCSeg segment interface
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public interface ISegment {

    /**
     * reset the reader
     *
     * @param input
     */
    public void reset(Reader input) throws IOException;

    /**
     * get the current length of the stream
     *
     * @return int
     */
    public int getStreamPosition();

    /**
     * segment a word from a char array
     * from a specified position.
     *
     * @return IWord
     */
    public ResultWord next() throws IOException;
}
