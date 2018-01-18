package com.winguo.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

/**
 * Created by admin on 2017/3/20.
 */

public abstract class RecyclerCommonAdapter<T> extends RecyclerAdapter<T> {

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public RecyclerCommonAdapter(final Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                return true;
            }

            @Override
            public void convert(RecylcerViewHolder holder, T t, int position) {
                RecyclerCommonAdapter.this.convert(holder, t, position);
            }

        });
    }

    protected abstract void convert(RecylcerViewHolder holder, T t, int position);
}
