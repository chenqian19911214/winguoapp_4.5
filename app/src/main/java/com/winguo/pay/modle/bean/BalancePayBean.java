package com.winguo.pay.modle.bean;

/**
 * Created by admin on 2017/12/27.
 * 余额支付
 */

public class BalancePayBean {

    @Override
    public String toString() {
        return "BalancePayBean{" +
                "message=" + message +
                '}';
    }

    /**
     * message : {"status":"error","text":"支付密码验证失败。密码输入错误，支付密码初始密码为登录密码，请重试.","code":-5}
     */

    private MessageBean message;

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean {
        @Override
        public String toString() {
            return "MessageBean{" +
                    "status='" + status + '\'' +
                    ", text='" + text + '\'' +
                    ", code=" + code +
                    '}';
        }

        /**
         * status : error
         * text : 支付密码验证失败。密码输入错误，支付密码初始密码为登录密码，请重试.
         * code : -5
         */

        private String status;
        private String text;
        private int code;

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
    }
}
