package com.winguo.product.modle.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/20.
 */

public class CollectBean implements Serializable {

    /**
     * code : 0
     * flag : 0
     * status : success
     * text : 没被收藏
     */

    public MessageBean message;

    public static class MessageBean implements Serializable {
        public int code;
        public int flag;
        public String status;
        public String text;

        @Override
        public String toString() {
            return "MessageBean{" +
                    "code=" + code +
                    ", flag=" + flag +
                    ", status='" + status + '\'' +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CollectBean{" +
                "message=" + message +
                '}';
    }
}
