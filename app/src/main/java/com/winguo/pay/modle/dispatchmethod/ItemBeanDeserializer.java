package com.winguo.pay.modle.dispatchmethod;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.pay.modle.bean.ItemBean;

import java.lang.reflect.Type;

/**
 * Created by Admin on 2017/1/11.
 */

public class ItemBeanDeserializer implements JsonDeserializer<ItemBean> {
    @Override
    public ItemBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject itemObject = jsonElement.getAsJsonObject();
        ItemBean itemBean=new ItemBean();

        double price = itemObject.get("price").getAsDouble();
        String name = itemObject.get("name").getAsString();
        int code = itemObject.get("code").getAsInt();
        itemBean.price =price;
        itemBean.name=name;
        itemBean.code=code;
        return itemBean;
    }
}
