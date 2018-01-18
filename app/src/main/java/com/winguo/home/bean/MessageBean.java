package com.winguo.home.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/2/23.
 */
public  class MessageBean implements Serializable {
    public ItemsBean items;


    @Override
    public String toString() {
        return "MessageBean{" +
                "items=" + items +
                '}';
    }
}