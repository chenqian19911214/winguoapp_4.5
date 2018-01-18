package com.winguo.mine.order.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.activity.ProductActivity;
import com.winguo.mine.order.bean.OrderDetailArryBean;
import com.winguo.mine.order.bean.OrderDetailObjBean;
import com.winguo.net.GlideUtil;
import com.winguo.utils.FixedViewUtil;

import java.util.List;

/**
 * @author hcpai
 * @desc 自定义订单详情item的
 */
public class OrderDetailItemView extends LinearLayout {
    private Context mContext;
    private OrderDetailAllBean mOrderDetailAllBean;
    private ListView mListView;

    public OrderDetailItemView(Context context) {
        super(context);
        this.mContext = context;
        //initView();
    }

    public OrderDetailItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public void setData(OrderDetailAllBean orderDetailAllBean) {
        this.mOrderDetailAllBean = orderDetailAllBean;
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

        private boolean mIsObject;

        @Override
        public int getCount() {
            mIsObject = mOrderDetailAllBean.getIsObject();
            if (mIsObject) {
                return 1;
            } else {
                return mOrderDetailAllBean.getOrderDetailArryBean().getRoot().getData().getData().getItems().size();
            }
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
                convertView = View.inflate(mContext, R.layout.order_detail_item, null);
                viewHolder.order_goods_name_tv = (TextView) convertView.findViewById(R.id.order_goods_name_tv);
                viewHolder.order_goods_picture_iv = (ImageView) convertView.findViewById(R.id.order_goods_picture_iv);
                viewHolder.order_discount_price_tv = (TextView) convertView.findViewById(R.id.order_discount_price_tv);
                viewHolder.order_goods_price_tv = (TextView) convertView.findViewById(R.id.order_goods_price_tv);
                viewHolder.order_goods_count_tv = (TextView) convertView.findViewById(R.id.order_goods_count_tv);
                viewHolder.order_goods_rl = (RelativeLayout) convertView.findViewById(R.id.order_goods_rl);
                //颜色 规格
                viewHolder.order_goods_color_tv = (TextView) convertView.findViewById(R.id.order_goods_color_tv);
                viewHolder.order_goods_color2_tv = (TextView) convertView.findViewById(R.id.order_goods_color2_tv);
                viewHolder.order_goods_standard_tv = (TextView) convertView.findViewById(R.id.order_goods_standard_tv);
                viewHolder.order_goods_standard2_tv = (TextView) convertView.findViewById(R.id.order_goods_standard2_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            initData(viewHolder, position);
            return convertView;
        }

        private void initData(ViewHolder viewHolder, final int position) {
            final int gid;
            if (mIsObject) {
                OrderDetailObjBean orderDetailObjBean = mOrderDetailAllBean.getOrderDetailObjBean();
                OrderDetailItemBean item = orderDetailObjBean.getRoot().getData().getData().getItems();
                GlideUtil.getInstance().loadImage(mContext, item.getIcon().getContent(), R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, viewHolder.order_goods_picture_iv);
                viewHolder.order_goods_name_tv.setText(item.getName());
                viewHolder.order_goods_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.order_goods_price_tv.setText("¥ " + item.getCost_unit_price() + "");
                viewHolder.order_discount_price_tv.setText("¥ " + item.getUnit_price() + "");
                viewHolder.order_goods_count_tv.setText("x" + item.getQuantity());
                //颜色规格
                viewHolder.order_goods_color_tv.setText(item.getColor_alias() + ": ");
                viewHolder.order_goods_color2_tv.setText(item.getColor_name().equals("")?"无":item.getColor_name());
                viewHolder.order_goods_standard_tv.setText(item.getSize_alias() + ": ");
                viewHolder.order_goods_standard2_tv.setText(item.getSku_size().equals("")?"无":item.getSku_size());

                gid = item.getItem_id();
            } else {
                OrderDetailArryBean orderDetailArryBean = mOrderDetailAllBean.getOrderDetailArryBean();
                List<OrderDetailItemBean> items = orderDetailArryBean.getRoot().getData().getData().getItems();
                OrderDetailItemBean orderDetailItemBean = items.get(position);
                GlideUtil.getInstance().loadImage(mContext, orderDetailItemBean.getIcon().getContent(), R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, viewHolder.order_goods_picture_iv);
                viewHolder.order_goods_name_tv.setText(orderDetailItemBean.getName());
                viewHolder.order_goods_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.order_goods_price_tv.setText("¥ " + orderDetailItemBean.getCost_unit_price() + "");
                viewHolder.order_discount_price_tv.setText("¥ " + orderDetailItemBean.getUnit_price() + "");
                viewHolder.order_goods_count_tv.setText("x " + orderDetailItemBean.getQuantity());
                //颜色规格
                viewHolder.order_goods_color_tv.setText(orderDetailItemBean.getColor_alias() + ": ");
                viewHolder.order_goods_color2_tv.setText(orderDetailItemBean.getColor_name().equals("")?"无":orderDetailItemBean.getColor_name());
                viewHolder.order_goods_standard_tv.setText(orderDetailItemBean.getSize_alias() + ": ");
                viewHolder.order_goods_standard2_tv.setText(orderDetailItemBean.getSku_size().equals("")?"无":orderDetailItemBean.getSku_size());
                gid = orderDetailItemBean.getItem_id();
            }
            viewHolder.order_goods_rl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("gid", gid);
                    intent.setClass(mContext, ProductActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    class ViewHolder {
        private TextView order_goods_name_tv,
                order_discount_price_tv, order_goods_price_tv, order_goods_count_tv,
                order_goods_color_tv, order_goods_color2_tv, order_goods_standard_tv, order_goods_standard2_tv;
        private ImageView order_goods_picture_iv;
        private RelativeLayout order_goods_rl;
    }
}
