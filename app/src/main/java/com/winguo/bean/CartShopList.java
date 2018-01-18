package com.winguo.bean;

import java.util.List;

/**
 * Created by admin on 2017/6/27.
 */

public class CartShopList {


    /**
     * content : [{"maker":{"m_maker_id":"2145","m_maker_name_ch":"京东旗舰店","m_maker_address_ch":"体育东路122号羊城国际商贸中心西塔1303","m_maker_mobile":"13622855072"},"item":[{"id":"18","name":"宁夏特产 宁安堡宁夏枸杞 500g","price":"55.00","num":"19","img_url":"","item_id":"46554","m_sku_entity_spec":""}]},{"maker":{"m_maker_id":"2422","m_maker_name_ch":"家宜乐","m_maker_address_ch":"","m_maker_mobile":"13416290389"},"item":[{"id":"19","name":"规格","price":"200.00","num":"1","img_url":"http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAollJ2s6AaXfiAAAxGah4Mwo44..jpg","item_id":"85903","m_sku_entity_spec":"杨海龙规格5"},{"id":"20","name":"测试数据","price":"2.00","num":"1","img_url":"","item_id":"85902","m_sku_entity_spec":"100"}]}]
     * size : 2
     * price : 257
     */

    private int size;
    private String price;
    private List<ContentBean> content;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * maker : {"m_maker_id":"2145","m_maker_name_ch":"京东旗舰店","m_maker_address_ch":"体育东路122号羊城国际商贸中心西塔1303","m_maker_mobile":"13622855072"}
         * item : [{"id":"18","name":"宁夏特产 宁安堡宁夏枸杞 500g","price":"55.00","num":"19","img_url":"","item_id":"46554","m_sku_entity_spec":""}]
         */

        private MakerBean maker;
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
            /**
             * m_maker_id : 2145
             * m_maker_name_ch : 京东旗舰店
             * m_maker_address_ch : 体育东路122号羊城国际商贸中心西塔1303
             * m_maker_mobile : 13622855072
             */

            private String m_maker_id;
            private String m_maker_name_ch;
            private String m_maker_address_ch;
            private String m_maker_mobile;

            public String getM_maker_id() {
                return m_maker_id;
            }

            public void setM_maker_id(String m_maker_id) {
                this.m_maker_id = m_maker_id;
            }

            public String getM_maker_name_ch() {
                return m_maker_name_ch;
            }

            public void setM_maker_name_ch(String m_maker_name_ch) {
                this.m_maker_name_ch = m_maker_name_ch;
            }

            public String getM_maker_address_ch() {
                return m_maker_address_ch;
            }

            public void setM_maker_address_ch(String m_maker_address_ch) {
                this.m_maker_address_ch = m_maker_address_ch;
            }

            public String getM_maker_mobile() {
                return m_maker_mobile;
            }

            public void setM_maker_mobile(String m_maker_mobile) {
                this.m_maker_mobile = m_maker_mobile;
            }
        }

        public static class ItemBean {
            /**
             * id : 18
             * name : 宁夏特产 宁安堡宁夏枸杞 500g
             * price : 55.00
             * num : 19
             * img_url :
             * item_id : 46554
             * m_sku_entity_spec :
             */

            private String id;
            private String name;
            private String num_price;
            private String price;
            private String num;
            private String img_url;
            private String item_id;
            private String m_sku_entity_spec;

            public String getNum_price() {
                return num_price;
            }

            public void setNum_price(String num_price) {
                this.num_price = num_price;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getItem_id() {
                return item_id;
            }

            public void setItem_id(String item_id) {
                this.item_id = item_id;
            }

            public String getM_sku_entity_spec() {
                return m_sku_entity_spec;
            }

            public void setM_sku_entity_spec(String m_sku_entity_spec) {
                this.m_sku_entity_spec = m_sku_entity_spec;
            }
        }
    }
}
