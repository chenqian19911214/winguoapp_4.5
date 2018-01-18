package com.winguo.product.modle;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.product.modle.bean.ItemSkuBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ItemSkuBeanDeserializer implements JsonDeserializer<ItemSkuBean> {
    @Override
    public ItemSkuBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject itemSkuObject = jsonElement.getAsJsonObject();
        ItemSkuBean itemSkuBean=new ItemSkuBean();
        //title的数据
        JsonObject title = itemSkuObject.get("title").getAsJsonObject();
        TitleBean titleBean=new TitleBean();
        String id = title.get("id").getAsString();
        String color_name = title.get("color_name").getAsString();

        JsonArray nameArray = title.get("name").getAsJsonArray();
        List<String> names=new ArrayList<>();
        for (int i=0;i<nameArray.size();i++){
            String name = nameArray.get(i).getAsString();
            names.add(name);

        }
        titleBean.id=id;
        titleBean.color_name=color_name;
        titleBean.name=names;
        //item的数据
        JsonElement itemElement = itemSkuObject.get("item");
        Item item=new Item();
        if (itemElement.isJsonObject()) {
            JsonObject itemObject = itemSkuObject.get("item").getAsJsonObject();
            JsonElement dataElement = itemObject.get("data");
            List<Data> datas = new ArrayList<>();
            if (dataElement.isJsonArray()) {
                JsonArray dataArray = dataElement.getAsJsonArray();
                for (int i = 0; i < dataArray.size(); i++) {
                    JsonObject dataObject = dataArray.get(i).getAsJsonObject();
                    Data data = new Data();
                    double original_price = dataObject.get("original_price").getAsDouble();
                    JsonArray valueArray = dataObject.get("value").getAsJsonArray();
                    List<String> values = new ArrayList<>();
                    for (int j = 0; j < valueArray.size(); j++) {
                        String value = valueArray.get(j).getAsString();
                        values.add(value);
                    }
                    data.original_price = original_price;
                    data.value = values;
                    datas.add(data);
                }
                item.data = datas;
            } else {
                JsonObject dataObject = dataElement.getAsJsonObject();
                Data data = new Data();
                double original_price = dataObject.get("original_price").getAsDouble();
                JsonArray valueArray = dataObject.get("value").getAsJsonArray();
                List<String> values = new ArrayList<>();
                for (int j = 0; j < valueArray.size(); j++) {
                    String value = valueArray.get(j).getAsString();
                    values.add(value);
                }
                data.original_price = original_price;
                data.value = values;
                datas.add(data);
                item.data = datas;
            }
        }else{
            item=null;
        }
        itemSkuBean.title=titleBean;
        itemSkuBean.item=item;
        return itemSkuBean;
    }
}
