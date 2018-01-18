package com.winguo.interfaces;

import android.view.View;

/**
 * ListView 内部条目item 控件点击事件回调接口
 * Created by 10632 on 2016/10/17.
 */

public abstract class OnCommentClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        listViewItemClick((Integer) v.getTag(), v);
    }

    public abstract void listViewItemClick(int position, View v);

}
