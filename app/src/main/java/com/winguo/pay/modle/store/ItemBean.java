package com.winguo.pay.modle.store;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/9.
 */

public class ItemBean implements Serializable {
    public int addressId;//收货人地址项id
    public int areaCode;//地区代码(如天河区)
    public String areaName;//地区名
    public int cityCode;//城市代码
    public String cityName;//城市名
    public String dinfo_address;//收货人地址
    public String dinfo_mobile;//收货人手机
    public String dinfo_received_name;//收货人名称
    public String dinfo_tel;//收货人电话
    public String dinfo_zip;//收货人邮编
    public int isDefaultAddress;//是否是默认地址
    public int provinceCode;//省份代码
    public String provinceName;//省份名
    public int townCode;//镇代码
    public String townName;//镇名
    public int is_update; //地址是否升级

    @Override
    public String toString() {
        return "ItemBean{" +
                "addressId=" + addressId +
                ", areaCode=" + areaCode +
                ", areaName='" + areaName + '\'' +
                ", cityCode=" + cityCode +
                ", cityName='" + cityName + '\'' +
                ", dinfo_address='" + dinfo_address + '\'' +
                ", dinfo_mobile='" + dinfo_mobile + '\'' +
                ", dinfo_received_name='" + dinfo_received_name + '\'' +
                ", dinfo_tel='" + dinfo_tel + '\'' +
                ", dinfo_zip=" + dinfo_zip +
                ", isDefaultAddress=" + isDefaultAddress +
                ", provinceCode=" + provinceCode +
                ", provinceName='" + provinceName + '\'' +
                ", townCode=" + townCode +
                ", townName='" + townName + '\'' +
                ", is_update=" + is_update +
                '}';
    }
}
