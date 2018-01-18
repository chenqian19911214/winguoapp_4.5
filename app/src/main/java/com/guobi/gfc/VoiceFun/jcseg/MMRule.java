package com.guobi.gfc.VoiceFun.jcseg;

import java.util.ArrayList;

import com.guobi.gfc.VoiceFun.jcseg.core.IChunk;
import com.guobi.gfc.VoiceFun.jcseg.core.IRule;

/**
 * the first filter rule
 * - the maxmum match rule for JCSeg.
 * this rule will return the chunks that own
 * the largest word length.
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public class MMRule implements IRule {

    /**
     * maxmum match rule instance.
     */
    private static MMRule __instance = null;

    /**
     * return the quote to the maximum match instance.
     *
     * @return MMRule
     */
    public static MMRule createRule() {
        if (__instance == null)
            __instance = new MMRule();
        return __instance;
    }

    private MMRule() {
    }

    /**
     * interface for maximum match rule.
     *
     * @see IRule#call(IChunk[])
     */
    @Override
    public IChunk[] call(IChunk[] chunks) {

        int maxLength = chunks[0].getLength();
        int j;
        //find the maximum word length
        for (j = 1; j < chunks.length; j++) {
            if (chunks[j].getLength() > maxLength)
                maxLength = chunks[j].getLength();
        }

        //get the items that the word length equals to
        //the max's length.
        ArrayList<IChunk> chunkArr = new ArrayList<IChunk>(chunks.length);
        for (j = 0; j < chunks.length; j++) {
            if (chunks[j].getLength() == maxLength)
                chunkArr.add(chunks[j]);
        }

        IChunk[] lchunk = new IChunk[chunkArr.size()];
        chunkArr.toArray(lchunk);
        chunkArr.clear();

        return lchunk;
    }

}
