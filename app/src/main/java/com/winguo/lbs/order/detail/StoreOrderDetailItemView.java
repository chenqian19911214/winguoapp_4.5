package com.winguo.lbs.order.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.lbs.bean.StoreOrderDetailBean;
import com.winguo.net.GlideUtil;
import com.winguo.utils.FixedViewUtil;

import java.util.List;

/**
 * @author hcpai
 * @desc 自定义实体店订单详情item的
 */
public class StoreOrderDetailItemView extends LinearLayout {
    private Context mContext;
    private ListView mListView;
    private List<StoreOrderDetailBean.ItemBean> mData;

    public StoreOrderDetailItemView(Context context) {
        super(context);
        this.mContext = context;
        //initView();
    }

    public StoreOrderDetailItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public void setData(List<StoreOrderDetailBean.ItemBean> itemBeans) {
        this.mData = itemBeans;
        mListView.setAdapter(new MyAdapter());
        FixedViewUtil.setListViewHeightBasedOnChildren(mListView);
    }

    private void initView() {
        mListView = new ListView(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mListView.setLayoutParams(layoutParams);
        addView(mListView);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.store_order_detail_item, null);
                viewHolder.store_detail_iv = (ImageView) convertView.findViewById(R.id.store_detail_iv);
                viewHolder.store_detail_name_tv = (TextView) convertView.findViewById(R.id.store_detail_name_tv);
                viewHolder.store_detail_price_tv = (TextView) convertView.findViewById(R.id.store_detail_price_tv);
                viewHolder.store_detail_number_tv = (TextView) convertView.findViewById(R.id.store_detail_number_tv);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            initData(viewHolder, position);
            return convertView;
        }

        private void initData(ViewHolder viewHolder, int position) {
            StoreOrderDetailBean.ItemBean itemBean = mData.get(position);
            GlideUtil.getInstance().loadImage(mContext, itemBean.getImg_url(), R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, viewHolder.store_detail_iv);
            viewHolder.store_detail_name_tv.setText(itemBean.getM_item_name());
            viewHolder.store_detail_price_tv.setText(String.format(mContext.getString(R.string.search_result_goods_price), itemBean.getT_juchuitem_price()));
            viewHolder.store_detail_number_tv.setText(String.format(mContext.getString(R.string.store_order_goods_number), itemBean.getT_juchuitem_quantity().split("\\.")[0]));
        }

    }

    class ViewHolder {
        private TextView store_detail_name_tv, store_detail_price_tv, store_detail_number_tv;
        private ImageView store_detail_iv;
    }
}
