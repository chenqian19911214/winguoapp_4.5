package com.winguo.lbs.order.list;

import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class StoreOrderBean {


    /**
     * content : [{"maker":{"id":"1475","m_maker_id":"2145","logo_url":"","m_maker_name_ch":"京东旗舰店","t_juchu_payment_status":"1","t_juchu_pick_code":"3702","t_juchu_price":"500.00","time":"2017-06-20 17:52:34","m_merge_order_no":"M201706200000009195","m_customer_tel_mobi":"13800138000","t_juchu_delivery_received_name":"测试账号1231","count":1},"item":[{"img_url":"","name":"ZIPPO打火机野狼28001","price":"125.00","num":"4.00"}]}]
     * has_more : 0
     * size : 1
     */

    private int has_more;
    private int size;
    /**
     * maker : {"id":"1475","m_maker_id":"2145","logo_url":"","m_maker_name_ch":"京东旗舰店","t_juchu_payment_status":"1","t_juchu_pick_code":"3702","t_juchu_price":"500.00","time":"2017-06-20 17:52:34","m_merge_order_no":"M201706200000009195","m_customer_tel_mobi":"13800138000","t_juchu_delivery_received_name":"测试账号1231","count":1}
     * item : [{"img_url":"","name":"ZIPPO打火机野狼28001","price":"125.00","num":"4.00"}]
     */

    private List<ContentBean> content;

    public int getHas_more() {
        return has_more;
    }

    public void setHas_more(int has_more) {
        this.has_more = has_more;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * id : 1475
         * m_maker_id : 2145
         * logo_url :
         * m_maker_name_ch : 京东旗舰店
         * t_juchu_payment_status : 1
         * t_juchu_pick_code : 3702
         * t_juchu_price : 500.00
         * time : 2017-06-20 17:52:34
         * m_merge_order_no : M201706200000009195
         * m_customer_tel_mobi : 13800138000
         * t_juchu_delivery_received_name : 测试账号1231
         * count : 1
         */

        private MakerBean maker;
        /**
         * img_url :
         * name : ZIPPO打火机野狼28001
         * price : 125.00
         * num : 4.00
         */

        private List<ItemBean> item;

        public MakerBean getMaker() {
            return maker;
        }

        public void setMaker(MakerBean maker) {
            this.maker = maker;
        }

        public List<ItemBean> getItem() {
            return item;
        }

        public void setItem(List<ItemBean> item) {
            this.item = item;
        }

        public static class MakerBean {
            private String id;
            private String m_maker_id;
            private String logo_url;
            private String m_maker_name_ch;
            private String t_juchu_payment_status;
            private String t_juchu_pick_code;
            private String t_juchu_price;
            private String time;
            private String m_merge_order_no;
            private String m_customer_tel_mobi;
            private String t_juchu_delivery_received_name;
            private int count;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getM_maker_id() {
                return m_maker_id;
            }

            public void setM_maker_id(String m_maker_id) {
                this.m_maker_id = m_maker_id;
            }

            public String getLogo_url() {
                return logo_url;
            }

            public void setLogo_url(String logo_url) {
                this.logo_url = logo_url;
            }

            public String getM_maker_name_ch() {
                return m_maker_name_ch;
            }

            public void setM_maker_name_ch(String m_maker_name_ch) {
                this.m_maker_name_ch = m_maker_name_ch;
            }

            public String getT_juchu_payment_status() {
                return t_juchu_payment_status;
            }

            public void setT_juchu_payment_status(String t_juchu_payment_status) {
                this.t_juchu_payment_status = t_juchu_payment_status;
            }

            public String getT_juchu_pick_code() {
                return t_juchu_pick_code;
            }

            public void setT_juchu_pick_code(String t_juchu_pick_code) {
                this.t_juchu_pick_code = t_juchu_pick_code;
            }

            public String getT_juchu_price() {
                return t_juchu_price;
            }

            public void setT_juchu_price(String t_juchu_price) {
                this.t_juchu_price = t_juchu_price;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getM_merge_order_no() {
                return m_merge_order_no;
            }

            public void setM_merge_order_no(String m_merge_order_no) {
                this.m_merge_order_no = m_merge_order_no;
            }

            public String getM_customer_tel_mobi() {
                return m_customer_tel_mobi;
            }

            public void setM_customer_tel_mobi(String m_customer_tel_mobi) {
                this.m_customer_tel_mobi = m_customer_tel_mobi;
            }

            public String getT_juchu_delivery_received_name() {
                return t_juchu_delivery_received_name;
            }

            public void setT_juchu_delivery_received_name(String t_juchu_delivery_received_name) {
                this.t_juchu_delivery_received_name = t_juchu_delivery_received_name;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }

        public static class ItemBean {
            private String img_url;
            private String name;
            private String price;
            private String num;

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }
        }
    }
}
