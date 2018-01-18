package com.winguo.pay.modle.store;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.pay.modle.bean.AddressInfoBean;
import com.winguo.pay.modle.bean.UserInfosBean;

import java.lang.reflect.Type;

/**
 * Created by Admin on 2017/1/9.
 */

public class AddressInfoBeanDeserializer implements JsonDeserializer<AddressInfoBean> {
    @Override
    public AddressInfoBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject addressInfoObject = jsonElement.getAsJsonObject();
        AddressInfoBean addressInfoBean=new AddressInfoBean();
        JsonElement userInfosElement = addressInfoObject.get("userInfos");
        if (userInfosElement.isJsonObject()){
            //如果是对象
            UserInfosBean userInfosBean=jsonDeserializationContext.deserialize(userInfosElement,UserInfosBean.class);
            addressInfoBean.userInfos=userInfosBean;
        }else{
            //如果是空
            addressInfoBean.userInfos=null;
        }

        return addressInfoBean;
    }
}
