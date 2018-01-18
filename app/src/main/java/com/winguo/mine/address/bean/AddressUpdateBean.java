package com.winguo.mine.address.bean;

/**
 * @author hcpai
 * @desc 修改地址
 */
public class AddressUpdateBean {

    /**
     * status : success
     * text : 配送信息更新成功。
     * code : 0
     */

    private MessageBean message;

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean {
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
