package com.winguo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;
import android.widget.ScrollView;

/**
 * 嵌入到ScrollView 中可滚动的Gridview
 * Created by admin on 2016/12/15.
 */

public class ScrollableGridView extends GridView {

    private ScrollView parentScrollView;
    private int maxHeight;

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public ScrollView getParentScrollView() {
        return parentScrollView;
    }

    public void setParentScrollView(ScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }

    public ScrollableGridView(Context context) {
        super(context);
    }

    public ScrollableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollableGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setParentScrollAble(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                setParentScrollAble(true);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setParentScrollAble(boolean parentScrollAble) {
        parentScrollView.requestDisallowInterceptTouchEvent(!parentScrollAble);
    }
}
