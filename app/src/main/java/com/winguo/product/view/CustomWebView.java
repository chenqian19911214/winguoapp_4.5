package com.winguo.product.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.winguo.utils.CommonUtil;


/**
 * Created by Administrator on 2016/12/24.
 */

public class CustomWebView extends WebView {

    private int downX;
    private int downY;

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                int moveX= (int) getX();
                int moveY= (int) event.getY();
                CommonUtil.printI("moveY的值是多少======",moveY+"");
                int dirY=moveY-downY;
                //获取当前可见区域到整个页面的距离
                CommonUtil.printI("dirY的值是多少======",dirY+"");
                int scrollY=getScrollY();
                CommonUtil.printI("scrollY的值是多少======",scrollY+"");
                if (scrollY==0&&dirY>0){
//                   return false;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
