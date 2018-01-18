package com.winguo.bean;

import java.util.List;

/**
 * 实体店商品详情
 */

public class StoreShopDetail {

    /**
     * Code : 0
     * Result : [{"m_entity_item_id":"2","m_entity_item_m_maker_id":"2425","m_entity_item_m_item_id":"85900","m_entity_item_is_long":"0","m_entity_item_start_time":"2017-06-16","m_entity_item_end_time":"2019-01-17","m_entity_item_no_use_date":"","m_entity_item_meet":"1","m_entity_item_shop_server":"0,2,3","m_entity_item_note":"电风扇","m_item_id":"85900","m_item_void_flag":"1","m_item_action_flag":"0","m_item_delete_flag":"0","m_item_except_sale_flag":"0","m_item_t_application_id":"0","m_item_m_maker_id":"2425","m_item_m_category_id":"751","m_item_m_region_province_provinceid":"0","m_item_m_region_city_cityid":"0","m_item_m_region_area_areaid":"0","m_item_type":"751","m_item_code":"","m_item_name":"测试数据","m_item_name_kana":"","m_item_description":"电风扇","m_item_spec":"","m_item_ingredient":"","m_item_property":"","m_item_m_image_id_old":"0","m_item_m_image_id":"634398","m_item_min_price":"100.00","m_item_max_price":"1000.00","m_item_order":"0","m_item_sale_doller":"0.00","m_item_sale_flag":"0","m_item_ruser":"2425","m_item_rdatetime":"2017-06-08 10:58:20","m_item_uuser":"0","m_item_auto_flag":"0","m_item_run_flag":"0","m_item_is_shared":"0","m_item_share_rate":"0.0000","m_item_channel_rate":"0.00","m_item_goods_flag":"0","m_item_differ_price":"0","m_item_autotime_flag":"0","m_item_recommend":"0","m_item_pay_method":"0","m_item_has_invoice":"0","m_item_is_repaired":"0","m_item_is_returned":"0","m_item_fare_type":"2","m_item_t_fare_id":"0","m_item_t_freight_temp_id":"0","m_item_activity_change_except_sale_flag":"0","m_item_t_application_timedate":"0000-00-00 00:00:00","m_item_sale_qty":"0","m_item_sale_manual_qty":"0","m_item_m_brand_id":"0","m_item_brand_type":"0","m_item_client_type":"2","m_item_counts":"0","m_item_main_pic_score":"0","m_item_perfect_degree_score":"0","m_item_quality_score":"0","m_item_title_rationality_score":"0","m_item_colligate_score":"0.00","m_item_net_content":"0.00","m_item_unit":"","m_item_udatetime":"2017-06-13 12:05:01","m_item_down_datetime":"0000-00-00 00:00:00","m_item_m_channel_ids":"","m_item_out_goods_id":"0","m_item_t_out_goods_return_id":"0","m_item_check_status":"0","m_item_is_post":"1","m_item_read_url":"","m_item_list_img":"/group1/M00/00/11/wKgAoVk_T7SAcPfAAACz-MkAfLQ04..png","itemSku":[{"m_sku_id":"86298","m_sku_stock":"10000","m_sku_entity_spec":"150W","m_sku_price":"100.00"},{"m_sku_id":"86306","m_sku_stock":"10000","m_sku_entity_spec":"www","m_sku_price":"1000.00"}],"itemImage":["http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVk-RiaAccp-AAPm0z-mVM827..png","http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVk-RkCAWXkYAAPm0z-mVM846..png","http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVk_T7SAcPfAAACz-MkAfLQ04..png","http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVk_T7uASLZXAAPm0z-mVM823..png","http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVk_T8SAHDKWAAPm0z-mVM890..png"]}]
     */

    private String Code;
    private List<ResultBean> Result;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public List<ResultBean> getResult() {
        return Result;
    }

    public void setResult(List<ResultBean> Result) {
        this.Result = Result;
    }

