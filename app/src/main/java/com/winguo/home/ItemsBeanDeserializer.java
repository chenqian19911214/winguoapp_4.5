package com.winguo.home;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.home.bean.ItemsBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/2/9.
 */

public class ItemsBeanDeserializer implements JsonDeserializer<ItemsBean> {
    @Override
    public ItemsBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject itemsBeanObject = jsonElement.getAsJsonObject();
        ItemsBean itemsBean=new ItemsBean();
        JsonElement dataElement = itemsBeanObject.get("data");
        List<ItemsBean.DataBean> dataBeans=new ArrayList<>();
        if (dataElement.isJsonArray()){
            JsonArray dataArray = dataElement.getAsJsonArray();
            for (int i=0;i<dataArray.size();i++){

                JsonObject dataObject = dataArray.get(i).getAsJsonObject();
                dealWith(dataBeans, dataObject);
            }
            itemsBean.data=dataBeans;
        }else if (dataElement.isJsonObject()){
            JsonObject dataObject = dataElement.getAsJsonObject();
            dealWith(dataBeans,dataObject);
            itemsBean.data=dataBeans;
        }else{
            itemsBean.data=dataBeans;
        }
        return itemsBean;
    }

    private void dealWith(List<ItemsBean.DataBean> dataBeans, JsonObject dataObject) {
        ItemsBean.DataBean dataBean=new ItemsBean.DataBean();
        JsonObject imageObject = dataObject.get("image").getAsJsonObject();
        ItemsBean.DataBean.ImageBean imageBean=new ItemsBean.DataBean.ImageBean();
        String content = imageObject.get("content").getAsString();
        imageBean.content=content;
        String url = dataObject.get("url").getAsString();
        dataBean.image=imageBean;
        dataBean.url=url;
        dataBeans.add(dataBean);
    }
}
