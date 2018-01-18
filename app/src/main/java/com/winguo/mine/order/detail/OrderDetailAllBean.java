package com.winguo.mine.order.detail;

import com.winguo.mine.order.bean.OrderDetailArryBean;
import com.winguo.mine.order.bean.OrderDetailObjBean;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc 总订单详情(自定义)
 */
public class OrderDetailAllBean implements Serializable {
    private OrderDetailObjBean orderDetailObjBean;
    private OrderDetailArryBean orderDetailArryBean;
    private boolean isObject;

    @Override
    public String toString() {
        return "OrderDetailAllBean{" +
                "orderDetailObjBean=" + orderDetailObjBean +
                ", orderDetailArryBean=" + orderDetailArryBean +
                ", isObject=" + isObject +
                '}';
    }

    public OrderDetailObjBean getOrderDetailObjBean() {
        return orderDetailObjBean;
    }

    public void setOrderDetailObjBean(OrderDetailObjBean orderDetailObjBean) {
        this.orderDetailObjBean = orderDetailObjBean;
    }

    public OrderDetailArryBean getOrderDetailArryBean() {
        return orderDetailArryBean;
    }

    public void setOrderDetailArryBean(OrderDetailArryBean orderDetailArryBean) {
        this.orderDetailArryBean = orderDetailArryBean;
    }

    public boolean getIsObject() {
        return isObject;
    }

    public void setIsObject(boolean object) {
        isObject = object;
    }
}
