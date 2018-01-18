package com.winguo.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author hcpai
 * @desc ScrollView和ListView嵌套
 */
public class FixedViewUtil {
    public static void setListViewHeightBasedOnChildren(ListView lv) {
        ListAdapter listAdapter = lv.getAdapter();
        int listViewHeight = 0;
        int adaptCount = listAdapter.getCount();
        for (int i = 0; i < adaptCount; i++) {
            View temp = listAdapter.getView(i, null, lv);
            temp.measure(0, 0);
            listViewHeight += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = lv.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = listViewHeight + (lv.getDividerHeight() * (listAdapter.getCount() - 1));
        lv.setLayoutParams(layoutParams);
    }
}
