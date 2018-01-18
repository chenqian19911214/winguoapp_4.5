package com.winguo.pay.modle.confirmorder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.pay.modle.bean.DataBean;
import com.winguo.pay.modle.bean.GoodsItemsBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/1/11.
 */

public class GoodsItemsBeanDeserializer implements JsonDeserializer<GoodsItemsBean> {
    @Override
    public GoodsItemsBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject goodsItemsObject = jsonElement.getAsJsonObject();
        GoodsItemsBean goodsItemsBean=new GoodsItemsBean();

        int temp_id = goodsItemsObject.get("temp_id").getAsInt();
        goodsItemsBean.temp_id=temp_id;

        JsonElement dataElement = goodsItemsObject.get("data");
        List<DataBean> dataBeans=new ArrayList<>();
        if (dataElement.isJsonArray()){
            //如果是集合
            JsonArray dataArray = dataElement.getAsJsonArray();
            for (int i=0;i<dataArray.size();i++){
                JsonElement dataBeanElement = dataArray.get(i);
                DataBean dataBean=jsonDeserializationContext.deserialize(dataBeanElement,DataBean.class);
                dataBeans.add(dataBean);
            }
            goodsItemsBean.data=dataBeans;
        }else{
            //是对象
            DataBean dataBean=jsonDeserializationContext.deserialize(dataElement,DataBean.class);
            dataBeans.add(dataBean);
            goodsItemsBean.data=dataBeans;
        }

        return goodsItemsBean;
    }
}
