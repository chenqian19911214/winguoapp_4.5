package com.winguo.product.modle.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/12/20.
 */

public class GoodDetail {

    @Override
    public String toString() {
        return "GoodDetail{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", product=" + product +
                '}';
    }

    /**
     * msg :
     * code : 0
     * product : {"entity_id":86393,"out_id":0,"collect_counts":"4","name":"北京方便面麻辣味 65g/70g/袋","entity_type":"simple","shop_id":"2206","shop_name":"众大食品","shop_url":"jq1234","shop_type":"0","address_ch":"金雀路999号","short_description":"","description":"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"> <head> <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" media=\"screen\"/> <\/head> <body><p><\/p><ul class=\"attributes-list\" style=\"margin: 0px; padding: 0px 15px; list-style: none; clear: both; font-family: tahoma, arial, 'Hiragino Sans GB', 宋体, sans-serif; line-height: 18px;\"><li title=\"QS410007010022\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">生产许可证编号:&nbsp;QS410007010022<\/li><li title=\"河南省南街村(集团)有限公司方便面厂\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">厂名:&nbsp;河南省南街村(集团)有限公司方便面厂<\/li><li title=\"1、河南省临颍县南街村朝阳路南段东侧2、河南省临颍县南街村3、临颍县纬二路北侧经一路西侧\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">厂址:&nbsp;1、河南省临颍县南街村朝阳路南段东侧2、河南省临颍县南街村3、临颍县纬二路北侧经一路西侧<\/li><li title=\"0395-8851396\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">厂家联系方式:&nbsp;0395-8851396<\/li><li title=\"面粉 食盐 植物油 碳酸钠\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">配料表:&nbsp;面粉 食盐 植物油 碳酸钠<\/li><li title=\"150\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">保质期:&nbsp;150<\/li><li title=\"碳酸钠\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">食品添加剂:&nbsp;碳酸钠<\/li><li title=\"2340g\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">净含量:&nbsp;2340g<\/li><li title=\"包装\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">包装方式:&nbsp;包装<\/li><li title=\"南街村\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">品牌:&nbsp;南街村<\/li><li title=\"北京方便面36袋整箱\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">系列:&nbsp;北京方便面36袋整箱<\/li><li title=\"否\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">是否为有机食品:&nbsp;否<\/li><li title=\"中国大陆\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">产地:&nbsp;中国大陆<\/li><li title=\"河南省\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">省份:&nbsp;河南省<\/li><li title=\"漯河市\" style=\"margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;\">城市:&nbsp;漯河市<\/li><\/ul><br /><p><\/p><p><\/p><ul class=\"attributes-list\" style=\"margin: 0px; padding: 0px 15px; list-style: none; clear: both; font-family: tahoma, arial, 'Hiragino Sans GB', 宋体, sans-serif; line-height: 18px;\"><span style=\"line-height: 23.999998092651367px; white-space: nowrap; color: rgb(255, 0, 0); font-size: 24.44444465637207px;\"><strong>注：<\/strong><\/span><span style=\"line-height: 23.999998092651367px; text-indent: 5px; white-space: nowrap; color: rgb(255, 0, 0); font-size: 24.44444465637207px;\"><strong>本商品限线下体验店 &nbsp;不包邮 谢谢谅解！驻马店市区送货<\/strong><\/span><\/ul><p><\/p><p><img align=\"absmiddle\" src=\"http://g1.img.tzdspt.com/group1/M00/4D/A5/wKgAjFfOH_OAQQP9AAHBFCh0GiI38..jpg\" alt=\"\" /><img align=\"absmiddle\" src=\"http://g1.img.tzdspt.com/group1/M00/4D/A5/wKgAjFfOH_OAT5fdAAHQva9zmJk32..jpg\" alt=\"\" /><img align=\"absmiddle\" src=\"http://g1.img.tzdspt.com/group1/M00/4D/A3/wKgAi1fOH9KAG-5dAAHZ67tbSrA52..jpg\" alt=\"\" /><img align=\"absmiddle\" src=\"http://g1.img.tzdspt.com/group1/M00/4D/A5/wKgAjFfOH_SAbfEpAAF1ODXmEeM92..jpg\" alt=\"\" /><img alt=\"\" align=\"absMiddle\" src=\"http://g1.img.winguo.com/group1/M00/4D/35/wKgAi1c-37eAAFYOAADSnXkAPrQ07..jpg\" /><img alt=\"\" align=\"absMiddle\" src=\"http://g1.img.winguo.com/group1/M00/4D/35/wKgAjFc-396Ae3crAAC94MUMnpM33..jpg\" /><\/p><\/body><\/html>","link":"http://md.winguo.com/goods/goodsinfo?gid=86393","icon":"http://g1.imgdev.winguo.com","in_stock":1,"is_salable":1,"is_shared":"1","shared":0,"rec":"0","recommended":"0","pay_method":0,"has_gallery":1,"has_options":0,"min_sale_qty":1,"rating_summary":5,"reviews_count":"54","has_size":1,"size_alias":"规格","color_alias":"颜色","stock":0,"sku_id":0,"special_price":"0.90","regular_price":"25.00","share_price":0,"original_price":"25.00","sale_min_price":0,"original_min_price":"0.90","freight":" ","sale_qty":506,"cityid":0,"cityname":"","provinceid":0,"provincename":"","fare_type":"2","temp_name":"快递0.00元;快递15.00元;快递25.00元;快递35.00元","is_collect":0,"is_seckill":0,"is_time_discount":0,"is_winguo_largess":0,"wap_flag":0,"additional_attributes":null,"item_sku":[{"title":{"m_sku_id":"sku id","m_sku_color":"颜色","m_sku_size":"规格","m_sku_price":"单价","m_sku_weight":"重量","m_sku_stock":"数量","m_sku_color_name":"颜色名称","m_sku_stock_last":"剩余数量","m_color_picture":"颜色图片","original_price":"原价","m_sku_cash_coupon_used":"现金卷抵扣"}},{"data":{"m_sku_id":"87281","m_sku_color":"","m_sku_size":"","m_sku_price":"23.00","m_sku_weight":"0.00","m_sku_stock":"74","m_sku_color_name":"","m_sku_stock_last":"74","m_color_picture":"","original_price":"23.00","m_sku_cash_coupon_used":"0.00"}},{"data":{"m_sku_id":"92320","m_sku_color":"","m_sku_size":"70g/袋整箱","m_sku_price":"25.00","m_sku_weight":"0.00","m_sku_stock":"945","m_sku_color_name":"","m_sku_stock_last":"945","m_color_picture":"","original_price":"25.00","m_sku_cash_coupon_used":"0.00"}},{"data":{"m_sku_id":"92321","m_sku_color":"","m_sku_size":"65g/袋整箱","m_sku_price":"25.00","m_sku_weight":"0.00","m_sku_stock":"816","m_sku_color_name":"","m_sku_stock_last":"816","m_color_picture":"","original_price":"25.00","m_sku_cash_coupon_used":"0.00"}},{"data":{"m_sku_id":"92322","m_sku_color":"","m_sku_size":"65g/袋","m_sku_price":"0.90","m_sku_weight":"0.00","m_sku_stock":"841","m_sku_color_name":"","m_sku_stock_last":"841","m_color_picture":"","original_price":"0.90","m_sku_cash_coupon_used":"0.00"}},{"data":{"m_sku_id":"92323","m_sku_color":"","m_sku_size":"70g/袋","m_sku_price":"0.90","m_sku_weight":"0.00","m_sku_stock":"932","m_sku_color_name":"","m_sku_stock_last":"932","m_color_picture":"","original_price":"0.90","m_sku_cash_coupon_used":"0.00"}}],"item_images":[{"data":{"url":"http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAUVh3PWGAZTdOAAcFx-ku2UM60..jpg","order":"0"}},{"data":{"url":"http://g1.imgdev.winguo.com/group1/M00/4E/43/wKgAi1hOSjOAJtpvAAdPfb316DQ87..jpg","order":"1"}},{"data":{"url":"http://g1.imgdev.winguo.com/group1/M00/4D/35/wKgAjFc-38iAJ24GAAA6AxPCHTQ03..jpg","order":"2"}},{"data":{"url":"http://g1.imgdev.winguo.com/group1/M00/4D/A3/wKgAi1fOIYaALts3AAGT_U9jxvE05..jpg","order":"3"}},{"data":{"url":"http://g1.imgdev.winguo.com/group1/M00/4D/A3/wKgAi1fOIYuARbUXAAFy7u90wrI49..jpg","order":"4"}}]}
     */

