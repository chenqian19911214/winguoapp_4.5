package com.winguo.pay.modle.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/11.
 */

public  class ItemBean implements Serializable{
    public double price;
    public String name;
    public int code;
    public boolean isChoosed;

    @Override
    public String toString() {
        return "ItemBean{" +
                "price=" + price +
                ", name='" + name + '\'' +
                ", code=" + code +
                ", isChoosed=" + isChoosed +
                '}';
    }
    public void setChoosed(boolean isChoosed){

        this.isChoosed = isChoosed;
    }
    public boolean getChoosed(){
        return isChoosed;
    }
}
