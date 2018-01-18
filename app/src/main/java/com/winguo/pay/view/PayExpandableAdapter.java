package com.winguo.pay.view;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.winguo.R;
import com.winguo.net.GlideUtil;
import com.winguo.pay.modle.bean.DataBean;
import com.winguo.pay.modle.bean.ExpressMethodBean;
import com.winguo.pay.modle.bean.ItemBean;
import com.winguo.pay.modle.store.selfbean.SelfShopInfo;

import java.util.List;

/**
 * Created by Admin on 2017/1/8.
 */

public class PayExpandableAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private List<SelfShopInfo> shops;
    private FrameLayout fl_cintainer;
    private TextView totalCount;


    private OnClickPropertyInterface onClickPropertyInterface;
    private int mTouchPosition = -1;


    /**
     * @param context 上下文
     * @param shops   店铺和商品的数据
     */
    public PayExpandableAdapter(Activity context, List<SelfShopInfo> shops, FrameLayout fl_cintainer, TextView totalCount, TextView totalPrice) {
        this.context = context;
        this.shops = shops;
        this.fl_cintainer = fl_cintainer;
        this.totalCount = totalCount;
    }


    @Override
    public int getGroupCount() {
        int groupCount = 0;
        if (shops != null) {
            groupCount = shops.size();
        }
        return groupCount;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        SelfShopInfo shopInfoBean = shops.get(groupPosition);
        List<DataBean> datas = shopInfoBean.goodsItem.data;
        int childrenCount = 0;
        if (datas != null) {
            childrenCount = datas.size();
        }
        return childrenCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return shops.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<DataBean> dataBeen = shops.get(groupPosition).goodsItem.data;
        return dataBeen.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.order_group_item, null);
            groupHolder = new GroupHolder();
            groupHolder.order_group_name = (TextView) convertView.findViewById(R.id.order_group_name);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        if (shops != null) {
            SelfShopInfo shopInfoBean = shops.get(groupPosition);
            groupHolder.order_group_name.setText(shopInfoBean.shopName);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final GoodsHolder goodsHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.order_goods_item, null);
            goodsHolder = new GoodsHolder();
            goodsHolder.iv_order_goods_icon = (ImageView) convertView.findViewById(R.id.iv_order_goods_icon);
            goodsHolder.tv_order_goods_name = (TextView) convertView.findViewById(R.id.tv_order_goods_name);
            goodsHolder.tv_order_goods_price = (TextView) convertView.findViewById(R.id.tv_order_goods_price);
            goodsHolder.ll_order_goods_color = (LinearLayout) convertView.findViewById(R.id.ll_order_goods_color);
            goodsHolder.ll_order_goods_size = (LinearLayout) convertView.findViewById(R.id.ll_order_goods_size);
            goodsHolder.tv_order_goods_color = (TextView) convertView.findViewById(R.id.tv_order_goods_color);
            goodsHolder.tv_order_goods_size = (TextView) convertView.findViewById(R.id.tv_order_goods_size);
            goodsHolder.tv_order_goods_count = (TextView) convertView.findViewById(R.id.tv_order_goods_count);
            //商品属性的部分
            goodsHolder.ll_order_property_container = (LinearLayout) convertView.findViewById(R.id.ll_order_property_container);
            goodsHolder.rl_order_sales_promotion = (RelativeLayout) convertView.findViewById(R.id.rl_order_sales_promotion); //现金券
            goodsHolder.tv_order_sales_promotion_cb = (CheckBox) convertView.findViewById(R.id.tv_order_sales_promotion_cb); //现金券 选中按钮
            goodsHolder.rl_order_dispatch_method = (RelativeLayout) convertView.findViewById(R.id.rl_order_dispatch_method);
            goodsHolder.tv_order_sales_method_cash = (TextView) convertView.findViewById(R.id.tv_order_sales_method_cash);
            goodsHolder.tv_order_transport_method = (TextView) convertView.findViewById(R.id.tv_order_transport_method);
            goodsHolder.et_order_message = (EditText) convertView.findViewById(R.id.et_order_message);
            convertView.setTag(goodsHolder);
        } else {
            goodsHolder = (GoodsHolder) convertView.getTag();
        }
        if (isLastChild) {
            goodsHolder.ll_order_property_container.setVisibility(View.VISIBLE);
        } else {
            goodsHolder.ll_order_property_container.setVisibility(View.GONE);
        }
        //获取数据
        List<DataBean> data = shops.get(groupPosition).goodsItem.data;
        DataBean dataBean = data.get(childPosition);
        if (isLastChild) {
            GlideUtil.getInstance().loadImage(context, dataBean.icon.content,
                    R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, goodsHolder.iv_order_goods_icon);
            goodsHolder.tv_order_goods_name.setText(dataBean.name);
            goodsHolder.tv_order_goods_price.setText(String.valueOf(dataBean.price));

            if (dataBean.color_name.length() == 0) {//颜色不存在
                goodsHolder.ll_order_goods_color.setVisibility(View.GONE);
            } else {
                goodsHolder.ll_order_goods_color.setVisibility(View.VISIBLE);
                goodsHolder.tv_order_goods_color.setText(dataBean.color_name);
            }
            if (dataBean.size.length() == 0) {//尺码不存在
                goodsHolder.ll_order_goods_size.setVisibility(View.GONE);
            } else {
                goodsHolder.ll_order_goods_size.setVisibility(View.VISIBLE);
                goodsHolder.tv_order_goods_size.setText(dataBean.size);
            }
            goodsHolder.tv_order_goods_count.setText(String.valueOf(dataBean.num));
            //这里得到每个商品的配送费
            List<ItemBean> item = shops.get(groupPosition).goodsItem.expressMethodBean.expressage.item;
            for (ItemBean itemBean : item) {
                if (itemBean.isChoosed) {
                    goodsHolder.tv_order_transport_method.setText(itemBean.name + " " + itemBean.price + " 元");
                }
            }
            //editText点击问题
            goodsHolder.et_order_message.setTag(groupPosition);
        } else {
            GlideUtil.getInstance().loadImage(context, dataBean.icon.content,
                    R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, goodsHolder.iv_order_goods_icon);
            goodsHolder.tv_order_goods_name.setText(dataBean.name);
            goodsHolder.tv_order_goods_price.setText(String.valueOf(dataBean.price));

            if (dataBean.color_name.length() == 0) {//颜色不存在
                goodsHolder.ll_order_goods_color.setVisibility(View.GONE);
            } else {
                goodsHolder.ll_order_goods_color.setVisibility(View.VISIBLE);
                goodsHolder.tv_order_goods_color.setText(dataBean.color_name);
            }
            if (dataBean.size.length() == 0) {//尺码不存在
                goodsHolder.ll_order_goods_size.setVisibility(View.GONE);
            } else {
                goodsHolder.ll_order_goods_size.setVisibility(View.VISIBLE);
                goodsHolder.tv_order_goods_size.setText(dataBean.size);
            }
            goodsHolder.tv_order_goods_count.setText(String.valueOf(dataBean.num));
        }
        setListener(goodsHolder, groupPosition, childPosition, shops.get(groupPosition).goodsItem.expressMethodBean);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private void setListener(final GoodsHolder goodsHolder, final int groupPosition, final int childPosition, final ExpressMethodBean expressMethodBean) {
        goodsHolder.rl_order_sales_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        goodsHolder.rl_order_dispatch_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsHolder.et_order_message.clearFocus();
                //修改配送方式
                //弹出窗体
                OrderExpressageWindow orderExpressageWindow = new OrderExpressageWindow(context, expressMethodBean, goodsHolder.tv_order_transport_method, PayExpandableAdapter.this);
                orderExpressageWindow.showAtLocation(fl_cintainer, Gravity.BOTTOM, 0, 0);
            }
        });

        goodsHolder.et_order_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //获取到输入的东西，并回传给activity
                String msg = goodsHolder.et_order_message.getText().toString().trim();
                shops.get(groupPosition).goodsItem.userMsg = msg;
            }
        });

    }


    public void setOnClickPropertyInterface(OnClickPropertyInterface onClickPropertyInterface) {

        this.onClickPropertyInterface = onClickPropertyInterface;
    }


    //点击促销和配送方式的回调接口
    public interface OnClickPropertyInterface {
        void onClickSales(String gid, TextView textView);

        void onClickDispatchMethod(String gid, TextView textView);
    }
}

class GroupHolder {
    TextView order_group_name;
}

class GoodsHolder {
    ImageView iv_order_goods_icon;
    CheckBox tv_order_sales_promotion_cb;
    TextView tv_order_goods_name, tv_order_goods_price;
    LinearLayout ll_order_goods_color, ll_order_goods_size;
    TextView tv_order_goods_color, tv_order_goods_size, tv_order_goods_count;
    LinearLayout ll_order_property_container;
    RelativeLayout rl_order_sales_promotion, rl_order_dispatch_method;
    TextView tv_order_sales_method_cash, tv_order_transport_method;
    EditText et_order_message;
}
