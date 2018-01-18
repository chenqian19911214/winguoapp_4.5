package com.winguo.product.modle;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.product.modle.bean.LogoBean;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/12/22.
 */

public class LogoBeanDeserializer implements JsonDeserializer<LogoBean> {
    @Override
    public LogoBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject logoObject = jsonElement.getAsJsonObject();
        LogoBean logoBean=new LogoBean();
        String modifyTime = logoObject.get("modifyTime").getAsString();
        logoBean.modifyTime=modifyTime;
        JsonElement contentElement = logoObject.get("content");
        if (contentElement==null){
            logoBean.content="";
        }else{
            String content = contentElement.getAsString();
            logoBean.content=content;
        }
        return logoBean;
    }
}
