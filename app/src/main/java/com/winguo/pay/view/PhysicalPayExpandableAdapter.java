package com.winguo.pay.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.net.GlideUtil;
import com.winguo.pay.modle.bean.PhysicalConfirmOderBean;

import java.util.List;

/**
 * Created by Admin on 2017/1/8.
 */

public class PhysicalPayExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<PhysicalConfirmOderBean.ResultBean.ListBean> listBeen;


    /**
     * @param context 上下文
     */
    public PhysicalPayExpandableAdapter(Context context, List<PhysicalConfirmOderBean.ResultBean.ListBean> listBeen) {
        this.context = context;
        this.listBeen = listBeen;
    }


    @Override
    public int getGroupCount() {
        int groupCount = 0;
        if (listBeen != null) {
            groupCount = listBeen.size();
        }
        return groupCount;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        PhysicalConfirmOderBean.ResultBean.ListBean listBean = listBeen.get(groupPosition);
        List<PhysicalConfirmOderBean.ResultBean.ListBean.ItemBean> items = listBean.item;
        int childrenCount = 0;
        if (items != null) {
            childrenCount = items.size();
        }
        return childrenCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listBeen.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listBeen.get(groupPosition).item.get(childPosition);
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
        PhysicalGroupHolder groupHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.physical_order_group_item, null);
            groupHolder = new PhysicalGroupHolder();
            groupHolder.order_group_name = (TextView) convertView.findViewById(R.id.order_group_name);
            groupHolder.physical_store_address = (TextView) convertView.findViewById(R.id.physical_store_address);
            groupHolder.physical_store_phone = (TextView) convertView.findViewById(R.id.physical_store_phone);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (PhysicalGroupHolder) convertView.getTag();
        }
        if (listBeen != null) {
            PhysicalConfirmOderBean.ResultBean.ListBean listBean = listBeen.get(groupPosition);
            groupHolder.order_group_name.setText(listBean.shop.m_maker_name_ch);
            groupHolder.physical_store_address.setText(listBean.shop.m_maker_address_ch);
            groupHolder.physical_store_phone.setText(listBean.shop.m_maker_mobile);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final PhysicalGoodsHolder goodsHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.physical_order_goods_item, null);
            goodsHolder = new PhysicalGoodsHolder();
            goodsHolder.iv_order_goods_icon = (ImageView) convertView.findViewById(R.id.iv_order_goods_icon);
            goodsHolder.tv_order_goods_name = (TextView) convertView.findViewById(R.id.tv_order_goods_name);
            goodsHolder.tv_order_goods_price = (TextView) convertView.findViewById(R.id.tv_order_goods_price);
            goodsHolder.ll_order_goods_standard = (LinearLayout) convertView.findViewById(R.id.ll_order_goods_standard);
            goodsHolder.tv_order_goods_standard = (TextView) convertView.findViewById(R.id.tv_order_goods_standard);
            goodsHolder.tv_order_goods_count = (TextView) convertView.findViewById(R.id.tv_order_goods_count);
            convertView.setTag(goodsHolder);
        } else {
            goodsHolder = (PhysicalGoodsHolder) convertView.getTag();
        }
        //获取数据
        PhysicalConfirmOderBean.ResultBean.ListBean.ItemBean childItemBean = listBeen.get(groupPosition).item.get(childPosition);
        GlideUtil.getInstance().loadImage(context, childItemBean.m_item_list_img,
                R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, goodsHolder.iv_order_goods_icon);
        goodsHolder.tv_order_goods_name.setText(childItemBean.m_item_name);
        goodsHolder.tv_order_goods_price.setText(childItemBean.m_sku_price);

        if (TextUtils.isEmpty(childItemBean.m_sku_entity_spec)) {//规格不存在
            goodsHolder.tv_order_goods_standard.setVisibility(View.INVISIBLE);
        } else {
            goodsHolder.tv_order_goods_standard.setVisibility(View.VISIBLE);
            goodsHolder.tv_order_goods_standard.setText(childItemBean.m_sku_entity_spec);
        }
        goodsHolder.tv_order_goods_count.setText(childItemBean.m_entity_cart_qty);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
class PhysicalGroupHolder {
    TextView order_group_name,physical_store_address,physical_store_phone;
}

class PhysicalGoodsHolder {
    ImageView iv_order_goods_icon;
    TextView tv_order_goods_name, tv_order_goods_price;
    LinearLayout ll_order_goods_standard;
    TextView tv_order_goods_standard, tv_order_goods_count;
}


