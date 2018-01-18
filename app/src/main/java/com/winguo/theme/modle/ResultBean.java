package com.winguo.theme.modle;

import java.io.Serializable;

public  class ResultBean implements Serializable {
        public String m_topics_add_time;
        public String m_topics_cate_id;
        public String m_topics_end_time;
        public String m_topics_ext;//价格
        public String m_topics_goods_id;//商品ID
        public String m_topics_id;
        public String m_topics_is_open;
        public String m_topics_numbers;
        public String m_topics_pic_url;//大图
        public String m_topics_pid;
        public String m_topics_recommend;
        public String m_topics_sort;
        public String m_topics_start_time;
        public String m_topics_thumb_url;//小图
        public String m_topics_title;//名称
        public String m_topics_type;
        public String m_topics_url;
        public String m_topics_shop_id;

        @Override
        public String toString() {
                return "ResultBean{" +
                        "m_topics_add_time='" + m_topics_add_time + '\'' +
                        ", m_topics_cate_id='" + m_topics_cate_id + '\'' +
                        ", m_topics_end_time='" + m_topics_end_time + '\'' +
                        ", m_topics_ext='" + m_topics_ext + '\'' +
                        ", m_topics_goods_id='" + m_topics_goods_id + '\'' +
                        ", m_topics_id='" + m_topics_id + '\'' +
                        ", m_topics_is_open='" + m_topics_is_open + '\'' +
                        ", m_topics_numbers='" + m_topics_numbers + '\'' +
                        ", m_topics_pic_url='" + m_topics_pic_url + '\'' +
                        ", m_topics_pid='" + m_topics_pid + '\'' +
                        ", m_topics_recommend='" + m_topics_recommend + '\'' +
                        ", m_topics_sort='" + m_topics_sort + '\'' +
                        ", m_topics_start_time='" + m_topics_start_time + '\'' +
                        ", m_topics_thumb_url='" + m_topics_thumb_url + '\'' +
                        ", m_topics_title='" + m_topics_title + '\'' +
                        ", m_topics_type='" + m_topics_type + '\'' +
                        ", m_topics_url='" + m_topics_url + '\'' +
                        ", m_topics_shop_id='" + m_topics_shop_id + '\'' +
                        '}';
        }
}