package com.winguo.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/2/9.
 */

public  class ItemsBean implements Serializable{

    public List<DataBean> data;

    @Override
    public String toString() {
        return "ItemsBean{" +
                "data=" + data +
                '}';
    }

    public static class DataBean implements Serializable{
        public ImageBean image;
        public String url;

        @Override
        public String toString() {
            return "DataBean{" +
                    "image=" + image +
                    ", url='" + url + '\'' +
                    '}';
        }

        public static class ImageBean implements Serializable{
            public String content;

            @Override
            public String toString() {
                return "ImageBean{" +
                        "content='" + content + '\'' +
                        '}';
            }
        }
    }
}
