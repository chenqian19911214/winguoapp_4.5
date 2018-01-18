package com.winguo.mine.order.detail.delivery.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.winguo.R;
import com.winguo.mine.order.detail.delivery.model.LogisticsAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义节点ListView
 *
 * @author zad
 */
public class CustomNodeListView extends LinearLayout {

    private CustomNodeLineView nodeLineView;
    private CustomListView listView;
    private BaseAdapter adapter;
    private List<Float> nodeRadiusDistances;

    public CustomNodeListView(Context context) {
        this(context, null);
    }

    public CustomNodeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public CustomNodeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(
                R.layout.custom_node_listview_layout, this);
        this.nodeLineView = (CustomNodeLineView) this
                .findViewById(R.id.nodeLineView);
        this.listView = (CustomListView) this.findViewById(R.id.listView);
        this.listView.setEnabled(false);
        this.nodeRadiusDistances = new ArrayList<>();
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        this.listView.setAdapter(adapter);
        this.nodeRadiusDistances = ((LogisticsAdapter) adapter).getNodeRadiusDistances();
        //最后一个用于添加listview底部分割线高度
        this.nodeRadiusDistances.set(this.nodeRadiusDistances.size() - 1, (float) this.listView.getDividerHeight());
        this.setNodeCount(adapter.getCount());
        this.setNodeRadiusDistances(nodeRadiusDistances);
        invalidate();
    }

    private void setNodeRadiusDistances(List<Float> nodeRadiusDistances) {
        this.nodeLineView.setNodeRadiusDistances(nodeRadiusDistances);
        invalidate();
    }

    private void setNodeCount(int nodeCount) {
        this.nodeLineView.setNodeCount(nodeCount);
        invalidate();
    }

    public void addHeaderView(View view) {
        this.setOrientation(LinearLayout.VERTICAL);
        this.addView(view, 0);
        invalidate();
    }
}
