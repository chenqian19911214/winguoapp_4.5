package com.winguo.mine.order.list;

import com.winguo.mine.order.bean.OrderDataBean;

import java.util.List;

/**
 * @author hcpai
 * @desc 自定义(新版)订单列表bean
 */
public class OrderBeanList {
    /**
     * 是否有更多
     */
    private int has_more_items;
    /**
     * 某种订单总数
     */
    private int count;
    /**
     * 商品订单数据
     */
    private List<OrderDataBean> orderDataBeanList;

    @Override
    public String toString() {
        return "OrderBeanList{" +
                "has_more_items=" + has_more_items +
                ", count=" + count +
                ", orderDataBeanList=" + orderDataBeanList +
                '}';
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

    public List<OrderDataBean> getOrderDataBeanList() {
        return orderDataBeanList;
    }

    public void setOrderDataBeanList(List<OrderDataBean> orderDataBeanList) {
        this.orderDataBeanList = orderDataBeanList;
    }
}
