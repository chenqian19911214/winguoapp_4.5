package com.winguo.personalcenter.wallet.bean;

import java.util.List;

/**
 * Created by admin on 2018/1/15.
 * 预存余额明细
 */

public class PrestoreDetail {

    /**
     * code : 0
     * msg : 获取成功
     * item : [{"title":"充值预购款","cash":"2000.00","time":"2018-01-11 16:55:41"},{"title":"充值预购款","cash":"2000.00","time":"2018-01-11 16:51:09"},{"title":"购买商品","cash":"-4850.00","time":"2018-01-10 13:19:29"},{"title":"购买商品","cash":"-2550.00","time":"2018-01-10 13:14:51"},{"title":"购买商品","cash":"-50.00","time":"2018-01-10 11:37:29"},{"title":"购买商品","cash":"-4850.00","time":"2018-01-10 10:46:28"},{"title":"购买商品","cash":"-2600.00","time":"2018-01-10 10:32:53"},{"title":"购买商品","cash":"-604.00","time":"2018-01-10 10:24:35"},{"title":"购买商品","cash":"-350.00","time":"2018-01-10 10:24:35"},{"title":"购买商品","cash":"-3000.00","time":"2018-01-08 16:32:56"},{"title":"购买商品","cash":"-3000.00","time":"2018-01-08 16:31:50"},{"title":"购买商品","cash":"-3000.00","time":"2018-01-08 16:31:20"},{"title":"购买商品","cash":"-3000.00","time":"2018-01-08 16:30:52"},{"title":"购买商品","cash":"-5400.00","time":"2018-01-08 16:30:29"},{"title":"购买商品","cash":"-4900.00","time":"2018-01-08 16:22:09"},{"title":"购买商品","cash":"-2550.00","time":"2018-01-08 16:04:50"},{"title":"购买商品","cash":"-2550.00","time":"2018-01-08 15:57:01"},{"title":"购买商品","cash":"-2550.00","time":"2018-01-08 15:44:25"},{"title":"购买商品","cash":"-2550.00","time":"2018-01-08 15:37:51"},{"title":"购买商品","cash":"-2550.00","time":"2018-01-08 15:33:07"},{"title":"购买商品","cash":"-2550.00","time":"2018-01-08 15:29:26"},{"title":"购买商品","cash":"-2550.00","time":"2018-01-08 15:22:15"},{"title":"购买商品","cash":"0.00","time":"2018-01-08 15:14:21"},{"title":"购买商品","cash":"-2000.05","time":"2018-01-05 10:34:00"},{"title":"创客注册资金","cash":"0.01","time":"2017-12-29 13:28:29"},{"title":"创客注册资金","cash":"0.01","time":"2017-12-29 13:27:54"},{"title":"创客注册资金","cash":"0.01","time":"2017-12-29 13:25:52"},{"title":"创客注册资金","cash":"0.01","time":"2017-12-29 13:21:55"},{"title":"创客注册资金","cash":"0.01","time":"2017-12-29 13:09:30"},{"title":"使用创客资金支付购买商品","cash":"-1444.00","time":"2017-12-27 19:16:32"},{"title":"使用创客资金支付购买商品","cash":"-278.00","time":"2017-12-27 19:03:26"},{"title":"使用创客资金支付购买商品","cash":"-278.00","time":"2017-12-27 17:43:27"},{"title":"使用创客资金支付购买商品","cash":"-120.00","time":"2017-12-27 17:09:32"},{"title":"使用创客资金支付购买商品","cash":"-100.00","time":"2017-12-27 17:04:50"},{"title":"使用创客资金支付购买商品","cash":"-100.00","time":"2017-12-27 16:58:09"},{"title":"使用创客资金支付购买商品","cash":"-100.00","time":"2017-12-27 16:38:56"},{"title":"使用创客资金支付购买商品","cash":"-100.00","time":"2017-12-27 16:27:39"},{"title":"使用创客资金支付购买商品","cash":"-478.00","time":"2017-12-27 16:10:17"},{"title":"使用创客资金支付购买商品","cash":"-139.00","time":"2017-12-27 15:57:53"},{"title":"使用创客资金支付购买商品","cash":"-179.00","time":"2017-12-27 15:54:13"},{"title":"使用创客资金支付购买商品","cash":"-179.00","time":"2017-12-27 15:47:26"},{"title":"使用创客资金支付购买商品","cash":"-179.00","time":"2017-12-27 14:49:20"},{"title":"使用创客资金支付购买商品","cash":"-179.00","time":"2017-12-27 14:17:03"},{"title":"使用创客资金支付购买商品","cash":"-229.00","time":"2017-12-27 14:07:25"},{"title":"使用创客资金支付购买商品","cash":"-209.00","time":"2017-12-27 13:53:21"},{"title":"使用创客资金支付购买商品","cash":"-229.00","time":"2017-12-27 13:34:05"},{"title":"后台添加信用卡金额","cash":"2000.00","time":"2017-10-24 16:00:25"}]
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
         * title : 充值预购款
         * cash : 2000.00
         * time : 2018-01-11 16:55:41
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
