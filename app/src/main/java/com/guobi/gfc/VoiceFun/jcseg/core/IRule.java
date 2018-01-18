package com.guobi.gfc.VoiceFun.jcseg.core;

/**
 * filter rule interface.
 * the most important concept for mmseg chinese
 * segment algorithm.
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public interface IRule {
    /**
     * do the filter work
     *
     * @param chunks
     * @return IChunk[]
     */
    public IChunk[] call(IChunk[] chunks);
}
