package com.winguo.pay.modle.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/7/5.
 */

public class PhysicalSubmitOrderBean implements Serializable {
    public int code;
    public String message;
    public ResultBean result;

    @Override
    public String toString() {
        return "PhysicalSubmitOrderBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    public class ResultBean implements Serializable{
        public String order_id;
        public String order_no;
        public String merge_order_no;

        @Override
        public String toString() {
            return "ResultBean{" +
                    "order_id='" + order_id + '\'' +
                    ", order_no='" + order_no + '\'' +
                    ", merge_order_no='" + merge_order_no + '\'' +
                    '}';
        }
    }



}
