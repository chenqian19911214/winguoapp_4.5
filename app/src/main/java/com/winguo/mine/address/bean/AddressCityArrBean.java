package com.winguo.mine.address.bean;

import java.util.List;

/**
 * @author hcpai
 * @desc 市
 */
public class AddressCityArrBean {

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

        private List<AddressInfoBean> item;

        public List<AddressInfoBean> getItem() {
            return item;
        }

        public void setItem(List<AddressInfoBean> item) {
            this.item = item;
        }

    }
}
