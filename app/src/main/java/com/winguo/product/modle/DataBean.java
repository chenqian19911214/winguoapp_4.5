package com.winguo.product.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */

public  class DataBean implements Serializable {
    public int order;
    /**
     * content : http://g1.img.winguo.com/group1/M00/4E/3F/wKgAi1hKYASAZX0kAAdDAg-KNo803..jpg
     * modifyTime : 1481269252
     */

    public UrlBean url;

    @Override
    public String toString() {
        return "DataBean{" +
                "order=" + order +
                ", url=" + url +
                '}';
    }


}
