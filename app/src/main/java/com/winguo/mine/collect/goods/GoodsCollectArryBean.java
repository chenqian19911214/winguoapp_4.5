package com.winguo.mine.collect.goods;

import java.util.List;

/**
 * @author hcpai
 * @desc 商品收藏-多个商品
 */
public class GoodsCollectArryBean {
    @Override
    public String toString() {
        return "GoodsCollectArryBean{" +
                "root=" + root +
                '}';
    }

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
                    "count=" + count +
                    ", has_more=" + has_more +
                    ", items=" + items +
                    '}';
        }

        private int count;
        private int has_more;
        private ItemsBean items;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getHas_more() {
            return has_more;
        }

        public void setHas_more(int has_more) {
            this.has_more = has_more;
        }

        public ItemsBean getItems() {
            return items;
        }

        public void setItems(ItemsBean items) {
            this.items = items;
        }

        public static class ItemsBean {
            private List<GoodsCollectItemBean> item;

            @Override
            public String toString() {
                return "ItemsBean{" +
                        "item=" + item +
                        '}';
            }

            public List<GoodsCollectItemBean> getItem() {
                return item;
            }

            public void setItem(List<GoodsCollectItemBean> item) {
                this.item = item;
            }
        }
    }
}
