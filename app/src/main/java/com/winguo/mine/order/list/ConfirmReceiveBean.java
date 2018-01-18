package com.winguo.mine.order.list;

/**
 * @author hcpai
 * @time 2017/5/5  11:39
 * @desc ${TODD}
 */

public class ConfirmReceiveBean {

    /**
     * status : success
     * text : 该订单交易成功了。
     * code : 1
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

