package com.winguo.pay.modle.store;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Admin on 2017/1/9.
 */

public class ItemBeanDeserializer implements JsonDeserializer<ItemBean> {
    @Override
    public ItemBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject itemBeanObject = jsonElement.getAsJsonObject();
        ItemBean itemBean = new ItemBean();
        int addressId = itemBeanObject.get("addressId").getAsInt();
        int areaCode = itemBeanObject.get("areaCode").getAsInt();
        String areaName = itemBeanObject.get("areaName").getAsString();
        int cityCode = itemBeanObject.get("cityCode").getAsInt();
        String cityName = itemBeanObject.get("cityName").getAsString();
        String dinfo_address = itemBeanObject.get("dinfo_address").getAsString();
        String dinfo_mobile = itemBeanObject.get("dinfo_mobile").getAsString();
        String dinfo_received_name = itemBeanObject.get("dinfo_received_name").getAsString();
        String dinfo_tel = itemBeanObject.get("dinfo_tel").getAsString();
        String dinfo_zip = itemBeanObject.get("dinfo_zip").getAsString();
        int isDefaultAddress = itemBeanObject.get("isDefaultAddress").getAsInt();
        int provinceCode = itemBeanObject.get("provinceCode").getAsInt();
        String provinceName = itemBeanObject.get("provinceName").getAsString();
        int townCode = itemBeanObject.get("townCode").getAsInt();
        String townName = itemBeanObject.get("townName").getAsString();
        int is_update = itemBeanObject.get("is_update").getAsInt();
        itemBean.addressId = addressId;
        itemBean.areaCode = areaCode;
        itemBean.areaName = areaName;
        itemBean.cityCode = cityCode;
        itemBean.cityName = cityName;
        itemBean.dinfo_address = dinfo_address;
        itemBean.dinfo_mobile = dinfo_mobile;
        itemBean.dinfo_received_name = dinfo_received_name;
        itemBean.dinfo_tel = dinfo_tel;
        itemBean.dinfo_zip = dinfo_zip;
        itemBean.isDefaultAddress = isDefaultAddress;
        itemBean.provinceCode = provinceCode;
        itemBean.provinceName = provinceName;
        itemBean.townCode = townCode;
        itemBean.townName = townName;
        itemBean.is_update = is_update;
        return itemBean;
    }
}
