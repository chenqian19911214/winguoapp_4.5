package com.winguo.login.register.bean;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class NewRegisterBean {

    /**
     * code : -42
     * mobile : 13106747490
     * status : error
     * text : 手机号码或账号已存在
     */

    private int code;
    private String mobile;
    private String status;
    private String text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

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
}
