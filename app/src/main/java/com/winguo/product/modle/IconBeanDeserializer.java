package com.winguo.product.modle;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.product.modle.bean.IconBean;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/12/20.
 */

public class IconBeanDeserializer implements JsonDeserializer<IconBean> {
    @Override
    public IconBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject iconObject = jsonElement.getAsJsonObject();
        IconBean iconBean=new IconBean();
        String content = iconObject.get("content").getAsString();
        String modifyTime = iconObject.get("modifyTime").getAsString();
        iconBean.content=content;
        iconBean.modifyTime=modifyTime;
        return iconBean;

    }
}
