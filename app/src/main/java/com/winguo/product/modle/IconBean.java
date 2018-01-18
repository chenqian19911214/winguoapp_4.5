package com.winguo.product.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */
public  class IconBean implements Serializable {
    public String content;
    public String modifyTime;

    @Override
    public String toString() {
        return "IconBean{" +
                "content='" + content + '\'' +
                ", modifyTime=" + modifyTime +
                '}';
    }
}

