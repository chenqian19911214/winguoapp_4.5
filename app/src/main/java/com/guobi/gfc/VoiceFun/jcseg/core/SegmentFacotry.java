package com.guobi.gfc.VoiceFun.jcseg.core;

import java.lang.reflect.Constructor;

/**
 * Segment factory to create singleton ISegment
 * object.  a path of the class that has implemented the ISegment
 * interface must be given first.
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public class SegmentFacotry {
    /**
     * the ISegment instance quote
     */
    private static ISegment __instance = null;

    /**
     * return the ISegment instance
     *
     * @param __segClass
     * @return ISegment
     */
    public static ISegment createSegment(String __segClass, boolean train) throws Exception {
        if (__instance == null)
            __instance = loadSegment(__segClass, train);
        return __instance;
    }

    private SegmentFacotry() {
    }

    /**
     * set the ISegment instance.
     *
     * @param seg
     */
    public static void setSegment(ISegment seg) {
        __instance = seg;
    }

    /**
     * load the ISegment class with the given path
     *
     * @param __segClass
     * @return ISegment
     */
    private static ISegment loadSegment(String __segClass, boolean train) throws Exception {
        ISegment seg = null;
        Class<?> _class = Class.forName(__segClass);
        Constructor<?> cons = _class.getConstructor(boolean.class);
        seg = (ISegment) cons.newInstance(train);

        return seg;
    }


}
