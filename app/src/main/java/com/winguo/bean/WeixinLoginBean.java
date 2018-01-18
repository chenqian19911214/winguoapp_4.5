package com.winguo.bean;

/**
 * Created by admin on 2017/9/19.
 */

public class WeixinLoginBean {

    /**
     * status : success
     * text : 登录成功
     * code : 0
     * name : owRmGwq17cdXEdlbQsuTq6Gbyn3A
     * not_verified : 1
     */

    private String status;
    private String text;
    private int code;
    private String name;
    private int not_verified;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNot_verified() {
        return not_verified;
    }

    public void setNot_verified(int not_verified) {
        this.not_verified = not_verified;
    }
}
