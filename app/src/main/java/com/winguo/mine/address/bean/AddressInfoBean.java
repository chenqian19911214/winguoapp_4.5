package com.winguo.mine.address.bean;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc 省市区的信息
 */
public class AddressInfoBean implements Serializable {
    @Override
    public String toString() {
        return "AddressInfoBean{" +
                "name='" + name + '\'' +
                ", code=" + code +
                '}';
    }

    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
