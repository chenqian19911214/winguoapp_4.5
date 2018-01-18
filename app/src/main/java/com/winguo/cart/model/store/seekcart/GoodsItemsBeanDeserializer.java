package com.winguo.cart.model.store.seekcart;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.cart.bean.DataBean;
import com.winguo.cart.bean.GoodsItemsBean;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/3.
 */

public class GoodsItemsBeanDeserializer implements JsonDeserializer<GoodsItemsBean> {
    @Override
    public GoodsItemsBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject goodsItemsObject = jsonElement.getAsJsonObject();
        GoodsItemsBean goodsItemsBean=new GoodsItemsBean();
        JsonElement dataElement = goodsItemsObject.get("data");
        ArrayList<DataBean> dataBeens=new ArrayList<>();
        if (dataElement.isJsonArray()){
            JsonArray dataBeanArray = dataElement.getAsJsonArray();
            for (int i=0;i<dataBeanArray.size();i++){
                JsonElement dataBeanElement = dataBeanArray.get(i);
                DataBean dataBean=jsonDeserializationContext.deserialize(dataBeanElement,DataBean.class);
                dataBeens.add(dataBean);
            }
            goodsItemsBean.data=dataBeens;

        }else{
            DataBean dataBean=jsonDeserializationContext.deserialize(dataElement,DataBean.class);
            dataBeens.add(dataBean);
            goodsItemsBean.data=dataBeens;
        }

        return goodsItemsBean;
    }
}
