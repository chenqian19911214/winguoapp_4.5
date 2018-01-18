package com.winguo.cart.model.store.seekcart;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/3.
 */

public  class IconBean implements Serializable{
    public String modifyTime;
    public String content;

    @Override
    public String toString() {
        return "IconBean{" +
                "modifyTime=" + modifyTime +
                ", content='" + content + '\'' +
                '}';
    }
}
