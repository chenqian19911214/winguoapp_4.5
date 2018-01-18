package com.winguo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 解决ListView 嵌套ScrollView 数据显示不全问题
 * Created by admin on 2016/12/7.
 */

public class IListView extends ListView {

    public IListView(Context context) {
        super(context);
    }

    public IListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandHeight);
    }
}