    private String msg;
    private int code;
    private ProductBean product;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public static class ProductBean implements Serializable{
        @Override
        public String toString() {
            return "ProductBean{" +
                    "entity_id=" + entity_id +
                    ", out_id=" + out_id +
                    ", collect_counts='" + collect_counts + '\'' +
                    ", name='" + name + '\'' +
                    ", entity_type='" + entity_type + '\'' +
                    ", shop_id='" + shop_id + '\'' +
                    ", shop_name='" + shop_name + '\'' +
                    ", shop_url='" + shop_url + '\'' +
                    ", shop_type='" + shop_type + '\'' +
                    ", address_ch='" + address_ch + '\'' +
                    ", short_description='" + short_description + '\'' +
                    ", description='" + description + '\'' +
                    ", link='" + link + '\'' +
                    ", icon='" + icon + '\'' +
                    ", in_stock=" + in_stock +
                    ", is_salable=" + is_salable +
                    ", is_shared='" + is_shared + '\'' +
                    ", shared=" + shared +
                    ", rec='" + rec + '\'' +
                    ", recommended='" + recommended + '\'' +
                    ", pay_method=" + pay_method +
                    ", has_gallery=" + has_gallery +
                    ", has_options=" + has_options +
                    ", min_sale_qty=" + min_sale_qty +
                    ", rating_summary=" + rating_summary +
                    ", reviews_count='" + reviews_count + '\'' +
                    ", has_size=" + has_size +
                    ", size_alias='" + size_alias + '\'' +
                    ", color_alias='" + color_alias + '\'' +
                    ", stock=" + stock +
                    ", sku_id=" + sku_id +
                    ", special_price='" + special_price + '\'' +
                    ", regular_price='" + regular_price + '\'' +
                    ", share_price=" + share_price +
                    ", original_price='" + original_price + '\'' +
                    ", sale_min_price=" + sale_min_price +
                    ", original_min_price='" + original_min_price + '\'' +
                    ", freight='" + freight + '\'' +
                    ", sale_qty=" + sale_qty +
                    ", cityid=" + cityid +
                    ", cityname='" + cityname + '\'' +
                    ", provinceid=" + provinceid +
                    ", provincename='" + provincename + '\'' +
                    ", fare_type='" + fare_type + '\'' +
                    ", temp_name='" + temp_name + '\'' +
                    ", is_collect=" + is_collect +
                    ", is_seckill=" + is_seckill +
                    ", is_time_discount=" + is_time_discount +
                    ", is_winguo_largess=" + is_winguo_largess +
                    ", wap_flag=" + wap_flag +
                    ", additional_attributes=" + additional_attributes +
                    ", item_sku=" + item_sku +
                    ", item_images=" + item_images +
                    '}';
        }

        /**
         * entity_id : 86393
         * out_id : 0
         * collect_counts : 4
         * name : 北京方便面麻辣味 65g/70g/袋
         * entity_type : simple
         * shop_id : 2206
         * shop_name : 众大食品
         * shop_url : jq1234
         * shop_type : 0
         * address_ch : 金雀路999号
         * short_description :
         * description : <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"> <head> <link rel="stylesheet" type="text/css" href="style.css" media="screen"/> </head> <body><p></p><ul class="attributes-list" style="margin: 0px; padding: 0px 15px; list-style: none; clear: both; font-family: tahoma, arial, 'Hiragino Sans GB', 宋体, sans-serif; line-height: 18px;"><li title="QS410007010022" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">生产许可证编号:&nbsp;QS410007010022</li><li title="河南省南街村(集团)有限公司方便面厂" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">厂名:&nbsp;河南省南街村(集团)有限公司方便面厂</li><li title="1、河南省临颍县南街村朝阳路南段东侧2、河南省临颍县南街村3、临颍县纬二路北侧经一路西侧" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">厂址:&nbsp;1、河南省临颍县南街村朝阳路南段东侧2、河南省临颍县南街村3、临颍县纬二路北侧经一路西侧</li><li title="0395-8851396" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">厂家联系方式:&nbsp;0395-8851396</li><li title="面粉 食盐 植物油 碳酸钠" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">配料表:&nbsp;面粉 食盐 植物油 碳酸钠</li><li title="150" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">保质期:&nbsp;150</li><li title="碳酸钠" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">食品添加剂:&nbsp;碳酸钠</li><li title="2340g" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">净含量:&nbsp;2340g</li><li title="包装" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">包装方式:&nbsp;包装</li><li title="南街村" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">品牌:&nbsp;南街村</li><li title="北京方便面36袋整箱" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">系列:&nbsp;北京方便面36袋整箱</li><li title="否" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">是否为有机食品:&nbsp;否</li><li title="中国大陆" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">产地:&nbsp;中国大陆</li><li title="河南省" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">省份:&nbsp;河南省</li><li title="漯河市" style="margin: 0px 20px 0px 0px; padding: 0px; display: inline; float: left; width: 206px; height: 24px; overflow: hidden; text-indent: 5px; line-height: 24px; white-space: nowrap; text-overflow: ellipsis;">城市:&nbsp;漯河市</li></ul><br /><p></p><p></p><ul class="attributes-list" style="margin: 0px; padding: 0px 15px; list-style: none; clear: both; font-family: tahoma, arial, 'Hiragino Sans GB', 宋体, sans-serif; line-height: 18px;"><span style="line-height: 23.999998092651367px; white-space: nowrap; color: rgb(255, 0, 0); font-size: 24.44444465637207px;"><strong>注：</strong></span><span style="line-height: 23.999998092651367px; text-indent: 5px; white-space: nowrap; color: rgb(255, 0, 0); font-size: 24.44444465637207px;"><strong>本商品限线下体验店 &nbsp;不包邮 谢谢谅解！驻马店市区送货</strong></span></ul><p></p><p><img align="absmiddle" src="http://g1.img.tzdspt.com/group1/M00/4D/A5/wKgAjFfOH_OAQQP9AAHBFCh0GiI38..jpg" alt="" /><img align="absmiddle" src="http://g1.img.tzdspt.com/group1/M00/4D/A5/wKgAjFfOH_OAT5fdAAHQva9zmJk32..jpg" alt="" /><img align="absmiddle" src="http://g1.img.tzdspt.com/group1/M00/4D/A3/wKgAi1fOH9KAG-5dAAHZ67tbSrA52..jpg" alt="" /><img align="absmiddle" src="http://g1.img.tzdspt.com/group1/M00/4D/A5/wKgAjFfOH_SAbfEpAAF1ODXmEeM92..jpg" alt="" /><img alt="" align="absMiddle" src="http://g1.img.winguo.com/group1/M00/4D/35/wKgAi1c-37eAAFYOAADSnXkAPrQ07..jpg" /><img alt="" align="absMiddle" src="http://g1.img.winguo.com/group1/M00/4D/35/wKgAjFc-396Ae3crAAC94MUMnpM33..jpg" /></p></body></html>
         * link : http://md.winguo.com/goods/goodsinfo?gid=86393
         * icon : http://g1.imgdev.winguo.com
         * in_stock : 1
         * is_salable : 1
         * is_shared : 1
         * shared : 0
         * rec : 0
         * recommended : 0
         * pay_method : 0
         * has_gallery : 1
         * has_options : 0
         * min_sale_qty : 1
         * rating_summary : 5
         * reviews_count : 54
         * has_size : 1
         * size_alias : 规格
         * color_alias : 颜色
         * stock : 0
         * sku_id : 0
         * special_price : 0.90
         * regular_price : 25.00
         * share_price : 0
         * original_price : 25.00
         * sale_min_price : 0
         * original_min_price : 0.90
         * freight :
         * sale_qty : 506
         * cityid : 0
         * cityname :
         * provinceid : 0
         * provincename :
         * fare_type : 2
         * temp_name : 快递0.00元;快递15.00元;快递25.00元;快递35.00元
         * is_collect : 0
         * is_seckill : 0
         * is_time_discount : 0
         * is_winguo_largess : 0
         * wap_flag : 0
         * additional_attributes : null
         * item_sku : [{"title":{"m_sku_id":"sku id","m_sku_color":"颜色","m_sku_size":"规格","m_sku_price":"单价","m_sku_weight":"重量","m_sku_stock":"数量","m_sku_color_name":"颜色名称","m_sku_stock_last":"剩余数量","m_color_picture":"颜色图片","original_price":"原价","m_sku_cash_coupon_used":"现金卷抵扣"}},{"data":{"m_sku_id":"87281","m_sku_color":"","m_sku_size":"","m_sku_price":"23.00","m_sku_weight":"0.00","m_sku_stock":"74","m_sku_color_name":"","m_sku_stock_last":"74","m_color_picture":"","original_price":"23.00","m_sku_cash_coupon_used":"0.00"}},{"data":{"m_sku_id":"92320","m_sku_color":"","m_sku_size":"70g/袋整箱","m_sku_price":"25.00","m_sku_weight":"0.00","m_sku_stock":"945","m_sku_color_name":"","m_sku_stock_last":"945","m_color_picture":"","original_price":"25.00","m_sku_cash_coupon_used":"0.00"}},{"data":{"m_sku_id":"92321","m_sku_color":"","m_sku_size":"65g/袋整箱","m_sku_price":"25.00","m_sku_weight":"0.00","m_sku_stock":"816","m_sku_color_name":"","m_sku_stock_last":"816","m_color_picture":"","original_price":"25.00","m_sku_cash_coupon_used":"0.00"}},{"data":{"m_sku_id":"92322","m_sku_color":"","m_sku_size":"65g/袋","m_sku_price":"0.90","m_sku_weight":"0.00","m_sku_stock":"841","m_sku_color_name":"","m_sku_stock_last":"841","m_color_picture":"","original_price":"0.90","m_sku_cash_coupon_used":"0.00"}},{"data":{"m_sku_id":"92323","m_sku_color":"","m_sku_size":"70g/袋","m_sku_price":"0.90","m_sku_weight":"0.00","m_sku_stock":"932","m_sku_color_name":"","m_sku_stock_last":"932","m_color_picture":"","original_price":"0.90","m_sku_cash_coupon_used":"0.00"}}]
         * item_images : [{"data":{"url":"http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAUVh3PWGAZTdOAAcFx-ku2UM60..jpg","order":"0"}},{"data":{"url":"http://g1.imgdev.winguo.com/group1/M00/4E/43/wKgAi1hOSjOAJtpvAAdPfb316DQ87..jpg","order":"1"}},{"data":{"url":"http://g1.imgdev.winguo.com/group1/M00/4D/35/wKgAjFc-38iAJ24GAAA6AxPCHTQ03..jpg","order":"2"}},{"data":{"url":"http://g1.imgdev.winguo.com/group1/M00/4D/A3/wKgAi1fOIYaALts3AAGT_U9jxvE05..jpg","order":"3"}},{"data":{"url":"http://g1.imgdev.winguo.com/group1/M00/4D/A3/wKgAi1fOIYuARbUXAAFy7u90wrI49..jpg","order":"4"}}]
         */

        private int entity_id;
        private int out_id;
        private String collect_counts;
        private String name;
        private String entity_type;
        private String shop_id;
        private String shop_name;
        private String shop_url;
        private String shop_type;
        private String address_ch;
        private String short_description;
        private String description;
        private String link;
        private String icon;
        private int in_stock;
        private int is_salable;
        private String is_shared;
        private int shared;
        private String rec;
        private String recommended;
        private int pay_method;
        private int has_gallery;
        private int has_options;
        private int min_sale_qty;
        private String rating_summary;
        private String reviews_count;
        private int has_size;
        private String size_alias;
        private String color_alias;
        private int stock;
        private int sku_id;
        private String special_price;
        private String regular_price;
        private int share_price;
        private String original_price;
        private int sale_min_price;
        private String original_min_price;
        private String freight;
        private int sale_qty;
        private int cityid;
        private String cityname;
        private int provinceid;
        private String provincename;
        private String fare_type;
        private String temp_name;
        private int is_collect;
        private int is_seckill;
        private int is_time_discount;
        private int is_winguo_largess;
        private int wap_flag;
        private Object additional_attributes;
        private List<ItemSkuBean> item_sku;
        private List<ItemImagesBean> item_images;

        public int getEntity_id() {
            return entity_id;
        }

        public void setEntity_id(int entity_id) {
            this.entity_id = entity_id;
        }

        public int getOut_id() {
            return out_id;
        }

        public void setOut_id(int out_id) {
            this.out_id = out_id;
        }

        public String getCollect_counts() {
            return collect_counts;
        }

        public void setCollect_counts(String collect_counts) {
            this.collect_counts = collect_counts;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEntity_type() {
            return entity_type;
        }

        public void setEntity_type(String entity_type) {
            this.entity_type = entity_type;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_url() {
            return shop_url;
        }

        public void setShop_url(String shop_url) {
            this.shop_url = shop_url;
        }

        public String getShop_type() {
            return shop_type;
        }

        public void setShop_type(String shop_type) {
            this.shop_type = shop_type;
        }

        public String getAddress_ch() {
            return address_ch;
        }

        public void setAddress_ch(String address_ch) {
            this.address_ch = address_ch;
        }

        public String getShort_description() {
            return short_description;
        }

        public void setShort_description(String short_description) {
            this.short_description = short_description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getIn_stock() {
            return in_stock;
        }

        public void setIn_stock(int in_stock) {
            this.in_stock = in_stock;
        }

        public int getIs_salable() {
            return is_salable;
        }

        public void setIs_salable(int is_salable) {
            this.is_salable = is_salable;
        }

        public String getIs_shared() {
            return is_shared;
        }

        public void setIs_shared(String is_shared) {
            this.is_shared = is_shared;
        }

        public int getShared() {
            return shared;
        }

        public void setShared(int shared) {
            this.shared = shared;
        }

        public String getRec() {
            return rec;
        }

        public void setRec(String rec) {
            this.rec = rec;
        }

        public String getRecommended() {
            return recommended;
        }

        public void setRecommended(String recommended) {
            this.recommended = recommended;
        }

        public int getPay_method() {
            return pay_method;
        }

        public void setPay_method(int pay_method) {
            this.pay_method = pay_method;
        }

        public int getHas_gallery() {
            return has_gallery;
        }

        public void setHas_gallery(int has_gallery) {
            this.has_gallery = has_gallery;
        }

        public int getHas_options() {
            return has_options;
        }

        public void setHas_options(int has_options) {
            this.has_options = has_options;
        }

        public int getMin_sale_qty() {
            return min_sale_qty;
        }

        public void setMin_sale_qty(int min_sale_qty) {
            this.min_sale_qty = min_sale_qty;
        }

        public String getRating_summary() {
            return rating_summary;
        }

        public void setRating_summary(String rating_summary) {
            this.rating_summary = rating_summary;
        }

        public String getReviews_count() {
            return reviews_count;
        }

        public void setReviews_count(String reviews_count) {
            this.reviews_count = reviews_count;
        }

        public int getHas_size() {
            return has_size;
        }

        public void setHas_size(int has_size) {
            this.has_size = has_size;
        }

        public String getSize_alias() {
            return size_alias;
        }

        public void setSize_alias(String size_alias) {
            this.size_alias = size_alias;
        }

        public String getColor_alias() {
            return color_alias;
        }

        public void setColor_alias(String color_alias) {
            this.color_alias = color_alias;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getSku_id() {
            return sku_id;
        }

        public void setSku_id(int sku_id) {
            this.sku_id = sku_id;
        }

        public String getSpecial_price() {
            return special_price;
        }

        public void setSpecial_price(String special_price) {
            this.special_price = special_price;
        }

        public String getRegular_price() {
            return regular_price;
        }

        public void setRegular_price(String regular_price) {
            this.regular_price = regular_price;
        }

        public int getShare_price() {
            return share_price;
        }

        public void setShare_price(int share_price) {
            this.share_price = share_price;
        }

        public String getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(String original_price) {
            this.original_price = original_price;
        }

        public int getSale_min_price() {
            return sale_min_price;
        }

        public void setSale_min_price(int sale_min_price) {
            this.sale_min_price = sale_min_price;
        }

        public String getOriginal_min_price() {
            return original_min_price;
        }

        public void setOriginal_min_price(String original_min_price) {
            this.original_min_price = original_min_price;
        }

        public String getFreight() {
            return freight;
        }

        public void setFreight(String freight) {
            this.freight = freight;
        }

        public int getSale_qty() {
            return sale_qty;
        }

        public void setSale_qty(int sale_qty) {
            this.sale_qty = sale_qty;
        }

        public int getCityid() {
            return cityid;
        }

        public void setCityid(int cityid) {
            this.cityid = cityid;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        public int getProvinceid() {
            return provinceid;
        }

        public void setProvinceid(int provinceid) {
            this.provinceid = provinceid;
        }

        public String getProvincename() {
            return provincename;
        }

        public void setProvincename(String provincename) {
            this.provincename = provincename;
        }

        public String getFare_type() {
            return fare_type;
        }

        public void setFare_type(String fare_type) {
            this.fare_type = fare_type;
        }

        public String getTemp_name() {
            return temp_name;
        }

        public void setTemp_name(String temp_name) {
            this.temp_name = temp_name;
        }

        public int getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(int is_collect) {
            this.is_collect = is_collect;
        }

        public int getIs_seckill() {
            return is_seckill;
        }

        public void setIs_seckill(int is_seckill) {
            this.is_seckill = is_seckill;
        }

        public int getIs_time_discount() {
            return is_time_discount;
        }

        public void setIs_time_discount(int is_time_discount) {
            this.is_time_discount = is_time_discount;
        }

        public int getIs_winguo_largess() {
            return is_winguo_largess;
        }

        public void setIs_winguo_largess(int is_winguo_largess) {
            this.is_winguo_largess = is_winguo_largess;
        }

        public int getWap_flag() {
            return wap_flag;
        }

        public void setWap_flag(int wap_flag) {
            this.wap_flag = wap_flag;
        }

        public Object getAdditional_attributes() {
            return additional_attributes;
        }

        public void setAdditional_attributes(Object additional_attributes) {
            this.additional_attributes = additional_attributes;
        }

        public List<ItemSkuBean> getItem_sku() {
            return item_sku;
        }

        public void setItem_sku(List<ItemSkuBean> item_sku) {
            this.item_sku = item_sku;
        }

        public List<ItemImagesBean> getItem_images() {
            return item_images;
        }

        public void setItem_images(List<ItemImagesBean> item_images) {
            this.item_images = item_images;
        }

        public static class ItemSkuBean implements Serializable {
            @Override
            public String toString() {
                return "ItemSkuBean{" +
                        "title=" + title +
                        ", data=" + data +
                        '}';
            }

            /**
             * title : {"m_sku_id":"sku id","m_sku_color":"颜色","m_sku_size":"规格","m_sku_price":"单价","m_sku_weight":"重量","m_sku_stock":"数量","m_sku_color_name":"颜色名称","m_sku_stock_last":"剩余数量","m_color_picture":"颜色图片","original_price":"原价","m_sku_cash_coupon_used":"现金卷抵扣"}
             * data : {"m_sku_id":"87281","m_sku_color":"","m_sku_size":"","m_sku_price":"23.00","m_sku_weight":"0.00","m_sku_stock":"74","m_sku_color_name":"","m_sku_stock_last":"74","m_color_picture":"","original_price":"23.00","m_sku_cash_coupon_used":"0.00"}
             */


            private TitleBean title;
            private DataBean data;

            public TitleBean getTitle() {
                return title;
            }

            public void setTitle(TitleBean title) {
                this.title = title;
            }

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public static class TitleBean implements Serializable {
                @Override
                public String toString() {
                    return "TitleBean{" +
                            "m_sku_id='" + m_sku_id + '\'' +
                            ", m_sku_color='" + m_sku_color + '\'' +
                            ", m_sku_size='" + m_sku_size + '\'' +
                            ", m_sku_price='" + m_sku_price + '\'' +
                            ", m_sku_weight='" + m_sku_weight + '\'' +
                            ", m_sku_stock='" + m_sku_stock + '\'' +
                            ", m_sku_color_name='" + m_sku_color_name + '\'' +
                            ", m_sku_stock_last='" + m_sku_stock_last + '\'' +
                            ", m_color_picture='" + m_color_picture + '\'' +
                            ", original_price='" + original_price + '\'' +
                            ", m_sku_cash_coupon_used='" + m_sku_cash_coupon_used + '\'' +
                            '}';
                }

                /**
                 * m_sku_id : sku id
                 * m_sku_color : 颜色
                 * m_sku_size : 规格
                 * m_sku_price : 单价
                 * m_sku_weight : 重量
                 * m_sku_stock : 数量
                 * m_sku_color_name : 颜色名称
                 * m_sku_stock_last : 剩余数量
                 * m_color_picture : 颜色图片
                 * original_price : 原价
                 * m_sku_cash_coupon_used : 现金卷抵扣
                 */

                private String m_sku_id;
                private String m_sku_color;
                private String m_sku_size;
                private String m_sku_price;
                private String m_sku_weight;
                private String m_sku_stock;
                private String m_sku_color_name;
                private String m_sku_stock_last;
                private String m_color_picture;
                private String original_price;
                private String m_sku_cash_coupon_used;

                public String getM_sku_id() {
                    return m_sku_id;
                }

                public void setM_sku_id(String m_sku_id) {
                    this.m_sku_id = m_sku_id;
                }

                public String getM_sku_color() {
                    return m_sku_color;
                }

                public void setM_sku_color(String m_sku_color) {
                    this.m_sku_color = m_sku_color;
                }

                public String getM_sku_size() {
                    return m_sku_size;
                }

                public void setM_sku_size(String m_sku_size) {
                    this.m_sku_size = m_sku_size;
                }

                public String getM_sku_price() {
                    return m_sku_price;
                }

                public void setM_sku_price(String m_sku_price) {
                    this.m_sku_price = m_sku_price;
                }

                public String getM_sku_weight() {
                    return m_sku_weight;
                }

                public void setM_sku_weight(String m_sku_weight) {
                    this.m_sku_weight = m_sku_weight;
                }

                public String getM_sku_stock() {
                    return m_sku_stock;
                }

                public void setM_sku_stock(String m_sku_stock) {
                    this.m_sku_stock = m_sku_stock;
                }

                public String getM_sku_color_name() {
                    return m_sku_color_name;
                }

                public void setM_sku_color_name(String m_sku_color_name) {
                    this.m_sku_color_name = m_sku_color_name;
                }

                public String getM_sku_stock_last() {
                    return m_sku_stock_last;
                }

                public void setM_sku_stock_last(String m_sku_stock_last) {
                    this.m_sku_stock_last = m_sku_stock_last;
                }

                public String getM_color_picture() {
                    return m_color_picture;
                }

                public void setM_color_picture(String m_color_picture) {
                    this.m_color_picture = m_color_picture;
                }

                public String getOriginal_price() {
                    return original_price;
                }

                public void setOriginal_price(String original_price) {
                    this.original_price = original_price;
                }

                public String getM_sku_cash_coupon_used() {
                    return m_sku_cash_coupon_used;
                }

                public void setM_sku_cash_coupon_used(String m_sku_cash_coupon_used) {
                    this.m_sku_cash_coupon_used = m_sku_cash_coupon_used;
                }
            }

            public static class DataBean implements Serializable{
                @Override
                public String toString() {
                    return "DataBean{" +
                            "m_sku_id='" + m_sku_id + '\'' +
                            ", m_sku_color='" + m_sku_color + '\'' +
                            ", m_sku_size='" + m_sku_size + '\'' +
                            ", m_sku_price='" + m_sku_price + '\'' +
                            ", m_sku_weight='" + m_sku_weight + '\'' +
                            ", m_sku_stock='" + m_sku_stock + '\'' +
                            ", m_sku_color_name='" + m_sku_color_name + '\'' +
                            ", m_sku_stock_last='" + m_sku_stock_last + '\'' +
                            ", m_color_picture='" + m_color_picture + '\'' +
                            ", original_price='" + original_price + '\'' +
                            ", m_sku_cash_coupon_used='" + m_sku_cash_coupon_used + '\'' +
                            '}';
                }

                /**
                 * m_sku_id : 87281
                 * m_sku_color :
                 * m_sku_size :
                 * m_sku_price : 23.00
                 * m_sku_weight : 0.00
                 * m_sku_stock : 74
                 * m_sku_color_name :
                 * m_sku_stock_last : 74
                 * m_color_picture :
                 * original_price : 23.00
                 * m_sku_cash_coupon_used : 0.00
                 */

                private String m_sku_id;
                private String m_sku_color;
                private String m_sku_size;
                private String m_sku_price;
                private String m_sku_weight;
                private String m_sku_stock;
                private String m_sku_color_name;
                private String m_sku_stock_last;
                private String m_color_picture;
                private String original_price;
                private String m_sku_cash_coupon_used;

                public String getM_sku_id() {
                    return m_sku_id;
                }

                public void setM_sku_id(String m_sku_id) {
                    this.m_sku_id = m_sku_id;
                }

                public String getM_sku_color() {
                    return m_sku_color;
                }

                public void setM_sku_color(String m_sku_color) {
                    this.m_sku_color = m_sku_color;
                }

                public String getM_sku_size() {
                    return m_sku_size;
                }

                public void setM_sku_size(String m_sku_size) {
                    this.m_sku_size = m_sku_size;
                }

                public String getM_sku_price() {
                    return m_sku_price;
                }

                public void setM_sku_price(String m_sku_price) {
                    this.m_sku_price = m_sku_price;
                }

                public String getM_sku_weight() {
                    return m_sku_weight;
                }

                public void setM_sku_weight(String m_sku_weight) {
                    this.m_sku_weight = m_sku_weight;
                }

                public String getM_sku_stock() {
                    return m_sku_stock;
                }

                public void setM_sku_stock(String m_sku_stock) {
                    this.m_sku_stock = m_sku_stock;
                }

                public String getM_sku_color_name() {
                    return m_sku_color_name;
                }

                public void setM_sku_color_name(String m_sku_color_name) {
                    this.m_sku_color_name = m_sku_color_name;
                }

                public String getM_sku_stock_last() {
                    return m_sku_stock_last;
                }

                public void setM_sku_stock_last(String m_sku_stock_last) {
                    this.m_sku_stock_last = m_sku_stock_last;
                }

                public String getM_color_picture() {
                    return m_color_picture;
                }

                public void setM_color_picture(String m_color_picture) {
                    this.m_color_picture = m_color_picture;
                }

                public String getOriginal_price() {
                    return original_price;
                }

                public void setOriginal_price(String original_price) {
                    this.original_price = original_price;
                }

                public String getM_sku_cash_coupon_used() {
                    return m_sku_cash_coupon_used;
                }

                public void setM_sku_cash_coupon_used(String m_sku_cash_coupon_used) {
                    this.m_sku_cash_coupon_used = m_sku_cash_coupon_used;
                }
            }
        }

        public static class ItemImagesBean implements Serializable {
            /**
             * data : {"url":"http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAUVh3PWGAZTdOAAcFx-ku2UM60..jpg","order":"0"}
             */

            private DataBeanX data;

            public DataBeanX getData() {
                return data;
            }

            public void setData(DataBeanX data) {
                this.data = data;
            }

            public static class DataBeanX implements Serializable {
                /**
                 * url : http://g1.imgdev.winguo.com/group1/M00/00/12/wKgAUVh3PWGAZTdOAAcFx-ku2UM60..jpg
                 * order : 0
                 */

                private String url;
                private String order;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getOrder() {
                    return order;
                }

                public void setOrder(String order) {
                    this.order = order;
                }
            }
        }
    }
}
