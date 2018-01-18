package com.winguo.mine.collect.goods.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.mine.collect.shop.bean.Logo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class GoodsCollectItemsDeserializer implements JsonDeserializer<GoodsCollectItems> {
    @Override
    public GoodsCollectItems deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject GoodsCollectItemsBeanObject = jsonElement.getAsJsonObject();
        GoodsCollectItems goodsCollectItems = new GoodsCollectItems();
        List<GoodsCollectItem> list = new ArrayList<>();
            JsonElement item = GoodsCollectItemsBeanObject.get("item");
            if (item != null) {
                if (item.isJsonObject()) {
                    JsonObject dataJsonObject = item.getAsJsonObject();
                    initData(dataJsonObject, list);
                } else if (item.isJsonArray()) {
                    JsonArray dataJsonArray = item.getAsJsonArray();
                    for (int i = 0; i < dataJsonArray.size(); i++) {
                        JsonObject dataJsonObject = dataJsonArray.get(i).getAsJsonObject();
                        initData(dataJsonObject, list);
                    }
                }
            }
        goodsCollectItems.item = list;
        return goodsCollectItems;
    }

    private void initData(JsonObject dataJsonObject, List<GoodsCollectItem> list) {
        GoodsCollectItem goodsCollectItem = new GoodsCollectItem();
        goodsCollectItem.name = dataJsonObject.get("city_name").getAsString();
        goodsCollectItem.gid = dataJsonObject.get("gid").getAsInt();
        goodsCollectItem.m_item_counts = dataJsonObject.get("m_item_counts").getAsInt();
        goodsCollectItem.name = dataJsonObject.get("name").getAsString();
        goodsCollectItem.price = dataJsonObject.get("price").getAsDouble();
        goodsCollectItem.sale_qty = dataJsonObject.get("sale_qty").getAsInt();
        goodsCollectItem.stock = dataJsonObject.get("stock").getAsInt();
        goodsCollectItem.t_freight_temp_id = dataJsonObject.get("t_freight_temp_id").getAsInt();
        JsonElement logo = dataJsonObject.get("pic");
        Logo logo2 = new Logo();
        if (logo.isJsonObject()) {
            JsonObject logoJsonObject = logo.getAsJsonObject();
            String modifyTime = logoJsonObject.get("modifyTime").getAsString();
            if (!modifyTime.equals("")) {
                logo2.modifyTime = modifyTime;
                logo2.content = logoJsonObject.get("content").getAsString();
            }
        }
        goodsCollectItem.pic = logo2;
        list.add(goodsCollectItem);
    }
}
