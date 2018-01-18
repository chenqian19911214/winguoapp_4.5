package com.winguo.product.modle;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.product.modle.bean.AdditionalAttributesBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public class AdditionalAttributesBeanDeserializer implements JsonDeserializer<AdditionalAttributesBean> {
    @Override
    public AdditionalAttributesBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject additionalAttributes = jsonElement.getAsJsonObject();
        JsonElement item = additionalAttributes.get("item");
        AdditionalAttributesBean additionalAttributesBean=new AdditionalAttributesBean();
        List<ItemBean> itemBeans=new ArrayList<>();
        if (item.isJsonArray()){
            JsonArray items = item.getAsJsonArray();
            for (int i=0;i<items.size();i++){
                JsonObject itemObject = items.get(i).getAsJsonObject();
                getItemBean(additionalAttributesBean, itemBeans, itemObject);
            }

        }else{
            JsonObject itemObject = item.getAsJsonObject();
            getItemBean(additionalAttributesBean, itemBeans, itemObject);
        }


        return additionalAttributesBean;
    }

    private void getItemBean(AdditionalAttributesBean additionalAttributesBean, List<ItemBean> itemBeans, JsonObject itemObject) {
        ItemBean itemBean=new ItemBean();
        String value = itemObject.get("value").getAsString();
        String label = itemObject.get("label").getAsString();
        itemBean.value=value;
        itemBean.label=label;
        itemBeans.add(itemBean);
        additionalAttributesBean.item=itemBeans;
    }
}
