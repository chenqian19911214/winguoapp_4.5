package com.winguo.product.modle;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.product.modle.bean.ItemImagesBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ItemImagesBeanDeserializer implements JsonDeserializer<ItemImagesBean> {
    @Override
    public ItemImagesBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject itemImages = jsonElement.getAsJsonObject();
        ItemImagesBean itemImagesBean=new ItemImagesBean();
        JsonElement data = itemImages.get("data");
        List<DataBean> dataBeans=new ArrayList<>();
        if (data.isJsonArray()){
            JsonArray datas = data.getAsJsonArray();
            for (int i=0;i<datas.size();i++){
                JsonObject dataBean = datas.get(i).getAsJsonObject();
                getDataBean(dataBeans, dataBean);
                itemImagesBean.data=dataBeans;
            }
        }else {
            JsonObject dataBean = data.getAsJsonObject();
            getDataBean(dataBeans, dataBean);
            itemImagesBean.data=dataBeans;
        }

        return itemImagesBean;
    }

    private void getDataBean(List<DataBean> dataBeans, JsonObject dataBean) {
        DataBean dataBn=new DataBean();

        int order = dataBean.get("order").getAsInt();

        UrlBean urlBean=new UrlBean();
        JsonObject url = dataBean.get("url").getAsJsonObject();
        String content = url.get("content").getAsString();
        String modifyTime = url.get("modifyTime").getAsString();
        urlBean.content=content;
        urlBean.modifyTime=modifyTime;

        dataBn.order=order;
        dataBn.url=urlBean;
        dataBeans.add(dataBn);
    }
}
