package com.winguo.productList.modle.classify;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.productList.bean.FiltersItemBean;
import com.winguo.productList.bean.ValuesEntity;

import java.lang.reflect.Type;

/**
 * Created by Admin on 2017/1/18.
 */

public class FiltersItemBeanDeserializer implements JsonDeserializer<FiltersItemBean> {
    @Override
    public FiltersItemBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject filtersItemObject = jsonElement.getAsJsonObject();
        FiltersItemBean filtersItemBean=new FiltersItemBean();

        String code = filtersItemObject.get("code").getAsString();
        String name = filtersItemObject.get("name").getAsString();
        JsonElement valuesElement = filtersItemObject.get("values");
        ValuesEntity valuesEntity =jsonDeserializationContext.deserialize(valuesElement,ValuesEntity.class);
        filtersItemBean.code=code;
        filtersItemBean.name=name;
        filtersItemBean.values=valuesEntity;

        return filtersItemBean;
    }
}
