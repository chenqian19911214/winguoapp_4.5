package com.winguo.productList.modle.city;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.productList.bean.CitiesBean;
import com.winguo.productList.bean.CityItemBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/1/16.
 */

public class CitiesBeanDeserializer implements JsonDeserializer<CitiesBean> {
    @Override
    public CitiesBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject citiesBeanObject = jsonElement.getAsJsonObject();
        CitiesBean citiesBean=new CitiesBean();
        JsonElement itemElement = citiesBeanObject.get("item");
        List<CityItemBean> cityItemBeens=new ArrayList<>();
        if (itemElement.isJsonArray()){
            JsonArray itemArray = itemElement.getAsJsonArray();
            for (int i=0;i<itemArray.size();i++){
                JsonObject itemObject = itemArray.get(i).getAsJsonObject();
                getCityItemBean(cityItemBeens, itemObject);
            }
            citiesBean.item=cityItemBeens;
        }else{
            JsonObject itemObject = itemElement.getAsJsonObject();
            getCityItemBean(cityItemBeens,itemObject);
            citiesBean.item=cityItemBeens;
        }

        return citiesBean;
    }

    private void getCityItemBean(List<CityItemBean> cityItemBeens, JsonObject itemObject) {
        CityItemBean cityItemBean=new CityItemBean();
        int code = itemObject.get("code").getAsInt();
        String name = itemObject.get("name").getAsString();
        cityItemBean.code=code;
        cityItemBean.name=name;
        cityItemBeens.add(cityItemBean);
    }
}
