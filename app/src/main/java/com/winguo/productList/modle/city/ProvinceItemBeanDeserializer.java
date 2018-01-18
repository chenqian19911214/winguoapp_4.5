package com.winguo.productList.modle.city;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.productList.bean.CitiesBean;
import com.winguo.productList.bean.ProvinceItemBean;

import java.lang.reflect.Type;

/**
 * Created by Admin on 2017/1/16.
 */

public class ProvinceItemBeanDeserializer implements JsonDeserializer<ProvinceItemBean> {
    @Override
    public ProvinceItemBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject provinceItemObject = jsonElement.getAsJsonObject();
        ProvinceItemBean provinceItemBean=new ProvinceItemBean();

        JsonElement citiesElement = provinceItemObject.get("cities");
        CitiesBean citiesBean=jsonDeserializationContext.deserialize(citiesElement,CitiesBean.class);
        provinceItemBean.cities=citiesBean;
        int code = provinceItemObject.get("code").getAsInt();
        String name = provinceItemObject.get("name").getAsString();
        provinceItemBean.code=code;
        provinceItemBean.name=name;
        return provinceItemBean;
    }
}
