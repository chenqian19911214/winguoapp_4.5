package com.winguo.productList.modle;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.productList.bean.ProductsEntity;
import com.winguo.productList.bean.SearchEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */

public class SearchEntityDeserializer implements JsonDeserializer<SearchEntity> {
    @Override
    public SearchEntity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject search = jsonElement.getAsJsonObject();
        SearchEntity searchEntity=new SearchEntity();
        JsonArray count = search.get("count").getAsJsonArray();
        List<Integer> counts=new ArrayList<>();
        for (int i=0;i<count.size();i++){
            counts.add(count.get(i).getAsInt());
        }
        JsonArray has_more = search.get("has_more_items").getAsJsonArray();
        List<Integer> has_more_items=new ArrayList<>();
        for (int i=0;i<has_more.size();i++){
            has_more_items.add(has_more.get(i).getAsInt());
        }
        searchEntity.count=counts;
        searchEntity.has_more_items=has_more_items;
        JsonElement products = search.get("products");
        if (products.isJsonObject()){
            ProductsEntity productsEntity = jsonDeserializationContext.deserialize(products, ProductsEntity.class);
            searchEntity.products=productsEntity;
        }else{
            ProductsEntity productsEntity =null;
            searchEntity.products=productsEntity;
        }


        return searchEntity;
    }
}
