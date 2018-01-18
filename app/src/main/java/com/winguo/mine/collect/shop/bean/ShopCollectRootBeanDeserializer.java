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
public class ShopCollectRootBeanDeserializer implements JsonDeserializer<ShopCollectRootBean> {
    @Override
    public ShopCollectRootBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject rootJsonObject = jsonElement.getAsJsonObject();
        ShopCollectRootBean shopCollectRootBean = new ShopCollectRootBean();
            JsonElement items = rootJsonObject.get("items");
        if (items.isJsonObject()) {
            shopCollectRootBean.items = jsonDeserializationContext.deserialize(items, ShopCollectItemsBean.class);
        } else {
            shopCollectRootBean.items = null;
        }
        JsonElement otherinfo = rootJsonObject.get("otherinfo");
        shopCollectRootBean.otherinfo = jsonDeserializationContext.deserialize(otherinfo, ShopCollectOtherInfoBean.class);
        return shopCollectRootBean;
    }
}
