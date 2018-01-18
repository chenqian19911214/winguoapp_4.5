package com.winguo.confirmpay.modle.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/14.
 */

public class MergeOrderBean implements Serializable{

    /**
     * order_number : M201701140000002139
     * balance : 0
     * order_amount : 29.9
     * winguo : 0
     * order_id : 2139
     * pay_type : 0
     * item_name : 充电触屏婴儿童智能音乐手机模型电话宝宝玩具早教启蒙益智1-3岁
     */

    public RootBean root;

    @Override
    public String toString() {
        return "MergeOrderBean{" +
                "root=" + root +
                '}';
    }

    public static class RootBean implements Serializable{
        public String order_number;
        public double balance;
        public String order_amount;
        public int winguo;
        public String order_id;
        public int pay_type;
        public String item_name;

        @Override
        public String toString() {
            return "RootBean{" +
                    "order_number='" + order_number + '\'' +
                    ", balance=" + balance +
                    ", order_amount=" + order_amount +
                    ", winguo=" + winguo +
                    ", order_id=" + order_id +
                    ", pay_type=" + pay_type +
                    ", item_name='" + item_name + '\'' +
                    '}';
        }
    }
}
