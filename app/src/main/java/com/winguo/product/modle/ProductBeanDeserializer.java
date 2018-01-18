package com.winguo.product.modle;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.winguo.product.modle.bean.AdditionalAttributesBean;
import com.winguo.product.modle.bean.IconBean;
import com.winguo.product.modle.bean.ItemImagesBean;
import com.winguo.product.modle.bean.ItemSkuBean;
import com.winguo.product.modle.bean.PriceBean;
import com.winguo.product.modle.bean.ProductBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ProductBeanDeserializer implements JsonDeserializer<ProductBean> {
    @Override
    public ProductBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject productObject = jsonElement.getAsJsonObject();
        ProductBean productBean = new ProductBean();

        String temp_name = productObject.get("temp_name").getAsString();
        double original_price = productObject.get("original_price").getAsDouble();
        int is_salable = productObject.get("is_salable").getAsInt();
        int is_collect = productObject.get("is_collect").getAsInt();
        int stock = productObject.get("stock").getAsInt();
        int is_seckill = productObject.get("is_seckill").getAsInt();
        String share_price = productObject.get("share_price").getAsString();
        String description = productObject.get("description").getAsString();
        int shared = productObject.get("shared").getAsInt();
        String wap_flag = productObject.get("wap_flag").getAsString();
        String shop_id = productObject.get("shop_id").getAsString();
        String provinceid = productObject.get("provinceid").getAsString();

        JsonElement price = productObject.get("price");
        PriceBean priceBean = jsonDeserializationContext.deserialize(price, PriceBean.class);
        int rating_summary = productObject.get("rating_summary").getAsInt();
        String sale_min_price = productObject.get("sale_min_price").getAsString();
        int reviews_count = productObject.get("reviews_count").getAsInt();
        int has_gallery = productObject.get("has_gallery").getAsInt();
        String entity_type = productObject.get("entity_type").getAsString();
        String is_shared = productObject.get("is_shared").getAsString();
        int sale_qty = productObject.get("sale_qty").getAsInt();
        int min_sale_qty = productObject.get("min_sale_qty").getAsInt();
        int collect_counts = productObject.get("collect_counts").getAsInt();
        int in_stock = productObject.get("in_stock").getAsInt();
        String link = productObject.get("link").getAsString();
        String color_alias = productObject.get("color_alias").getAsString();
        int recommended = productObject.get("recommended").getAsInt();

        JsonElement price_intervalElement = productObject.get("price_interval");
        PriceIntervalBean priceIntervalBean = new PriceIntervalBean();
        if (price_intervalElement.isJsonObject()) {
            JsonObject price_intervalObject = price_intervalElement.getAsJsonObject();
            JsonElement innerDataElement = price_intervalObject.get("data");
            List<InnerDataBean> innerDataBeans=new ArrayList<>();

            if (innerDataElement.isJsonArray()) {
                JsonArray innerDataArray = innerDataElement.getAsJsonArray();
                for (int j = 0; j < innerDataArray.size(); j++) {
                    JsonObject innerDataObject = innerDataArray.get(j).getAsJsonObject();
                    setInnerData(innerDataBeans, innerDataObject);
                }
                priceIntervalBean.data=innerDataBeans;
            }
            if(innerDataElement.isJsonObject()){
                JsonObject innerDataObject = innerDataElement.getAsJsonObject();
                setInnerData(innerDataBeans, innerDataObject);
                priceIntervalBean.data=innerDataBeans;
            }
        }else{
            priceIntervalBean=null;
        }

        String entity_id = productObject.get("entity_id").getAsString();
        String provincename = productObject.get("provincename").getAsString();

        JsonElement item_images = productObject.get("item_images");
        ItemImagesBean itemImages;
        if (item_images.isJsonObject()) {
             itemImages = jsonDeserializationContext.deserialize(item_images, ItemImagesBean.class);
        }else{
            itemImages=null;
        }
        String is_winguo_largess = productObject.get("is_winguo_largess").getAsString();
        String short_description = productObject.get("short_description").getAsString();
        String name = productObject.get("name").getAsString();
        String size_alias = productObject.get("size_alias").getAsString();
        int sku_id = productObject.get("sku_id").getAsInt();

        JsonElement icon = productObject.get("icon");
        IconBean iconBean = jsonDeserializationContext.deserialize(icon, IconBean.class);
        int is_time_discount = productObject.get("is_time_discount").getAsInt();
        int has_options = productObject.get("has_options").getAsInt();
        int has_size = productObject.get("has_size").getAsInt();

        JsonElement additional_attributes = productObject.get("additional_attributes");
        AdditionalAttributesBean additionalAttributesBean = new AdditionalAttributesBean();
        if (additional_attributes.isJsonObject()) {
            additionalAttributesBean = jsonDeserializationContext.deserialize(additional_attributes, AdditionalAttributesBean.class);
        } else {//没有数据
            additionalAttributesBean = null;
        }
        String cityid = productObject.get("cityid").getAsString();
        int original_min_price = productObject.get("original_min_price").getAsInt();
        String shop_name = productObject.get("shop_name").getAsString();

        JsonElement item_sku = productObject.get("item_sku");
        ItemSkuBean itemSkuBean = jsonDeserializationContext.deserialize(item_sku, ItemSkuBean.class);
        String fare_type = productObject.get("fare_type").getAsString();
        String rec = productObject.get("rec").getAsString();
        int pay_method = productObject.get("pay_method").getAsInt();
        String freight = productObject.get("freight").getAsString();
        String cityname = productObject.get("cityname").getAsString();
        String out_id = productObject.get("out_id").getAsString();
        String shop_url = productObject.get("shop_url").getAsString();

        productBean.temp_name = temp_name;
        productBean.wap_flag = wap_flag;
        productBean.additional_attributes = additionalAttributesBean;
        productBean.cityid = cityid;
        productBean.cityname = cityname;
        productBean.collect_counts = collect_counts;
        productBean.color_alias = color_alias;
        productBean.description = description;
        productBean.entity_id = entity_id;
        productBean.entity_type = entity_type;
        productBean.fare_type = fare_type;
        productBean.freight = freight;
        productBean.has_gallery = has_gallery;
        productBean.has_options = has_options;
        productBean.has_size = has_size;
        productBean.icon = iconBean;
        productBean.in_stock = in_stock;
        productBean.is_collect = is_collect;
        productBean.is_salable = is_salable;
        productBean.is_seckill = is_seckill;
        productBean.is_shared = is_shared;
        productBean.is_time_discount = is_time_discount;
        productBean.is_winguo_largess = is_winguo_largess;
        productBean.item_images = itemImages;

        productBean.item_sku = itemSkuBean;
        productBean.link = link;
        productBean.min_sale_qty = min_sale_qty;
        productBean.name = name;
        productBean.original_min_price = original_min_price;
        productBean.original_price = original_price;
        productBean.out_id = out_id;
        productBean.pay_method = pay_method;
        productBean.price = priceBean;
        productBean.price_interval = priceIntervalBean;
        productBean.provinceid = provinceid;
        productBean.provincename = provincename;
        productBean.rating_summary = rating_summary;
        productBean.rec = rec;
        productBean.recommended = recommended;
        productBean.reviews_count = reviews_count;
        productBean.sale_min_price = sale_min_price;
        productBean.sale_qty = sale_qty;
        productBean.share_price = share_price;
        productBean.shared = shared;
        productBean.shop_id = shop_id;
        productBean.shop_name = shop_name;
        productBean.shop_url = shop_url;
        productBean.short_description = short_description;
        productBean.size_alias = size_alias;
        productBean.sku_id = sku_id;
        productBean.stock = stock;

        return productBean;
    }

    private void setInnerData(List<InnerDataBean> innerDataBeans, JsonObject innerDataObject) {
        InnerDataBean innerDataBean = new InnerDataBean();
        String qty = innerDataObject.get("qty").getAsString();
        String rate = innerDataObject.get("rate").getAsString();
        innerDataBean.qty=qty;
        innerDataBean.rate=rate;
        innerDataBeans.add(innerDataBean);
    }
}
