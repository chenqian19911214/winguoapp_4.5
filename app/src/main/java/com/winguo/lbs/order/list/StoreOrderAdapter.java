package com.winguo.lbs.order.list;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.net.GlideUtil;


/**
 * @author hcpai
 * @desc ${TODD}
 */

public class StoreOrderAdapter extends BaseQuickAdapter<StoreOrderBean.ContentBean, BaseViewHolder> {
    private IBaseViewHolderListener mListener;

    public StoreOrderAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreOrderBean.ContentBean item) {
        //店铺信息
        StoreOrderBean.ContentBean.MakerBean maker = item.getMaker();
        //商品信息
        StoreOrderBean.ContentBean.ItemBean itemBean = item.getItem().get(0);

        //根据订单状态初始化view
        initViewByStatus(helper, item);


        //店铺logo
        ImageView store_order_shop_logo_iv = helper.getView(R.id.store_order_shop_logo_iv);
        GlideUtil.getInstance().loadImage(mContext, maker.getLogo_url(), R.drawable.column_theme_loading_bg,
                R.drawable.column_theme_error_bg, store_order_shop_logo_iv);
        //店铺名称
        helper.setText(R.id.store_order_shop_name_tv, maker.getM_maker_name_ch());


        //商品logo
        ImageView store_order_goods_logo_iv = helper.getView(R.id.store_order_goods_logo_iv);
        GlideUtil.getInstance().loadImage(mContext, itemBean.getImg_url(), R.drawable.column_theme_loading_bg,
                R.drawable.column_theme_error_bg, store_order_goods_logo_iv);
        helper.setText(R.id.store_order_goods_name_tv, itemBean.getName());
        //商品价格
        helper.setText(R.id.store_order_goods_price_tv, String.format(mContext.getString(R.string.cart_goods_price), itemBean.getPrice()));
        //商品数量
        helper.setText(R.id.store_order_goods_number_tv, String.format(mContext.getString(R.string.store_order_goods_number), itemBean.getNum().split("\\.")[0]));

        //一个订单多种商品
        ImageView order_list_more_iv = helper.getView(R.id.order_list_more_iv);
        order_list_more_iv.setVisibility(item.getItem().size() > 1 ? View.VISIBLE : View.GONE);
        //StoreOrderListItemView store_order_list_item = helper.getView(R.id.store_order_list_item);
        //store_order_list_item.setData(item.getItem());


        //订单时间
        helper.setText(R.id.store_order_time_tv, maker.getTime());
        //订单数量
        helper.setText(R.id.store_order_number_tv, String.format(mContext.getString(R.string.store_order_number), maker.getCount()));
        //订单价格
        helper.setText(R.id.store_order_price_tv, String.format(mContext.getString(R.string.store_order_price), maker.getT_juchu_price()));
    }


    private void initViewByStatus(BaseViewHolder helper, final StoreOrderBean.ContentBean item) {
        StoreOrderBean.ContentBean.MakerBean maker = item.getMaker();
        //订单状态
        final String t_juchu_payment_status = maker.getT_juchu_payment_status();
        Button store_order_right_btn = helper.getView(R.id.store_order_right_btn);
        store_order_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.callback(item);
            }
        });
        //待付款
        if ("1".equals(t_juchu_payment_status)) {
            helper.setText(R.id.store_order_state_tv, "待付款");
            store_order_right_btn.setBackgroundColor(mContext.getResources().getColor(R.color.title_color));
            store_order_right_btn.setTextColor(mContext.getResources().getColor(R.color.white));
            store_order_right_btn.setText("立即付款");
            //待备货
        } else if ("2".equals(t_juchu_payment_status)) {
            helper.setText(R.id.store_order_state_tv, "待备货");
            store_order_right_btn.setBackgroundColor(mContext.getResources().getColor(R.color.title_color));
            store_order_right_btn.setTextColor(mContext.getResources().getColor(R.color.white));
            store_order_right_btn.setText("提醒备货");
            //待提货
        } else if ("3".equals(t_juchu_payment_status)) {
            helper.setText(R.id.store_order_state_tv, "待提货");
            store_order_right_btn.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.store_order_btn_bg));
            store_order_right_btn.setTextColor(mContext.getResources().getColor(R.color.title_color));
            store_order_right_btn.setText("提货码");
            //已关闭
        } else if ("4".equals(t_juchu_payment_status)) {
            helper.setText(R.id.store_order_state_tv, "已关闭");
            store_order_right_btn.setVisibility(View.GONE);
        } else if ("5".equals(t_juchu_payment_status)) {
            helper.setText(R.id.store_order_state_tv, "已完成");
            store_order_right_btn.setVisibility(View.GONE);
        }
    }

    /**
     * 自定义接口
     */
    interface IBaseViewHolderListener {
        void callback(StoreOrderBean.ContentBean item);
    }


    /**
     * 自定义方法
     */
    public void setListener(IBaseViewHolderListener listener) {
        this.mListener = listener;
    }
}
