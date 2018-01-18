package com.winguo.mine.collect.shop;

import java.util.List;

/**
 * @author hcpai
 * @desc 多个店铺收藏
 */
public class ShopCollectArryBean {

    /**
     * otherinfo : {"has_more_items":0,"count":2}
     * items : {"data":[{"id":2145,"name":"京东旗舰店","mobile_url":"jdsc","adwords":"","logo":"","item_counts":130,"shop_counts":8},{"id":2231,"name":"金雀服饰旗舰店","mobile_url":"y666","adwords":"金雀服饰","logo":{"modifyTime":1463381036,"content":"http://g1.img.winguo.com/group1/M00/4D/22/wKgAjFc5bCyACw8BAAB0DP2DTC0388.png"},"item_counts":54,"shop_counts":3}]}
     */

    private RootBean root;

    public RootBean getRoot() {
        return root;
    }

    public void setRoot(RootBean root) {
        this.root = root;
    }

    public static class RootBean {
        /**
         * has_more_items : 0
         * count : 2
         */

        private OtherinfoBean otherinfo;
        private ItemsBean items;

        public OtherinfoBean getOtherinfo() {
            return otherinfo;
        }

        public void setOtherinfo(OtherinfoBean otherinfo) {
            this.otherinfo = otherinfo;
        }

        public ItemsBean getItems() {
            return items;
        }

        public void setItems(ItemsBean items) {
            this.items = items;
        }

        public static class OtherinfoBean {
            private int has_more_items;
            private int count;

            public int getHas_more_items() {
                return has_more_items;
            }

            public void setHas_more_items(int has_more_items) {
                this.has_more_items = has_more_items;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }

        public static class ItemsBean {
            /**
             * id : 2145
             * name : 京东旗舰店
             * mobile_url : jdsc
             * adwords :
             * logo :
             * item_counts : 130
             * shop_counts : 8
             */

            private List<ShopCollectItemBean> data;

            public List<ShopCollectItemBean> getData() {
                return data;
            }

            public void setData(List<ShopCollectItemBean> data) {
                this.data = data;
            }
        }
    }
}
