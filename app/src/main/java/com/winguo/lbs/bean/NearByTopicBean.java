package com.winguo.lbs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class NearByTopicBean implements Serializable{
    @Override
    public String toString() {
        return "NearByTopicBean{" +
                "Result=" + Result +
                ", Code='" + Code + '\'' +
                '}';
    }

    private ResultBean Result;
    private String Code;

    public ResultBean getResult() {
        return Result;
    }

    public void setResult(ResultBean Result) {
        this.Result = Result;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public static class ResultBean {
        @Override
        public String toString() {
            return "ResultBean{" +
                    "Length1=" + Length1 +
                    ", Length2=" + Length2 +
                    ", BanLen=" + BanLen +
                    ", Line1=" + Line1 +
                    ", Line2=" + Line2 +
                    ", Ban=" + Ban +
                    '}';
        }

        private int Length1;
        private int Length2;
        private int BanLen;


        private List<Line1Bean> Line1;


        private List<Line2Bean> Line2;
        private List<BanBean> Ban;

        public int getLength1() {
            return Length1;
        }

        public void setLength1(int Length1) {
            this.Length1 = Length1;
        }

        public int getLength2() {
            return Length2;
        }

        public void setLength2(int Length2) {
            this.Length2 = Length2;
        }

        public int getBanLen() {
            return BanLen;
        }

        public void setBanLen(int BanLen) {
            this.BanLen = BanLen;
        }

        public List<Line1Bean> getLine1() {
            return Line1;
        }

        public void setLine1(List<Line1Bean> Line1) {
            this.Line1 = Line1;
        }

        public List<Line2Bean> getLine2() {
            return Line2;
        }

        public void setLine2(List<Line2Bean> Line2) {
            this.Line2 = Line2;
        }

        public List<BanBean> getBan() {
            return Ban;
        }

        public void setBan(List<BanBean> Ban) {
            this.Ban = Ban;
        }

        public static class Line1Bean {
            @Override
            public String toString() {
                return "Line1Bean{" +
                        "m_topics_id='" + m_topics_id + '\'' +
                        ", m_topics_pid='" + m_topics_pid + '\'' +
                        ", m_topics_cate_id='" + m_topics_cate_id + '\'' +
                        ", m_topics_title='" + m_topics_title + '\'' +
                        ", m_topics_brief=" + m_topics_brief +
                        ", m_topics_url=" + m_topics_url +
                        ", m_topics_is_open='" + m_topics_is_open + '\'' +
                        ", m_topics_start_time='" + m_topics_start_time + '\'' +
                        ", m_topics_end_time='" + m_topics_end_time + '\'' +
                        ", m_topics_pic_url='" + m_topics_pic_url + '\'' +
                        ", m_topics_thumb_url=" + m_topics_thumb_url +
                        ", m_topics_recommend='" + m_topics_recommend + '\'' +
                        ", m_topics_sort='" + m_topics_sort + '\'' +
                        ", m_topics_desc=" + m_topics_desc +
                        ", m_topics_add_time='" + m_topics_add_time + '\'' +
                        ", m_topics_numbers='" + m_topics_numbers + '\'' +
                        ", m_topics_ext=" + m_topics_ext +
                        ", m_topics_goods_id='" + m_topics_goods_id + '\'' +
                        ", m_topics_type='" + m_topics_type + '\'' +
                        ", m_topics_shop_id=" + m_topics_shop_id +
                        '}';
            }

            private String m_topics_id;
            private String m_topics_pid;
            private String m_topics_cate_id;
            private String m_topics_title;
            private Object m_topics_brief;
            private Object m_topics_url;
            private String m_topics_is_open;
            private String m_topics_start_time;
            private String m_topics_end_time;
            private String m_topics_pic_url;
            private Object m_topics_thumb_url;
            private String m_topics_recommend;
            private String m_topics_sort;
            private Object m_topics_desc;
            private String m_topics_add_time;
            private String m_topics_numbers;
            private Object m_topics_ext;
            private String m_topics_goods_id;
            private String m_topics_type;
            private Object m_topics_shop_id;

            public String getM_topics_id() {
                return m_topics_id;
            }

            public void setM_topics_id(String m_topics_id) {
                this.m_topics_id = m_topics_id;
            }

            public String getM_topics_pid() {
                return m_topics_pid;
            }

            public void setM_topics_pid(String m_topics_pid) {
                this.m_topics_pid = m_topics_pid;
            }

            public String getM_topics_cate_id() {
                return m_topics_cate_id;
            }

            public void setM_topics_cate_id(String m_topics_cate_id) {
                this.m_topics_cate_id = m_topics_cate_id;
            }

            public String getM_topics_title() {
                return m_topics_title;
            }

            public void setM_topics_title(String m_topics_title) {
                this.m_topics_title = m_topics_title;
            }

            public Object getM_topics_brief() {
                return m_topics_brief;
            }

            public void setM_topics_brief(Object m_topics_brief) {
                this.m_topics_brief = m_topics_brief;
            }

            public Object getM_topics_url() {
                return m_topics_url;
            }

            public void setM_topics_url(Object m_topics_url) {
                this.m_topics_url = m_topics_url;
            }

            public String getM_topics_is_open() {
                return m_topics_is_open;
            }

            public void setM_topics_is_open(String m_topics_is_open) {
                this.m_topics_is_open = m_topics_is_open;
            }

            public String getM_topics_start_time() {
                return m_topics_start_time;
            }

            public void setM_topics_start_time(String m_topics_start_time) {
                this.m_topics_start_time = m_topics_start_time;
            }

            public String getM_topics_end_time() {
                return m_topics_end_time;
            }

            public void setM_topics_end_time(String m_topics_end_time) {
                this.m_topics_end_time = m_topics_end_time;
            }

            public String getM_topics_pic_url() {
                return m_topics_pic_url;
            }

            public void setM_topics_pic_url(String m_topics_pic_url) {
                this.m_topics_pic_url = m_topics_pic_url;
            }

            public Object getM_topics_thumb_url() {
                return m_topics_thumb_url;
            }

            public void setM_topics_thumb_url(Object m_topics_thumb_url) {
                this.m_topics_thumb_url = m_topics_thumb_url;
            }

            public String getM_topics_recommend() {
                return m_topics_recommend;
            }

            public void setM_topics_recommend(String m_topics_recommend) {
                this.m_topics_recommend = m_topics_recommend;
            }

            public String getM_topics_sort() {
                return m_topics_sort;
            }

            public void setM_topics_sort(String m_topics_sort) {
                this.m_topics_sort = m_topics_sort;
            }

            public Object getM_topics_desc() {
                return m_topics_desc;
            }

            public void setM_topics_desc(Object m_topics_desc) {
                this.m_topics_desc = m_topics_desc;
            }

            public String getM_topics_add_time() {
                return m_topics_add_time;
            }

            public void setM_topics_add_time(String m_topics_add_time) {
                this.m_topics_add_time = m_topics_add_time;
            }

            public String getM_topics_numbers() {
                return m_topics_numbers;
            }

            public void setM_topics_numbers(String m_topics_numbers) {
                this.m_topics_numbers = m_topics_numbers;
            }

            public Object getM_topics_ext() {
                return m_topics_ext;
            }

            public void setM_topics_ext(Object m_topics_ext) {
                this.m_topics_ext = m_topics_ext;
            }

            public String getM_topics_goods_id() {
                return m_topics_goods_id;
            }

            public void setM_topics_goods_id(String m_topics_goods_id) {
                this.m_topics_goods_id = m_topics_goods_id;
            }

            public String getM_topics_type() {
                return m_topics_type;
            }

            public void setM_topics_type(String m_topics_type) {
                this.m_topics_type = m_topics_type;
            }

            public Object getM_topics_shop_id() {
                return m_topics_shop_id;
            }

            public void setM_topics_shop_id(Object m_topics_shop_id) {
                this.m_topics_shop_id = m_topics_shop_id;
            }
        }

        public static class Line2Bean {
            @Override
            public String toString() {
                return "Line2Bean{" +
                        "m_topics_id='" + m_topics_id + '\'' +
                        ", m_topics_pid='" + m_topics_pid + '\'' +
                        ", m_topics_cate_id='" + m_topics_cate_id + '\'' +
                        ", m_topics_title='" + m_topics_title + '\'' +
                        ", m_topics_brief=" + m_topics_brief +
                        ", m_topics_url=" + m_topics_url +
                        ", m_topics_is_open='" + m_topics_is_open + '\'' +
                        ", m_topics_start_time='" + m_topics_start_time + '\'' +
                        ", m_topics_end_time='" + m_topics_end_time + '\'' +
                        ", m_topics_pic_url='" + m_topics_pic_url + '\'' +
                        ", m_topics_thumb_url=" + m_topics_thumb_url +
                        ", m_topics_recommend='" + m_topics_recommend + '\'' +
                        ", m_topics_sort='" + m_topics_sort + '\'' +
                        ", m_topics_desc=" + m_topics_desc +
                        ", m_topics_add_time='" + m_topics_add_time + '\'' +
                        ", m_topics_numbers='" + m_topics_numbers + '\'' +
                        ", m_topics_ext=" + m_topics_ext +
                        ", m_topics_goods_id='" + m_topics_goods_id + '\'' +
                        ", m_topics_type='" + m_topics_type + '\'' +
                        ", m_topics_shop_id=" + m_topics_shop_id +
                        '}';
            }

            private String m_topics_id;
            private String m_topics_pid;
            private String m_topics_cate_id;
            private String m_topics_title;
            private Object m_topics_brief;
            private Object m_topics_url;
            private String m_topics_is_open;
            private String m_topics_start_time;
            private String m_topics_end_time;
            private String m_topics_pic_url;
            private Object m_topics_thumb_url;
            private String m_topics_recommend;
            private String m_topics_sort;
            private Object m_topics_desc;
            private String m_topics_add_time;
            private String m_topics_numbers;
            private Object m_topics_ext;
            private String m_topics_goods_id;
            private String m_topics_type;
            private Object m_topics_shop_id;

            public String getM_topics_id() {
                return m_topics_id;
            }

            public void setM_topics_id(String m_topics_id) {
                this.m_topics_id = m_topics_id;
            }

            public String getM_topics_pid() {
                return m_topics_pid;
            }

            public void setM_topics_pid(String m_topics_pid) {
                this.m_topics_pid = m_topics_pid;
            }

            public String getM_topics_cate_id() {
                return m_topics_cate_id;
            }

            public void setM_topics_cate_id(String m_topics_cate_id) {
                this.m_topics_cate_id = m_topics_cate_id;
            }

            public String getM_topics_title() {
                return m_topics_title;
            }

            public void setM_topics_title(String m_topics_title) {
                this.m_topics_title = m_topics_title;
            }

            public Object getM_topics_brief() {
                return m_topics_brief;
            }

            public void setM_topics_brief(Object m_topics_brief) {
                this.m_topics_brief = m_topics_brief;
            }

            public Object getM_topics_url() {
                return m_topics_url;
            }

            public void setM_topics_url(Object m_topics_url) {
                this.m_topics_url = m_topics_url;
            }

            public String getM_topics_is_open() {
                return m_topics_is_open;
            }

            public void setM_topics_is_open(String m_topics_is_open) {
                this.m_topics_is_open = m_topics_is_open;
            }

            public String getM_topics_start_time() {
                return m_topics_start_time;
            }

            public void setM_topics_start_time(String m_topics_start_time) {
                this.m_topics_start_time = m_topics_start_time;
            }

            public String getM_topics_end_time() {
                return m_topics_end_time;
            }

            public void setM_topics_end_time(String m_topics_end_time) {
                this.m_topics_end_time = m_topics_end_time;
            }

            public String getM_topics_pic_url() {
                return m_topics_pic_url;
            }

            public void setM_topics_pic_url(String m_topics_pic_url) {
                this.m_topics_pic_url = m_topics_pic_url;
            }

            public Object getM_topics_thumb_url() {
                return m_topics_thumb_url;
            }

            public void setM_topics_thumb_url(Object m_topics_thumb_url) {
                this.m_topics_thumb_url = m_topics_thumb_url;
            }

            public String getM_topics_recommend() {
                return m_topics_recommend;
            }

            public void setM_topics_recommend(String m_topics_recommend) {
                this.m_topics_recommend = m_topics_recommend;
            }

            public String getM_topics_sort() {
                return m_topics_sort;
            }

            public void setM_topics_sort(String m_topics_sort) {
                this.m_topics_sort = m_topics_sort;
            }

            public Object getM_topics_desc() {
                return m_topics_desc;
            }

            public void setM_topics_desc(Object m_topics_desc) {
                this.m_topics_desc = m_topics_desc;
            }

            public String getM_topics_add_time() {
                return m_topics_add_time;
            }

            public void setM_topics_add_time(String m_topics_add_time) {
                this.m_topics_add_time = m_topics_add_time;
            }

            public String getM_topics_numbers() {
                return m_topics_numbers;
            }

            public void setM_topics_numbers(String m_topics_numbers) {
                this.m_topics_numbers = m_topics_numbers;
            }

            public Object getM_topics_ext() {
                return m_topics_ext;
            }

            public void setM_topics_ext(Object m_topics_ext) {
                this.m_topics_ext = m_topics_ext;
            }

            public String getM_topics_goods_id() {
                return m_topics_goods_id;
            }

            public void setM_topics_goods_id(String m_topics_goods_id) {
                this.m_topics_goods_id = m_topics_goods_id;
            }

            public String getM_topics_type() {
                return m_topics_type;
            }

            public void setM_topics_type(String m_topics_type) {
                this.m_topics_type = m_topics_type;
            }

            public Object getM_topics_shop_id() {
                return m_topics_shop_id;
            }

            public void setM_topics_shop_id(Object m_topics_shop_id) {
                this.m_topics_shop_id = m_topics_shop_id;
            }
        }

        public static class BanBean {
            @Override
            public String toString() {
                return "BanBean{" +
                        "m_topics_id='" + m_topics_id + '\'' +
                        ", m_topics_pid='" + m_topics_pid + '\'' +
                        ", m_topics_cate_id='" + m_topics_cate_id + '\'' +
                        ", m_topics_title='" + m_topics_title + '\'' +
                        ", m_topics_brief='" + m_topics_brief + '\'' +
                        ", m_topics_url='" + m_topics_url + '\'' +
                        ", m_topics_is_open='" + m_topics_is_open + '\'' +
                        ", m_topics_start_time='" + m_topics_start_time + '\'' +
                        ", m_topics_end_time='" + m_topics_end_time + '\'' +
                        ", m_topics_pic_url='" + m_topics_pic_url + '\'' +
                        ", m_topics_thumb_url='" + m_topics_thumb_url + '\'' +
                        ", m_topics_recommend='" + m_topics_recommend + '\'' +
                        ", m_topics_sort='" + m_topics_sort + '\'' +
                        ", m_topics_desc=" + m_topics_desc +
                        ", m_topics_add_time='" + m_topics_add_time + '\'' +
                        ", m_topics_numbers=" + m_topics_numbers +
                        ", m_topics_ext=" + m_topics_ext +
                        ", m_topics_goods_id='" + m_topics_goods_id + '\'' +
                        ", m_topics_type='" + m_topics_type + '\'' +
                        ", m_topics_shop_id=" + m_topics_shop_id +
                        '}';
            }

            private String m_topics_id;
            private String m_topics_pid;
            private String m_topics_cate_id;
            private String m_topics_title;
            private String m_topics_brief;
            private String m_topics_url;
            private String m_topics_is_open;
            private String m_topics_start_time;
            private String m_topics_end_time;
            private String m_topics_pic_url;
            private String m_topics_thumb_url;
            private String m_topics_recommend;
            private String m_topics_sort;
            private Object m_topics_desc;
            private String m_topics_add_time;
            private Object m_topics_numbers;
            private Object m_topics_ext;
            private String m_topics_goods_id;
            private String m_topics_type;
            private Object m_topics_shop_id;

            public String getM_topics_id() {
                return m_topics_id;
            }

            public void setM_topics_id(String m_topics_id) {
                this.m_topics_id = m_topics_id;
            }

            public String getM_topics_pid() {
                return m_topics_pid;
            }

            public void setM_topics_pid(String m_topics_pid) {
                this.m_topics_pid = m_topics_pid;
            }

            public String getM_topics_cate_id() {
                return m_topics_cate_id;
            }

            public void setM_topics_cate_id(String m_topics_cate_id) {
                this.m_topics_cate_id = m_topics_cate_id;
            }

            public String getM_topics_title() {
                return m_topics_title;
            }

            public void setM_topics_title(String m_topics_title) {
                this.m_topics_title = m_topics_title;
            }

            public String getM_topics_brief() {
                return m_topics_brief;
            }

            public void setM_topics_brief(String m_topics_brief) {
                this.m_topics_brief = m_topics_brief;
            }

            public String getM_topics_url() {
                return m_topics_url;
            }

            public void setM_topics_url(String m_topics_url) {
                this.m_topics_url = m_topics_url;
            }

            public String getM_topics_is_open() {
                return m_topics_is_open;
            }

            public void setM_topics_is_open(String m_topics_is_open) {
                this.m_topics_is_open = m_topics_is_open;
            }

            public String getM_topics_start_time() {
                return m_topics_start_time;
            }

            public void setM_topics_start_time(String m_topics_start_time) {
                this.m_topics_start_time = m_topics_start_time;
            }

            public String getM_topics_end_time() {
                return m_topics_end_time;
            }

            public void setM_topics_end_time(String m_topics_end_time) {
                this.m_topics_end_time = m_topics_end_time;
            }

            public String getM_topics_pic_url() {
                return m_topics_pic_url;
            }

            public void setM_topics_pic_url(String m_topics_pic_url) {
                this.m_topics_pic_url = m_topics_pic_url;
            }

            public String getM_topics_thumb_url() {
                return m_topics_thumb_url;
            }

            public void setM_topics_thumb_url(String m_topics_thumb_url) {
                this.m_topics_thumb_url = m_topics_thumb_url;
            }

            public String getM_topics_recommend() {
                return m_topics_recommend;
            }

            public void setM_topics_recommend(String m_topics_recommend) {
                this.m_topics_recommend = m_topics_recommend;
            }

            public String getM_topics_sort() {
                return m_topics_sort;
            }

            public void setM_topics_sort(String m_topics_sort) {
                this.m_topics_sort = m_topics_sort;
            }

            public Object getM_topics_desc() {
                return m_topics_desc;
            }

            public void setM_topics_desc(Object m_topics_desc) {
                this.m_topics_desc = m_topics_desc;
            }

            public String getM_topics_add_time() {
                return m_topics_add_time;
            }

            public void setM_topics_add_time(String m_topics_add_time) {
                this.m_topics_add_time = m_topics_add_time;
            }

            public Object getM_topics_numbers() {
                return m_topics_numbers;
            }

            public void setM_topics_numbers(Object m_topics_numbers) {
                this.m_topics_numbers = m_topics_numbers;
            }

            public Object getM_topics_ext() {
                return m_topics_ext;
            }

            public void setM_topics_ext(Object m_topics_ext) {
                this.m_topics_ext = m_topics_ext;
            }

            public String getM_topics_goods_id() {
                return m_topics_goods_id;
            }

            public void setM_topics_goods_id(String m_topics_goods_id) {
                this.m_topics_goods_id = m_topics_goods_id;
            }

            public String getM_topics_type() {
                return m_topics_type;
            }

            public void setM_topics_type(String m_topics_type) {
                this.m_topics_type = m_topics_type;
            }

            public Object getM_topics_shop_id() {
                return m_topics_shop_id;
            }

            public void setM_topics_shop_id(Object m_topics_shop_id) {
                this.m_topics_shop_id = m_topics_shop_id;
            }
        }
    }
}
