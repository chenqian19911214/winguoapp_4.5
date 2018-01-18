package com.winguo.bean;

import java.io.Serializable;
import java.util.List;

/**
 *实体店商品
 */

public class StoreShop {


    /**
     * Code : 0
     * resLen : 2
     * Result : [{"m_item_name":"实体店商品65","m_item_list_img":"","m_item_recommend":"0","m_item_max_price":"100.00","m_item_min_price":"30.00","m_item_description":"aaaa\"\"\"\"","m_entity_item_m_item_id":"85899","sku":[{"m_sku_id":"86293","m_sku_stock":"100","m_sku_entity_spec":"大份acx","m_sku_price":"100.00"},{"m_sku_id":"86296","m_sku_stock":"100","m_sku_entity_spec":"中等","m_sku_price":"30.00"}],"itemthumb":"http://g1.imgdev.winguo.com"},{"m_item_name":"实体店商品65","m_item_list_img":"/group1/M00/00/10/wKgAoVkxDVqAWNP_AAAs6dWbuTg23..jpg","m_item_recommend":"0","m_item_max_price":"100.00","m_item_min_price":"10.00","m_item_description":"sdfg","m_entity_item_m_item_id":"85898","sku":[{"m_sku_id":"86282","m_sku_stock":"10000","m_sku_entity_spec":"中等份量加辣","m_sku_price":"100.00"},{"m_sku_id":"86283","m_sku_stock":"10000","m_sku_entity_spec":"大份","m_sku_price":"50.00"},{"m_sku_id":"86301","m_sku_stock":"10000","m_sku_entity_spec":"10op","m_sku_price":"10.00"}],"itemthumb":"http://g1.imgdev.winguo.com/group1/M00/00/10/wKgAoVkxDVqAWNP_AAAs6dWbuTg23..jpg"}]
     * Recommend : [{"m_item_name":"测试数据","m_item_list_img":"","m_item_recommend":"1","m_item_max_price":"2.00","m_item_min_price":"2.00","m_item_description":"这是条测试数据","m_entity_item_m_item_id":"85902","sku":[{"m_sku_id":"86307","m_sku_stock":"10000","m_sku_entity_spec":"1","m_sku_price":"2.00"}],"itemthumb":"http://g1.imgdev.winguo.com"}]
     * Hasmore : 1
     */

    private String Code;
    private int resLen;
    private String Hasmore;
    private List<ResultBean> Result;
    private List<ResultBean> Recommend;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public int getResLen() {
        return resLen;
    }

    public void setResLen(int resLen) {
        this.resLen = resLen;
    }

    public String getHasmore() {
        return Hasmore;
    }

    public void setHasmore(String Hasmore) {
        this.Hasmore = Hasmore;
    }

    public List<ResultBean> getResult() {
        return Result;
    }

    public void setResult(List<ResultBean> Result) {
        this.Result = Result;
    }

    public List<ResultBean> getRecommend() {
        return Recommend;
    }

    public void setRecommend(List<ResultBean> Recommend) {
        this.Recommend = Recommend;
    }

    public static class ResultBean implements Serializable {
        /**
         * m_item_name : 实体店商品65
         * m_item_list_img :
         * m_item_recommend : 0
         * m_item_max_price : 100.00
         * m_item_min_price : 30.00
         * m_item_description : aaaa""""
         * m_entity_item_m_item_id : 85899
         * sku : [{"m_sku_id":"86293","m_sku_stock":"100","m_sku_entity_spec":"大份acx","m_sku_price":"100.00"},{"m_sku_id":"86296","m_sku_stock":"100","m_sku_entity_spec":"中等","m_sku_price":"30.00"}]
         * itemthumb : http://g1.imgdev.winguo.com
         */

        private String m_item_name;
        private String m_item_list_img;
        private String m_item_recommend;
        private String m_item_max_price;
        private String m_item_min_price;
        private String m_item_description;
        private String m_entity_item_m_item_id;
        private String itemthumb;
        private List<SkuBean> sku;

        public String getM_item_name() {
            return m_item_name;
        }

        public void setM_item_name(String m_item_name) {
            this.m_item_name = m_item_name;
        }

        public String getM_item_list_img() {
            return m_item_list_img;
        }

        public void setM_item_list_img(String m_item_list_img) {
            this.m_item_list_img = m_item_list_img;
        }

        public String getM_item_recommend() {
            return m_item_recommend;
        }

        public void setM_item_recommend(String m_item_recommend) {
            this.m_item_recommend = m_item_recommend;
        }

        public String getM_item_max_price() {
            return m_item_max_price;
        }

        public void setM_item_max_price(String m_item_max_price) {
            this.m_item_max_price = m_item_max_price;
        }

        public String getM_item_min_price() {
            return m_item_min_price;
        }

        public void setM_item_min_price(String m_item_min_price) {
            this.m_item_min_price = m_item_min_price;
        }

        public String getM_item_description() {
            return m_item_description;
        }

        public void setM_item_description(String m_item_description) {
            this.m_item_description = m_item_description;
        }

        public String getM_entity_item_m_item_id() {
            return m_entity_item_m_item_id;
        }

        public void setM_entity_item_m_item_id(String m_entity_item_m_item_id) {
            this.m_entity_item_m_item_id = m_entity_item_m_item_id;
        }

        public String getItemthumb() {
            return itemthumb;
        }

        public void setItemthumb(String itemthumb) {
            this.itemthumb = itemthumb;
        }

        public List<SkuBean> getSku() {
            return sku;
        }

        public void setSku(List<SkuBean> sku) {
            this.sku = sku;
        }

        public static class SkuBean implements Serializable{
            /**
             * m_sku_id : 86293
             * m_sku_stock : 100
             * m_sku_entity_spec : 大份acx
             * m_sku_price : 100.00
             */

            private String m_sku_id;
            private String m_sku_stock;
            private String m_sku_entity_spec;
            private String m_sku_price;

            public String getM_sku_id() {
                return m_sku_id;
            }

            public void setM_sku_id(String m_sku_id) {
                this.m_sku_id = m_sku_id;
            }

            public String getM_sku_stock() {
                return m_sku_stock;
            }

            public void setM_sku_stock(String m_sku_stock) {
                this.m_sku_stock = m_sku_stock;
            }

            public String getM_sku_entity_spec() {
                return m_sku_entity_spec;
            }

            public void setM_sku_entity_spec(String m_sku_entity_spec) {
                this.m_sku_entity_spec = m_sku_entity_spec;
            }

            public String getM_sku_price() {
                return m_sku_price;
            }

            public void setM_sku_price(String m_sku_price) {
                this.m_sku_price = m_sku_price;
            }
        }
    }

}
