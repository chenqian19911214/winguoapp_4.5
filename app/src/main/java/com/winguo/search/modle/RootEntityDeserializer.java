package com.winguo.search.modle;

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
 * Created by Administrator on 2016/12/17.
 */

public class RootEntityDeserializer implements JsonDeserializer<RootEntity> {
    @Override
    public RootEntity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject rootObject = jsonElement.getAsJsonObject();
        JsonElement item = rootObject.get("item");
        int has_more = rootObject.get("has_more").getAsInt();
        RootEntity rootEntity = new RootEntity();
        rootEntity.has_more = has_more;
        List<String> items = new ArrayList<>();
        if (item==null) {
            rootEntity.item = null;
        } else {
            if (item.isJsonArray()) {
                JsonArray itemArray = item.getAsJsonArray();
                for (int i = 0; i < itemArray.size(); i++) {
                    String text = itemArray.get(i).getAsString();
                    items.add(text);
                }
                rootEntity.item = items;
            } else {
                String text = item.getAsString();
                items.add(text);
                rootEntity.item = items;
            }
        }
        return rootEntity;
    }
}
