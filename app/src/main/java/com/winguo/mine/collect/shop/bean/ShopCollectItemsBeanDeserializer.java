package com.winguo.mine.collect.shop.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class ShopCollectItemsBeanDeserializer implements JsonDeserializer<ShopCollectItemsBean> {
    @Override
    public ShopCollectItemsBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject shopCollectItemsBeanObject = jsonElement.getAsJsonObject();
        ShopCollectItemsBean shopCollectItemsBean = new ShopCollectItemsBean();
        List<ShopCollectDataBean> list = new ArrayList<>();
        JsonElement data = shopCollectItemsBeanObject.get("data");
        if (data.isJsonObject()) {
            JsonObject dataJsonObject = data.getAsJsonObject();
            initData(dataJsonObject, list);
        } else if (data.isJsonArray()) {
            JsonArray dataJsonArray = data.getAsJsonArray();
            for (int i = 0; i < dataJsonArray.size(); i++) {
                JsonObject dataJsonObject = dataJsonArray.get(i).getAsJsonObject();
                initData(dataJsonObject, list);
            }
        }
        shopCollectItemsBean.data = list;
        return shopCollectItemsBean;
    }

    private void initData(JsonObject dataJsonObject, List<ShopCollectDataBean> list) {
        ShopCollectDataBean shopCollectDataBean = new ShopCollectDataBean();
        shopCollectDataBean.adwords = dataJsonObject.get("adwords").getAsString();
        shopCollectDataBean.id = dataJsonObject.get("id").getAsInt();
        shopCollectDataBean.item_counts = dataJsonObject.get("item_counts").getAsInt();
        JsonElement logo = dataJsonObject.get("logo");
        Logo logo2 = new Logo();
        if (logo.isJsonObject()) {
            JsonObject logoJsonObject = logo.getAsJsonObject();
            logo2.content = logoJsonObject.get("content").getAsString();
            logo2.modifyTime = logoJsonObject.get("modifyTime").getAsString();
        }
        shopCollectDataBean.logo = logo2;
        shopCollectDataBean.name = dataJsonObject.get("name").getAsString();
        shopCollectDataBean.shop_counts = dataJsonObject.get("shop_counts").getAsInt();
        list.add(shopCollectDataBean);
    }
}
