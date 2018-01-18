package com.winguo.classify;


import com.winguo.productList.modle.ItemEntitys;

import java.util.List;

/**
 * @author hcpai
 * @desc 分类商品
 */
public class ClassifyGoodsBean {
    @Override
    public String toString() {
        return "ClassifyGoodsBean{" +
                "category=" + category +
                '}';
    }

    private CategoryBean category;

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public static class CategoryBean {
        private CategoryInfoBean category_info;
        private ProductsBean products;

        @Override
        public String toString() {
            return "CategoryBean{" +
                    "category_info=" + category_info +
                    ", products=" + products +
                    '}';
        }

        public CategoryInfoBean getCategory_info() {
            return category_info;
        }

        public void setCategory_info(CategoryInfoBean category_info) {
            this.category_info = category_info;
        }

        public ProductsBean getProducts() {
            return products;
        }

        public void setProducts(ProductsBean products) {
            this.products = products;
        }

        public static class CategoryInfoBean {
            private String parent_title;
            private int parent_id;
            private int has_more_items;
            private int count;
            private String content_type;

            @Override
            public String toString() {
                return "CategoryInfoBean{" +
                        "parent_title='" + parent_title + '\'' +
                        ", parent_id=" + parent_id +
                        ", has_more_items=" + has_more_items +
                        ", count=" + count +
                        ", content_type='" + content_type + '\'' +
                        '}';
            }

            public String getParent_title() {
                return parent_title;
            }

            public void setParent_title(String parent_title) {
                this.parent_title = parent_title;
            }

            public int getParent_id() {
                return parent_id;
            }

            public void setParent_id(int parent_id) {
                this.parent_id = parent_id;
            }

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

            public String getContent_type() {
                return content_type;
            }

            public void setContent_type(String content_type) {
                this.content_type = content_type;
            }
        }

        public static class ProductsBean {
            @Override
            public String toString() {
                return "ProductsBean{" +
                        "item=" + item +
                        '}';
            }

            private List<ItemEntitys> item;

            public List<ItemEntitys> getItem() {
                return item;
            }

            public void setItem(List<ItemEntitys> item) {
                this.item = item;
            }
        }
    }
}
