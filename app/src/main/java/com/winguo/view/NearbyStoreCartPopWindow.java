package com.winguo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.R;
import com.winguo.activity.NearbyStoreHomeActivity;
import com.winguo.activity.PhysicalPayActivity;
import com.winguo.adapter.NearbyStoreHomeAdapter;
import com.winguo.adapter.SpacesItemDecoration;
import com.winguo.adapter.StoreCartPopAdapter;
import com.winguo.bean.CartSectionType;
import com.winguo.bean.CartShopList;
import com.winguo.utils.CalculateUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NearbyStoreUtil;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;

import java.math.BigDecimal;
import java.util.List;

import static com.unionpay.mobile.android.global.b.c;
import static com.unionpay.mobile.android.global.b.p;

/**
 * Created by admin on 2017/6/7.
 */

public class NearbyStoreCartPopWindow extends PopupWindow implements StoreCartPopAdapter.OnAddOrDelClickListener, NearbyStoreUtil.IRequestModifyShopNumber, NearbyStoreUtil.IRequestDelCartShop {

    private View mMenuView;
    private StoreCartPopAdapter adapter;
    private Context context;
    private String make_id;
    private String uid;
    private TextView nearby_store_home_bt_pay;
    private TextView nearby_store_home_bt_price;
    private TextView nearby_store_home_cart_number;
    private FrameLayout nearby_store_home_cart_fl;
    private NearbyStoreHomeActivity activity;
    private NearbyStoreHomeAdapter pullToRefreshAdapter;
    List<CartSectionType> tempCart;
    private int shopNumber = 0;
    private double total_price = 0.00;

