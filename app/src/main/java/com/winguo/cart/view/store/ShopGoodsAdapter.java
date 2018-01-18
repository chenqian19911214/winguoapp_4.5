package com.winguo.cart.view.store;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.winguo.R;
import com.winguo.cart.bean.DataBean;
import com.winguo.cart.model.store.seekcart.ShopInfoBean;
import com.winguo.net.GlideUtil;
import com.winguo.utils.CommonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 2017/1/5.
 */

public class ShopGoodsAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ShopInfoBean> shopInfos;
    private CheckInterface checkInterface;
    private ChangedCountInterface changedCountInterface;
    private int is_prompt = 0;//不是立即购买商品

    public ShopGoodsAdapter(Context mContext, List<ShopInfoBean> shopInfos, HashMap<String, List<DataBean>> datas) {
        this.mContext = mContext;
        this.shopInfos = shopInfos;
    }

    @Override
    public int getGroupCount() {
        int groupCount = 0;
        if (shopInfos != null) {
            groupCount = shopInfos.size();
        }
        return groupCount;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ShopInfoBean shopInfoBean = shopInfos.get(groupPosition);
        List<DataBean> datas = shopInfoBean.goodsItems.data;
        int childrenCount = 0;
        if (datas != null) {
            childrenCount = datas.size();
        }
        return childrenCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return shopInfos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return shopInfos.get(groupPosition).goodsItems.data.get(childPosition);
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.cart_shop_item, null);
            holder = new ViewHolder();
            convertView.findViewById(R.id.physical_style_tv).setVisibility(View.GONE);
            holder.cart_shop_selected_btn = (CheckBox) convertView.findViewById(R.id.cart_shop_selected_btn);
            holder.fl_cart_shop_selected_btn = (FrameLayout) convertView.findViewById(R.id.fl_cart_shop_selected_btn);
            holder.tv_cart_shop_name = (TextView) convertView.findViewById(R.id.tv_cart_shop_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShopInfoBean shopInfoBean = shopInfos.get(groupPosition);
        //选择状态点击事件

        holder.fl_cart_shop_selected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.printI("holder.cart_shop_selected_btn.isChecked()+++1",holder.cart_shop_selected_btn.isChecked()+"");
                holder.cart_shop_selected_btn.setChecked(!(holder.cart_shop_selected_btn.isChecked()));
                CommonUtil.printI("holder.cart_shop_selected_btn.isChecked()+++2",holder.cart_shop_selected_btn.isChecked()+"");
                shopInfoBean.setChoosed(holder.cart_shop_selected_btn.isChecked());
                checkInterface.checkGroup(groupPosition, holder.cart_shop_selected_btn.isChecked());
            }
        });
        holder.cart_shop_selected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.printI("holder.cart_shop_selected_btn.isChecked()+++checkbox",holder.cart_shop_selected_btn.isChecked()+"");
                shopInfoBean.setChoosed(holder.cart_shop_selected_btn.isChecked());
                checkInterface.checkGroup(groupPosition, holder.cart_shop_selected_btn.isChecked());
            }
        });
        holder.cart_shop_selected_btn.setChecked(shopInfoBean.isChoosed());
        holder.tv_cart_shop_name.setText(shopInfoBean.shopName);
        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final InterViewHolder interViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.cart_inter_goods_item, null);
            interViewHolder = new InterViewHolder();
            interViewHolder.cart_goods_selected_btn = (CheckBox) convertView.findViewById(R.id.cart_goods_selected_btn);
            interViewHolder.fl_cart_goods_selected_btn = (FrameLayout) convertView.findViewById(R.id.fl_cart_goods_selected_btn);
            interViewHolder.iv_cart_goods_icon = (ImageView) convertView.findViewById(R.id.iv_cart_goods_icon);
            interViewHolder.tv_cart_goods_name = (TextView) convertView.findViewById(R.id.tv_cart_goods_name);
            interViewHolder.tv_cart_goods_price = (TextView) convertView.findViewById(R.id.tv_cart_goods_price);
            interViewHolder.ll_cart_goods_property = (LinearLayout) convertView.findViewById(R.id.ll_cart_goods_property);
            interViewHolder.ll_cart_goods_count = (LinearLayout) convertView.findViewById(R.id.ll_cart_goods_count);
            interViewHolder.tv_cart_goods_standard = (TextView) convertView.findViewById(R.id.tv_cart_goods_standard);
            interViewHolder.tv_cart_goods_count = (TextView) convertView.findViewById(R.id.tv_cart_goods_count);
            interViewHolder.cart_line = convertView.findViewById(R.id.cart_line);
            convertView.setTag(interViewHolder);
        } else {
            interViewHolder = (InterViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (isLastChild) {
            params.rightMargin = 0;
            params.leftMargin = 0;
            interViewHolder.cart_line.setLayoutParams(params);
        } else {
            params.rightMargin = 20;
            params.leftMargin = 20;
            interViewHolder.cart_line.setLayoutParams(params);
        }
        final DataBean dataBean = shopInfos.get(groupPosition).goodsItems.data.get(childPosition);
        interViewHolder.fl_cart_goods_selected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interViewHolder.cart_goods_selected_btn.setChecked(!(interViewHolder.cart_goods_selected_btn.isChecked()));
                dataBean.setChoosed(interViewHolder.cart_goods_selected_btn.isChecked());
                checkInterface.checkChild(groupPosition, childPosition, interViewHolder.cart_goods_selected_btn.isChecked());

            }
        });
        interViewHolder.cart_goods_selected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBean.setChoosed(interViewHolder.cart_goods_selected_btn.isChecked());
                checkInterface.checkChild(groupPosition, childPosition, interViewHolder.cart_goods_selected_btn.isChecked());

            }
        });
        //数量的点击事件
        interViewHolder.ll_cart_goods_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changedCountInterface.doChanged(groupPosition, childPosition, dataBean.skuid, is_prompt, interViewHolder.tv_cart_goods_count, dataBean.limit_buy,
                        dataBean.num, interViewHolder.cart_goods_selected_btn.isChecked());
            }
        });
        interViewHolder.cart_goods_selected_btn.setChecked(dataBean.isChoosed());
        String content = dataBean.icon.content;
        GlideUtil.getInstance().loadImage(mContext, content,
                R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, interViewHolder.iv_cart_goods_icon);

        interViewHolder.tv_cart_goods_name.setText(dataBean.name);
        interViewHolder.tv_cart_goods_price.setText(String.format(mContext.getString(R.string.cart_goods_price), String.valueOf(dataBean.price)));
        if (dataBean.color_name.length() == 0 && dataBean.size.length() == 0) {//颜色和尺码不存在
            interViewHolder.ll_cart_goods_property.setVisibility(View.INVISIBLE);
        } else if (dataBean.color_name.length() != 0 && dataBean.size.length() == 0) {//尺码不存在
            interViewHolder.ll_cart_goods_property.setVisibility(View.VISIBLE);
            interViewHolder.tv_cart_goods_standard.setText(dataBean.color_name);
        } else if (dataBean.color_name.length() == 0 && dataBean.size.length() != 0) {//颜色不存在
            interViewHolder.ll_cart_goods_property.setVisibility(View.VISIBLE);
            interViewHolder.tv_cart_goods_standard.setText(dataBean.size);
        } else if (dataBean.color_name.length() != 0 && dataBean.size.length() != 0) {
            interViewHolder.ll_cart_goods_property.setVisibility(View.VISIBLE);
            interViewHolder.tv_cart_goods_standard.setText(dataBean.color_name + " ; " + dataBean.size);
        }
        interViewHolder.tv_cart_goods_count.setText(String.valueOf(dataBean.num));
        return convertView;
    }

    //返回true,响应条目点击事件
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    //回调函数
    public void setOnCheckInterface(CheckInterface checkInterface) {

        this.checkInterface = checkInterface;
    }

    /**
     * 复选框回调接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        public void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        public void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    public void setOnChangedCountInterface(ChangedCountInterface changedCountInterface) {
        this.changedCountInterface = changedCountInterface;
    }

    /**
     * 改变数量的接口
     */
    public interface ChangedCountInterface {
        /**
         * 增删操作
         *
         * @param showCountView 用于展示变化后数量的View
         * @param limit_buy
         * @param isChecked
         */
        public void doChanged(int groupPosition, int childPosition, int sku_id, int is_prompt, TextView showCountView, String limit_buy, int goodsNum, boolean isChecked);
    }
}

class ViewHolder {
    public CheckBox cart_shop_selected_btn;
    public FrameLayout fl_cart_shop_selected_btn;
    TextView tv_cart_shop_name;
}

class InterViewHolder {
    CheckBox cart_goods_selected_btn;
    FrameLayout fl_cart_goods_selected_btn;
    ImageView iv_cart_goods_icon;
    TextView tv_cart_goods_name;
    TextView tv_cart_goods_price;
    LinearLayout ll_cart_goods_count, ll_cart_goods_property;
    TextView tv_cart_goods_standard, tv_cart_goods_count;
    View cart_line;
}

