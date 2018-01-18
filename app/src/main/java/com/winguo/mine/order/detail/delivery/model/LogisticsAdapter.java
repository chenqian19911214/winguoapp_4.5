package com.winguo.mine.order.detail.delivery.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;

import com.winguo.R;
import com.winguo.mine.order.bean.DeliveryRootBean;
import com.winguo.mine.order.detail.delivery.view.CustomTextView;

import java.util.ArrayList;
import java.util.List;


public class LogisticsAdapter extends BaseAdapter {

    private List<DeliveryRootBean.ItemBean> mLists;
    private Context context;
    private List<Float> nodeRadiusDistances;

    public LogisticsAdapter(List<DeliveryRootBean.ItemBean> lists, Context context) {
        this.mLists = lists;
        this.context = context;
        this.nodeRadiusDistances = new ArrayList<>();
        for (int i = 0; i < mLists.size() + 2; i++) {
            // 倒数第二个用于装载item顶部内边距,倒数第一个用于装载listview分割线高度
            this.nodeRadiusDistances.add(0.0f);
        }
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //这里只所以不用复用，是因为使用了复用之后测量出item的高度就不准确了
        convertView = LayoutInflater.from(context).inflate(
                R.layout.person_logistics_info_item_line, null);
        CustomTextView info_txt = (CustomTextView) convertView
                .findViewById(R.id.info_txt);
        CustomTextView dateTime_txt = (CustomTextView) convertView
                .findViewById(R.id.dateTime_txt);
        info_txt.setText(mLists.get(position).getContext());
        dateTime_txt.setText(mLists.get(position).getTime());
        //监听View的伸展，以便测量高度
        ViewTreeObserver vto = info_txt.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new MyGlobalLayoutListener(info_txt,
                dateTime_txt, position));

        return convertView;
    }

    public List<Float> getNodeRadiusDistances() {
        return nodeRadiusDistances;
    }

    /**
     * 监听TextView高度的改变
     *
     * @author zad
     */
    private class MyGlobalLayoutListener implements OnGlobalLayoutListener {

        private int position;
        private CustomTextView info_txt;
        private CustomTextView dateTime_txt;

        public MyGlobalLayoutListener(CustomTextView info_txt,
                                      CustomTextView dateTime_txt, int position) {
            this.position = position;
            this.info_txt = info_txt;
            this.dateTime_txt = dateTime_txt;
            // 倒数第二个位置装载顶部内边距
            nodeRadiusDistances.set(nodeRadiusDistances.size() - 2,
                    (float) info_txt.getParentPaddingTop());
        }

        @Override
        public void onGlobalLayout() {
            /**
             * 获取子项的真实高度
             */
            float realHeight = info_txt.getTxtHeight()
                    + info_txt.getParentPaddingTop()
                    + info_txt.getParentPaddingBotton()
                    + dateTime_txt.getTxtHeight();
            LayoutParams childParams = (LayoutParams) info_txt
                    .getLayoutParams();
            realHeight += childParams.bottomMargin;
            if (realHeight > nodeRadiusDistances.get(position)) {
                nodeRadiusDistances.set(position, realHeight);
            }
        }
    }
}
