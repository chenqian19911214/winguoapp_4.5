package com.winguo.pay.modle.confirmorder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.pay.modle.bean.DetailBean;
import com.winguo.pay.modle.bean.ShopInfoBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/1/11.
 */

public class DetailBeanDeserializer implements JsonDeserializer<DetailBean> {
    @Override
    public DetailBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
         DetailBean detailBean=new DetailBean();
        if (jsonElement.isJsonObject()) {
            JsonObject detailBeanObject = jsonElement.getAsJsonObject();

            JsonElement shopInfoElement = detailBeanObject.get("shopInfo");
            List<ShopInfoBean> shopInfoBeens=new ArrayList<>();
            if (shopInfoElement.isJsonArray()){
                //是个集合
                JsonArray shopInfoArray = shopInfoElement.getAsJsonArray();
                for (int i=0;i<shopInfoArray.size();i++){
                    JsonElement shopElement = shopInfoArray.get(i);
                    ShopInfoBean shopInfoBean=jsonDeserializationContext.deserialize(shopElement,ShopInfoBean.class);
                    shopInfoBeens.add(shopInfoBean);
                }
                detailBean.shopInfo=shopInfoBeens;
            }else{
                //是个对象
                ShopInfoBean shopInfoBean=jsonDeserializationContext.deserialize(shopInfoElement,ShopInfoBean.class);
                shopInfoBeens.add(shopInfoBean);
                detailBean.shopInfo=shopInfoBeens;
            }
            return detailBean;
        }
        return detailBean;
    }
}
