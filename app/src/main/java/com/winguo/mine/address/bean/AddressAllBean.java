package com.winguo.mine.address.bean;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc 封装省市区
 */
public class AddressAllBean implements Serializable {
    private AddressInfoBean province;
    private AddressInfoBean city;
    private AddressInfoBean area;
    private AddressInfoBean town;
    /**
     * 县的bena,因为有些市是没有县的(新疆省-博尔塔拉蒙古自治州阿拉山口口岸)
     */
    private AddressAreaBean areaBean;
    /**
     * 镇的bean,因为有些是没有镇的
     */
    private AddressTownBean townBean;

    @Override
    public String toString() {
        return "AddressAllBean{" +
                "province=" + province +
                ", city=" + city +
                ", area=" + area +
                ", town=" + town +
                ", areaBean=" + areaBean +
                ", townBean=" + townBean +
                '}';
    }

    public AddressInfoBean getProvince() {
        return province;
    }

    public void setProvince(AddressInfoBean province) {
        this.province = province;
    }

    public AddressInfoBean getCity() {
        return city;
    }

    public void setCity(AddressInfoBean city) {
        this.city = city;
    }

    public AddressInfoBean getArea() {
        return area;
    }

    public void setArea(AddressInfoBean area) {
        this.area = area;
    }

    public AddressInfoBean getTown() {
        return town;
    }

    public void setTown(AddressInfoBean town) {
        this.town = town;
    }

    public AddressAreaBean getAreaBean() {
        return areaBean;
    }

    public void setAreaBean(AddressAreaBean areaBean) {
        this.areaBean = areaBean;
    }

    public AddressTownBean getTownBean() {
        return townBean;
    }

    public void setTownBean(AddressTownBean townBean) {
        this.townBean = townBean;
    }
}
