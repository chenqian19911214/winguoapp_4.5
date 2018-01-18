package com.winguo.product.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public  class ItemImagesBean implements Serializable {
    /**
     * order : 0
     * url : {"content":"http://g1.img.winguo.com/group1/M00/4E/3F/wKgAi1hKYASAZX0kAAdDAg-KNo803..jpg","modifyTime":1481269252}
     */

    public List<DataBean> data;

    @Override
    public String toString() {
        return "ItemImagesBean{" +
                "data=" + data +
                '}';
    }


}