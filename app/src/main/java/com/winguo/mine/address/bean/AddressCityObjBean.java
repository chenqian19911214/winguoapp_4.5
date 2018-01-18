package com.winguo.mine.address.bean;

/**
 * @author hcpai
 * @desc 市
 */
public class AddressCityObjBean {

    private CityBean city;

    public CityBean getCity() {
        return city;
    }

    public void setCity(CityBean city) {
        this.city = city;
    }

    public static class CityBean {
        /**
         * code : 1601
         * name : 广州市
         */

        private AddressInfoBean item;

        public AddressInfoBean getItem() {
            return item;
        }

        public void setItem(AddressInfoBean item) {
            this.item = item;
        }
    }
}
