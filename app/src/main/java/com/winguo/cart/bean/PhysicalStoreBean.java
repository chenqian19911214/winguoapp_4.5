package com.winguo.cart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/6/26.
 */

public class PhysicalStoreBean implements Serializable {

    /**
     * content : [{"maker":{"m_maker_id":"2145","m_maker_name_ch":"京东旗舰店","m_maker_address_ch":"体育东路122号羊城国际商贸中心西塔1303","m_maker_mobile":"13622855072"},"item":[{"id":"18","name":"宁夏特产 宁安堡宁夏枸杞 500g","price":"55.00","num":"3","img_url":"","item_id":"46554","m_sku_entity_spec":""}]},{"maker":{"m_maker_id":"2422","m_maker_name_ch":"家宜乐","m_maker_address_ch":"","m_maker_mobile":"13416290389"},"item":[{"id":"19","name":"规格","price":"200.00","num":"1","img_url":"http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAollJ2s6AaXfiAAAxGah4Mwo44..jpg","item_id":"85903","m_sku_entity_spec":"杨海龙规格5"},{"id":"20","name":"测试数据","price":"2.00","num":"1","img_url":"","item_id":"85902","m_sku_entity_spec":"100"}]}]
     * size : 2
     * price : 257
     * code:"202"
     */
    public String code;//返回码
    public int size;//有几条数据
    public String price;//总价
    /**
     * maker : {"m_maker_id":"2145","m_maker_name_ch":"京东旗舰店","m_maker_address_ch":"体育东路122号羊城国际商贸中心西塔1303","m_maker_mobile":"13622855072"}
     * item : [{"id":"18","name":"宁夏特产 宁安堡宁夏枸杞 500g","price":"55.00","num":"3","img_url":"","item_id":"46554","m_sku_entity_spec":""}]
     */

    public List<ContentBean> content;

    @Override
    public String toString() {
        return "PhysicalStoreBean{" +
                "code=" + code +
                "size=" + size +
                ", price=" + price +
                ", content=" + content +
                '}';
    }

    public  class ContentBean implements Serializable {
        /**
         * m_maker_id : 2145
         * m_maker_name_ch : 京东旗舰店
         * m_maker_address_ch : 体育东路122号羊城国际商贸中心西塔1303
         * m_maker_mobile : 13622855072
         */

        public MakerBean maker;
        /**
         * id : 18
         * name : 宁夏特产 宁安堡宁夏枸杞 500g
         * price : 55.00
         * num : 3
         * img_url :
         * item_id : 46554
         * m_sku_entity_spec :
         */

        public List<ItemBean> item;

        @Override
        public String toString() {
            return "ContentBean{" +
                    "maker=" + maker +
                    ", item=" + item +
                    '}';
        }

        public  class MakerBean implements Serializable{
            public String m_maker_id;//店铺ID
            public String m_maker_name_ch;//店铺名称
            public String m_maker_address_ch;//店铺地址
            public String m_maker_mobile;//店铺电话
            protected boolean isChoosed;//是否被选中

            @Override
            public String toString() {
                return "MakerBean{" +
                        "m_maker_id='" + m_maker_id + '\'' +
                        ", m_maker_name_ch='" + m_maker_name_ch + '\'' +
                        ", m_maker_address_ch='" + m_maker_address_ch + '\'' +
                        ", m_maker_mobile='" + m_maker_mobile + '\'' +
                        ", isChoosed='" + isChoosed + '\'' +
                        '}';
            }
            public boolean isChoosed()
            {
                return isChoosed;
            }

            public void setChoosed(boolean isChoosed)
            {
                this.isChoosed = isChoosed;
            }
        }

        public  class ItemBean implements Serializable{
            public String id;//购物车ID
            public String name;//商品名称
            public String price;//商品单价
            public String num_price;//商品合计
            public String num;//商品数量
            public String img_url;//商品图片
            public String item_id;//商品ID
            public String m_sku_entity_spec;//商品规格
            protected boolean isChoosed;//是否被选中
            public boolean isChoosed()
            {
                return isChoosed;
            }

            public void setChoosed(boolean isChoosed)
            {
                this.isChoosed = isChoosed;
            }
            @Override
            public String toString() {
                return "ItemBean{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", price='" + price + '\'' +
                        ", num='" + num + '\'' +
                        ", img_url='" + img_url + '\'' +
                        ", item_id='" + item_id + '\'' +
                        ", m_sku_entity_spec='" + m_sku_entity_spec + '\'' +
                        ", num_price='" + num_price + '\'' +
                        ", isChoosed='" + isChoosed + '\'' +
                        '}';
            }
        }
    }
}
