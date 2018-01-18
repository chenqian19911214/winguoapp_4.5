package com.winguo.cart.model.physicalstore;

import java.io.Serializable;

/**
 * Created by Admin on 2017/6/27.
 */

public class PhysicalUpdataStoreBean implements Serializable {
    /**
     * code : 202
     * content : 加入成功
     */
    public String code;
    public String content;

    @Override
    public String toString() {
        return "PhysicalUpdataStoreBean{" +
                "code='" + code + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
