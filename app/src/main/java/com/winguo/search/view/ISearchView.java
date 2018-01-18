package com.winguo.search.view;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */

public interface ISearchView {
    /**
     * 返回搜索关键字列表数据
     * @param items
     */
    void showWordsList(List<String> items);
}
