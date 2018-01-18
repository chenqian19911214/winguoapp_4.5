package com.winguo.bean;

/**
 * Created by chenq on 2017/12/27.
 */

public class BindMobileGson {
    /**
     * message : {"status":"error","text":"请输入正确的手机号码。","code":-2}
     */

    private MessageBean message;

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * status : error
         * text : 请输入正确的手机号码。
         * code : -2
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
