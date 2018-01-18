package com.winguo.productList.modle.classify;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/15.
 */

public class ValueEntity implements Serializable{
    public int count;
    public String id;
    public String label;
    public boolean isChoosed;

    @Override
    public String toString() {
        return "ValueEntity{" +
                "count=" + count +
                ", id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", isChoosed='" + isChoosed + '\'' +
                '}';
    }
}
