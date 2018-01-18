package com.winguo.pay.modle.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/6/29.
 */

public class PhysicalConfirmOderBean implements Serializable {
    /**
     * code : 1
     * message : 成功
     * result : {"list":[{"item":[{"m_entity_cart_qty":"4","m_item_id":"46554","m_item_list_img":"","m_item_min_price":"55.00","m_item_name":"宁夏特产 宁安堡宁夏枸杞 500g","m_sku_entity_spec":"","m_sku_id":"46550"}],"shop":{"m_maker_address_ch":"体育东路122号羊城国际商贸中心西塔1303","m_maker_id":"2145","m_maker_mobile":"13622855072","m_maker_name_ch":"京东旗舰店"}},{"item":[{"m_entity_cart_qty":"1","m_item_id":"85903","m_item_list_img":"http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAollJ2s6AaXfiAAAxGah4Mwo44..jpg","m_item_min_price":"200.00","m_item_name":"规格","m_sku_entity_spec":"杨海龙规格5","m_sku_id":"86310"},{"m_entity_cart_qty":"1","m_item_id":"85902","m_item_list_img":"","m_item_min_price":"2.00","m_item_name":"测试数据","m_sku_entity_spec":"100","m_sku_id":"86311"}],"shop":{"m_maker_address_ch":"","m_maker_id":"2422","m_maker_mobile":"13416290389","m_maker_name_ch":"家宜乐"}}],"total_price":422,"total_qty":6,"user":{"m_customer_id":"39361","m_customer_name":"","m_customer_tel_mobi":"13416290389"}}
     */
    public int code;
    public String message;
    /**
     * list : [{"item":[{"m_entity_cart_qty":"4","m_item_id":"46554","m_item_list_img":"","m_item_min_price":"55.00","m_item_name":"宁夏特产 宁安堡宁夏枸杞 500g","m_sku_entity_spec":"","m_sku_id":"46550"}],"shop":{"m_maker_address_ch":"体育东路122号羊城国际商贸中心西塔1303","m_maker_id":"2145","m_maker_mobile":"13622855072","m_maker_name_ch":"京东旗舰店"}},{"item":[{"m_entity_cart_qty":"1","m_item_id":"85903","m_item_list_img":"http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAollJ2s6AaXfiAAAxGah4Mwo44..jpg","m_item_min_price":"200.00","m_item_name":"规格","m_sku_entity_spec":"杨海龙规格5","m_sku_id":"86310"},{"m_entity_cart_qty":"1","m_item_id":"85902","m_item_list_img":"","m_item_min_price":"2.00","m_item_name":"测试数据","m_sku_entity_spec":"100","m_sku_id":"86311"}],"shop":{"m_maker_address_ch":"","m_maker_id":"2422","m_maker_mobile":"13416290389","m_maker_name_ch":"家宜乐"}}]
     * total_price : 422
     * total_qty : 6
     * user : {"m_customer_id":"39361","m_customer_name":"","m_customer_tel_mobi":"13416290389"}
     */

    public ResultBean result;

    public  class ResultBean implements Serializable{
        public String total_price;
        public int total_qty;
        /**
         * m_customer_id : 39361
         * m_customer_name :
         * m_customer_tel_mobi : 13416290389
         */

        public UserBean user;
        /**
         * item : [{"m_entity_cart_qty":"4","m_item_id":"46554","m_item_list_img":"","m_item_min_price":"55.00","m_item_name":"宁夏特产 宁安堡宁夏枸杞 500g","m_sku_entity_spec":"","m_sku_id":"46550"}]
         * shop : {"m_maker_address_ch":"体育东路122号羊城国际商贸中心西塔1303","m_maker_id":"2145","m_maker_mobile":"13622855072","m_maker_name_ch":"京东旗舰店"}
         */

        public List<ListBean> list;

        public  class UserBean implements Serializable{
            public String m_customer_id;
            public String m_customer_name;
            public String m_customer_tel_mobi;
        }

        public  class ListBean implements Serializable{
            /**
             * m_maker_address_ch : 体育东路122号羊城国际商贸中心西塔1303
             * m_maker_id : 2145
             * m_maker_mobile : 13622855072
             * m_maker_name_ch : 京东旗舰店
             */

            public ShopBean shop;
            /**
             * m_entity_cart_qty : 4
             * m_item_id : 46554
             * m_item_list_img :
             * m_item_min_price : 55.00
             * m_item_name : 宁夏特产 宁安堡宁夏枸杞 500g
             * m_sku_entity_spec :
             * m_sku_id : 46550
             */

            public List<ItemBean> item;

            public  class ShopBean implements Serializable{
                public String m_maker_address_ch;
                public String m_maker_id;
                public String m_maker_mobile;
                public String m_maker_name_ch;
            }

            public  class ItemBean implements Serializable{
                public String m_entity_cart_qty;
                public String m_item_id;
                public String m_item_list_img;
                public String m_sku_price;
                public String m_item_name;
                public String m_sku_entity_spec;
                public String m_sku_id;
            }
        }
    }
}
