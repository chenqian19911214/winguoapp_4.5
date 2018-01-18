package com.winguo.alipay;

import java.io.Serializable;

/**
 * Created by Admin on 2017/2/27.
 */

public class ResultBean implements Serializable{

    public AlipayTradeAppPayResponseBean alipay_trade_app_pay_response;

    public  class AlipayTradeAppPayResponseBean implements Serializable{
        public String timestamp;//支付宝支付时间
        public String trade_no;//支付宝支付流水号
        public String out_trade_no;//订单号
        public String total_amount;//支付金额

        @Override
        public String toString() {
            return "AlipayTradeAppPayResponseBean{" +
                    "timestamp='" + timestamp + '\'' +
                    ", trade_no='" + trade_no + '\'' +
                    ", out_trade_no='" + out_trade_no + '\'' +
                    ", total_amount='" + total_amount + '\'' +
                    '}';
        }
    }

}