    public static class ResultBean {
        /**
         * m_entity_item_id : 2
         * m_entity_item_m_maker_id : 2425
         * m_entity_item_m_item_id : 85900
         * m_entity_item_is_long : 0
         * m_entity_item_start_time : 2017-06-16
         * m_entity_item_end_time : 2019-01-17
         * m_entity_item_no_use_date :
         * m_entity_item_meet : 1
         * m_entity_item_shop_server : 0,2,3
         * m_entity_item_note : 电风扇
         * m_item_id : 85900
         * m_item_void_flag : 1
         * m_item_action_flag : 0
         * m_item_delete_flag : 0
         * m_item_except_sale_flag : 0
         * m_item_t_application_id : 0
         * m_item_m_maker_id : 2425
         * m_item_m_category_id : 751
         * m_item_m_region_province_provinceid : 0
         * m_item_m_region_city_cityid : 0
         * m_item_m_region_area_areaid : 0
         * m_item_type : 751
         * m_item_code :
         * m_item_name : 测试数据
         * m_item_name_kana :
         * m_item_description : 电风扇
         * m_item_spec :
         * m_item_ingredient :
         * m_item_property :
         * m_item_m_image_id_old : 0
         * m_item_m_image_id : 634398
         * m_item_min_price : 100.00
         * m_item_max_price : 1000.00
         * m_item_order : 0
         * m_item_sale_doller : 0.00
         * m_item_sale_flag : 0
         * m_item_ruser : 2425
         * m_item_rdatetime : 2017-06-08 10:58:20
         * m_item_uuser : 0
         * m_item_auto_flag : 0
         * m_item_run_flag : 0
         * m_item_is_shared : 0
         * m_item_share_rate : 0.0000
         * m_item_channel_rate : 0.00
         * m_item_goods_flag : 0
         * m_item_differ_price : 0
         * m_item_autotime_flag : 0
         * m_item_recommend : 0
         * m_item_pay_method : 0
         * m_item_has_invoice : 0
         * m_item_is_repaired : 0
         * m_item_is_returned : 0
         * m_item_fare_type : 2
         * m_item_t_fare_id : 0
         * m_item_t_freight_temp_id : 0
         * m_item_activity_change_except_sale_flag : 0
         * m_item_t_application_timedate : 0000-00-00 00:00:00
         * m_item_sale_qty : 0
         * m_item_sale_manual_qty : 0
         * m_item_m_brand_id : 0
         * m_item_brand_type : 0
         * m_item_client_type : 2
         * m_item_counts : 0
         * m_item_main_pic_score : 0
         * m_item_perfect_degree_score : 0
         * m_item_quality_score : 0
         * m_item_title_rationality_score : 0
         * m_item_colligate_score : 0.00
         * m_item_net_content : 0.00
         * m_item_unit :
         * m_item_udatetime : 2017-06-13 12:05:01
         * m_item_down_datetime : 0000-00-00 00:00:00
         * m_item_m_channel_ids :
         * m_item_out_goods_id : 0
         * m_item_t_out_goods_return_id : 0
         * m_item_check_status : 0
         * m_item_is_post : 1
         * m_item_read_url :
         * m_item_list_img : /group1/M00/00/11/wKgAoVk_T7SAcPfAAACz-MkAfLQ04..png
         * itemSku : [{"m_sku_id":"86298","m_sku_stock":"10000","m_sku_entity_spec":"150W","m_sku_price":"100.00"},{"m_sku_id":"86306","m_sku_stock":"10000","m_sku_entity_spec":"www","m_sku_price":"1000.00"}]
         * itemImage : ["http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVk-RiaAccp-AAPm0z-mVM827..png","http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVk-RkCAWXkYAAPm0z-mVM846..png","http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVk_T7SAcPfAAACz-MkAfLQ04..png","http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVk_T7uASLZXAAPm0z-mVM823..png","http://g1.imgdev.winguo.com/group1/M00/00/11/wKgAoVk_T8SAHDKWAAPm0z-mVM890..png"]
         */

