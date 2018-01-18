package com.winguo.mine.order.detail;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc 订单详情item(一部分)
 */
public class OrderDetailItemBean implements Serializable {

    /**
     * oiid : 617
     * item_id : 85364
     * item_code : 20054
     * refundState : 0
     * refid :
     * name : 邓老汤料系列·山麦金豆汤汤料
     * icon : {"modifyTime":1434011487,"content":"http://g1.imgdev.winguo.com/group1/M00/02/40/wKgAi1V5R1-AISfaAAVrAOnwXD0567.jpg"}
     * sku_id : 85356
     * maker_id : 15
     * sku_size :
     * size_alias : 尺码
     * quantity : 1
     * ship_quantity : 1
     * unit_price : 15.8
     * cost_unit_price : 15.8
     * largess_total : 0
     * color :
     * color_name :
     * color_alias : 颜色
     * is_review : 0
     * discount : 0.0%
     * discount_type : 不打折
     */

    private int oiid;
    private int item_id;
    private String item_code;
    private int refundState;
    private String refid;
    private String name;
    /**
     * modifyTime : 1434011487
     * content : http://g1.imgdev.winguo.com/group1/M00/02/40/wKgAi1V5R1-AISfaAAVrAOnwXD0567.jpg
     */

    private IconBean icon;
    private int sku_id;
    private int maker_id;
    private String sku_size;
    private String size_alias;
    private int quantity;
    private int ship_quantity;
    private double unit_price;
    private double cost_unit_price;
    /**
     * 满就减
     */
    private double full_sale;
    private double full_sale_freepost;

    private int largess_total;
    private String color_name;
    private String color_alias;
    private int is_review;
    private String discount;
    private String discount_type;

    public int getOiid() {
        return oiid;
    }

    public void setOiid(int oiid) {
        this.oiid = oiid;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public String getRefid() {
        return refid;
    }

    public double getFull_sale() {
        return full_sale;
    }

    public void setFull_sale(double full_sale) {
        this.full_sale = full_sale;
    }

    public double getFull_sale_freepost() {
        return full_sale_freepost;
    }

    public void setFull_sale_freepost(double full_sale_freepost) {
        this.full_sale_freepost = full_sale_freepost;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IconBean getIcon() {
        return icon;
    }

    public void setIcon(IconBean icon) {
        this.icon = icon;
    }

    public int getSku_id() {
        return sku_id;
    }

    public void setSku_id(int sku_id) {
        this.sku_id = sku_id;
    }

    public int getMaker_id() {
        return maker_id;
    }

    public void setMaker_id(int maker_id) {
        this.maker_id = maker_id;
    }

    public String getSku_size() {
        return sku_size;
    }

    public void setSku_size(String sku_size) {
        this.sku_size = sku_size;
    }

    public String getSize_alias() {
        return size_alias;
    }

    public void setSize_alias(String size_alias) {
        this.size_alias = size_alias;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getShip_quantity() {
        return ship_quantity;
    }

    public void setShip_quantity(int ship_quantity) {
        this.ship_quantity = ship_quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getCost_unit_price() {
        return cost_unit_price;
    }

    public void setCost_unit_price(double cost_unit_price) {
        this.cost_unit_price = cost_unit_price;
    }

    public int getLargess_total() {
        return largess_total;
    }

    public void setLargess_total(int largess_total) {
        this.largess_total = largess_total;
    }


    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getColor_alias() {
        return color_alias;
    }

    public void setColor_alias(String color_alias) {
        this.color_alias = color_alias;
    }

    public int getIs_review() {
        return is_review;
    }

    public void setIs_review(int is_review) {
        this.is_review = is_review;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public static class IconBean implements Serializable {
        private String modifyTime;
        private String content;

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
