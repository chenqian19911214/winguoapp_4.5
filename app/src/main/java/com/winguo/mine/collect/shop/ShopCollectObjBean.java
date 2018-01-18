package com.winguo.mine.collect.shop;

/**
 * @author hcpai
 * @desc 单个店铺收藏
 */
public class ShopCollectObjBean {
    @Override
    public String toString() {
        return "ShopCollectObjBean{" +
                "root=" + root +
                '}';
    }

    /**
     * items : {"data":{"adwords":"","id":2145,"item_counts":130,"logo":"","mobile_url":"jdsc","name":"京东旗舰店","shop_counts":8}}
     * otherinfo : {"count":1,"has_more_items":0}
     */

    private RootBean root;

    public RootBean getRoot() {
        return root;
    }

    public void setRoot(RootBean root) {
        this.root = root;
    }

    public static class RootBean {
        @Override
        public String toString() {
            return "RootBean{" +
                    "items=" + items +
                    ", otherinfo=" + otherinfo +
                    '}';
        }

        /**
         * data : {"adwords":"","id":2145,"item_counts":130,"logo":"","mobile_url":"jdsc","name":"京东旗舰店","shop_counts":8}
         */

        private ItemsBean items;
        /**
         * count : 1
         * has_more_items : 0
         */

        private OtherinfoBean otherinfo;

        public ItemsBean getItems() {
            return items;
        }

        public void setItems(ItemsBean items) {
            this.items = items;
        }

        public OtherinfoBean getOtherinfo() {
            return otherinfo;
        }

        public void setOtherinfo(OtherinfoBean otherinfo) {
            this.otherinfo = otherinfo;
        }

        public static class ItemsBean {
            /**
             * adwords :
             * id : 2145
             * item_counts : 130
             * logo :
             * mobile_url : jdsc
             * name : 京东旗舰店
             * shop_counts : 8
             */

            private ShopCollectItemBean data;

            public ShopCollectItemBean getData() {
                return data;
            }

            public void setData(ShopCollectItemBean data) {
                this.data = data;
            }

            @Override
            public String toString() {
                return "ItemsBean{" +
                        "data=" + data +
                        '}';
            }
        }

        public static class OtherinfoBean {
            private int count;
            private int has_more_items;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getHas_more_items() {
                return has_more_items;
            }

            public void setHas_more_items(int has_more_items) {
                this.has_more_items = has_more_items;
            }

            @Override
            public String toString() {
                return "OtherinfoBean{" +
                        "count=" + count +
                        ", has_more_items=" + has_more_items +
                        '}';
            }
        }
    }

}
