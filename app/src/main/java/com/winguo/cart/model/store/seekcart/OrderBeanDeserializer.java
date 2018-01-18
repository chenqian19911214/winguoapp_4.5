package com.winguo.cart.model.store.seekcart;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.cart.bean.DetailBean;
import com.winguo.cart.bean.OrderBean;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/1/3.
 */

public class OrderBeanDeserializer implements JsonDeserializer<OrderBean> {
    @Override
    public OrderBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject orderBeanObject = jsonElement.getAsJsonObject();
        OrderBean orderBean=new OrderBean();

        int max_largess_qty = orderBeanObject.get("max_largess_qty").getAsInt();
        int goodsNum = orderBeanObject.get("goodsNum").getAsInt();
        double totalprice = orderBeanObject.get("totalprice").getAsDouble();
        int pay_method = orderBeanObject.get("pay_method").getAsInt();
        JsonElement detailElement = orderBeanObject.get("detail");
        if (detailElement.isJsonObject()){
            DetailBean detailBean = jsonDeserializationContext.deserialize(detailElement, DetailBean.class);
            orderBean.detail=detailBean;
        }else{
            DetailBean detailBean =null;
            orderBean.detail=detailBean;
        }
        orderBean.goodsNum=goodsNum;
        orderBean.max_largess_qty=max_largess_qty;
        orderBean.pay_method=pay_method;
        orderBean.totalprice=totalprice;

        return orderBean;
    }
}
