package com.winguo.product.modle.bean;

import com.winguo.product.modle.PriceIntervalBean;
import com.winguo.product.modle.bean.AdditionalAttributesBean;
import com.winguo.product.modle.bean.IconBean;
import com.winguo.product.modle.bean.ItemImagesBean;
import com.winguo.product.modle.bean.ItemSkuBean;
import com.winguo.product.modle.bean.PriceBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */

public  class ProductBean implements Serializable {

    public String temp_name;//快递0.00元,详细页商品运费展示
    public double original_price;//原价
    public int is_salable;//
    public int is_collect;//是否收藏 1,收藏 0,没收藏
    public int stock;//尺码颜色属性的商品就读取这个库存数
    public int is_seckill;//1为秒杀 0为普通商品
    public String share_price;//分享价格
    public String description;//描述
    public int shared;//分享
    public String wap_flag;//
    public String shop_id;//商铺id
    public String provinceid;//省份id
    /**
     * regular : 150
     * special :
     */

    public PriceBean price;//价格 //无尺码颜色属性的商品就读取这个价格
    public int rating_summary;//商品评分
    public String sale_min_price;
    public int reviews_count;//评论数
    public int has_gallery;
    public String entity_type;//商品类型
    public String is_shared;//是否分享
    public int sale_qty;//销量
    public int min_sale_qty;
    public int collect_counts;//收藏量
    public int in_stock;//是否有库存1.有库存，0.无库存
    public String link;//链接
    public String color_alias;
    public int recommended;
    public PriceIntervalBean price_interval;//价格区间
    public String entity_id;//商品id
    public String provincename;//省
    /**
     * data : {"order":0,"url":{"content":"http://g1.img.winguo.com/group1/M00/4E/3F/wKgAi1hKYASAZX0kAAdDAg-KNo803..jpg","modifyTime":1481269252}}
     */
    public ItemImagesBean item_images;//图片
    public String is_winguo_largess;
    public String short_description;//简短描述
    public String name;//名字
    public String size_alias;
    public int sku_id;
    /**
     * content : http://g1.img.winguo.com/group1/M00/4E/3F/wKgAi1hKYASAZX0kAAdDAg-KNo803..jpg
     * modifyTime : 1481269252
     */
    public IconBean icon;//图片
    public int is_time_discount;//是否限时折扣
    public long time_discount_left;//限时时间
    public int time_qty_limit;//限购数
    public int has_options;
    public int has_size;//是否有尺码颜色属性：0、没有，1.有
    /**
     * item : {"value":"智高（ZHIGAO）","label":"品牌"}
     */

    public AdditionalAttributesBean additional_attributes;//商品附件属性
    public String cityid;
    public int original_min_price;
    public String shop_name;//店铺名
    /**
     * title : {"id":"sku id","color_name":"颜色名称","name":["颜色","尺码","单价","重量","数量","剩余数量","颜色图片","原价"]}
     * item : {"data":{"value":[89461,"","自定义属性1",150,0,1000,"",1000,""],"original_price":150}}
     */

    public ItemSkuBean item_sku;
    public String fare_type;//是否包邮 0：不包邮 1：包邮
    public String rec;
    public int pay_method;//支付方式
    public String freight;//运费
    public String cityname;
    public String out_id;//
    public String shop_url;//店铺url

    @Override
    public String toString() {
        return "ProductBean{" +
                "temp_name='" + temp_name + '\'' +
                ", original_price=" + original_price +
                ", is_salable=" + is_salable +
                ", is_collect=" + is_collect +
                ", stock=" + stock +
                ", is_seckill=" + is_seckill +
                ", share_price='" + share_price + '\'' +
                ", description='" + description + '\'' +
                ", shared=" + shared +
                ", wap_flag='" + wap_flag + '\'' +
                ", shop_id=" + shop_id +
                ", provinceid='" + provinceid + '\'' +
                ", price=" + price +
                ", rating_summary=" + rating_summary +
                ", sale_min_price='" + sale_min_price + '\'' +
                ", reviews_count=" + reviews_count +
                ", has_gallery=" + has_gallery +
                ", entity_type='" + entity_type + '\'' +
                ", is_shared=" + is_shared +
                ", sale_qty=" + sale_qty +
                ", min_sale_qty=" + min_sale_qty +
                ", collect_counts=" + collect_counts +
                ", in_stock=" + in_stock +
                ", link='" + link + '\'' +
                ", color_alias='" + color_alias + '\'' +
                ", recommended=" + recommended +
                ", price_interval='" + price_interval + '\'' +
                ", entity_id=" + entity_id +
                ", provincename='" + provincename + '\'' +
                ", item_images=" + item_images +
                ", is_winguo_largess='" + is_winguo_largess + '\'' +
                ", short_description='" + short_description + '\'' +
                ", name='" + name + '\'' +
                ", size_alias='" + size_alias + '\'' +
                ", sku_id=" + sku_id +
                ", icon=" + icon +
                ", is_time_discount=" + is_time_discount +
                ", has_options=" + has_options +
                ", has_size=" + has_size +
                ", additional_attributes=" + additional_attributes +
                ", cityid='" + cityid + '\'' +
                ", original_min_price=" + original_min_price +
                ", shop_name='" + shop_name + '\'' +
                ", item_sku=" + item_sku +
                ", fare_type=" + fare_type +
                ", rec=" + rec +
                ", pay_method=" + pay_method +
                ", freight='" + freight + '\'' +
                ", cityname='" + cityname + '\'' +
                ", out_id=" + out_id +
                ", shop_url='" + shop_url + '\'' +
                '}';
    }
}
