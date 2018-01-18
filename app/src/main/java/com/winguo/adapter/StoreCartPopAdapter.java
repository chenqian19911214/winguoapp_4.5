package com.winguo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.bean.CartSectionType;
import com.winguo.bean.CartShopList;
import com.winguo.bean.SectionType;
import com.winguo.bean.StoreShop;
import com.winguo.utils.CalculateUtil;
import com.winguo.utils.ToastUtil;

import java.util.List;

/**
 * Created by admin on 2017/6/7.
 */

public class StoreCartPopAdapter extends RecyclerCommonAdapter<CartSectionType> {

    private OnAddOrDelClickListener addOrDelClickListener;
    List<CartSectionType> datas;

    public void setAddOrDelClickListener(OnAddOrDelClickListener addOrDelClickListener) {
        this.addOrDelClickListener = addOrDelClickListener;
    }

    public StoreCartPopAdapter(Context context, int layoutId, List<CartSectionType> datas) {
        super(context, layoutId, datas);
        this.datas = datas;
    }

    @Override
    protected void convert(RecylcerViewHolder holder, final CartSectionType sectionType, final int position) {
        final CartShopList.ContentBean.ItemBean resultBean = sectionType.t;
        ImageView add = holder.getView(R.id.shop_item_add);
        holder.setText(R.id.shop_item_title,resultBean.getName());
        final ImageView del = holder.getView(R.id.shop_item_del);
        final TextView number = holder.getView(R.id.shop_item_number);
        final TextView price = holder.getView(R.id.shop_item_price);
        final TextView spec = holder.getView(R.id.shop_item_spec);
        number.setText(resultBean.getNum());
        price.setText(resultBean.getPrice());
        spec.setText(resultBean.getM_sku_entity_spec());

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrDelClickListener.delNumber(position,Integer.valueOf(resultBean.getNum()));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrDelClickListener.addNumber(position,Integer.valueOf(resultBean.getNum()));
            }
        });
    }


    /**
     * 点击条目的加 减回调
     */
    public interface OnAddOrDelClickListener {
        void addNumber(int position, int number );

        void delNumber(int position, int number);

    }

    public interface ModifyNumberClickListener {
        void modifyResult(boolean isSuccess);
    }

}
