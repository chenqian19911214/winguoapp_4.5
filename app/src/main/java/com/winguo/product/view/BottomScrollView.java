package com.winguo.product.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.winguo.utils.CommonUtil;


/**
 * Created by Administrator on 2016/12/21.
 */

public class BottomScrollView extends ScrollView {
    private static String TAG = BottomScrollView.class.getName();
    private int downX;
    private int downY;
    public boolean isInterceptChild=false;

    public void setScrollListener(ScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    private ScrollListener mScrollListener;

    public BottomScrollView(Context context) {
        super(context);
    }

    public BottomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) getX();
                downY = (int) getY();
                CommonUtil.printI("ScrollView的滑动事件","+++++++++++++++++++++++++");
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX= (int) getX();
                int moveY= (int) ev.getY();
                int dirY=moveY-downY;
                //滑动的距离
                if (mScrollListener != null) {
                    int contentHeight = getChildAt(0).getHeight();
                    int scrollHeight = getHeight();

                    int scrollY = getScrollY();
                    mScrollListener.onScroll(scrollY);

                    if (scrollY + scrollHeight >= contentHeight || contentHeight <= scrollHeight) {
                        mScrollListener.onScrollToBottom();
                    } else {
                        mScrollListener.notBottom();
                    }

                    if (scrollY == 0&&dirY>0) {
                        mScrollListener.onScrollToTop();
                        CommonUtil.printI("滑动到顶部了","scrollY=0");
                        return false;
                    }

                }

                break;
        }
        boolean result = super.onTouchEvent(ev);
        requestDisallowInterceptTouchEvent(false);

        return result;
    }



    public interface ScrollListener {
        void onScrollToBottom();

        void onScrollToTop();

        void onScroll(int scrollY);

        void notBottom();
    }
}
