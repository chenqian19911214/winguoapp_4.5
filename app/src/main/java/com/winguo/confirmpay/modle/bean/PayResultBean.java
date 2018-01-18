package com.winguo.confirmpay.modle.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/2/27.
 */

public class PayResultBean implements Serializable {

    /**
     * data : {"charge_total":0.01,"balance_total":0,"winguo_total":0,"winguo_largess_total":0,"trade_status":1,"pay_type":0}
     */

    public RootBean root;

    @Override
    public String toString() {
        return "PayResultBean{" +
                "root=" + root +
                '}';
    }

    public static class RootBean implements Serializable{

        @Override
        public String toString() {
            return "RootBean{" +
                    "data=" + data +
                    '}';
        }

        public DataBean data;

        public static class DataBean implements Serializable {
            public String charge_total;//在线支付金额
            public String balance_total;
            public String winguo_total;
            public String winguo_largess_total;
            public String trade_status;//支付结果状态 0：支付失败；1：支付成功
            public String pay_type;

            @Override
            public String toString() {
                return "DataBean{" +
                        "charge_total='" + charge_total + '\'' +
                        ", balance_total='" + balance_total + '\'' +
                        ", winguo_total='" + winguo_total + '\'' +
                        ", winguo_largess_total='" + winguo_largess_total + '\'' +
                        ", trade_status='" + trade_status + '\'' +
                        ", pay_type='" + pay_type + '\'' +
                        '}';
            }
        }
    }
}
