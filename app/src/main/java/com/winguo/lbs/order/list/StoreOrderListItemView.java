package com.winguo.lbs.order.list;

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
import com.winguo.net.GlideUtil;

import java.util.List;

/**
 * @author hcpai
 * @desc 自定义实体店订单列表item的
 */
public class StoreOrderListItemView extends LinearLayout {
    private Context mContext;
    private ListView mListView;
    private List<StoreOrderBean.ContentBean.ItemBean> mData;

    public StoreOrderListItemView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public StoreOrderListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public void setData(List<StoreOrderBean.ContentBean.ItemBean> itemBeans) {
        this.mData = itemBeans;
        mListView.setAdapter(new MyAdapter());
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
            MyViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new MyViewHolder();
                convertView = View.inflate(mContext, R.layout.store_order_goods_item, null);
                viewHolder.store_order_goods_logo_iv = (ImageView) convertView.findViewById(R.id.store_order_goods_logo_iv);
                viewHolder.store_order_goods_name_tv = (TextView) convertView.findViewById(R.id.store_order_goods_name_tv);
                viewHolder.store_order_goods_price_tv = (TextView) convertView.findViewById(R.id.store_order_goods_price_tv);
                viewHolder.store_order_goods_number_tv = (TextView) convertView.findViewById(R.id.store_order_goods_number_tv);
            } else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            initData(viewHolder, position);
            return convertView;
        }

        private void initData(MyViewHolder viewHolder, int position) {
            StoreOrderBean.ContentBean.ItemBean itemBean = mData.get(position);
            GlideUtil.getInstance().loadImage(mContext, itemBean.getImg_url(), R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, viewHolder.store_order_goods_logo_iv);
            viewHolder.store_order_goods_name_tv.setText(itemBean.getName());
            viewHolder.store_order_goods_price_tv.setText(String.format(mContext.getString(R.string.cart_goods_price), itemBean.getPrice()));
            viewHolder.store_order_goods_number_tv.setText(String.format(mContext.getString(R.string.store_order_goods_number), itemBean.getNum().split("\\.")[0]));
        }

    }

    private class MyViewHolder {
        private TextView store_order_goods_name_tv, store_order_goods_price_tv, store_order_goods_number_tv;
        private ImageView store_order_goods_logo_iv;
    }
}
