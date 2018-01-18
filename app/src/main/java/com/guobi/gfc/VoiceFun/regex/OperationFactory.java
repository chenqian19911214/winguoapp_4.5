package com.guobi.gfc.VoiceFun.regex;

import java.util.ArrayList;

public class OperationFactory {
    /**
     * 目前只有一种类型，OR
     *
     * @param string
     * @return
     */
    public Operation createType(String string) {
        if (string == null)
            return null;

        final int count = string.length();
        if (0 == count)
            return null;


        ArrayList<Integer> list = new ArrayList<Integer>();
        boolean isOrType = false;
        Operation type = null;
        int lastPos = 0;

        for (int i = 0; i < count; i++) {
            char ch = string.charAt(i);

            if (ch == '|') {
                list.add(i);
                OROperation ort;

                if (!isOrType) {
                    ort = new OROperation();
                    type = ort;
                    isOrType = true;
                } else {
                    ort = (OROperation) type;
                }

                if (i > lastPos) {
                    ort.addOrCondition(string.substring(lastPos, i));
                    lastPos = i + 1;
                }
            }
        }

        if (isOrType) {
            OROperation ort = (OROperation) type;
            ort.addOrCondition(string.substring(lastPos));
        }

        return type;
//		if (isOrType) {
//			ORType type = new ORType();
//		}
    }

//	private static final int OR_TYPE = 0;
}
