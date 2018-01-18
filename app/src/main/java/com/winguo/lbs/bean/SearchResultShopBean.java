package com.winguo.lbs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class SearchResultShopBean implements Serializable {

    /**
     * Code : 0
     * Result : [{"m_maker_id":"2436","m_maker_m_trade_id":"23","m_entity_maker_logo":"/group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg","m_entity_maker_average_consumption":"60.00","lng":"113.6603164673","lat":"22.9354877472","address":"广东省东莞市南环路与吉祥路交汇处大润发商城壹楼1号铺","shop_name":"康师傅私房牛肉面（厚街店）","logo":"http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg","distance":87109.53,"tradeName":"家用电器"},{"m_maker_id":"2432","m_maker_m_trade_id":"7","m_entity_maker_logo":"/group1/M00/00/12/wKgAoVlRtOmAUYf9AAAr3tJuaTE155.jpg","m_entity_maker_average_consumption":"9.00","lng":"113.4634094238","lat":"23.1147632599","address":"广州市天河区广州市天河区政务服务中心","shop_name":"","logo":"http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAoVlRtOmAUYf9AAAr3tJuaTE155.jpg","distance":64518.25,"tradeName":"居家日用"},{"m_maker_id":"2","m_maker_m_trade_id":"2","m_entity_maker_logo":"/group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg","m_entity_maker_average_consumption":"55.00","lng":"113.3651885986","lat":"23.1622142792","address":"广州市天河区羊城国际商贸中心-西塔2","shop_name":"尚水健康水会（万江店3）","logo":"http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg","distance":54714.91,"tradeName":"服装服饰"}]
     * Hasmore : 0
     * Total : 3
     */

    private String Code;
    private int Hasmore;
    private String Total;
    /**
     * m_maker_id : 2436
     * m_maker_m_trade_id : 23
     * m_entity_maker_logo : /group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg
     * m_entity_maker_average_consumption : 60.00
     * lng : 113.6603164673
     * lat : 22.9354877472
     * address : 广东省东莞市南环路与吉祥路交汇处大润发商城壹楼1号铺
     * shop_name : 康师傅私房牛肉面（厚街店）
     * logo : http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg
     * distance : 87109.53
     * tradeName : 家用电器
     */

    private List<ResultBean> Result;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public int getHasmore() {
        return Hasmore;
    }

    public void setHasmore(int Hasmore) {
        this.Hasmore = Hasmore;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }

    public List<ResultBean> getResult() {
        return Result;
    }

    public void setResult(List<ResultBean> Result) {
        this.Result = Result;
    }

    public static class ResultBean {
        private String m_maker_id;
        private String m_maker_m_trade_id;
        private String m_entity_maker_logo;
        private String m_entity_maker_average_consumption;
        private String lng;
        private String lat;
        private String address;
        private String shop_name;
        private String logo;
        private double distance;
        private String tradeName;

        public String getM_maker_id() {
            return m_maker_id;
        }

        public void setM_maker_id(String m_maker_id) {
            this.m_maker_id = m_maker_id;
        }

        public String getM_maker_m_trade_id() {
            return m_maker_m_trade_id;
        }

        public void setM_maker_m_trade_id(String m_maker_m_trade_id) {
            this.m_maker_m_trade_id = m_maker_m_trade_id;
        }

        public String getM_entity_maker_logo() {
            return m_entity_maker_logo;
        }

        public void setM_entity_maker_logo(String m_entity_maker_logo) {
            this.m_entity_maker_logo = m_entity_maker_logo;
        }

        public String getM_entity_maker_average_consumption() {
            return m_entity_maker_average_consumption;
        }

        public void setM_entity_maker_average_consumption(String m_entity_maker_average_consumption) {
            this.m_entity_maker_average_consumption = m_entity_maker_average_consumption;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getTradeName() {
            return tradeName;
        }

        public void setTradeName(String tradeName) {
            this.tradeName = tradeName;
        }
    }
}
