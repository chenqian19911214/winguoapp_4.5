package com.winguo.mine.address.bean;

import com.winguo.mine.history.HistoryItemBean;

import java.util.List;

/**
 * @author hcpai
 * @desc 历史记录bean
 */
public class HistoryBean {

    @Override
    public String toString() {
        return "HistoryBean{" +
                "count=" + count +
                ", has_more=" + has_more +
                ", data=" + data +
                '}';
    }

    private int count;
    private boolean has_more;

    private List<HistoryItemBean> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public List<HistoryItemBean> getData() {
        return data;
    }

    public void setData(List<HistoryItemBean> data) {
        this.data = data;
    }
}
