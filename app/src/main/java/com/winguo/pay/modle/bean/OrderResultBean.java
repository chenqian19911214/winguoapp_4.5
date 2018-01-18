package com.winguo.pay.modle.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/13.
 */

public class OrderResultBean implements Serializable{

    /**
     * order_number : M201701130000002118
     * order_amount : 20
     * text : 下单成功。
     * code : 1
     * order_id : 1581
     * item_name : 尺码：套,商品：玩具电动警车
     * status : success
     */

    public MessageBean message;

    @Override
    public String toString() {
        return "OrderResultBean{" +
                "message=" + message +
                '}';
    }

    public static class MessageBean implements Serializable{
        public String order_number;
        public double order_amount;
        public String text;
        public int code;
        public String order_id;
        public String item_name;
        public String status;

        @Override
        public String toString() {
            return "MessageBean{" +
                    "order_number='" + order_number + '\'' +
                    ", order_amount=" + order_amount +
                    ", text='" + text + '\'' +
                    ", code=" + code +
                    ", order_id=" + order_id +
                    ", item_name='" + item_name + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
    //{"message":{"status":"error","text":"购物车中无商品数据。","code":-1}}
}
