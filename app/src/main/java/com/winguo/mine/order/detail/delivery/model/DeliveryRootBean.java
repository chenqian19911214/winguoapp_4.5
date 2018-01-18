package com.winguo.mine.order.detail.delivery.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc 物流信息bean
 */
public class DeliveryRootBean implements Serializable{
    @Override
    public String toString() {
        return "DeliveryRootBean{" +
                "has_msg=" + has_msg +
                ", item=" + item +
                '}';
    }

    private int has_msg;

    private List<ItemBean> item;

    public int getHas_msg() {
        return has_msg;
    }

    public void setHas_msg(int has_msg) {
        this.has_msg = has_msg;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class ItemBean implements Serializable{
        @Override
        public String toString() {
            return "ItemBean{" +
                    "time='" + time + '\'' +
                    ", context='" + context + '\'' +
                    '}';
        }

        private String time;
        private String context;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }
    }
}

