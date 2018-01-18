package com.winguo.mine.order.list;

import com.winguo.mine.order.detail.delivery.model.DeliveryRootBean;
import com.winguo.pay.modle.store.ItemBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author hcpai
 * @desc 订单列表的data部分(自定义)
 */
public class OrderDataBean implements Serializable {
    @Override
    public String toString() {
        return "OrderDataBean{" +
                "datetime='" + datetime + '\'' +
                ", oid=" + oid +
                ", number=" + number +
                ", date_count=" + date_count +
                ", price=" + price +
                ", quantity=" + quantity +
                ", mid=" + mid +
                ", mname='" + mname + '\'' +
                ", delivery_charges=" + delivery_charges +
                ", image=" + image +
                ", pay_method=" + pay_method +
                ", order_status='" + order_status + '\'' +
                ", status_code=" + status_code +
                ", is_closed=" + is_closed +
                ", is_review=" + is_review +
                ", color_name='" + color_name + '\'' +
                ", color_alias='" + color_alias + '\'' +
                ", sku_size='" + sku_size + '\'' +
                ", size_alias='" + size_alias + '\'' +
                ", name='" + name + '\'' +
                ", singlePrice=" + singlePrice +
                ", singleCount=" + singleCount +
                ", isMore=" + isMore +
                ", address=" + address +
                ", CommonBeans=" + CommonBeans +
                ", gid=" + gid +
                ", deliveryRootBean=" + deliveryRootBean +
                '}';
    }

    private String datetime;
    /**
     * 订单详情
     */
    private int oid;
    private long number;
    private int date_count;
    /**
     * 订单总数量
     */
    private double price;
    /**
     * 订单总数量
     */
    private int quantity;
    private int mid;
    private String mname;
    private double delivery_charges;
    /**
     * modifyTime : 1473811618
     * content : http://g1.img.winguo.com/group1/M00/4D/B4/wKgAi1fYlKKAR4ZmAAdyPprUboc74..jpg
     */

    private ImageBean image;
    private int pay_method;
    private String order_status;
    private int status_code;
    private int is_closed;
    private int is_review;
    /**
     * 规格和颜色
     */
    private String color_name;
    private String color_alias;
    private String sku_size;
    private String size_alias;
    /**
     * 单个商品名称
     */
    private String name;
    /**
     * 单个商品单价
     */
    private double singlePrice;
    /**
     * 单个商品数量
     */
    private int singleCount;
    /**
     * 是否是同家店买多个商品
     */
    private boolean isMore;
    /**
     * 收件人地址
     */
    private ItemBean address;
    /**
     * 传给支付界面的bean:一个订单多个商品
     */
    private ArrayList<CommonBean> CommonBeans;
    /**
     * 物流信息,从订单列表中跳转到物流详情
     */
    /**
     * 商品id
     */
    private int gid;

    private DeliveryRootBean deliveryRootBean;
    /*----------------------get,set方法-----------------------*/

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

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public DeliveryRootBean getDeliveryRootBean() {
        return deliveryRootBean;
    }

    public void setDeliveryRootBean(DeliveryRootBean deliveryRootBean) {
        this.deliveryRootBean = deliveryRootBean;
    }

    public ArrayList<CommonBean> getCommonBeans() {
        return CommonBeans;
    }

    public void setCommonBeans(ArrayList<CommonBean> commonBeans) {
        CommonBeans = commonBeans;
    }

    public ItemBean getAddress() {
        return address;
    }

    public void setAddress(ItemBean address) {
        this.address = address;
    }

    public boolean getIsMore() {
        return isMore;
    }

    public void setIsMore(boolean more) {
        isMore = more;
    }

    public double getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(double singlePrice) {
        this.singlePrice = singlePrice;
    }

    public int getSingleCount() {
        return singleCount;
    }

    public void setSingleCount(int singleCount) {
        this.singleCount = singleCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getDate_count() {
        return date_count;
    }

    public void setDate_count(int date_count) {
        this.date_count = date_count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public double getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(double delivery_charges) {
        this.delivery_charges = delivery_charges;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    public int getPay_method() {
        return pay_method;
    }

    public void setPay_method(int pay_method) {
        this.pay_method = pay_method;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public int getIs_closed() {
        return is_closed;
    }

    public void setIs_closed(int is_closed) {
        this.is_closed = is_closed;
    }

    public int getIs_review() {
        return is_review;
    }

    public void setIs_review(int is_review) {
        this.is_review = is_review;
    }

    public static class ImageBean {
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