        private String m_entity_item_id;
        private String m_entity_item_m_maker_id;
        private String m_entity_item_use_etime;
        private String m_entity_item_use_stime;
        private String m_entity_item_m_item_id;
        private String m_entity_item_is_long;
        private String m_entity_item_start_time;
        private String m_entity_item_end_time;
        private String m_entity_item_no_use_date;
        private String m_entity_item_meet;
        private String m_entity_item_shop_server;
        private String m_entity_item_note;
        private String m_item_id;
        private String m_item_void_flag;
        private String m_item_action_flag;
        private String m_item_delete_flag;
        private String m_item_except_sale_flag;
        private String m_item_t_application_id;
        private String m_item_m_maker_id;
        private String m_item_m_category_id;
        private String m_item_m_region_province_provinceid;
        private String m_item_m_region_city_cityid;
        private String m_item_m_region_area_areaid;
        private String m_item_type;
        private String m_item_code;
        private String m_item_name;
        private String m_item_name_kana;
        private String m_item_description;
        private String m_item_spec;
        private String m_item_ingredient;
        private String m_item_property;
        private String m_item_m_image_id_old;
        private String m_item_m_image_id;
        private String m_item_min_price;
        private String m_item_max_price;
        private String m_item_order;
        private String m_item_sale_doller;
        private String m_item_sale_flag;
        private String m_item_ruser;
        private String m_item_rdatetime;
        private String m_item_uuser;
        private String m_item_auto_flag;
        private String m_item_run_flag;
        private String m_item_is_shared;
        private String m_item_share_rate;
        private String m_item_channel_rate;
        private String m_item_goods_flag;
        private String m_item_differ_price;
        private String m_item_autotime_flag;
        private String m_item_recommend;
        private String m_item_pay_method;
        private String m_item_has_invoice;
        private String m_item_is_repaired;
        private String m_item_is_returned;
        private String m_item_fare_type;
        private String m_item_t_fare_id;
        private String m_item_t_freight_temp_id;
        private String m_item_activity_change_except_sale_flag;
        private String m_item_t_application_timedate;
        private String m_item_sale_qty;
        private String m_item_sale_manual_qty;
        private String m_item_m_brand_id;
        private String m_item_brand_type;
        private String m_item_client_type;
        private String m_item_counts;
        private String m_item_main_pic_score;
        private String m_item_perfect_degree_score;
        private String m_item_quality_score;
        private String m_item_title_rationality_score;
        private String m_item_colligate_score;
        private String m_item_net_content;
        private String m_item_unit;
        private String m_item_udatetime;
        private String m_item_down_datetime;
        private String m_item_m_channel_ids;
        private String m_item_out_goods_id;
        private String m_item_t_out_goods_return_id;
        private String m_item_check_status;
        private String m_item_is_post;
        private String m_item_read_url;
        private String m_item_list_img;
        private  List<StoreShop.ResultBean.SkuBean> itemSku;
        private List<String> itemImage;

        public String getM_entity_item_use_etime() {
            return m_entity_item_use_etime;
        }

        public void setM_entity_item_use_etime(String m_entity_item_use_etime) {
            this.m_entity_item_use_etime = m_entity_item_use_etime;
        }

        public String getM_entity_item_use_stime() {
            return m_entity_item_use_stime;
        }

        public void setM_entity_item_use_stime(String m_entity_item_use_stime) {
            this.m_entity_item_use_stime = m_entity_item_use_stime;
        }

        public String getM_entity_item_id() {
            return m_entity_item_id;
        }

        public void setM_entity_item_id(String m_entity_item_id) {
            this.m_entity_item_id = m_entity_item_id;
        }

        public String getM_entity_item_m_maker_id() {
            return m_entity_item_m_maker_id;
        }

        public void setM_entity_item_m_maker_id(String m_entity_item_m_maker_id) {
            this.m_entity_item_m_maker_id = m_entity_item_m_maker_id;
        }

        public String getM_entity_item_m_item_id() {
            return m_entity_item_m_item_id;
        }

        public void setM_entity_item_m_item_id(String m_entity_item_m_item_id) {
            this.m_entity_item_m_item_id = m_entity_item_m_item_id;
        }

        public String getM_entity_item_is_long() {
            return m_entity_item_is_long;
        }

        public void setM_entity_item_is_long(String m_entity_item_is_long) {
            this.m_entity_item_is_long = m_entity_item_is_long;
        }

        public String getM_entity_item_start_time() {
            return m_entity_item_start_time;
        }

        public void setM_entity_item_start_time(String m_entity_item_start_time) {
            this.m_entity_item_start_time = m_entity_item_start_time;
        }

        public String getM_entity_item_end_time() {
            return m_entity_item_end_time;
        }

        public void setM_entity_item_end_time(String m_entity_item_end_time) {
            this.m_entity_item_end_time = m_entity_item_end_time;
        }

        public String getM_entity_item_no_use_date() {
            return m_entity_item_no_use_date;
        }

        public void setM_entity_item_no_use_date(String m_entity_item_no_use_date) {
            this.m_entity_item_no_use_date = m_entity_item_no_use_date;
        }

        public String getM_entity_item_meet() {
            return m_entity_item_meet;
        }

        public void setM_entity_item_meet(String m_entity_item_meet) {
            this.m_entity_item_meet = m_entity_item_meet;
        }

