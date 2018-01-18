package com.winguo.mine.history;

import java.util.List;

/**
 * @author hcpai
 * @desc 分组item
 */
public class HistoryCategoryItemBean {
    private String date;
    private List<HistoryItemBean> lists;

    @Override
    public String toString() {
        return "HistoryCategoryItemBean{" +
                "date='" + date + '\'' +
                ", lists=" + lists +
                '}';
    }

    public List<HistoryItemBean> getLists() {
        return lists;
    }

    public void setLists(List<HistoryItemBean> lists) {
        this.lists = lists;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 当前类别Item总数。分组也需要占用一个Item
     *
     * @return
     */
    public int getItemCount() {
        return lists.size() + 1;
    }

    /**
     * 获取Item内容
     *
     * @param position
     * @return
     */
    public Object getItem(int position) {
        // Category排在第一位
        if (position == 0) {
            return date;
        } else {
            return lists.get(position - 1);
        }
    }
}
