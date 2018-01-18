package com.winguo.mine.address.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc 区
 */
public class AddressAreaBean implements Serializable {

    private AreaBean area;

    public AreaBean getArea() {
        return area;
    }

    public void setArea(AreaBean area) {
        this.area = area;
    }

    public static class AreaBean implements Serializable {

        private List<AddressInfoBean> item;

        public List<AddressInfoBean> getItem() {
            return item;
        }

        public void setItem(List<AddressInfoBean> item) {
            this.item = item;
        }
    }
}
