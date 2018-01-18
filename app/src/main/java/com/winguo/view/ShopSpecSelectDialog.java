package com.winguo.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.adapter.RecyclerCommonAdapter;
import com.winguo.adapter.RecylcerViewHolder;
import com.winguo.adapter.SpacesItemDecoration;
import com.winguo.bean.StoreShop;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品售罄弹窗
 */

public class ShopSpecSelectDialog extends Dialog {

    public ShopSpecSelectDialog(Context context) {
        super(context);
    }

    public ShopSpecSelectDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ShopSpecSelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private RecylcerViewHolder mViewHolder;
        private int mPosition = -1;
        private List<StoreShop.ResultBean.SkuBean> data;
        private Context context;
        private String title;
        private String defSelectSpec = "";
        private double defSelectPrice;
        private OnSelectorSpecListener selectorSpecListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSpecData(List<StoreShop.ResultBean.SkuBean> data) {
            this.data = data;
            return this;
        }

        public Builder setDefSelectSpec(String spec, double defSelectPrice) {
            this.defSelectSpec = spec;
            this.defSelectPrice = defSelectPrice;
            return this;
        }

        public Builder setSelectListener(OnSelectorSpecListener selectorSpecListener) {
            this.selectorSpecListener = selectorSpecListener;
            return this;
        }


        public ShopSpecSelectDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ShopSpecSelectDialog dialog = new ShopSpecSelectDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.shop_spec_select_dialog, null, false);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(30, 10, 30, 10);
            dialog.addContentView(layout, lp);
            TextView titleTV = (TextView) layout.findViewById(R.id.title);
            RecyclerView shop_spec_select_rv = (RecyclerView) layout.findViewById(R.id.shop_spec_select_rv);
            shop_spec_select_rv.setLayoutManager(new GridLayoutManager(context, 2));
            shop_spec_select_rv.addItemDecoration(new SpacesItemDecoration(5));
            shop_spec_select_rv.setItemAnimator(new DefaultItemAnimator());

            final TextView shop_spec_price = (TextView) layout.findViewById(R.id.shop_spec_price);

            shop_spec_select_rv.setAdapter(new RecyclerCommonAdapter<StoreShop.ResultBean.SkuBean>(context, R.layout.shop_spec_item, data) {
                @Override
                protected void convert(final RecylcerViewHolder holder, final StoreShop.ResultBean.SkuBean skuBean, final int position) {
                    if (TextUtils.isEmpty(defSelectSpec)) {
                        if (position == 0) {
                            mViewHolder = holder;
                            ((RadioButton) holder.getView(R.id.shop_spec_name)).setChecked(true);
                            shop_spec_price.setText(skuBean.getM_sku_price() + "");
                            mPosition = position;
                        }
                    } else {
                        if (skuBean.getM_sku_entity_spec().equals(defSelectSpec)) {
                            mViewHolder = holder;
                            ((RadioButton) holder.getView(R.id.shop_spec_name)).setChecked(true);
                            shop_spec_price.setText(defSelectPrice + "");
                            mPosition = position;
                        }
                    }

                    holder.setText(R.id.shop_spec_name, skuBean.getM_sku_entity_spec());
                    holder.setOnClickListener(R.id.shop_spec_name, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RadioButton radio = (RadioButton) v;
                            if (mViewHolder != null && !mViewHolder.equals(holder)) {
                                ((RadioButton) mViewHolder.getView(R.id.shop_spec_name)).setChecked(false);
                            } else {
                                return;
                            }
                            radio.setChecked(true);
                            mViewHolder = holder;
                            mPosition = position;
                            shop_spec_price.setText(skuBean.getM_sku_price());


                        }
                    });
                }
            });


            TextView shop_spec_submit = (TextView) layout.findViewById(R.id.shop_spec_submit);
            if (!TextUtils.isEmpty(defSelectSpec))
                shop_spec_submit.setText("确定");

            shop_spec_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2017/6/9  选中价格
                    dialog.dismiss();
                    StoreShop.ResultBean.SkuBean skuBean = data.get(mPosition);
                    int m_sku_stock = Integer.valueOf(skuBean.getM_sku_stock());
                    //选中规格后 判断库存量
                    if (m_sku_stock > 1) {
                        String s_price = shop_spec_price.getText().toString();
                        if (s_price != null) {
                            Double price = Double.valueOf(shop_spec_price.getText().toString());
                            selectorSpecListener.selectPrice(price, skuBean.getM_sku_entity_spec(), skuBean.getM_sku_id());
                        }

                    } else {
                        shopSellOut();
                    }

                }
            });

            if (title != null) {
                titleTV.setText(title);
            } else {
                titleTV.setVisibility(View.GONE);
            }

            dialog.setContentView(layout, lp);
            return dialog;

        }

        /**
         * 售罄弹窗
         */
        private void shopSellOut() {
            ShopSellOutDialog.Builder builder = new ShopSellOutDialog.Builder(context);
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


    }

    public interface OnSelectorSpecListener {
        void selectPrice(double price, String spec,String sku_id);
    }

}
