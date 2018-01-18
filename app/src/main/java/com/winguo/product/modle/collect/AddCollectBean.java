package com.winguo.product.modle.collect;

/**
 * @author hcpai
 * @desc 添加商品收藏
 */
public class AddCollectBean {

    /**
     * status : success
     * text : 收藏成功
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
