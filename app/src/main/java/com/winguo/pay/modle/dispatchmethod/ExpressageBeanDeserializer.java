package com.winguo.pay.modle.dispatchmethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.pay.modle.bean.ExpressageBean;
import com.winguo.pay.modle.bean.ItemBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/1/11.
 */

public class ExpressageBeanDeserializer implements JsonDeserializer<ExpressageBean> {
    @Override
    public ExpressageBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject expressageObject = jsonElement.getAsJsonObject();
        ExpressageBean expressageBean=new ExpressageBean();

        JsonElement itemElement = expressageObject.get("item");
        List<ItemBean> itemBeans=new ArrayList<>();
        if (itemElement.isJsonArray()){
            JsonArray itemArray = itemElement.getAsJsonArray();
            for (int i=0;i<itemArray.size();i++){
                JsonElement itemBeanElement = itemArray.get(i);
                ItemBean itemBean=jsonDeserializationContext.deserialize(itemBeanElement,ItemBean.class);
                itemBeans.add(itemBean);
            }
            expressageBean.item=itemBeans;
        }else{
            //是对象
            ItemBean itemBean=jsonDeserializationContext.deserialize(itemElement,ItemBean.class);
            itemBeans.add(itemBean);
            expressageBean.item=itemBeans;
        }

        return expressageBean;
    }
}
