package com.winguo.productList.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/16.
 */

public class IconEntity implements Serializable{
    /**
     * modifyTime : 1442509170
     * content : http://g1.img.winguo.com/group1/M00/0F/26/wKgAjFX68XKAYwq0AAB7pybblwQ210.jpg
     */
    public String modifyTime;
    public String content;

    @Override
    public String toString() {
        return "IconEntity{" +
                "modifyTime=" + modifyTime +
                ", content='" + content + '\'' +
                '}';
    }
}
