package com.winguo.pay.modle.confirmorder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.pay.modle.bean.ShopInfoBean;
import com.winguo.pay.modle.bean.TempItemsBean;

import java.lang.reflect.Type;

/**
 * Created by Admin on 2017/1/11.
 */

public class ShopInfoDeserializer implements JsonDeserializer<ShopInfoBean> {
    @Override
    public ShopInfoBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject shopInfoObject = jsonElement.getAsJsonObject();
        ShopInfoBean shopInfoBean=new ShopInfoBean();
        JsonElement tempItemsElement = shopInfoObject.get("tempItems");
        TempItemsBean tempItemsBean=jsonDeserializationContext.deserialize(tempItemsElement,TempItemsBean.class);
        String shopName = shopInfoObject.get("shopName").getAsString();
        int shopId = shopInfoObject.get("shopId").getAsInt();
        int is_repaired = shopInfoObject.get("is_repaired").getAsInt();
        int is_returned = shopInfoObject.get("is_returned").getAsInt();
        int goodsNum = shopInfoObject.get("goodsNum").getAsInt();
        int has_invoice = shopInfoObject.get("has_invoice").getAsInt();
        double totalPrice = shopInfoObject.get("totalPrice").getAsDouble();

        shopInfoBean.tempItems=tempItemsBean;
        shopInfoBean.shopName=shopName;
        shopInfoBean.shopId=shopId;
        shopInfoBean.is_repaired=is_repaired;
        shopInfoBean.is_returned=is_returned;
        shopInfoBean.goodsNum=goodsNum;
        shopInfoBean.has_invoice=has_invoice;
        shopInfoBean.totalPrice=totalPrice;

        return shopInfoBean;
    }
}
