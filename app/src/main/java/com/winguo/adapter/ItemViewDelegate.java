package com.winguo.adapter;

/**
 * Created by admin on 2017/3/20.
 */

public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(RecylcerViewHolder holder, T t, int position);
}
