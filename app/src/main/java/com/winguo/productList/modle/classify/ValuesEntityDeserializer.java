package com.winguo.productList.modle.classify;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.productList.bean.ValueEntity;
import com.winguo.productList.bean.ValuesEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */

public class ValuesEntityDeserializer implements JsonDeserializer<ValuesEntity> {
    @Override
    public ValuesEntity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ValuesEntity valuesEntity=new ValuesEntity();
        JsonObject values = jsonElement.getAsJsonObject();
        JsonElement value = values.get("value");
        List<ValueEntity> valueEntities=new ArrayList<>();
        if (value.isJsonArray()){
            JsonArray valueArray = value.getAsJsonArray();
            for (int i=0;i<valueArray.size();i++){
                JsonElement valueElement = valueArray.get(i);
                ValueEntity valueEnt = jsonDeserializationContext.deserialize(valueElement, ValueEntity.class);
                valueEntities.add(valueEnt);
            }
            valuesEntity.value=valueEntities;
        }else{
            ValueEntity valueEnt = jsonDeserializationContext.deserialize(value, ValueEntity.class);
            valueEntities.add(valueEnt);
            valuesEntity.value=valueEntities;
        }


        return valuesEntity;
    }
}
