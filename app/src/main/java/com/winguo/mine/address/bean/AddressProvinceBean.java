package com.winguo.mine.address.bean;

import java.util.List;

/**
 * @author hcpai
 * @desc 省
 */
public class AddressProvinceBean {

    private ProvinceBean province;

    public ProvinceBean getProvince() {
        return province;
    }

    public void setProvince(ProvinceBean province) {
        this.province = province;
    }

    public static class ProvinceBean {
        private List<AddressInfoBean> item;

        public List<AddressInfoBean> getItem() {
            return item;
        }

        public void setItem(List<AddressInfoBean> item) {
            this.item = item;
        }
    }
}
