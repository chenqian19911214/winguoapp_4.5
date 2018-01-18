package com.winguo.product.modle;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc 自定义商品评论
 */
public class EvaluateObjectBean implements Serializable{

    private ReviewListBean reviewList;

    public ReviewListBean getReviewList() {
        return reviewList;
    }

    public void setReviewList(ReviewListBean reviewList) {
        this.reviewList = reviewList;
    }

    public static class ReviewListBean implements Serializable{
        private int entity_id;
        private String name;
        private int has_more_items;
        private int count;
        private double avg_score;
        private String service_rate;
        private String delivery_rate;
        private String transport_rate;
        private ItemsBean items;

        public int getEntity_id() {
            return entity_id;
        }

        public void setEntity_id(int entity_id) {
            this.entity_id = entity_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getHas_more_items() {
            return has_more_items;
        }

        public void setHas_more_items(int has_more_items) {
            this.has_more_items = has_more_items;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getAvg_score() {
            return avg_score;
        }

        public void setAvg_score(double avg_score) {
            this.avg_score = avg_score;
        }

        public String getService_rate() {
            return service_rate;
        }

        public void setService_rate(String service_rate) {
            this.service_rate = service_rate;
        }

        public String getDelivery_rate() {
            return delivery_rate;
        }

        public void setDelivery_rate(String delivery_rate) {
            this.delivery_rate = delivery_rate;
        }

        public String getTransport_rate() {
            return transport_rate;
        }

        public void setTransport_rate(String transport_rate) {
            this.transport_rate = transport_rate;
        }

        public ItemsBean getItems() {
            return items;
        }

        public void setItems(ItemsBean items) {
            this.items = items;
        }

        public static class ItemsBean implements Serializable{

            private EvaluateItemBean item;

            public EvaluateItemBean getItem() {
                return item;
            }

            public void setItem(EvaluateItemBean item) {
                this.item = item;
            }
        }
    }
}
