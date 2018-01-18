package com.winguo.productList.modle.classify;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.productList.bean.FiltersBean;
import com.winguo.productList.bean.FiltersItemBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/1/18.
 */

public class FiltersBeanDeserializer implements JsonDeserializer<FiltersBean> {
    @Override
    public FiltersBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject filtersBeanObject =jsonElement.getAsJsonObject();
        FiltersBean filtersBean=new FiltersBean();
        JsonElement filtersItemBeanElement = filtersBeanObject.get("item");
        List<FiltersItemBean> filtersItemBeen=new ArrayList<>();
        if (filtersItemBeanElement.isJsonArray()){
            JsonArray filtersItemArray = filtersItemBeanElement.getAsJsonArray();
            for (int i=0;i<filtersItemArray.size();i++){
                JsonElement filtersItemElement = filtersItemArray.get(i);
                FiltersItemBean filtersItemBean=jsonDeserializationContext.deserialize(filtersItemElement,FiltersItemBean.class);
                filtersItemBeen.add(filtersItemBean);

            }
            filtersBean.item=filtersItemBeen;
        }else{
            FiltersItemBean filtersItemBean=jsonDeserializationContext.deserialize(filtersItemBeanElement,FiltersItemBean.class);
            filtersItemBeen.add(filtersItemBean);
            filtersBean.item=filtersItemBeen;
        }

        return filtersBean;
    }
}
