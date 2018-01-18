package com.guobi.gfc.VoiceFun.jcseg;

import java.io.Reader;

import com.guobi.gfc.VoiceFun.jcseg.core.IChunk;
import com.guobi.gfc.VoiceFun.jcseg.core.IDictionary;
import com.guobi.gfc.VoiceFun.jcseg.core.IWord;

/**
 * simplex segment for JCSeg,
 * has extend from ASegment. <br />
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com} <br />
 */
public class SimpleSeg extends ASegment {

    public SimpleSeg() throws Exception {
        super(false);
    }

    public SimpleSeg(boolean train) throws Exception {
        super(train);
    }

    public SimpleSeg(Reader input, boolean train) throws Exception {
        super(input, train);
    }

    public SimpleSeg(Reader input, IDictionary dic, boolean train) throws Exception {
        super(input, dic, train);
    }

    /**
     * @see ASegment#getBestCJKChunk(char[], int)
     */
    @Override
    public IChunk getBestCJKChunk(char[] chars, int index) {
        IWord[] words = getNextMatch(chars, index);
        return new Chunk(new IWord[]{words[words.length - 1]});
    }

}
