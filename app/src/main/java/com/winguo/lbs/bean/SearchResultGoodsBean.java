package com.winguo.lbs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class SearchResultGoodsBean implements Serializable {

    /**
     * content : [{"id":"85876","img_url":"","m":"","m_item_m_maker_id":"2365","name":"实体店商品001","price":"200.00","sales":"12","unid":"km"},{"id":"85878","img_url":"","m":"","m_item_m_maker_id":"2385","name":"wintest 实体店","price":"100.00","sales":"2","unid":"km"},{"id":"85879","img_url":"","m":"","m_item_m_maker_id":"2385","name":"wintest 实体店2","price":"110.00","sales":"3","unid":"km"},{"id":"85882","img_url":"","m":"","m_item_m_maker_id":"2402","name":"实体店商品002","price":"500.00","sales":"0","unid":"km"},{"id":"85883","img_url":"","m":"","m_item_m_maker_id":"2402","name":"实体店商品002","price":"200.00","sales":"0","unid":"km"},{"id":"85886","img_url":"","m":"","m_item_m_maker_id":"2386","name":"实体店商品002","price":"1000.00","sales":"0","unid":"km"},{"id":"85887","img_url":"","m":"","m_item_m_maker_id":"2422","name":"实体店商品002","price":"0.00","sales":"0","unid":"km"},{"id":"85888","img_url":"","m":"","m_item_m_maker_id":"2422","name":"实体店63","price":"0.00","sales":"0","unid":"km"},{"id":"85889","img_url":"","m":"","m_item_m_maker_id":"2422","name":"实体店45","price":"0.00","sales":"0","unid":"km"},{"id":"85890","img_url":"","m":"","m_item_m_maker_id":"2422","name":"实体店商品65","price":"20.00","sales":"0","unid":"km"}]
     * page_count : 1
     * size : 10
     */

    private int page_count;
    private int size;
    /**
     * id : 85876
     * img_url :
     * m :
     * m_item_m_maker_id : 2365
     * name : 实体店商品001
     * price : 200.00
     * sales : 12
     * unid : km
     */

    private List<ContentBean> content;

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        private String id;
        private String img_url;
        private String m;
        private String m_item_m_maker_id;
        private String name;
        private String price;
        private String sales;
        private String unid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getM() {
            return m;
        }

        public void setM(String m) {
            this.m = m;
        }

        public String getM_item_m_maker_id() {
            return m_item_m_maker_id;
        }

        public void setM_item_m_maker_id(String m_item_m_maker_id) {
            this.m_item_m_maker_id = m_item_m_maker_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }

        public String getUnid() {
            return unid;
        }

        public void setUnid(String unid) {
            this.unid = unid;
        }
    }
}
