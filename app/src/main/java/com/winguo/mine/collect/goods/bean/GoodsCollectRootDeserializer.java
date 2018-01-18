package com.winguo.mine.collect.goods.bean;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class GoodsCollectRootDeserializer implements JsonDeserializer<GoodsCollectRootBean> {
    @Override
    public GoodsCollectRootBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject rootJsonObject = jsonElement.getAsJsonObject();
        GoodsCollectRootBean goodsCollectRoot = new GoodsCollectRootBean();
        goodsCollectRoot.count = rootJsonObject.get("count").getAsInt();
        goodsCollectRoot.has_more = rootJsonObject.get("has_more").getAsInt();
        JsonElement items = rootJsonObject.get("items");
        try {
            goodsCollectRoot.items = jsonDeserializationContext.deserialize(items, GoodsCollectItems.class);
        } catch (JsonSyntaxException e) {
            goodsCollectRoot.items = new GoodsCollectItems();
        }
        return goodsCollectRoot;
    }
}
