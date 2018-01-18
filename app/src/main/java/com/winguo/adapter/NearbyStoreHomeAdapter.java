package com.winguo.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winguo.R;
import com.winguo.bean.SectionType;
import com.winguo.bean.StoreShop;
import com.winguo.login.LoginActivity;
import com.winguo.net.GlideUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.ToastUtil;
import com.winguo.view.ShopSpecSelectDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/5.
 */

public class NearbyStoreHomeAdapter extends BaseSectionQuickAdapter<SectionType, BaseViewHolder> {

    private OnAddOrDelClickListener onAddOrDelClickListner;
    private Context context;
    private String uid,storeid;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public NearbyStoreHomeAdapter(Context context, int layoutResId, int sectionHeadResId, List<SectionType> data,String uid,String storeid) {
        super(layoutResId, sectionHeadResId, data);
        this.context = context;
        this.storeid= storeid;
        this.uid = uid;
    }


    @Override
    protected void convertHead(BaseViewHolder helper, SectionType item) {
        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more, item.isMore());
        helper.addOnClickListener(R.id.more);
    }

    /**
     * 临时存储购物车数据
     */
    List<SectionType> temp = new ArrayList<>();

    @Override
    protected void convert(final BaseViewHolder helper, final SectionType item) {
        final StoreShop.ResultBean resultBean = item.t;
        item.setHelper(helper);
        ImageView shop_item_ic = helper.getView(R.id.shop_item_ic);
        GlideUtil.getInstance().loadImage(context, resultBean.getItemthumb(), R.drawable.little_theme_loading_bg, R.drawable.little_theme_error_bg, shop_item_ic);
        helper.setText(R.id.shop_item_title, resultBean.getM_item_name());
        if (!TextUtils.isEmpty(resultBean.getM_item_min_price())) {
            helper.setText(R.id.shop_item_price, "¥" + resultBean.getM_item_max_price());
        } else if (!TextUtils.isEmpty(resultBean.getM_item_max_price())) {
            helper.setText(R.id.shop_item_price, "¥" + resultBean.getM_item_min_price());
        } else {
            helper.setText(R.id.shop_item_price, "¥ /");
        }
        ImageView add = helper.getView(R.id.shop_item_add);
        final ImageView del = helper.getView(R.id.shop_item_del);
        final TextView number = helper.getView(R.id.shop_item_number);
        if (item.isAdd()) {
            // del.setVisibility(View.VISIBLE);
            // number.setVisibility(View.VISIBLE);
            number.setText(item.getNumber() + "");
        } else {
            del.setVisibility(View.INVISIBLE);
            number.setVisibility(View.INVISIBLE);
            number.setText(0 + "");
        }

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("uid setOnClickListener",""+uid);
                if (uid!=null) {
                    boolean b = haveDifferent(helper);
                    if (b) {
                        //购物车 存在同一商品 不同规格 此处不能修改数量
                        ToastUtil.showToast(context, "请去购物车中修改！");
                    } else {
                        //购物车 同一商品 相同规格 直接修改数量
                        for (SectionType it : temp) {
                            if (it.getHelper() != null) {
                                if (helper.equals(it.getHelper())) {
                                    double selectPrice = it.getSelectPrice();

                                    int num = Integer.valueOf(number.getText().toString());
                                    --num;
                                    if (num == 0) {
                                        del.setVisibility(View.INVISIBLE);
                                        number.setVisibility(View.INVISIBLE);
                                        item.setAdd(false);
                                        onAddOrDelClickListner.delNumber(num, selectPrice);
                                        number.setText(num + "");
                                        item.setNumber(num);
                                        item.setSelectSpec("");
                                        item.setSelectPrice(0.0d);
                                        temp.remove(it);
                                    } else {
                                        onAddOrDelClickListner.delNumber(num, selectPrice);
                                        number.setText(num + "");
                                        item.setNumber(num);
                                        it.setNumber(num);
                                    }

                                    break;
                                }
                            }
                        }

                    }
                } else {
                    //未登录
                    Intent it = new Intent(context,LoginActivity.class);
                    it.putExtra("putExtra", Constants.NEARBY_STORE_HOME_CART);
                    it.putExtra("storeid",storeid);
                    context.startActivity(it);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid != null) {
                    final List<StoreShop.ResultBean.SkuBean> sku = resultBean.getSku();
                    //有多种规格
                    // TODO: 2017/6/9 选择套餐

                    ShopSpecSelectDialog.Builder builder = new ShopSpecSelectDialog.Builder(context);
                    builder.setTitle(resultBean.getM_item_name()).setSpecData(sku)
                            .setSelectListener(new ShopSpecSelectDialog.OnSelectorSpecListener() {
                                @Override
                                public void selectPrice(double price, String spec, String sku_id) {
                                    //添加到购物车
                                    if (temp.isEmpty()) {
                                        SectionType sectionType = new SectionType(resultBean);
                                        sectionType.setHelper(helper);
                                        sectionType.setNumber(1);
                                        sectionType.setSelectPrice(price);
                                        sectionType.setSelectSpec(spec);
                                        sectionType.setSku_id(sku_id);
                                        sectionType.setAdd(true);
                                        temp.add(sectionType);
                                        onAddOrDelClickListner.addNumber(1, price, sku_id, resultBean.getM_entity_item_m_item_id(), temp);
                                    } else {
                                        //如果是同一条目 同一规格只修改数量，否者添加新的
                                        boolean isDo = false;
                                      /*  SectionType modifyNum = null;
                                        for (SectionType it : temp) {
                                            if (it.getHelper().equals(helper)) {
                                                if (spec.equals(it.getSelectSpec())) {
                                                    it.setNumber(it.getNumber() + 1);
                                                    isDo = true;
                                                    modifyNum = it;
                                                    break;
                                                }
                                            }
                                        }*/
                                        if (!isDo) {
                                            SectionType sectionType = new SectionType(resultBean);
                                            sectionType.setHelper(helper);
                                            sectionType.setNumber(1);
                                            sectionType.setSelectPrice(price);
                                            sectionType.setSelectSpec(spec);
                                            sectionType.setSku_id(sku_id);
                                            sectionType.setAdd(true);
                                            temp.add(sectionType);
                                            onAddOrDelClickListner.addNumber(1, price, sku_id, resultBean.getM_entity_item_m_item_id(), temp);

                                        }/* else {
                                        //修改商品数量
                                        onAddOrDelClickListner.addShopNumCart(modifyNum.getNumber(),modifyNum.t.getM_entity_item_m_item_id());
                                    }*/
                                    }
                                    //记录添加数量
                                    // del.setVisibility(View.VISIBLE);
                                    // number.setVisibility(View.VISIBLE);
                                    int num = Integer.valueOf(number.getText().toString());
                                    ++num;
                                    item.setAdd(true);
                                    item.setNumber(num);
                                    item.setSelectSpec(spec);
                                    item.setSelectPrice(price);
                                    item.setSku_id(sku_id);
                                    //item.setHelper(helper);
                                    number.setText(num + "");

                                }
                            });
                    builder.create().show();

                } else {
                    //未登录
                    Intent it = new Intent(context,LoginActivity.class);
                    it.putExtra("putExtra", Constants.NEARBY_STORE_HOME_CART);
                    it.putExtra("storeid",storeid);
                    context.startActivity(it);
                }

            }
        });


    }

    /**
     * 购物车 是否存在同一商品 不同规格
     *
     * @param helper
     * @return
     */
    private boolean haveDifferent(BaseViewHolder helper) {
        int count = 0;
        for (SectionType it : temp) {
            if (it.getHelper() != null) {
                if (helper.equals(it.getHelper())) {
                    count++;
                }
            }
        }
        if (count > 1) {
            return true;
        } else {
            return false;
        }
    }


    public void setOnAddOrDelClickListener(OnAddOrDelClickListener OnAddOrDelClickListener) {
        this.onAddOrDelClickListner = OnAddOrDelClickListener;
    }

    /**
     * 购物车修改 列表数据
     *
     * @param position
     * @param number
     */
    public void modifyNumber(int position, int number) {

        if (!temp.isEmpty()) {
            int size = temp.size();
            if (size > position) {
                SectionType sectionType = temp.get(position);
                sectionType.setNumber(number);
                BaseViewHolder helper = sectionType.getHelper();
                for (SectionType it : getData()) {
                    if (it.getHelper() != null) {
                        if (helper.equals(it.getHelper())) {
                            it.setNumber(number);
                            if (number == 0)
                                it.setAdd(false);
                            break;
                        }
                    }
                }
            }

        }

    }

    /**
     * 商品详情回调 选择商品的结果
     * 未添加到购物车 选择商品规格
     *
     * @param modifySectionType
     * @param itemPosition
     */
    public void addShopByShopDetail(SectionType modifySectionType, int itemPosition) {
        if (modifySectionType != null) {
            //修改商品列表
            List<SectionType> data = getData();
            SectionType sectionType = data.get(itemPosition);
            sectionType.setAdd(true);
            sectionType.setNumber(modifySectionType.getNumber());
            sectionType.setSelectSpec(modifySectionType.getSelectSpec());
            sectionType.setSelectPrice(modifySectionType.getSelectPrice());
        /*  BaseViewHolder helper = sectionType.getHelper();
            final ImageView del = helper.getView(R.id.shop_item_del);
            final TextView number = helper.getView(R.id.shop_item_number);
            del.setVisibility(View.VISIBLE);
            number.setVisibility(View.VISIBLE);
            number.setText(sectionType.getNumber()+"");  */
            //绑定viewholder 可以在购物车中修改（添加 删除）
            modifySectionType.setAdd(true);
            modifySectionType.setHelper(sectionType.getHelper());
            temp.add(modifySectionType);
            StoreShop.ResultBean t = modifySectionType.t;
            Log.e("nearbyStorehomeAdapter", "ResultBean" + t);
            if (t != null) {
                onAddOrDelClickListner.addNumber(modifySectionType.getNumber(), modifySectionType.getSelectPrice(), modifySectionType.getSku_id(), t.getM_entity_item_m_item_id(), temp);
            }
        }

    }

    public void delShow(View view) {
        float cx = view.getX();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "alpha", 0.1f, 1f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "x", cx, cx - 80f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.setInterpolator(new LinearInterpolator());
        //两个动画同时执行
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }

    public void delHide(View view) {
        float cx = view.getX();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.1f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "x", cx);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.setInterpolator(new LinearInterpolator());
        //两个动画同时执行
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }

    /**
     * 点击条目的加 减回调
     */
    public interface OnAddOrDelClickListener {
        void addNumber(int number, double price, String sku_id, String productid, List<SectionType> cartData);

        //void modifySpecByShop(double oldPrice,double newPrice,List<SectionType> sectionType);

        void delNumber(int number, double price);

       /* void addShopNumCart(int number,String product_id);

        void delShopNumCart(int number,String product_id);*/
    }

}
