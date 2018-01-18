package com.winguo.classify;

import java.util.List;

/**
 * @author hcpai
 * @desc 二级分类
 */
public class ClassifySecondBean {
    @Override
    public String toString() {
        return "ClassifySecondBean{" +
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
                    "has_more_items=" + has_more_items +
                    ", count=" + count +
                    ", categories=" + categories +
                    '}';
        }

        private int has_more_items;
        private int count;
        private CategoriesBean categories;

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

        public CategoriesBean getCategories() {
            return categories;
        }

        public void setCategories(CategoriesBean categories) {
            this.categories = categories;
        }

        public static class CategoriesBean {
            @Override
            public String toString() {
                return "CategoriesBean{" +
                        "position=" + position +
                        ", item=" + item +
                        '}';
            }

            /**
             * 自定义属性:父id
             */
            private int position;
            private List<ItemBean> item;

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public List<ItemBean> getItem() {
                return item;
            }

            public void setItem(List<ItemBean> item) {
                this.item = item;
            }

            public static class ItemBean {
                @Override
                public String toString() {
                    return "ItemBean{" +
                            "cate_id=" + cate_id +
                            ", name='" + name + '\'' +
                            ", m_category_path='" + m_category_path + '\'' +
                            ", m_category_is_leaf=" + m_category_is_leaf +
                            ", m_category_image=" + m_category_image +
                            ", content_type='" + content_type + '\'' +
                            ", row_counts=" + row_counts +
                            '}';
                }

                private int cate_id;
                private String name;
                private String m_category_path;
                private int m_category_is_leaf;
                /**
                 * modifyTime :
                 */

                private MCategoryImageBean m_category_image;
                private String content_type;
                private int row_counts;

                public int getCate_id() {
                    return cate_id;
                }

                public void setCate_id(int cate_id) {
                    this.cate_id = cate_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getM_category_path() {
                    return m_category_path;
                }

                public void setM_category_path(String m_category_path) {
                    this.m_category_path = m_category_path;
                }

                public int getM_category_is_leaf() {
                    return m_category_is_leaf;
                }

                public void setM_category_is_leaf(int m_category_is_leaf) {
                    this.m_category_is_leaf = m_category_is_leaf;
                }

                public MCategoryImageBean getM_category_image() {
                    return m_category_image;
                }

                public void setM_category_image(MCategoryImageBean m_category_image) {
                    this.m_category_image = m_category_image;
                }

                public String getContent_type() {
                    return content_type;
                }

                public void setContent_type(String content_type) {
                    this.content_type = content_type;
                }

                public int getRow_counts() {
                    return row_counts;
                }

                public void setRow_counts(int row_counts) {
                    this.row_counts = row_counts;
                }

                public static class MCategoryImageBean {
                    private String modifyTime;

                    public String getModifyTime() {
                        return modifyTime;
                    }

                    public void setModifyTime(String modifyTime) {
                        this.modifyTime = modifyTime;
                    }
                }
            }
        }
    }
}
