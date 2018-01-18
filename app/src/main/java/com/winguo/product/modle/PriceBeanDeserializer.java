package com.winguo.product.modle;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.product.modle.bean.PriceBean;

import java.lang.reflect.Type;

/**
 * Created by Admin on 2017/4/27.
 */

public class PriceBeanDeserializer implements JsonDeserializer<PriceBean> {

    @Override
    public PriceBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject priceObject = jsonElement.getAsJsonObject();
        PriceBean priceBean=new PriceBean();
        String regular = priceObject.get("regular").getAsString();
        String special = priceObject.get("special").getAsString();
        priceBean.regular=regular;
        priceBean.special=special;
        return priceBean;
    }
}
