package com.winguo.mine.order.bean;

import com.winguo.mine.order.bean.DeliveryRootBean;
import com.winguo.mine.order.detail.OrderDetailItemBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc 订单列表(自定义) 多了物流
 */
public class OrderDetailArryBean implements Serializable {

    @Override
    public String toString() {
        return "OrderDetailArryBean{" +
                "root=" + root +
                '}';
    }

    /**
     * data : {"no":201612080000000532,"order_date":"2016-12-08 16:46:38","kno":"","name":"","cname":"","received_name":"额鹅鹅","zip":111111,"address":"是佛刚开始就让你赶快来你多少分离开过几年的方式来苦尽甘来大富科技","tel":"","mobile":18602029479,"provinceCode":19,"provinceName":"广东","cityCode":1601,"cityName":"广州市","areaCode":3633,"areaName":"天河区","townCode":35747,"townName":"暨南大学","mid":15,"mname":"邓老凉茶","mowner":"黄锐","mpopularity":103503,"madwords":"送烟送酒不如送健康，买金买银不如买邓老！","logo":{"modifyTime":"","content":"/group1/M00/00/1B/wKgAi1V4VO6ADWfpAABVmZ1MrYI808.jpg"},"date_count":0,"delivery_charges":19.37,"fact_charges":19.37,"delivery_company":"","send_method":"快递","total_price":879.8,"total_winguo":0,"largess_total":0,"status":"已取消","pay_method":0,"pay_status":0,"status_code":4,"is_closed":0,"is_review":0,"message":"","data":{"items":[{"oiid":616,"item_id":85358,"item_code":"","refundState":0,"refid":"","name":"邓老杏仁桔红龟苓膏24碗装","icon":{"modifyTime":1434011368,"content":"http://g1.imgdev.winguo.com/group1/M00/02/3F/wKgAi1V5RuiAHmsnAAH3ZOUP9ks775.jpg"},"sku_id":85350,"maker_id":15,"sku_size":"","size_alias":"尺码","quantity":2,"ship_quantity":2,"unit_price":432,"cost_unit_price":432,"largess_total":0,"color":"","color_name":"","color_alias":"颜色","is_review":0,"discount":"0.0%","discount_type":"不打折"},{"oiid":617,"item_id":85364,"item_code":20054,"refundState":0,"refid":"","name":"邓老汤料系列·山麦金豆汤汤料","icon":{"modifyTime":1434011487,"content":"http://g1.imgdev.winguo.com/group1/M00/02/40/wKgAi1V5R1-AISfaAAVrAOnwXD0567.jpg"},"sku_id":85356,"maker_id":15,"sku_size":"","size_alias":"尺码","quantity":1,"ship_quantity":1,"unit_price":15.8,"cost_unit_price":15.8,"largess_total":0,"color":"","color_name":"","color_alias":"颜色","is_review":0,"discount":"0.0%","discount_type":"不打折"}]}}
     */

    private RootBean root;

    public RootBean getRoot() {
        return root;
    }

    public void setRoot(RootBean root) {
        this.root = root;
    }

