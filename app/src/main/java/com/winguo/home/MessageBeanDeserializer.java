package com.winguo.home;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.home.bean.ItemsBean;
import com.winguo.home.bean.MessageBean;

import java.lang.reflect.Type;

/**
 * Created by admin on 2017/2/23.
 */

public class MessageBeanDeserializer implements JsonDeserializer<MessageBean> {

    @Override
    public MessageBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject messageObject = jsonElement.getAsJsonObject();
        MessageBean messageBean = new MessageBean();
        JsonElement itemsElement = messageObject.get("items");
        ItemsBean itemsBean;
        if (itemsElement.isJsonObject()) {
             itemsBean = jsonDeserializationContext.deserialize(itemsElement, ItemsBean.class);
        } else {
            itemsBean = null;
        }
        messageBean.items = itemsBean;
        return messageBean;

    }
}
