package com.winguo.productList.modle.classify;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.productList.bean.ValueEntity;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/12/15.
 */

public class ValueEntityDeserializer implements JsonDeserializer<ValueEntity> {
    @Override
    public ValueEntity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ValueEntity valueEntity=new ValueEntity();
        JsonObject value = jsonElement.getAsJsonObject();
        int count = value.get("count").getAsInt();
        String id = value.get("id").getAsString();
        String label = value.get("label").getAsString();
        valueEntity.count=count;
        valueEntity.id=id;
        valueEntity.label=label;
        return valueEntity;
    }
}
