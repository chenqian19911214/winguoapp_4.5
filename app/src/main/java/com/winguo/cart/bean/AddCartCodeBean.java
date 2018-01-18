package com.winguo.cart.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/30.
 */

public class AddCartCodeBean implements Serializable{

    @Override
    public String toString() {
        return "AddCartCodeBean{" +
                "message=" + message +
                '}';
    }

    /**
     * status : error
     * text : 登录已过期，请重新登录。
     * code : -101
     */

    public MessageBean message;

    public static class MessageBean {
        public String status;
        public String text;
        public int code;

        @Override
        public String toString() {
            return "MessageBean{" +
                    "status='" + status + '\'' +
                    ", text='" + text + '\'' +
                    ", code=" + code +
                    '}';
        }
    }
}
