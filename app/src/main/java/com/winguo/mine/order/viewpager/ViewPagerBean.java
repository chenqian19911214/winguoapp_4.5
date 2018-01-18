package com.winguo.mine.order.viewpager;

import com.winguo.mine.order.detail.OrderDetailAllBean;
import com.winguo.mine.order.bean.OrderDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */

public class ViewPagerBean {
    private int page;
    private List<OrderDetailAllBean> orderDetailAllBeanList;
    private ArrayList<OrderDataBean> adapterDatas;
    private boolean isEmpty;
    private boolean isFirst;

    @Override
    public String toString() {
        return "ViewPagerBean{" +
                "page=" + page +
                ", orderDetailAllBeanList=" + orderDetailAllBeanList +
                ", adapterDatas=" + adapterDatas +
                ", isEmpty=" + isEmpty +
                ", isFirst=" + isFirst +
                '}';
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<OrderDetailAllBean> getOrderDetailAllBeanList() {
        return orderDetailAllBeanList;
    }

    public void setOrderDetailAllBeanList(List<OrderDetailAllBean> orderDetailAllBeanList) {
        this.orderDetailAllBeanList = orderDetailAllBeanList;
    }

    public ArrayList<OrderDataBean> getAdapterDatas() {
        return adapterDatas;
    }

    public void setAdapterDatas(ArrayList<OrderDataBean> adapterDatas) {
        this.adapterDatas = adapterDatas;
    }
}
