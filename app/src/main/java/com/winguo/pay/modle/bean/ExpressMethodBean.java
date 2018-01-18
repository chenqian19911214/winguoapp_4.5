package com.winguo.pay.modle.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/10.
 */

public class ExpressMethodBean implements Serializable{


    /**
     * item : {"price":0,"name":"卖家承担运费","code":0}
     */
    public int temp_id;
    public ExpressageBean expressage;


    @Override
    public String toString() {
        return "ExpressMethodBean{" +
                "temp_id=" + temp_id +
                ", expressage=" + expressage +
                '}';
    }
}