    public NearbyStoreCartPopWindow(final Context context, List<CartSectionType> tempCart, NearbyStoreHomeAdapter pullToRefreshAdapter, String uid, String make_id) {
        super(context);
        this.context = context;
        this.uid = uid;
        this.make_id = make_id;
        this.tempCart = tempCart;
        this.activity = (NearbyStoreHomeActivity) context;
        this.pullToRefreshAdapter = pullToRefreshAdapter;
        adapter = new StoreCartPopAdapter(context, R.layout.nearby_store_cart_shop_item, tempCart);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (adapter.getDatas().size() > 3) {
            mMenuView = inflater.inflate(R.layout.nearby_store_cart_pop, null, false);
        } else {
            mMenuView = inflater.inflate(R.layout.nearby_store_cart_pop1, null, false);
        }
        this.setContentView(mMenuView);
        adapter.setAddOrDelClickListener(this);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.myPopupWindpw_anim_style);
        this.setOnDismissListener(new PoponDismissListener());
        mMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        initView(context);
    }

    private void initView(final Context context) {

        RecyclerView nearby_store_home_cart_pop_rv = (RecyclerView) mMenuView.findViewById(R.id.nearby_store_home_cart_pop_rv);
        nearby_store_home_cart_pop_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        nearby_store_home_cart_pop_rv.setItemAnimator(new DefaultItemAnimator());
        nearby_store_home_cart_pop_rv.addItemDecoration(new SpacesItemDecoration(3));
        nearby_store_home_cart_pop_rv.setAdapter(adapter);
        //底部购物车view
        View nearby_home_bt_cart_view = mMenuView.findViewById(R.id.nearby_home_bt_cart_view);
        nearby_store_home_cart_fl = (FrameLayout) mMenuView.findViewById(R.id.nearby_store_home_cart_fl);
        nearby_store_home_cart_number = (TextView) mMenuView.findViewById(R.id.nearby_store_home_cart_number);
        nearby_store_home_bt_price = (TextView) mMenuView.findViewById(R.id.nearby_store_home_bt_price);
        nearby_store_home_bt_pay = (TextView) mMenuView.findViewById(R.id.nearby_store_home_bt_pay);

        calcNumPricce();

        nearby_store_home_cart_number.setVisibility(View.VISIBLE);
        nearby_store_home_cart_number.setText(shopNumber + "");
        nearby_store_home_bt_price.setText(total_price + "");
        nearby_store_home_bt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/6/8 去支付结算
                // ToastUtil.showToast(this,"结算");
                if (cartIds == null) {
                    cartIds = new StringBuffer();
                    for (int i = 0; i < tempCart.size(); i++) {
                        CartShopList.ContentBean.ItemBean itemBean = tempCart.get(i).t;
                        if (i == (tempCart.size() - 1)) {
                            cartIds.append(itemBean.getId());
                        } else {
                            cartIds.append(itemBean.getId()).append(",");
                        }
                    }
                }
                GBLogUtils.DEBUG_DISPLAY("FINAL", cartIds.toString());
                Intent it = new Intent(context, PhysicalPayActivity.class);
                it.putExtra("cart_ids", cartIds.toString());
                context.startActivity(it);
                dismiss();
            }
        });
    }

    /**
     * 计算数量和总价
     */
    private void calcNumPricce() {
        total_price = 0.00;
        shopNumber = 0;
        if (!tempCart.isEmpty()) {
            for (CartSectionType it : tempCart) {
                CartShopList.ContentBean.ItemBean t = it.t;
                shopNumber += Integer.valueOf(t.getNum());
                total_price = new BigDecimal(total_price).add(new BigDecimal(t.getNum_price())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
    }

    /**
     * 添加监听接口
     *
     * @param position
     * @param number
     */
    @Override
    public void addNumber(int position, int number) {
        List<CartSectionType> datas = adapter.getDatas();
        String id = datas.get(position).t.getId();
        if (NetWorkUtil.isNetworkAvailable(context)) {
            LoadDialog.show(context, true);
            NearbyStoreUtil.requestModifyShopNumber(context, id, uid, "1", this, position, number + 1, make_id);
        } else {
            ToastUtil.showToast(context, context.getString(R.string.timeout));
        }
    }

    /**
     * 减数量
     *
     * @param position
     * @param number
     */
    @Override
    public void delNumber(int position, int number) {
        ;
        String id = tempCart.get(position).t.getId();
        if (NetWorkUtil.isNetworkAvailable(context)) {
            LoadDialog.show(context, true);
            if (number - 1 == 0) {
                //删除购物车指定商品
                NearbyStoreUtil.requestDelShop(context, id, uid, this, position, number, make_id);
            } else {
                //减商品数量
                NearbyStoreUtil.requestModifyShopNumber(context, id, uid, "2", this, position, number - 1, make_id);
            }

        } else {
            ToastUtil.showToast(context, context.getString(R.string.timeout));
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        backgroundAlpha(0.5f);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.5f);

    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        backgroundAlpha(0.5f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1.0f);
    }

    /**
     * PopouWindow设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    @Override
    public void addShopNumber(String key, List<CartShopList.ContentBean.ItemBean> item, int position, int number) {
        LoadDialog.dismiss(context);
        //ToastUtil.showToast(context, key);
        if (position != -1) {
            dataChange(item);
            nearby_store_home_cart_number.setVisibility(View.VISIBLE);
            nearby_store_home_cart_number.setText(shopNumber + "");
            nearby_store_home_bt_price.setText((total_price) + "");
            activity.addNumberByCart(position, shopNumber, total_price, cartIds);
        }

    }

    @Override
    public void delShopNumber(String key, List<CartShopList.ContentBean.ItemBean> item, int position, int number) {
        LoadDialog.dismiss(context);
        //ToastUtil.showToast(context, key);
        if (position != -1) {
            //修改商品数量
            dataChange(item);
            nearby_store_home_cart_number.setText(shopNumber + "");
            nearby_store_home_bt_price.setText((total_price) + "");
            activity.delNumberByCart(position, shopNumber, total_price, cartIds);
        }
    }

    private StringBuffer cartIds;

    private void dataChange(List<CartShopList.ContentBean.ItemBean> item) {
        cartIds = new StringBuffer();
        tempCart.clear();
        if (item != null) {
            for (int i = 0; i < item.size(); i++) {
                CartShopList.ContentBean.ItemBean itemBean = item.get(i);
                CartSectionType c = new CartSectionType(itemBean);
                c.setAdd(true);
                tempCart.add(c);
                if (i == (item.size() - 1)) {
                    cartIds.append(itemBean.getId());
                } else {
                    cartIds.append(itemBean.getId()).append(",");
                }
            }
        }
        adapter.notifyDataSetChanged();
        calcNumPricce();
    }

    @Override
    public void modifyShopNumberErrorMsg(String error) {
        LoadDialog.dismiss(context);
        ToastUtil.showToast(context, error);
    }

    @Override
    public void delCartShop(String key, List<CartShopList.ContentBean.ItemBean> item, int position, int number) {
        LoadDialog.dismiss(context);
        //ToastUtil.showToast(context, key);
        if (item != null) {
            dataChange(item);
        } else {
            //最后一件商品
            //cartIds = new StringBuffer();
           dataChange(item);
        }

        if (position != -1) {
            if (shopNumber != 0) {
                nearby_store_home_cart_number.setText(shopNumber + "");
                nearby_store_home_bt_price.setText((total_price) + "");
                activity.delNumberByCart(position, shopNumber, total_price, cartIds);
            } else {
                nearby_store_home_cart_number.setText(shopNumber + "");
                nearby_store_home_bt_price.setText((total_price) + "");
                activity.delNumberByCart(position, shopNumber, total_price, cartIds);
                dismiss();
            }

        }

    }

    @Override
    public void delCartShopErrorMsg(String error) {
        LoadDialog.dismiss(context);
        ToastUtil.showToast(context, error);
    }


    /**
     * PopouWindow添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class PoponDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }


}