        public String getM_entity_item_shop_server() {
            return m_entity_item_shop_server;
        }

        public void setM_entity_item_shop_server(String m_entity_item_shop_server) {
            this.m_entity_item_shop_server = m_entity_item_shop_server;
        }

        public String getM_entity_item_note() {
            return m_entity_item_note;
        }

        public void setM_entity_item_note(String m_entity_item_note) {
            this.m_entity_item_note = m_entity_item_note;
        }

        public String getM_item_id() {
            return m_item_id;
        }

        public void setM_item_id(String m_item_id) {
            this.m_item_id = m_item_id;
        }

        public String getM_item_void_flag() {
            return m_item_void_flag;
        }

        public void setM_item_void_flag(String m_item_void_flag) {
            this.m_item_void_flag = m_item_void_flag;
        }

        public String getM_item_action_flag() {
            return m_item_action_flag;
        }

        public void setM_item_action_flag(String m_item_action_flag) {
            this.m_item_action_flag = m_item_action_flag;
        }

        public String getM_item_delete_flag() {
            return m_item_delete_flag;
        }

        public void setM_item_delete_flag(String m_item_delete_flag) {
            this.m_item_delete_flag = m_item_delete_flag;
        }

        public String getM_item_except_sale_flag() {
            return m_item_except_sale_flag;
        }

        public void setM_item_except_sale_flag(String m_item_except_sale_flag) {
            this.m_item_except_sale_flag = m_item_except_sale_flag;
        }

        public String getM_item_t_application_id() {
            return m_item_t_application_id;
        }

        public void setM_item_t_application_id(String m_item_t_application_id) {
            this.m_item_t_application_id = m_item_t_application_id;
        }

        public String getM_item_m_maker_id() {
            return m_item_m_maker_id;
        }

        public void setM_item_m_maker_id(String m_item_m_maker_id) {
            this.m_item_m_maker_id = m_item_m_maker_id;
        }

        public String getM_item_m_category_id() {
            return m_item_m_category_id;
        }

        public void setM_item_m_category_id(String m_item_m_category_id) {
            this.m_item_m_category_id = m_item_m_category_id;
        }

        public String getM_item_m_region_province_provinceid() {
            return m_item_m_region_province_provinceid;
        }

        public void setM_item_m_region_province_provinceid(String m_item_m_region_province_provinceid) {
            this.m_item_m_region_province_provinceid = m_item_m_region_province_provinceid;
        }

        public String getM_item_m_region_city_cityid() {
            return m_item_m_region_city_cityid;
        }

        public void setM_item_m_region_city_cityid(String m_item_m_region_city_cityid) {
            this.m_item_m_region_city_cityid = m_item_m_region_city_cityid;
        }

        public String getM_item_m_region_area_areaid() {
            return m_item_m_region_area_areaid;
        }

        public void setM_item_m_region_area_areaid(String m_item_m_region_area_areaid) {
            this.m_item_m_region_area_areaid = m_item_m_region_area_areaid;
        }

        public String getM_item_type() {
            return m_item_type;
        }

        public void setM_item_type(String m_item_type) {
            this.m_item_type = m_item_type;
        }

        public String getM_item_code() {
            return m_item_code;
        }

        public void setM_item_code(String m_item_code) {
            this.m_item_code = m_item_code;
        }

        public String getM_item_name() {
            return m_item_name;
        }

        public void setM_item_name(String m_item_name) {
            this.m_item_name = m_item_name;
        }

        public String getM_item_name_kana() {
            return m_item_name_kana;
        }

        public void setM_item_name_kana(String m_item_name_kana) {
            this.m_item_name_kana = m_item_name_kana;
        }

        public String getM_item_description() {
            return m_item_description;
        }

        public void setM_item_description(String m_item_description) {
            this.m_item_description = m_item_description;
        }

        public String getM_item_spec() {
            return m_item_spec;
        }

        public void setM_item_spec(String m_item_spec) {
            this.m_item_spec = m_item_spec;
        }

        public String getM_item_ingredient() {
            return m_item_ingredient;
        }

        public void setM_item_ingredient(String m_item_ingredient) {
            this.m_item_ingredient = m_item_ingredient;
        }

        public String getM_item_property() {
            return m_item_property;
        }

        public void setM_item_property(String m_item_property) {
            this.m_item_property = m_item_property;
        }

        public String getM_item_m_image_id_old() {
            return m_item_m_image_id_old;
        }

