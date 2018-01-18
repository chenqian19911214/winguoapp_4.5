package com.winguo.classify;

import java.util.List;

/**
 * @author hcpai
 * @desc 一级分类
 */
public class ClassifyFirstBean {

    private RootBean root;

    public RootBean getRoot() {
        return root;
    }

    public void setRoot(RootBean root) {
        this.root = root;
    }

    public static class RootBean {
        private int has_more_items;
        private int count;
        private String url;
        private ItemsBean items;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ItemsBean getItems() {
            return items;
        }

        public void setItems(ItemsBean items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * id : 35
             * name : 服装服饰
             * category_name : 衬衫 T恤 休闲裤 设计师/潮牌 ...
             * image : {"modifyTime":1434035350,"content":"http://g1.img.winguo.com/group1/M00/02/FD/wKgAi1V5pJaAf5fMAAAodZnw7rI586.png"}
             * order : 10
             */

            private List<DataBean> data;

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {
                private int id;
                private String name;
                private String category_name;
                /**
                 * modifyTime : 1434035350
                 * content : http://g1.img.winguo.com/group1/M00/02/FD/wKgAi1V5pJaAf5fMAAAodZnw7rI586.png
                 */

                private ImageBean image;
                private int order;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getCategory_name() {
                    return category_name;
                }

                public void setCategory_name(String category_name) {
                    this.category_name = category_name;
                }

                public ImageBean getImage() {
                    return image;
                }

                public void setImage(ImageBean image) {
                    this.image = image;
                }

                public int getOrder() {
                    return order;
                }

                public void setOrder(int order) {
                    this.order = order;
                }

                public static class ImageBean {
                    private int modifyTime;
                    private String content;

                    public int getModifyTime() {
                        return modifyTime;
                    }

                    public void setModifyTime(int modifyTime) {
                        this.modifyTime = modifyTime;
                    }

                    public String getContent() {
                        return content;
                    }

                    public void setContent(String content) {
                        this.content = content;
                    }
                }
            }
        }
    }
}
