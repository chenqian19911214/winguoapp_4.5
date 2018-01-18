package com.winguo.pay.modle.confirmorder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.pay.modle.bean.GoodsItemsBean;
import com.winguo.pay.modle.bean.TempItemsBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/1/11.
 */

public class TempItemsBeanDeserializer implements JsonDeserializer<TempItemsBean> {
    @Override
    public TempItemsBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject tempItemsObject = jsonElement.getAsJsonObject();
        TempItemsBean tempItemsBean=new TempItemsBean();
        List<GoodsItemsBean> goodsItemsBeen=new ArrayList<>();
        JsonElement goodsItemsElement = tempItemsObject.get("goodsItems");
        if (goodsItemsElement.isJsonArray()){
            JsonArray goodsItemArray = goodsItemsElement.getAsJsonArray();
            for (int i=0;i<goodsItemArray.size();i++){
                GoodsItemsBean goodsItemsBean=jsonDeserializationContext.deserialize(goodsItemArray.get(i),GoodsItemsBean.class);
                goodsItemsBeen.add(goodsItemsBean);
            }
        }else if (goodsItemsElement.isJsonObject()){
            GoodsItemsBean goodsItemsBean=jsonDeserializationContext.deserialize(goodsItemsElement,GoodsItemsBean.class);
            goodsItemsBeen.add(goodsItemsBean);
        }
        tempItemsBean.goodsItems=goodsItemsBeen;
        return tempItemsBean;
    }
}
