package com.winguo.confirmpay.modle;

import java.io.Serializable;

/**
 * Created by Admin on 2017/3/15.
 */
@Deprecated
public class UnionpayOrderBean implements Serializable {

    /**
     * msg : 下单成功
     * tn :"788671914303677514600"
     * code : 0
     */
        public String msg;
        public String tn;//TN
        public int code;


    @Override
    public String toString() {
        return "UnionpayOrderBean{" +
                "msg='" + msg + '\'' +
                ", tn='" + tn + '\'' +
                ", code=" + code +
                '}';
    }
}
