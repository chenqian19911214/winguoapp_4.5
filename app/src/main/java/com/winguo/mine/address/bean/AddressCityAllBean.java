package com.winguo.mine.address.bean;

/**
 * @author hcpai
 * @desc 市
 */
public class AddressCityAllBean {
    private AddressCityObjBean addressCityObjBean;
    private AddressCityArrBean addressCityArrBean;
    private boolean isObj;

    @Override
    public String toString() {
        return "AddressCityAllBean{" +
                "addressCityObjBean=" + addressCityObjBean +
                ", addressCityArrBean=" + addressCityArrBean +
                ", isObj=" + isObj +
                '}';
    }

    public AddressCityObjBean getAddressCityObjBean() {
        return addressCityObjBean;
    }

    public void setAddressCityObjBean(AddressCityObjBean addressCityObjBean) {
        this.addressCityObjBean = addressCityObjBean;
    }

    public AddressCityArrBean getAddressCityArrBean() {
        return addressCityArrBean;
    }

    public void setAddressCityArrBean(AddressCityArrBean addressCityArrBean) {
        this.addressCityArrBean = addressCityArrBean;
    }

    public boolean isObj() {
        return isObj;
    }

    public void setObj(boolean obj) {
        isObj = obj;
    }
}
