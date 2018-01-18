package com.winguo.product.modle.productattribute;

import android.view.View;

import java.util.HashSet;

public abstract class TagAdapter<T> {
    private Attribute mTagDatas;
    private TagFlowLayout mOnDataChangedListener;
    private HashSet<Integer> mCheckedPosList = new HashSet<>();

    public TagAdapter(Attribute ab) {
        mTagDatas = ab;
    }


    static interface OnDataChangedListener {
        void onChanged();
    }

    void setOnDataChangedListener(TagFlowLayout tagFlowLayout) {
        mOnDataChangedListener = tagFlowLayout;
    }

    public void setSelectedList(int... pos) {
        for (int i = 0; i < pos.length; i++)
            mCheckedPosList.add(pos[i]);
        notifyDataChanged();
    }

    public HashSet<Integer> getPreCheckedList() {
        return mCheckedPosList;
    }

    public int getCount() {
        return mTagDatas == null ? 0 : mTagDatas.aliasName.size();
    }

    public void notifyDataChanged() {
        mOnDataChangedListener.onChanged();
    }

    public Attribute getItem(int position) {
        return mTagDatas;
    }

    public abstract View getView(FlowLayout parent, int position, Attribute t);

}