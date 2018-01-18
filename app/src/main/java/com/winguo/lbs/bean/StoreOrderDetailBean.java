package com.winguo.lbs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class StoreOrderDetailBean implements Serializable {

    /**
     * img_url :
     * m_customer_tel_mobi : 13800138000
     * m_maker_address_ch : 体育东路122号羊城国际商贸中心西塔1303
     * m_maker_id : 2145
     * m_maker_mobile : 13622855072
     * m_maker_name :
     * m_merge_order_no : M201706200000009195
     * t_juchu_datetime : 2017-06-20 17:52:34
     * t_juchu_delivery_received_name : 测试账号1231
     * t_juchu_id : 1475
     * t_juchu_payment_status : 1
     * t_juchu_pick_code : 3702
     * t_juchu_price : 500.00
     */

    private MakerBean maker;
    /**
     * img_url :
     * m_item_name : ZIPPO打火机野狼28001
     * t_juchuitem_price : 125.00
     * t_juchuitem_quantity : 4.00
     */

    private List<ItemBean> item;

    public MakerBean getMaker() {
        return maker;
    }

    public void setMaker(MakerBean maker) {
        this.maker = maker;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class MakerBean {
        private String img_url;
        private String m_customer_tel_mobi;
        private String m_maker_address_ch;
        private String m_maker_id;
        private String m_maker_mobile;
        private String m_maker_name;
        private String m_merge_order_no;
        private String t_juchu_datetime;
        private String t_juchu_delivery_received_name;
        private String t_juchu_id;
        private String t_juchu_payment_status;
        private String t_juchu_pick_code;
        private String t_juchu_price;

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getM_customer_tel_mobi() {
            return m_customer_tel_mobi;
        }

        public void setM_customer_tel_mobi(String m_customer_tel_mobi) {
            this.m_customer_tel_mobi = m_customer_tel_mobi;
        }

        public String getM_maker_address_ch() {
            return m_maker_address_ch;
        }

        public void setM_maker_address_ch(String m_maker_address_ch) {
            this.m_maker_address_ch = m_maker_address_ch;
        }

        public String getM_maker_id() {
            return m_maker_id;
        }

        public void setM_maker_id(String m_maker_id) {
            this.m_maker_id = m_maker_id;
        }

        public String getM_maker_mobile() {
            return m_maker_mobile;
        }

        public void setM_maker_mobile(String m_maker_mobile) {
            this.m_maker_mobile = m_maker_mobile;
        }

        public String getM_maker_name() {
            return m_maker_name;
        }

        public void setM_maker_name(String m_maker_name) {
            this.m_maker_name = m_maker_name;
        }

        public String getM_merge_order_no() {
            return m_merge_order_no;
        }

        public void setM_merge_order_no(String m_merge_order_no) {
            this.m_merge_order_no = m_merge_order_no;
        }

        public String getT_juchu_datetime() {
            return t_juchu_datetime;
        }

        public void setT_juchu_datetime(String t_juchu_datetime) {
            this.t_juchu_datetime = t_juchu_datetime;
        }

        public String getT_juchu_delivery_received_name() {
            return t_juchu_delivery_received_name;
        }

        public void setT_juchu_delivery_received_name(String t_juchu_delivery_received_name) {
            this.t_juchu_delivery_received_name = t_juchu_delivery_received_name;
        }

        public String getT_juchu_id() {
            return t_juchu_id;
        }

        public void setT_juchu_id(String t_juchu_id) {
            this.t_juchu_id = t_juchu_id;
        }

        public String getT_juchu_payment_status() {
            return t_juchu_payment_status;
        }

        public void setT_juchu_payment_status(String t_juchu_payment_status) {
            this.t_juchu_payment_status = t_juchu_payment_status;
        }

        public String getT_juchu_pick_code() {
            return t_juchu_pick_code;
        }

        public void setT_juchu_pick_code(String t_juchu_pick_code) {
            this.t_juchu_pick_code = t_juchu_pick_code;
        }

        public String getT_juchu_price() {
            return t_juchu_price;
        }

        public void setT_juchu_price(String t_juchu_price) {
            this.t_juchu_price = t_juchu_price;
        }
    }

    public static class ItemBean {
        private String img_url;
        private String m_item_name;
        private String t_juchuitem_price;
        private String t_juchuitem_quantity;

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getM_item_name() {
            return m_item_name;
        }

        public void setM_item_name(String m_item_name) {
            this.m_item_name = m_item_name;
        }

        public String getT_juchuitem_price() {
            return t_juchuitem_price;
        }

        public void setT_juchuitem_price(String t_juchuitem_price) {
            this.t_juchuitem_price = t_juchuitem_price;
        }

        public String getT_juchuitem_quantity() {
            return t_juchuitem_quantity;
        }

        public void setT_juchuitem_quantity(String t_juchuitem_quantity) {
            this.t_juchuitem_quantity = t_juchuitem_quantity;
        }
    }
}
