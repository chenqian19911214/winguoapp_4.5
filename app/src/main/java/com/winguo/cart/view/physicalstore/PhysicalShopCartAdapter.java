package com.winguo.cart.view.physicalstore;

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
import com.winguo.cart.bean.PhysicalStoreBean;
import com.winguo.net.GlideUtil;

import java.util.List;

/**
 * Created by Admin on 2017/6/27.
 */

public class PhysicalShopCartAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<PhysicalStoreBean.ContentBean> contentBeen;
    private PhysicalChangedCountInterface changedCountInterface;
    private PhysicalCheckInterface checkInterface;

    public PhysicalShopCartAdapter(Context mContext, List<PhysicalStoreBean.ContentBean > contentBeen) {
        this.mContext = mContext;
        this.contentBeen = contentBeen;
    }

    @Override
    public int getGroupCount() {
        int groupCount=0;
        if (contentBeen!=null) {
            groupCount = contentBeen.size();
        }
        return groupCount;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        PhysicalStoreBean.ContentBean contentBean = contentBeen.get(groupPosition);
        List<PhysicalStoreBean.ContentBean.ItemBean> items = contentBean.item;
        int childrenCount=0;
        if (items!=null){
            childrenCount= items.size();
        }
        return childrenCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return contentBeen.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return contentBeen.get(groupPosition).item.get(childPosition);
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
        if (convertView==null){
            convertView=View.inflate(mContext, R.layout.cart_shop_item,null);
            holder=new ViewHolder();
            convertView.findViewById(R.id.physical_style_tv).setVisibility(View.VISIBLE);
            holder.cart_shop_selected_btn= (CheckBox) convertView.findViewById(R.id.cart_shop_selected_btn);
            holder.fl_cart_shop_selected_btn= (FrameLayout) convertView.findViewById(R.id.fl_cart_shop_selected_btn);
            holder.tv_cart_shop_name= (TextView) convertView.findViewById(R.id.tv_cart_shop_name);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        final PhysicalStoreBean.ContentBean contentBean = contentBeen.get(groupPosition);
        //选择状态点击事件

        holder.cart_shop_selected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentBean.maker.setChoosed(holder.cart_shop_selected_btn.isChecked());
                checkInterface.checkGroup(groupPosition,holder.cart_shop_selected_btn.isChecked());
            }
        });
        holder.fl_cart_shop_selected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cart_shop_selected_btn.setChecked(!(holder.cart_shop_selected_btn.isChecked()));
                contentBean.maker.setChoosed(holder.cart_shop_selected_btn.isChecked());
                checkInterface.checkGroup(groupPosition,holder.cart_shop_selected_btn.isChecked());
            }
        });
        holder.cart_shop_selected_btn.setChecked(contentBean.maker.isChoosed());
        holder.tv_cart_shop_name.setText(contentBean.maker.m_maker_name_ch);
        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final InterViewHolder interViewHolder;
        if (convertView==null){
            convertView=View.inflate(mContext, R.layout.cart_inter_goods_item,null);
            interViewHolder=new InterViewHolder();
            interViewHolder.cart_goods_selected_btn= (CheckBox) convertView.findViewById(R.id.cart_goods_selected_btn);
            interViewHolder.fl_cart_goods_selected_btn= (FrameLayout) convertView.findViewById(R.id.fl_cart_goods_selected_btn);
            interViewHolder.iv_cart_goods_icon= (ImageView) convertView.findViewById(R.id.iv_cart_goods_icon);
            interViewHolder.tv_cart_goods_name= (TextView) convertView.findViewById(R.id.tv_cart_goods_name);
            interViewHolder.tv_cart_goods_price= (TextView) convertView.findViewById(R.id.tv_cart_goods_price);
            interViewHolder.ll_cart_goods_property= (LinearLayout) convertView.findViewById(R.id.ll_cart_goods_property);
            interViewHolder.ll_cart_goods_count= (LinearLayout) convertView.findViewById(R.id.ll_cart_goods_count);
            interViewHolder.tv_cart_goods_standard= (TextView) convertView.findViewById(R.id.tv_cart_goods_standard);
            interViewHolder.tv_cart_goods_count= (TextView) convertView.findViewById(R.id.tv_cart_goods_count);
            interViewHolder.cart_line=  convertView.findViewById(R.id.cart_line);
            convertView.setTag(interViewHolder);
        }else{
            interViewHolder= (InterViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (isLastChild){
            params.rightMargin=0;
            params.leftMargin=0;
            interViewHolder.cart_line.setLayoutParams(params);
        }else{
            params.rightMargin=20;
            params.leftMargin=20;
            interViewHolder.cart_line.setLayoutParams(params);
        }
        final PhysicalStoreBean.ContentBean.ItemBean itemBean = contentBeen.get(groupPosition).item.get(childPosition);
        interViewHolder.cart_goods_selected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemBean.setChoosed(interViewHolder.cart_goods_selected_btn.isChecked());
                checkInterface.checkChild(groupPosition,childPosition,interViewHolder.cart_goods_selected_btn.isChecked());

            }
        });
        interViewHolder.fl_cart_goods_selected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interViewHolder.cart_goods_selected_btn.setChecked(!(interViewHolder.cart_goods_selected_btn.isChecked()));
                itemBean.setChoosed(interViewHolder.cart_goods_selected_btn.isChecked());
                checkInterface.checkChild(groupPosition,childPosition,interViewHolder.cart_goods_selected_btn.isChecked());

            }
        });
        //数量的点击事件
        interViewHolder.ll_cart_goods_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changedCountInterface.doChanged(groupPosition,childPosition,itemBean.id,interViewHolder.tv_cart_goods_count,String.valueOf(Integer.MAX_VALUE),
                        Integer.valueOf(itemBean.num),interViewHolder.cart_goods_selected_btn.isChecked());
            }
        });
        interViewHolder.cart_goods_selected_btn.setChecked(itemBean.isChoosed());
        String img_url = itemBean.img_url;
        GlideUtil.getInstance().loadImage(mContext,img_url,
                R.drawable.electric_theme_loading_bg,R.drawable.electric_theme_error_bg,interViewHolder.iv_cart_goods_icon);

        interViewHolder.tv_cart_goods_name.setText(itemBean.name);
        interViewHolder.tv_cart_goods_price.setText(String.format(mContext.getString(R.string.cart_goods_price),itemBean.price));
        if (itemBean.m_sku_entity_spec.length()==0){//规格不存在
            interViewHolder.ll_cart_goods_property.setVisibility(View.INVISIBLE);
        }else {
            interViewHolder.ll_cart_goods_property.setVisibility(View.VISIBLE);
            interViewHolder.tv_cart_goods_standard.setText(itemBean.m_sku_entity_spec);
        }
        interViewHolder.tv_cart_goods_count.setText(itemBean.num);
        return convertView;
    }
    //返回true,响应条目点击事件
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    //回调函数
    public void setOnCheckInterface(PhysicalCheckInterface checkInterface){

        this.checkInterface = checkInterface;
    }

    /**
     * 复选框回调接口
     */
    public interface PhysicalCheckInterface
    {
        /**
         * 组选框状态改变触发的事件
         * @param groupPosition
         *            组元素位置
         * @param isChecked
         *            组元素选中与否
         */
        public void checkGroup(int groupPosition, boolean isChecked);
        /**
         * 子选框状态改变时触发的事件
         * @param groupPosition
         *            组元素位置
         * @param childPosition
         *            子元素位置
         * @param isChecked
         *            子元素选中与否
         */
        public void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    public void setOnChangedCountInterface(PhysicalChangedCountInterface changedCountInterface){
        this.changedCountInterface = changedCountInterface;
    }
    /**
     * 改变数量的接口
     */
    public interface PhysicalChangedCountInterface
    {
        /**
         * 增删操作
         * @param showCountView
         *            用于展示变化后数量的View
         * @param limit_buy
         * @param isChecked
         */
        public void doChanged(int groupPosition, int childPosition,String cartId, TextView showCountView, String limit_buy, int goodsNum, boolean isChecked);
    }
}
class ViewHolder{
    public CheckBox cart_shop_selected_btn;
    TextView tv_cart_shop_name;
    FrameLayout fl_cart_shop_selected_btn;

}

class InterViewHolder{
    CheckBox cart_goods_selected_btn;
    FrameLayout fl_cart_goods_selected_btn;
    ImageView iv_cart_goods_icon;
    TextView tv_cart_goods_name;
    TextView tv_cart_goods_price;
    LinearLayout ll_cart_goods_property,ll_cart_goods_count;
    TextView tv_cart_goods_standard,tv_cart_goods_count;
    View cart_line;
}