package com.winguo.lbs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class NearByRootBean implements Serializable {

    private int code;
    private String message;

    private ResultBean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private int total_page;
        private int totals;


        private List<StoreListsBean> store_lists;

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public int getTotals() {
            return totals;
        }

        public void setTotals(int totals) {
            this.totals = totals;
        }

        public List<StoreListsBean> getStore_lists() {
            return store_lists;
        }

        public void setStore_lists(List<StoreListsBean> store_lists) {
            this.store_lists = store_lists;
        }

        public static class StoreListsBean {
            private String business_hours;
            private String distance;
            private String id;
            private String per_consumption;
            private String shop_address;
            private String shop_lat;
            private String shop_lng;
            private String shop_name;
            private String shop_pic;
            private String shop_thumb;
            private String telphone;

            public String getBusiness_hours() {
                return business_hours;
            }

            public void setBusiness_hours(String business_hours) {
                this.business_hours = business_hours;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPer_consumption() {
                return per_consumption;
            }

            public void setPer_consumption(String per_consumption) {
                this.per_consumption = per_consumption;
            }

            public String getShop_address() {
                return shop_address;
            }

            public void setShop_address(String shop_address) {
                this.shop_address = shop_address;
            }

            public String getShop_lat() {
                return shop_lat;
            }

            public void setShop_lat(String shop_lat) {
                this.shop_lat = shop_lat;
            }

            public String getShop_lng() {
                return shop_lng;
            }

            public void setShop_lng(String shop_lng) {
                this.shop_lng = shop_lng;
            }

            public String getShop_name() {
                return shop_name;
            }

            public void setShop_name(String shop_name) {
                this.shop_name = shop_name;
            }

            public String getShop_pic() {
                return shop_pic;
            }

            public void setShop_pic(String shop_pic) {
                this.shop_pic = shop_pic;
            }

            public String getShop_thumb() {
                return shop_thumb;
            }

            public void setShop_thumb(String shop_thumb) {
                this.shop_thumb = shop_thumb;
            }

            public String getTelphone() {
                return telphone;
            }

            public void setTelphone(String telphone) {
                this.telphone = telphone;
            }
        }
    }
}
