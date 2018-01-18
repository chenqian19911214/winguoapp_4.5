package com.winguo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.winguo.R;

/**
 *解决GridView 嵌入到ScrollView 显示不全
 * Created by admin on 2016/12/15.
 */

public class IGridView extends GridView {

    public IGridView(Context context) {
        super(context);
    }

    public IGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandHeight);
    }

}
