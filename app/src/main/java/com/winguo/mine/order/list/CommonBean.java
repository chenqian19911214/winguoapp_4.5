package com.winguo.mine.order.list;

/**
 * Created by Admin on 2017/2/13.
 */

import java.io.Serializable;

/**
 * 支付成功要展示的商品数据
 */
public class CommonBean implements Serializable{
    public String content;
    public String name;
    public int num;

    @Override
    public String toString() {
        return "CommonBean{" +
                "content='" + content + '\'' +
                ", name='" + name + '\'' +
                ", num=" + num +
                '}';
    }
}
