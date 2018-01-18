package com.winguo.productList.modle;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.productList.bean.ProductsEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */

public class ProductsEntityDeserializer implements JsonDeserializer<ProductsEntity> {
    @Override
    public ProductsEntity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject productObject = jsonElement.getAsJsonObject();
        ProductsEntity productEntity=new ProductsEntity();
        JsonElement item1 = productObject.get("item");
        if (item1.isJsonArray()){
            JsonArray item =item1.getAsJsonArray();
            List<ItemEntitys> items=new ArrayList<>();
            for (int i=0;i<item.size();i++){
                JsonObject itemObject = item.get(i).getAsJsonObject();
                getItemEntitys(items, itemObject);
            }
            productEntity.item=items;
        }else{
            JsonObject itemObject = item1.getAsJsonObject();
            List<ItemEntitys> items=new ArrayList<>();
           getItemEntitys(items,itemObject);
            productEntity.item=items;
        }
        return productEntity;
    }

    private void getItemEntitys(List<ItemEntitys> items, JsonObject itemObject) {
        ItemEntitys itemEntitys=new ItemEntitys();
        IconEntity icon=new IconEntity();
        JsonElement icon1 = itemObject.get("icon");
        String modifyTime =icon1.getAsJsonObject().get("modifyTime").getAsString();
        String content =icon1.getAsJsonObject().get("content").getAsString();
        icon.modifyTime=modifyTime;
        icon.content=content;
        int shop_id = itemObject.get("shop_id").getAsInt();
        int sale_qty = itemObject.get("sale_qty").getAsInt();

        PriceEntity price=new PriceEntity();
        JsonElement price1 = itemObject.get("price");
        String special =price1.getAsJsonObject().get("special").getAsString();
        String regular = price1.getAsJsonObject().get("regular").getAsString();
        price.regular=regular;
        price.special=special;

        String cityname = itemObject.get("cityname").getAsString();
        int entity_id = itemObject.get("entity_id").getAsInt();
        String name = itemObject.get("name").getAsString();
        itemEntitys.icon=icon;
        itemEntitys.shop_id=shop_id;
        itemEntitys.entity_id=entity_id;
        itemEntitys.sale_qty=sale_qty;
        itemEntitys.cityname=cityname;
        itemEntitys.name=name;
        itemEntitys.price=price;
        items.add(itemEntitys);
    }
}
