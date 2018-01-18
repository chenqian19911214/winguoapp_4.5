package com.guobi.gfc.VoiceFun.jcseg.core;

/**
 * JCSeg rule.
 * after the filter of the four rule,
 * if there is still more than one rule, JSCSegRule will
 * return the specified chunk.
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public interface ILastRule {
    /**
     * filter the chunks
     *
     * @return IChunk.
     */
    public IChunk call(IChunk[] chunks);
}
