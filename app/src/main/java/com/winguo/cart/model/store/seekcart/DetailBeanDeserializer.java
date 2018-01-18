package com.winguo.cart.model.store.seekcart;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.cart.bean.DetailBean;
import com.winguo.cart.bean.GoodsItemsBean;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/3.
 */

public class DetailBeanDeserializer implements JsonDeserializer<DetailBean> {
    @Override
    public DetailBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject detailObject = jsonElement.getAsJsonObject();
        DetailBean detailBean=new DetailBean();

        JsonElement shopInfoElememt = detailObject.get("shopInfo");
        ArrayList<ShopInfoBean> shopInfoBeans=new ArrayList<>();
        if (shopInfoElememt.isJsonArray()){
            JsonArray shopInfoArray = shopInfoElememt.getAsJsonArray();
            for (int i=0;i<shopInfoArray.size();i++){
                ShopInfoBean shopInfoBean=new ShopInfoBean();
                JsonObject shopObject = shopInfoArray.get(i).getAsJsonObject();
                getShopInfo(jsonDeserializationContext, shopInfoBeans, shopInfoBean, shopObject);
            }
            detailBean.shopInfo=shopInfoBeans;
        }else{
            ShopInfoBean shopInfoBean=new ShopInfoBean();
            JsonObject shopObject = shopInfoElememt.getAsJsonObject();
            getShopInfo(jsonDeserializationContext,shopInfoBeans,shopInfoBean,shopObject);
            detailBean.shopInfo=shopInfoBeans;
        }

        return detailBean;
    }

    private void getShopInfo(JsonDeserializationContext jsonDeserializationContext, ArrayList<ShopInfoBean> shopInfoBeans, ShopInfoBean shopInfoBean, JsonObject shopObject) {
        String shopid = shopObject.get("shopId").getAsString();
        if (!TextUtils.isEmpty(shopid)) {
            int shopId = shopObject.get("shopId").getAsInt();
            String shopName = shopObject.get("shopName").getAsString();
            int goodsNum = shopObject.get("goodsNum").getAsInt();
            double totalPrice = shopObject.get("totalPrice").getAsDouble();
            int has_invoice = shopObject.get("has_invoice").getAsInt();
            int is_repaired = shopObject.get("is_repaired").getAsInt();
            int is_returned = shopObject.get("is_returned").getAsInt();
            JsonElement goodsItemsElemet = shopObject.get("goodsItems");
            GoodsItemsBean goodsItemBean = jsonDeserializationContext.deserialize(goodsItemsElemet, GoodsItemsBean.class);
            shopInfoBean.shopId=shopId;
            shopInfoBean.goodsNum=goodsNum;
            shopInfoBean.shopName=shopName;
            shopInfoBean.totalPrice=totalPrice;
            shopInfoBean.has_invoice=has_invoice;
            shopInfoBean.is_repaired=is_repaired;
            shopInfoBean.is_returned=is_returned;
            shopInfoBean.goodsItems=goodsItemBean;
            shopInfoBeans.add(shopInfoBean);
        }
    }
}