    public static class RootBean implements Serializable {
        @Override
        public String toString() {
            return "RootBean{" +
                    "data=" + data +
                    '}';
        }

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean implements Serializable {
            @Override
            public String toString() {
                return "DataBean{" +
                        "no=" + no +
                        ", order_date='" + order_date + '\'' +
                        ", pay_datetime='" + pay_datetime + '\'' +
                        ", maker_deliverydate='" + maker_deliverydate + '\'' +
                        ", confirmreceive_datetime='" + confirmreceive_datetime + '\'' +
                        ", evaluate_datetime='" + evaluate_datetime + '\'' +
                        ", deliveryRootBean=" + deliveryRootBean +
                        ", order_close_date='" + order_close_date + '\'' +
                        ", kno='" + kno + '\'' +
                        ", name='" + name + '\'' +
                        ", cname='" + cname + '\'' +
                        ", received_name='" + received_name + '\'' +
                        ", zip='" + zip + '\'' +
                        ", address='" + address + '\'' +
                        ", tel='" + tel + '\'' +
                        ", mobile=" + mobile +
                        ", provinceCode=" + provinceCode +
                        ", provinceName='" + provinceName + '\'' +
                        ", cityCode=" + cityCode +
                        ", cityName='" + cityName + '\'' +
                        ", areaCode=" + areaCode +
                        ", areaName='" + areaName + '\'' +
                        ", townCode=" + townCode +
                        ", townName='" + townName + '\'' +
                        ", mid=" + mid +
                        ", mname='" + mname + '\'' +
                        ", mowner='" + mowner + '\'' +
                        ", mpopularity=" + mpopularity +
                        ", madwords='" + madwords + '\'' +
                        ", logo=" + logo +
                        ", date_count=" + date_count +
                        ", delivery_charges='" + delivery_charges + '\'' +
                        ", fact_charges='" + fact_charges + '\'' +
                        ", delivery_company='" + delivery_company + '\'' +
                        ", send_method='" + send_method + '\'' +
                        ", total_price='" + total_price + '\'' +
                        ", total_winguo=" + total_winguo +
                        ", largess_total=" + largess_total +
                        ", status='" + status + '\'' +
                        ", pay_method=" + pay_method +
                        ", pay_status=" + pay_status +
                        ", status_code=" + status_code +
                        ", is_closed=" + is_closed +
                        ", is_review=" + is_review +
                        ", message='" + message + '\'' +
                        ", data=" + data +
                        '}';
            }

            private long no;
            /**
             * 下单时间
             */
            private String order_date;
            /**
             * 付款时间
             */
            private String pay_datetime;
            /**
             * 发货时间
             */
            private String maker_deliverydate;
            /**
             * 确认收货时间
             */
            private String confirmreceive_datetime;
            /**
             * 评论时间
             */
            private String evaluate_datetime;
            /**
             * 物流信息
             */
            private DeliveryRootBean deliveryRootBean;
            /**
             * 订单关闭时间
             */
            private String order_close_date;

            private String kno;
            private String name;
            private String cname;
            private String received_name;
            private String zip;
            private String address;
            private String tel;
            private String mobile;
            private int provinceCode;
            private String provinceName;
            private int cityCode;
            private String cityName;
            private int areaCode;
            private String areaName;
            private int townCode;
            private String townName;
            private int mid;
            private String mname;
            private String mowner;
            private int mpopularity;
            private String madwords;
            /**
             * modifyTime :
             * content : /group1/M00/00/1B/wKgAi1V4VO6ADWfpAABVmZ1MrYI808.jpg
             */

            private LogoBean logo;
            private int date_count;
            private String delivery_charges;
            private String fact_charges;
            private String delivery_company;
            private String send_method;
            private String total_price;
            private String total_winguo;
            private int largess_total;
            private String status;
            private int pay_method;
            private int pay_status;
            private int status_code;
            private int is_closed;
            private int is_review;
            private String message;
            private InnerDataBean data;

            public String getOrder_close_date() {
                return order_close_date;
            }

            public void setOrder_close_date(String order_close_date) {
                this.order_close_date = order_close_date;
            }

            public DeliveryRootBean getDeliveryRootBean() {
                return deliveryRootBean;
            }

            public void setDeliveryRootBean(DeliveryRootBean deliveryRootBean) {
                this.deliveryRootBean = deliveryRootBean;
            }

            public InnerDataBean getData() {
                return data;
            }

            public String getPay_datetime() {
                return pay_datetime;
            }

            public void setPay_datetime(String pay_datetime) {
                this.pay_datetime = pay_datetime;
            }

            public String getMaker_deliverydate() {
                return maker_deliverydate;
            }

            public void setMaker_deliverydate(String maker_deliverydate) {
                this.maker_deliverydate = maker_deliverydate;
            }

            public String getConfirmreceive_datetime() {
                return confirmreceive_datetime;
            }

            public void setConfirmreceive_datetime(String confirmreceive_datetime) {
                this.confirmreceive_datetime = confirmreceive_datetime;
            }

            public String getEvaluate_datetime() {
                return evaluate_datetime;
            }

            public void setEvaluate_datetime(String evaluate_datetime) {
                this.evaluate_datetime = evaluate_datetime;
            }

            public void setData(InnerDataBean data) {
                this.data = data;
            }

            public long getNo() {
                return no;
            }

            public void setNo(long no) {
                this.no = no;
            }

            public String getOrder_date() {
                return order_date;
            }

            public void setOrder_date(String order_date) {
                this.order_date = order_date;
            }

            public String getKno() {
                return kno;
            }

            public void setKno(String kno) {
                this.kno = kno;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCname() {
                return cname;
            }

            public void setCname(String cname) {
                this.cname = cname;
            }

            public String getReceived_name() {
                return received_name;
            }

            public void setReceived_name(String received_name) {
                this.received_name = received_name;
            }

            public String getZip() {
                return zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public int getProvinceCode() {
                return provinceCode;
            }

            public void setProvinceCode(int provinceCode) {
                this.provinceCode = provinceCode;
            }

            public String getProvinceName() {
                return provinceName;
            }

            public void setProvinceName(String provinceName) {
                this.provinceName = provinceName;
            }

            public int getCityCode() {
                return cityCode;
            }

            public void setCityCode(int cityCode) {
                this.cityCode = cityCode;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public int getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(int areaCode) {
                this.areaCode = areaCode;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public int getTownCode() {
                return townCode;
            }

            public void setTownCode(int townCode) {
                this.townCode = townCode;
            }

            public String getTownName() {
                return townName;
            }

            public void setTownName(String townName) {
                this.townName = townName;
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

            public String getMowner() {
                return mowner;
            }

            public void setMowner(String mowner) {
                this.mowner = mowner;
            }

            public int getMpopularity() {
                return mpopularity;
            }

            public void setMpopularity(int mpopularity) {
                this.mpopularity = mpopularity;
            }

            public String getMadwords() {
                return madwords;
            }

            public void setMadwords(String madwords) {
                this.madwords = madwords;
            }

            public LogoBean getLogo() {
                return logo;
            }

            public void setLogo(LogoBean logo) {
                this.logo = logo;
            }

            public int getDate_count() {
                return date_count;
            }

            public void setDate_count(int date_count) {
                this.date_count = date_count;
            }

            public String getDelivery_charges() {
                return delivery_charges;
            }

            public void setDelivery_charges(String delivery_charges) {
                this.delivery_charges = delivery_charges;
            }

            public String getFact_charges() {
                return fact_charges;
            }

            public void setFact_charges(String fact_charges) {
                this.fact_charges = fact_charges;
            }

            public String getDelivery_company() {
                return delivery_company;
            }

            public void setDelivery_company(String delivery_company) {
                this.delivery_company = delivery_company;
            }

            public String getSend_method() {
                return send_method;
            }

            public void setSend_method(String send_method) {
                this.send_method = send_method;
            }

            public String getTotal_price() {
                return total_price;
            }

            public void setTotal_price(String total_price) {
                this.total_price = total_price;
            }

            public String getTotal_winguo() {
                return total_winguo;
            }

            public void setTotal_winguo(String total_winguo) {
                this.total_winguo = total_winguo;
            }

            public int getLargess_total() {
                return largess_total;
            }

            public void setLargess_total(int largess_total) {
                this.largess_total = largess_total;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getPay_method() {
                return pay_method;
            }

            public void setPay_method(int pay_method) {
                this.pay_method = pay_method;
            }

            public int getPay_status() {
                return pay_status;
            }

            public void setPay_status(int pay_status) {
                this.pay_status = pay_status;
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

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public static class LogoBean implements Serializable {
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

            public static class InnerDataBean implements Serializable {
                private List<OrderDetailItemBean> items;

                public List<OrderDetailItemBean> getItems() {
                    return items;
                }

                public void setItems(List<OrderDetailItemBean> items) {
                    this.items = items;
                }
            }
        }
    }
}

