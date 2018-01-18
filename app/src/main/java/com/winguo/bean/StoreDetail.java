package com.winguo.bean;

import java.util.List;

/**
 * 实体店详情
 */

public class StoreDetail {


    /**
     * Code : 0
     * Result : [{"m_maker_id":"2436","m_maker_address_ch":"广州天河","m_maker_logo":"","m_maker_m_trade_id":"23","m_maker_name_ch":"kevin02171","m_maker_mobile":"13800138000","m_entity_maker_average_consumption":"60.00","m_entity_maker_hour_begin":"10:00","m_entity_maker_hour_end":"21:00","m_entity_maker_logo":"/group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg","lng":"113.6603164673","lat":"22.9354877472","tradeName":"家用电器","shopView":["http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAollKYAiAAoqJAACexLKVfDw932.jpg","http://g1.imgdev.winguo.com","http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg","http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVlIfTmADloAAAAr3tJuaTE120.jpg"],"distance":4794240.1,"canView":"http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg","thumbView":""}]
     */

    private String Code;
    private List<ResultBean> Result;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public List<ResultBean> getResult() {
        return Result;
    }

    public void setResult(List<ResultBean> Result) {
        this.Result = Result;
    }

    public static class ResultBean {
        /**
         * m_maker_id : 2436
         * m_maker_address_ch : 广州天河
         * m_maker_logo :
         * m_maker_m_trade_id : 23
         * m_maker_name_ch : kevin02171
         * m_maker_mobile : 13800138000
         * m_entity_maker_average_consumption : 60.00
         * m_entity_maker_hour_begin : 10:00
         * m_entity_maker_hour_end : 21:00
         * m_entity_maker_logo : /group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg
         * lng : 113.6603164673
         * lat : 22.9354877472
         * tradeName : 家用电器
         * shopView : ["http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAollKYAiAAoqJAACexLKVfDw932.jpg","http://g1.imgdev.winguo.com","http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg","http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVlIfTmADloAAAAr3tJuaTE120.jpg"]
         * distance : 4794240.1
         * canView : http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAoVlKU8KAS9Q-AABL0P62S9w047.jpg
         * thumbView :
         */

        private String m_maker_id;
        private String m_maker_address_ch;
        private String m_maker_logo;
        private String m_maker_m_trade_id;
        private String m_maker_name_ch;
        private String m_maker_mobile;
        private String m_entity_maker_average_consumption;
        private String m_entity_maker_hour_begin;
        private String m_entity_maker_hour_end;
        private String m_entity_maker_logo;
        private String lng;
        private String lat;
        private String tradeName;
        private double distance;
        private String canView;
        private String thumbView;
        private List<String> shopView;

        public String getM_maker_id() {
            return m_maker_id;
        }

        public void setM_maker_id(String m_maker_id) {
            this.m_maker_id = m_maker_id;
        }

        public String getM_maker_address_ch() {
            return m_maker_address_ch;
        }

        public void setM_maker_address_ch(String m_maker_address_ch) {
            this.m_maker_address_ch = m_maker_address_ch;
        }

        public String getM_maker_logo() {
            return m_maker_logo;
        }

        public void setM_maker_logo(String m_maker_logo) {
            this.m_maker_logo = m_maker_logo;
        }

        public String getM_maker_m_trade_id() {
            return m_maker_m_trade_id;
        }

        public void setM_maker_m_trade_id(String m_maker_m_trade_id) {
            this.m_maker_m_trade_id = m_maker_m_trade_id;
        }

        public String getM_maker_name_ch() {
            return m_maker_name_ch;
        }

        public void setM_maker_name_ch(String m_maker_name_ch) {
            this.m_maker_name_ch = m_maker_name_ch;
        }

        public String getM_maker_mobile() {
            return m_maker_mobile;
        }

        public void setM_maker_mobile(String m_maker_mobile) {
            this.m_maker_mobile = m_maker_mobile;
        }

        public String getM_entity_maker_average_consumption() {
            return m_entity_maker_average_consumption;
        }

        public void setM_entity_maker_average_consumption(String m_entity_maker_average_consumption) {
            this.m_entity_maker_average_consumption = m_entity_maker_average_consumption;
        }

        public String getM_entity_maker_hour_begin() {
            return m_entity_maker_hour_begin;
        }

        public void setM_entity_maker_hour_begin(String m_entity_maker_hour_begin) {
            this.m_entity_maker_hour_begin = m_entity_maker_hour_begin;
        }

        public String getM_entity_maker_hour_end() {
            return m_entity_maker_hour_end;
        }

        public void setM_entity_maker_hour_end(String m_entity_maker_hour_end) {
            this.m_entity_maker_hour_end = m_entity_maker_hour_end;
        }

        public String getM_entity_maker_logo() {
            return m_entity_maker_logo;
        }

        public void setM_entity_maker_logo(String m_entity_maker_logo) {
            this.m_entity_maker_logo = m_entity_maker_logo;
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

        public String getTradeName() {
            return tradeName;
        }

        public void setTradeName(String tradeName) {
            this.tradeName = tradeName;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getCanView() {
            return canView;
        }

        public void setCanView(String canView) {
            this.canView = canView;
        }

        public String getThumbView() {
            return thumbView;
        }

        public void setThumbView(String thumbView) {
            this.thumbView = thumbView;
        }

        public List<String> getShopView() {
            return shopView;
        }

        public void setShopView(List<String> shopView) {
            this.shopView = shopView;
        }
    }
}
