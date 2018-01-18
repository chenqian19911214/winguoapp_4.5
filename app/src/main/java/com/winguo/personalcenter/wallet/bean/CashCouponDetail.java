package com.winguo.personalcenter.wallet.bean;

import java.util.List;

/**
 * Created by admin on 2017/11/7.
 * 现金券明细
 */

public class CashCouponDetail {

    /**
     * code : 0
     * msg : 获取成功
     * item : [{"title":"办理信用卡返现","cash":"2000.00","time":"2017-11-06 11:02:30"},{"title":"注册送现金卷","cash":"100.00","time":"2017-10-24 16:38:24"}]
     */

    private int code;
    private String msg;
    private List<ItemBean> item;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * title : 办理信用卡返现
         * cash : 2000.00
         * time : 2017-11-06 11:02:30
         */

        private String title;
        private String cash;
        private String time;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCash() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
