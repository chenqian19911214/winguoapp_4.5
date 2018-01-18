package com.winguo.pay.modle.confirmorder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.pay.modle.bean.DataBean;

import java.lang.reflect.Type;

/**
 * Created by Admin on 2017/1/11.
 */

public class DataBeanDeserializer implements JsonDeserializer<DataBean> {
    @Override
    public DataBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject dataBeanObject = jsonElement.getAsJsonObject();
        DataBean dataBean = new DataBean();

        JsonObject iconObject= dataBeanObject.get("icon").getAsJsonObject();
        DataBean.IconBean iconBean = new DataBean.IconBean();
        String modifyTime = iconObject.get("modifyTime").getAsString();
       /* if (TextUtils.isEmpty(modifyTime)) {
            iconBean.content = "";
        }else{
            String content = iconObject.get("content").getAsString();
            iconBean.content = content;
        }*/
        String content = iconObject.get("content").getAsString();
        iconBean.content = content;

        iconBean.modifyTime = modifyTime;

        int largess_qty = dataBeanObject.get("largess_qty").getAsInt();
        double cost_price = dataBeanObject.get("cost_price").getAsDouble();
        String color_name = dataBeanObject.get("color_name").getAsString();
        int distributor_id = dataBeanObject.get("distributor_id").getAsInt();
        String color_alias = dataBeanObject.get("color_alias").getAsString();
        int skuid = dataBeanObject.get("skuid").getAsInt();
        String discount = dataBeanObject.get("discount").getAsString();
        String size = dataBeanObject.get("size").getAsString();
        int num = dataBeanObject.get("num").getAsInt();
        int stock = dataBeanObject.get("stock").getAsInt();
        double price = dataBeanObject.get("price").getAsDouble();
        double cash_coupon = dataBeanObject.get("cash_coupon").getAsDouble();
        String color = dataBeanObject.get("color").getAsString();
        String discount_type = dataBeanObject.get("discount_type").getAsString();
        int item_id = dataBeanObject.get("item_id").getAsInt();
        String name = dataBeanObject.get("name").getAsString();
        String size_alias = dataBeanObject.get("size_alias").getAsString();
        String share_rate = dataBeanObject.get("share_rate").getAsString();

        dataBean.icon = iconBean;
        dataBean.largess_qty = largess_qty;
        dataBean.cost_price = cost_price;
        dataBean.color_name = color_name;
        dataBean.distributor_id = distributor_id;
        dataBean.color_alias = color_alias;
        dataBean.skuid = skuid;
        dataBean.discount = discount;
        dataBean.size = size;
        dataBean.num = num;
        dataBean.cash_coupon = cash_coupon;
        dataBean.stock = stock;
        dataBean.price = price;
        dataBean.color = color;
        dataBean.discount_type = discount_type;
        dataBean.item_id = item_id;
        dataBean.name = name;
        dataBean.size_alias = size_alias;
        dataBean.share_rate = share_rate;

        return dataBean;
    }
}
