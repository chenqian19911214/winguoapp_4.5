package com.winguo.mine.collect.goods;

/**
 * @author hcpai
 * @desc 商品收藏-单个商品
 */
public class GoodsCollectObjBean {

    private RootBean root;

    public RootBean getRoot() {
        return root;
    }

    public void setRoot(RootBean root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "GoodsCollectObjBean{" +
                "root=" + root +
                '}';
    }

    public static class RootBean {
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

        @Override
        public String toString() {
            return "RootBean{" +
                    "count=" + count +
                    ", has_more=" + has_more +
                    ", items=" + items +
                    '}';
        }
    }


    public static class ItemsBean {
        private GoodsCollectItemBean item;

        public GoodsCollectItemBean getItem() {
            return item;
        }

        public void setItem(GoodsCollectItemBean item) {
            this.item = item;
        }

        @Override
        public String toString() {
            return "ItemsBean{" +
                    "item=" + item +
                    '}';
        }
    }
}
