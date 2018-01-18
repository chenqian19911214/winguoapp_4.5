package com.guobi.gfc.VoiceFun.jcseg;

import com.guobi.gfc.VoiceFun.jcseg.core.IChunk;
import com.guobi.gfc.VoiceFun.jcseg.core.ILastRule;
import com.guobi.gfc.VoiceFun.jcseg.core.IRule;

/**
 * the last rule.
 * -clear the ambiguity after the four rule.
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public class LASTRule implements ILastRule {

    /**
     * maxmum match rule instance.
     */
    private static LASTRule __instance = null;

    /**
     * return the quote to the maximum match instance.
     *
     * @return MMRule
     */
    public static LASTRule createRule() {
        if (__instance == null)
            __instance = new LASTRule();
        return __instance;
    }

    private LASTRule() {
    }

    /**
     * last rule interface.
     * here we simply return the first chunk.
     *
     * @see IRule#call(IChunk[])
     */
    @Override
    public IChunk call(IChunk[] chunks) {
        return chunks[0];
    }

}