        public void setM_item_m_image_id_old(String m_item_m_image_id_old) {
            this.m_item_m_image_id_old = m_item_m_image_id_old;
        }

        public String getM_item_m_image_id() {
            return m_item_m_image_id;
        }

        public void setM_item_m_image_id(String m_item_m_image_id) {
            this.m_item_m_image_id = m_item_m_image_id;
        }

        public String getM_item_min_price() {
            return m_item_min_price;
        }

        public void setM_item_min_price(String m_item_min_price) {
            this.m_item_min_price = m_item_min_price;
        }

        public String getM_item_max_price() {
            return m_item_max_price;
        }

        public void setM_item_max_price(String m_item_max_price) {
            this.m_item_max_price = m_item_max_price;
        }

        public String getM_item_order() {
            return m_item_order;
        }

        public void setM_item_order(String m_item_order) {
            this.m_item_order = m_item_order;
        }

        public String getM_item_sale_doller() {
            return m_item_sale_doller;
        }

        public void setM_item_sale_doller(String m_item_sale_doller) {
            this.m_item_sale_doller = m_item_sale_doller;
        }

        public String getM_item_sale_flag() {
            return m_item_sale_flag;
        }

        public void setM_item_sale_flag(String m_item_sale_flag) {
            this.m_item_sale_flag = m_item_sale_flag;
        }

        public String getM_item_ruser() {
            return m_item_ruser;
        }

        public void setM_item_ruser(String m_item_ruser) {
            this.m_item_ruser = m_item_ruser;
        }

        public String getM_item_rdatetime() {
            return m_item_rdatetime;
        }

        public void setM_item_rdatetime(String m_item_rdatetime) {
            this.m_item_rdatetime = m_item_rdatetime;
        }

        public String getM_item_uuser() {
            return m_item_uuser;
        }

        public void setM_item_uuser(String m_item_uuser) {
            this.m_item_uuser = m_item_uuser;
        }

        public String getM_item_auto_flag() {
            return m_item_auto_flag;
        }

        public void setM_item_auto_flag(String m_item_auto_flag) {
            this.m_item_auto_flag = m_item_auto_flag;
        }

        public String getM_item_run_flag() {
            return m_item_run_flag;
        }

        public void setM_item_run_flag(String m_item_run_flag) {
            this.m_item_run_flag = m_item_run_flag;
        }

        public String getM_item_is_shared() {
            return m_item_is_shared;
        }

        public void setM_item_is_shared(String m_item_is_shared) {
            this.m_item_is_shared = m_item_is_shared;
        }

        public String getM_item_share_rate() {
            return m_item_share_rate;
        }

        public void setM_item_share_rate(String m_item_share_rate) {
            this.m_item_share_rate = m_item_share_rate;
        }

        public String getM_item_channel_rate() {
            return m_item_channel_rate;
        }

        public void setM_item_channel_rate(String m_item_channel_rate) {
            this.m_item_channel_rate = m_item_channel_rate;
        }

        public String getM_item_goods_flag() {
            return m_item_goods_flag;
        }

        public void setM_item_goods_flag(String m_item_goods_flag) {
            this.m_item_goods_flag = m_item_goods_flag;
        }

        public String getM_item_differ_price() {
            return m_item_differ_price;
        }

        public void setM_item_differ_price(String m_item_differ_price) {
            this.m_item_differ_price = m_item_differ_price;
        }

        public String getM_item_autotime_flag() {
            return m_item_autotime_flag;
        }

        public void setM_item_autotime_flag(String m_item_autotime_flag) {
            this.m_item_autotime_flag = m_item_autotime_flag;
        }

        public String getM_item_recommend() {
            return m_item_recommend;
        }

        public void setM_item_recommend(String m_item_recommend) {
            this.m_item_recommend = m_item_recommend;
        }

        public String getM_item_pay_method() {
            return m_item_pay_method;
        }

        public void setM_item_pay_method(String m_item_pay_method) {
            this.m_item_pay_method = m_item_pay_method;
        }

        public String getM_item_has_invoice() {
            return m_item_has_invoice;
        }

        public void setM_item_has_invoice(String m_item_has_invoice) {
            this.m_item_has_invoice = m_item_has_invoice;
        }

        public String getM_item_is_repaired() {
            return m_item_is_repaired;
        }

        public void setM_item_is_repaired(String m_item_is_repaired) {
            this.m_item_is_repaired = m_item_is_repaired;
        }

