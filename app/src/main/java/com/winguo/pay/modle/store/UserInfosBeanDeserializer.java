package com.winguo.pay.modle.store;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.pay.modle.bean.UserInfosBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/1/9.
 */

public class UserInfosBeanDeserializer implements JsonDeserializer<UserInfosBean> {
    @Override
    public UserInfosBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject userInfosObject = jsonElement.getAsJsonObject();
        //创建UserInfosBean对象
        UserInfosBean userInfosBean=new UserInfosBean();
        JsonElement itemElement = userInfosObject.get("item");
        //存放ItemBean的集合
        List<ItemBean> itemBeens=new ArrayList<>();
        if (itemElement.isJsonArray()){
            JsonArray itemArray = itemElement.getAsJsonArray();
            //如果是数组
            for (int i=0;i<itemArray.size();i++){
                JsonElement itemBeanElement = itemArray.get(i);
                ItemBean itemBean=jsonDeserializationContext.deserialize(itemBeanElement,ItemBean.class);
                itemBeens.add(itemBean);
            }
            userInfosBean.item=itemBeens;
        }else{
            //如果是对象
            ItemBean itemBean=jsonDeserializationContext.deserialize(itemElement,ItemBean.class);
            itemBeens.add(itemBean);
            userInfosBean.item=itemBeens;
        }

        return userInfosBean;
    }
}
