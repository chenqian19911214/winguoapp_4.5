package com.winguo.product.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/22.
 */

public class ShopSimpleBean implements Serializable {

    @Override
    public String toString() {
        return "ShopSimpleBean{" +
                "message=" + message +
                '}';
    }

    public MessageBean message;

    public static class MessageBean implements Serializable{
        public int brandCount;//收藏人数
        public int itemCount;//商品数
        public String shop_url;//店铺的url
        public int is_collect;//是否收藏
        /**
         * content : http://g1.img.winguo.com/group1/M00/4D/22/wKgAjFc5bCyACw8BAAB0DP2DTC0388.png
         * modifyTime : 1463381036
         */
        public LogoBean logo;//店铺logo
        public String name;//店铺名

        @Override
        public String toString() {
            return "MessageBean{" +
                    "brandCount=" + brandCount +
                    ", itemCount=" + itemCount +
                    ", logo=" + logo +
                    ", name='" + name + '\'' +
                    ", shop_url='" + shop_url + '\'' +
                    ", is_collect='" + is_collect + '\'' +
                    '}';
        }
    }
}