        public String getM_item_is_returned() {
            return m_item_is_returned;
        }

        public void setM_item_is_returned(String m_item_is_returned) {
            this.m_item_is_returned = m_item_is_returned;
        }

        public String getM_item_fare_type() {
            return m_item_fare_type;
        }

        public void setM_item_fare_type(String m_item_fare_type) {
            this.m_item_fare_type = m_item_fare_type;
        }

        public String getM_item_t_fare_id() {
            return m_item_t_fare_id;
        }

        public void setM_item_t_fare_id(String m_item_t_fare_id) {
            this.m_item_t_fare_id = m_item_t_fare_id;
        }

        public String getM_item_t_freight_temp_id() {
            return m_item_t_freight_temp_id;
        }

        public void setM_item_t_freight_temp_id(String m_item_t_freight_temp_id) {
            this.m_item_t_freight_temp_id = m_item_t_freight_temp_id;
        }

        public String getM_item_activity_change_except_sale_flag() {
            return m_item_activity_change_except_sale_flag;
        }

        public void setM_item_activity_change_except_sale_flag(String m_item_activity_change_except_sale_flag) {
            this.m_item_activity_change_except_sale_flag = m_item_activity_change_except_sale_flag;
        }

        public String getM_item_t_application_timedate() {
            return m_item_t_application_timedate;
        }

        public void setM_item_t_application_timedate(String m_item_t_application_timedate) {
            this.m_item_t_application_timedate = m_item_t_application_timedate;
        }

        public String getM_item_sale_qty() {
            return m_item_sale_qty;
        }

        public void setM_item_sale_qty(String m_item_sale_qty) {
            this.m_item_sale_qty = m_item_sale_qty;
        }

        public String getM_item_sale_manual_qty() {
            return m_item_sale_manual_qty;
        }

        public void setM_item_sale_manual_qty(String m_item_sale_manual_qty) {
            this.m_item_sale_manual_qty = m_item_sale_manual_qty;
        }

        public String getM_item_m_brand_id() {
            return m_item_m_brand_id;
        }

        public void setM_item_m_brand_id(String m_item_m_brand_id) {
            this.m_item_m_brand_id = m_item_m_brand_id;
        }

        public String getM_item_brand_type() {
            return m_item_brand_type;
        }

        public void setM_item_brand_type(String m_item_brand_type) {
            this.m_item_brand_type = m_item_brand_type;
        }

        public String getM_item_client_type() {
            return m_item_client_type;
        }

        public void setM_item_client_type(String m_item_client_type) {
            this.m_item_client_type = m_item_client_type;
        }

        public String getM_item_counts() {
            return m_item_counts;
        }

        public void setM_item_counts(String m_item_counts) {
            this.m_item_counts = m_item_counts;
        }

        public String getM_item_main_pic_score() {
            return m_item_main_pic_score;
        }

        public void setM_item_main_pic_score(String m_item_main_pic_score) {
            this.m_item_main_pic_score = m_item_main_pic_score;
        }

        public String getM_item_perfect_degree_score() {
            return m_item_perfect_degree_score;
        }

        public void setM_item_perfect_degree_score(String m_item_perfect_degree_score) {
            this.m_item_perfect_degree_score = m_item_perfect_degree_score;
        }

        public String getM_item_quality_score() {
            return m_item_quality_score;
        }

        public void setM_item_quality_score(String m_item_quality_score) {
            this.m_item_quality_score = m_item_quality_score;
        }

        public String getM_item_title_rationality_score() {
            return m_item_title_rationality_score;
        }

        public void setM_item_title_rationality_score(String m_item_title_rationality_score) {
            this.m_item_title_rationality_score = m_item_title_rationality_score;
        }

        public String getM_item_colligate_score() {
            return m_item_colligate_score;
        }

        public void setM_item_colligate_score(String m_item_colligate_score) {
            this.m_item_colligate_score = m_item_colligate_score;
        }

        public String getM_item_net_content() {
            return m_item_net_content;
        }

        public void setM_item_net_content(String m_item_net_content) {
            this.m_item_net_content = m_item_net_content;
        }

        public String getM_item_unit() {
            return m_item_unit;
        }

        public void setM_item_unit(String m_item_unit) {
            this.m_item_unit = m_item_unit;
        }

        public String getM_item_udatetime() {
            return m_item_udatetime;
        }

        public void setM_item_udatetime(String m_item_udatetime) {
            this.m_item_udatetime = m_item_udatetime;
        }

