package com.winguo.bean;

/**
 * 关键词搜索实体类
 *
 */

public class SearchKeyWord {

    public static final int TYPE_CONTACT = 1;
    public static final int TYPE_APP = 2;
    public static final int TYPE_MESSAGE = 3;
    public static final int TYPE_ROUND = 4;

    public int typeIcon;
    public String typeText;

    public SearchKeyWord(int typeIcon, String typeText) {
        this.typeIcon = typeIcon;
        this.typeText = typeText;
    }
}
