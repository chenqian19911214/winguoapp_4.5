package com.winguo.lbs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class ShopInfoBean implements Serializable {

    private int code;
    private String message;

    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private String id;
        private String shop_address;
        private String shop_name;
        private String shop_pic;
        private String shop_thumb;
        private String business_hours;
        private String telphone;
        private String per_consumption;
        private String shop_lng;
        private String shop_lat;
        private Object distance;
        private String unit;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShop_address() {
            return shop_address;
        }

        public void setShop_address(String shop_address) {
            this.shop_address = shop_address;
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

        public String getBusiness_hours() {
            return business_hours;
        }

        public void setBusiness_hours(String business_hours) {
            this.business_hours = business_hours;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

        public String getPer_consumption() {
            return per_consumption;
        }

        public void setPer_consumption(String per_consumption) {
            this.per_consumption = per_consumption;
        }

        public String getShop_lng() {
            return shop_lng;
        }

        public void setShop_lng(String shop_lng) {
            this.shop_lng = shop_lng;
        }

        public String getShop_lat() {
            return shop_lat;
        }

        public void setShop_lat(String shop_lat) {
            this.shop_lat = shop_lat;
        }

        public Object getDistance() {
            return distance;
        }

        public void setDistance(Object distance) {
            this.distance = distance;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
