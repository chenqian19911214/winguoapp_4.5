package com.guobi.gfc.VoiceFun.regex;

import java.util.ArrayList;

public class OROperation extends Operation {

    private ArrayList<String> mConList;

    public OROperation() {
//		super('|');
        mConList = new ArrayList<String>();
    }

    @Override
    public int operate(String string) {
        if (string == null || string.isEmpty())
            return -1;

        int len = -1;
        for (String str : mConList) {
            if (string.startsWith(str)) {
                final int count = str.length();

                if (count > len)
                    len = count;
//				return count;
            }
        }

        return len;
    }

    public void addOrCondition(String condition) {
        mConList.add(condition);
    }

}
