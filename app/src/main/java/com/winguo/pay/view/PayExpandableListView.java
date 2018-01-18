package com.winguo.pay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by Admin on 2017/1/9.
 */

public class PayExpandableListView extends ExpandableListView {
    public PayExpandableListView(Context context) {
        super(context);
    }

    public PayExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PayExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
