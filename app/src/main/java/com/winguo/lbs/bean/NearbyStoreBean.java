package com.winguo.lbs.bean;

import java.io.Serializable;
import java.util.List;

/**
 *  附近实体店列表
 */

public class NearbyStoreBean implements Serializable{


    /**
     * totals : 20
     * total_page : 4
     * store_lists : [{"id":"23","shop_id":"2649","shop_name":"","lng":"113.3387756348","lat":"23.1400547028","geohash":"uxbryxfpfrgxbrbpfrfz","address":"广州市天河区丽枫酒店(广州太古汇广场石牌桥地铁站店)","addtime":"1499408113","type":"0","distance":"462.23m","storeDetail":[{"tradeName":"酒店预订","m_maker_id":"2649","m_maker_name_ch":"丽枫酒店","m_maker_logo":"","m_maker_m_trade_id":"55","m_entity_maker_logo":"/group1/M00/05/1F/wKgAUVle-sOAHEzPAAEvJz71Lrw336.jpg","m_entity_maker_average_consumption":"428.00","real_name":"丽枫酒店"}]},{"id":"12","shop_id":"2638","shop_name":"","lng":"113.3398590088","lat":"23.1427555084","geohash":"uxbryxupcrbzfpzpuxbr","address":"广东广州市天河区城区天河区天河东路155号骏源大厦附楼","addtime":"1499336494","type":"0","distance":"547.54m","storeDetail":[]},{"id":"18","shop_id":"2642","shop_name":"","lng":"113.3393325806","lat":"23.1387996674","geohash":"uxbryxcpvpgzczuxzxyr","address":"广东广州市天河区暨南大学天河路240号丰兴广场BC座1楼","addtime":"1499392789","type":"0","distance":"579.6m","storeDetail":[]},{"id":"19","shop_id":"2645","shop_name":"","lng":"113.3409957886","lat":"23.1398696899","geohash":"uxbryxczypczvrzxvrbz","address":"广东广州市天河区城区天河东路168号石牌酒店3楼","addtime":"1499393649","type":"0","distance":"682.46m","storeDetail":[]},{"id":"22","shop_id":"2648","shop_name":"","lng":"113.3286209106","lat":"23.1384639740","geohash":"uxbryxbzvrczgpypczyp","address":"广州市天河区粤海喜来登酒店","addtime":"1499396553","type":"0","distance":"712.11m","storeDetail":[{"tradeName":"酒店预订","m_maker_id":"2648","m_maker_name_ch":"粤海喜来登酒店","m_maker_logo":"","m_maker_m_trade_id":"55","m_entity_maker_logo":"/group1/M00/05/1E/wKgAUVle-PWAMkpnAADmtAZ9M9s326.jpg","m_entity_maker_average_consumption":"928.00","real_name":"粤海喜来登酒店"}]}]
     */

    private int totals;
    private int total_page;


    private String buy_flag;
    private List<StoreListsBean> store_lists;

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }
    public String getBuy_flag() {
        return buy_flag;
    }

    public void setBuy_flag(String buy_flag) {
        this.buy_flag = buy_flag;
    }

    public List<StoreListsBean> getStore_lists() {
        return store_lists;
    }

    public void setStore_lists(List<StoreListsBean> store_lists) {
        this.store_lists = store_lists;
    }

    public static class StoreListsBean {
        /**
         * id : 23
         * shop_id : 2649
         * shop_name :
         * lng : 113.3387756348
         * lat : 23.1400547028
         * geohash : uxbryxfpfrgxbrbpfrfz
         * address : 广州市天河区丽枫酒店(广州太古汇广场石牌桥地铁站店)
         * addtime : 1499408113
         * type : 0
         * distance : 462.23m
         * storeDetail : [{"tradeName":"酒店预订","m_maker_id":"2649","m_maker_name_ch":"丽枫酒店","m_maker_logo":"","m_maker_m_trade_id":"55","m_entity_maker_logo":"/group1/M00/05/1F/wKgAUVle-sOAHEzPAAEvJz71Lrw336.jpg","m_entity_maker_average_consumption":"428.00","real_name":"丽枫酒店"}]
         */

        private String id;
        private String shop_id;
        private String shop_name;
        private String lng;
        private String lat;
        private String geohash;
        private String address;
        private String addtime;
        private String type;
        private String distance;
        private List<StoreDetailBean> storeDetail;
        //爬虫数据新添
        private String consumption;
        private String logo;

        public String getConsumption() {
            return consumption;
        }

        public void setConsumption(String consumption) {
            this.consumption = consumption;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
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

        public String getGeohash() {
            return geohash;
        }

        public void setGeohash(String geohash) {
            this.geohash = geohash;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public List<StoreDetailBean> getStoreDetail() {
            return storeDetail;
        }

        public void setStoreDetail(List<StoreDetailBean> storeDetail) {
            this.storeDetail = storeDetail;
        }

        public static class StoreDetailBean {
            /**
             * tradeName : 酒店预订
             * m_maker_id : 2649
             * m_maker_name_ch : 丽枫酒店
             * m_maker_logo :
             * m_maker_m_trade_id : 55
             * m_entity_maker_logo : /group1/M00/05/1F/wKgAUVle-sOAHEzPAAEvJz71Lrw336.jpg
             * m_entity_maker_average_consumption : 428.00
             * real_name : 丽枫酒店
             */

            private String tradeName;
            private String m_maker_id;
            private String m_maker_name_ch;
            private String m_maker_logo;
            private String m_maker_m_trade_id;
            private String m_entity_maker_logo;
            private String m_entity_maker_average_consumption;


            public String getTradeName() {
                return tradeName;
            }

            public void setTradeName(String tradeName) {
                this.tradeName = tradeName;
            }

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

        }
    }
}
