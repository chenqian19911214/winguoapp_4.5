package com.winguo.pay.modle.bean;

/**
 * @author hcpai
 * @time 2017/10/18  16:12
 * @desc ${TODD}
 */

public class ReChargeBean {

    /**
     * status : success
     * text : 生成支付订单成功。
     * code : 0
     * orderNumber : M20171018161302vLu5H
     */

    private String status;
    private String text;
    private int code;
    private String orderNumber;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