        public String getM_item_down_datetime() {
            return m_item_down_datetime;
        }

        public void setM_item_down_datetime(String m_item_down_datetime) {
            this.m_item_down_datetime = m_item_down_datetime;
        }

        public String getM_item_m_channel_ids() {
            return m_item_m_channel_ids;
        }

        public void setM_item_m_channel_ids(String m_item_m_channel_ids) {
            this.m_item_m_channel_ids = m_item_m_channel_ids;
        }

        public String getM_item_out_goods_id() {
            return m_item_out_goods_id;
        }

        public void setM_item_out_goods_id(String m_item_out_goods_id) {
            this.m_item_out_goods_id = m_item_out_goods_id;
        }

        public String getM_item_t_out_goods_return_id() {
            return m_item_t_out_goods_return_id;
        }

        public void setM_item_t_out_goods_return_id(String m_item_t_out_goods_return_id) {
            this.m_item_t_out_goods_return_id = m_item_t_out_goods_return_id;
        }

        public String getM_item_check_status() {
            return m_item_check_status;
        }

        public void setM_item_check_status(String m_item_check_status) {
            this.m_item_check_status = m_item_check_status;
        }

        public String getM_item_is_post() {
            return m_item_is_post;
        }

        public void setM_item_is_post(String m_item_is_post) {
            this.m_item_is_post = m_item_is_post;
        }

        public String getM_item_read_url() {
            return m_item_read_url;
        }

        public void setM_item_read_url(String m_item_read_url) {
            this.m_item_read_url = m_item_read_url;
        }

        public String getM_item_list_img() {
            return m_item_list_img;
        }

        public void setM_item_list_img(String m_item_list_img) {
            this.m_item_list_img = m_item_list_img;
        }

        public List<StoreShop.ResultBean.SkuBean> getItemSku() {
            return itemSku;
        }

        public void setItemSku( List<StoreShop.ResultBean.SkuBean> itemSku) {
            this.itemSku = itemSku;
        }

        public List<String> getItemImage() {
            return itemImage;
        }

