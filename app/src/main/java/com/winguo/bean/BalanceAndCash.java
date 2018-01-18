package com.winguo.bean;

/**
 * Created by admin on 2017/12/26.
 * 新获取余额  创客基金 及现金券
 */

public class BalanceAndCash {


    /**
     * code : 0
     * msg : 获取成功
     * item : {"cash_coupon":"2100.00","cash_credit":"2000.00","purse_balance":"973094.90"}
     */

    private int code;
    private String msg;
    private ItemBean item;

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

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * cash_coupon : 2100.00
         * cash_credit : 2000.00
         * purse_balance : 973094.90
         */

        private double cash_coupon;
        private double cash_credit;
        private double purse_balance;

        public double getCash_coupon() {
            return cash_coupon;
        }

        public void setCash_coupon(double cash_coupon) {
            this.cash_coupon = cash_coupon;
        }

        public double getCash_credit() {
            return cash_credit;
        }

        public void setCash_credit(double cash_credit) {
            this.cash_credit = cash_credit;
        }

        public double getPurse_balance() {
            return purse_balance;
        }

        public void setPurse_balance(double purse_balance) {
            this.purse_balance = purse_balance;
        }
    }
}
