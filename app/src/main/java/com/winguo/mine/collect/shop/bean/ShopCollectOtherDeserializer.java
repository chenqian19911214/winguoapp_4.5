package com.winguo.mine.collect.shop.bean;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class ShopCollectOtherDeserializer implements JsonDeserializer<ShopCollectOtherInfoBean> {
    @Override
    public ShopCollectOtherInfoBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject shopCollectOtherObject = jsonElement.getAsJsonObject();
        ShopCollectOtherInfoBean shopCollectOtherInfoBean = new ShopCollectOtherInfoBean();
        shopCollectOtherInfoBean.count = shopCollectOtherObject.get("count").getAsInt();
        shopCollectOtherInfoBean.has_more_items = shopCollectOtherObject.get("has_more_items").getAsInt();
        return shopCollectOtherInfoBean;
    }
}