        public void setItemImage(List<String> itemImage) {
            this.itemImage = itemImage;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "m_entity_item_id='" + m_entity_item_id + '\'' +
                    ", m_entity_item_m_maker_id='" + m_entity_item_m_maker_id + '\'' +
                    ", m_entity_item_use_etime='" + m_entity_item_use_etime + '\'' +
                    ", m_entity_item_use_stime='" + m_entity_item_use_stime + '\'' +
                    ", m_entity_item_m_item_id='" + m_entity_item_m_item_id + '\'' +
                    ", m_entity_item_is_long='" + m_entity_item_is_long + '\'' +
                    ", m_entity_item_start_time='" + m_entity_item_start_time + '\'' +
                    ", m_entity_item_end_time='" + m_entity_item_end_time + '\'' +
                    ", m_entity_item_no_use_date='" + m_entity_item_no_use_date + '\'' +
                    ", m_entity_item_meet='" + m_entity_item_meet + '\'' +
                    ", m_entity_item_shop_server='" + m_entity_item_shop_server + '\'' +
                    ", m_entity_item_note='" + m_entity_item_note + '\'' +
                    ", m_item_id='" + m_item_id + '\'' +
                    ", m_item_void_flag='" + m_item_void_flag + '\'' +
                    ", m_item_action_flag='" + m_item_action_flag + '\'' +
                    ", m_item_delete_flag='" + m_item_delete_flag + '\'' +
                    ", m_item_except_sale_flag='" + m_item_except_sale_flag + '\'' +
                    ", m_item_t_application_id='" + m_item_t_application_id + '\'' +
                    ", m_item_m_maker_id='" + m_item_m_maker_id + '\'' +
                    ", m_item_m_category_id='" + m_item_m_category_id + '\'' +
                    ", m_item_m_region_province_provinceid='" + m_item_m_region_province_provinceid + '\'' +
                    ", m_item_m_region_city_cityid='" + m_item_m_region_city_cityid + '\'' +
                    ", m_item_m_region_area_areaid='" + m_item_m_region_area_areaid + '\'' +
                    ", m_item_type='" + m_item_type + '\'' +
                    ", m_item_code='" + m_item_code + '\'' +
                    ", m_item_name='" + m_item_name + '\'' +
                    ", m_item_name_kana='" + m_item_name_kana + '\'' +
                    ", m_item_description='" + m_item_description + '\'' +
                    ", m_item_spec='" + m_item_spec + '\'' +
                    ", m_item_ingredient='" + m_item_ingredient + '\'' +
                    ", m_item_property='" + m_item_property + '\'' +
                    ", m_item_m_image_id_old='" + m_item_m_image_id_old + '\'' +
                    ", m_item_m_image_id='" + m_item_m_image_id + '\'' +
                    ", m_item_min_price='" + m_item_min_price + '\'' +
                    ", m_item_max_price='" + m_item_max_price + '\'' +
                    ", m_item_order='" + m_item_order + '\'' +
                    ", m_item_sale_doller='" + m_item_sale_doller + '\'' +
                    ", m_item_sale_flag='" + m_item_sale_flag + '\'' +
                    ", m_item_ruser='" + m_item_ruser + '\'' +
                    ", m_item_rdatetime='" + m_item_rdatetime + '\'' +
                    ", m_item_uuser='" + m_item_uuser + '\'' +
                    ", m_item_auto_flag='" + m_item_auto_flag + '\'' +
                    ", m_item_run_flag='" + m_item_run_flag + '\'' +
                    ", m_item_is_shared='" + m_item_is_shared + '\'' +
                    ", m_item_share_rate='" + m_item_share_rate + '\'' +
                    ", m_item_channel_rate='" + m_item_channel_rate + '\'' +
                    ", m_item_goods_flag='" + m_item_goods_flag + '\'' +
                    ", m_item_differ_price='" + m_item_differ_price + '\'' +
                    ", m_item_autotime_flag='" + m_item_autotime_flag + '\'' +
                    ", m_item_recommend='" + m_item_recommend + '\'' +
                    ", m_item_pay_method='" + m_item_pay_method + '\'' +
                    ", m_item_has_invoice='" + m_item_has_invoice + '\'' +
                    ", m_item_is_repaired='" + m_item_is_repaired + '\'' +
                    ", m_item_is_returned='" + m_item_is_returned + '\'' +
                    ", m_item_fare_type='" + m_item_fare_type + '\'' +
                    ", m_item_t_fare_id='" + m_item_t_fare_id + '\'' +
                    ", m_item_t_freight_temp_id='" + m_item_t_freight_temp_id + '\'' +
                    ", m_item_activity_change_except_sale_flag='" + m_item_activity_change_except_sale_flag + '\'' +
                    ", m_item_t_application_timedate='" + m_item_t_application_timedate + '\'' +
                    ", m_item_sale_qty='" + m_item_sale_qty + '\'' +
                    ", m_item_sale_manual_qty='" + m_item_sale_manual_qty + '\'' +
                    ", m_item_m_brand_id='" + m_item_m_brand_id + '\'' +
                    ", m_item_brand_type='" + m_item_brand_type + '\'' +
                    ", m_item_client_type='" + m_item_client_type + '\'' +
                    ", m_item_counts='" + m_item_counts + '\'' +
                    ", m_item_main_pic_score='" + m_item_main_pic_score + '\'' +
                    ", m_item_perfect_degree_score='" + m_item_perfect_degree_score + '\'' +
                    ", m_item_quality_score='" + m_item_quality_score + '\'' +
                    ", m_item_title_rationality_score='" + m_item_title_rationality_score + '\'' +
                    ", m_item_colligate_score='" + m_item_colligate_score + '\'' +
                    ", m_item_net_content='" + m_item_net_content + '\'' +
                    ", m_item_unit='" + m_item_unit + '\'' +
                    ", m_item_udatetime='" + m_item_udatetime + '\'' +
                    ", m_item_down_datetime='" + m_item_down_datetime + '\'' +
                    ", m_item_m_channel_ids='" + m_item_m_channel_ids + '\'' +
                    ", m_item_out_goods_id='" + m_item_out_goods_id + '\'' +
                    ", m_item_t_out_goods_return_id='" + m_item_t_out_goods_return_id + '\'' +
                    ", m_item_check_status='" + m_item_check_status + '\'' +
                    ", m_item_is_post='" + m_item_is_post + '\'' +
                    ", m_item_read_url='" + m_item_read_url + '\'' +
                    ", m_item_list_img='" + m_item_list_img + '\'' +
                    ", itemSku=" + itemSku +
                    ", itemImage=" + itemImage +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "StoreShopDetail{" +
                "Code='" + Code + '\'' +
                ", Result=" + Result +
                ",getM_item_max_price"+Result.get(0).toString()+
                '}';
    }
}
