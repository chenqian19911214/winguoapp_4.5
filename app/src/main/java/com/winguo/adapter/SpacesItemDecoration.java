package com.winguo.adapter;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aispeech.client.IFacadeListener;

/**
 * 为条目（上下左右设置间距）设置分隔线
 * 使用必须RecyclerView 所在的布局背景不能为白色
 * Created by admin on 2017/4/12.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                outRect.right=space;
                outRect.bottom=space;
                outRect.top = space;
                if(parent.getChildAdapterPosition(view)==0){
                    outRect.left=space;
                }
            } else if (orientation == LinearLayoutManager.VERTICAL) {
                outRect.bottom=space;
                if(parent.getChildAdapterPosition(view)==0){
                    outRect.top = space;
                }
            }
        } else {
            outRect.left=space;
            outRect.right=space;
            outRect.bottom=space;
            if(parent.getChildAdapterPosition(view)==0){
                outRect.top=space;
            }
        }

    }
}

