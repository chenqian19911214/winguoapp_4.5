package com.winguo.cart.model.store.seekcart;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.cart.bean.DataBean;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/1/3.
 */

public class DataBeanDeserializer implements JsonDeserializer<DataBean> {
    @Override
    public DataBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject dataBeanObject = jsonElement.getAsJsonObject();
        DataBean dataBean=new DataBean();

        int skuid = dataBeanObject.get("skuid").getAsInt();
        JsonObject iconObject = dataBeanObject.get("icon").getAsJsonObject();
        IconBean iconBean=new IconBean();
        String modifyTime = iconObject.get("modifyTime").getAsString();
        JsonElement contentElemet = iconObject.get("content");
        String content ="";
        if (contentElemet!=null){
             content = iconObject.get("content").getAsString();
        }
        iconBean.modifyTime=modifyTime;
        iconBean.content=content;

        int distributor_id = dataBeanObject.get("distributor_id").getAsInt();
        String share_rate = dataBeanObject.get("share_rate").getAsString();
        String name = dataBeanObject.get("name").getAsString();
        int item_id = dataBeanObject.get("item_id").getAsInt();
        String size = dataBeanObject.get("size").getAsString();
        String color = dataBeanObject.get("color").getAsString();
        String color_name = dataBeanObject.get("color_name").getAsString();
        int num = dataBeanObject.get("num").getAsInt();
        int largess_qty = dataBeanObject.get("largess_qty").getAsInt();
        double price = dataBeanObject.get("price").getAsDouble();
        double cost_price = dataBeanObject.get("cost_price").getAsDouble();
        int stock = dataBeanObject.get("stock").getAsInt();
        String limit_buy = dataBeanObject.get("limit_buy").getAsString();
        String discount = dataBeanObject.get("discount").getAsString();
        String discount_type = dataBeanObject.get("discount_type").getAsString();

        dataBean.skuid=skuid;
        dataBean.icon=iconBean;
        dataBean.distributor_id=distributor_id;
        dataBean.share_rate=share_rate;
        dataBean.name=name;
        dataBean.item_id=item_id;
        dataBean.size=size;
        dataBean.color=color;
        dataBean.color_name=color_name;
        dataBean.num=num;
        dataBean.largess_qty=largess_qty;
        dataBean.price=price;
        dataBean.cost_price=cost_price;
        dataBean.stock=stock;
        dataBean.limit_buy=limit_buy;
        dataBean.discount=discount;
        dataBean.discount_type=discount_type;
        return dataBean;
    }
}
