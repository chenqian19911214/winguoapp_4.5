package com.winguo.adapter;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.bean.SectionType2;
import com.winguo.bean.StoreDetail2;
import com.winguo.net.GlideUtil;
import com.winguo.view.ShopSellOutDialog;

import java.util.List;

/**
 * 大众点评数据
 * Created by admin on 2017/5/11.
 */

public class NearbyHomeAdapter extends BaseSectionQuickAdapter<SectionType2, BaseViewHolder>{


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public NearbyHomeAdapter(int layoutResId, int sectionHeadResId, List<SectionType2> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    /**
     * 售罄弹窗
     */
    private void shopSellOut() {
        ShopSellOutDialog.Builder builder = new ShopSellOutDialog.Builder(mContext);
        builder.setMessage("该商品已售罄，可收藏该商品，补货第一时间通知")
                .setMessageColor(R.color.content_color)
                .setNegativeButton("看看其他", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButtonColor(R.color.negative_color)
                .setPositiveButton("收藏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButtonColor(R.color.positive_color);
        builder.create().show();
    }


    @Override
    protected void convertHead(BaseViewHolder helper, SectionType2 item) {
        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more, item.isMore());
        helper.addOnClickListener(R.id.more);
    }

    @Override
    protected void convert(BaseViewHolder helper, SectionType2 item) {
        StoreDetail2.ResultBean.ItemsBean t2 = item.t;
        ImageView shop_item_ic = helper.getView(R.id.shop_item_ic);
        GlideUtil.getInstance().loadImage(mContext, t2.getPic_url(), R.drawable.little_theme_loading_bg, R.drawable.little_theme_error_bg, shop_item_ic);
        helper.setText(R.id.shop_item_title, t2.getName());

        if (!TextUtils.isEmpty(t2.getPrice())) {
            helper.setText(R.id.shop_item_price, "¥" + t2.getPrice());
        }  else {
            helper.setText(R.id.shop_item_price, "¥ /");
        }
        ImageView add = helper.getView(R.id.shop_item_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopSellOut();
            }
        });
    }

}
