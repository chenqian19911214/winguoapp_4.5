package com.winguo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.cart.controller.store.CartController;
import com.winguo.cart.bean.AddCartCodeBean;
import com.winguo.cart.view.store.IAddCartView;
import com.winguo.net.GlideUtil;
import com.winguo.product.modle.Data;
import com.winguo.product.modle.bean.GoodDetail;
import com.winguo.product.modle.bean.ItemSkuBean;
import com.winguo.product.modle.productattribute.Attribute;
import com.winguo.product.modle.productattribute.FlowLayout;
import com.winguo.product.modle.productattribute.NumberAddSubView;
import com.winguo.product.modle.productattribute.TagAdapter;
import com.winguo.product.modle.productattribute.TagFlowLayout;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品属性选择
 */
public class ProductPropertyActivity extends BaseTitleActivity implements IAddCartView ,View.OnClickListener{
    private TagFlowLayout tf_color;
    private TagFlowLayout tf_size;
    private TextView popupwind_ok;
    private ArrayList<String> mSizeDate;
    private ArrayList<String> mColorDate;
    private ArrayList<String> mFailureSkuDate;
    private ArrayList<String> mAllSkuDate;

    private Attribute sizeAtt = new Attribute();
    private Attribute colorAtt = new Attribute();
    private String sku;
    private int defaultColor;
    private int color;
    private int defaultSize;
    private int size;
    private LayoutInflater mInflater;
    private mTagAdapter mColorAdapter;
    private mTagAdapter mSizeAdapter;
    private String colorStr;
    private String sizeStr;
    private String sku_id;
    private LinearLayout ll_property_color;
    private LinearLayout ll_property_size;
    private ItemSkuBean skuBean;
    private List<Data> datas;
    private NumberAddSubView number_addsub_view;
    private boolean isFromProperty;//是否是点击属性进入
    private TextView popupwind_add_cart;
    private TextView popupwind_just_pay;
    private LinearLayout ll_popupwind_bootom_btn;
    private GoodDetail.ProductBean product;
    private ImageView iv_product_list_icon;
    private TextView tv_product_list_name;
    private TextView tv_product_list_price2;
    private TextView product_cash_coupon_used;
    //传递过来的数据,用来判断是否是立即购买
    private int mIs_prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_property);
        setBackBtn();
        initViews();
        initListener();
    }

    private void initViews() {
        Intent intent = getIntent();
        isFromProperty = intent.getBooleanExtra("isFromProperty", false);
        skuBean = (ItemSkuBean) intent.getSerializableExtra("itemsku");
        product = (GoodDetail.ProductBean) intent.getSerializableExtra("product");
        mIs_prompt = intent.getIntExtra("mIs_prompt", 0);
        mInflater = LayoutInflater.from(this);
        initView();
        //需要的数据
        data();
    }

    private void initListener() {
        popupwind_ok.setOnClickListener(this);
        popupwind_add_cart.setOnClickListener(this);
        popupwind_just_pay.setOnClickListener(this);
        number_addsub_view.setOnButtonClickListenter(new NumberAddSubView.OnButtonClickListenter() {
            @Override
            public void onButtonAddClick(View view, int value) {
                //先去判断是否有属性和是否选择了所有的属性,然后判断是否有库存和库存的多少
                //购物车需要的东西是sku id,数量,要返回的数据有属性和加入购物车的数据
                if (sku_id != null && number_addsub_view.getValue() < number_addsub_view.getMaxValue()) {
                    //设置过属性了
                    number_addsub_view.btn_add.setEnabled(true);
                }
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                if (sku_id != null && number_addsub_view.getValue() > number_addsub_view.getMinValue()) {
                    //设置过属性了
                    number_addsub_view.btn_sub.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popupwind_ok:
                //加入购物车或者立即支付
                onClickOk();
                break;
            case R.id.popupwind_add_cart:
                //判断是否登录
                if (SPUtils.contains(ProductPropertyActivity.this,"accountName")) {
                    //是否有添加属性
                    if (sku_id != null) {//属性已经选好
                        //加入购物车
                        addCart();
                    }
                } else {
                    ToastUtil.showToast(ProductPropertyActivity.this, getResources().getString(R.string.no_login_hint_text));
                }
                break;
            case R.id.popupwind_just_pay:
                mIs_prompt = 1;
                //判断是否登录
                if (SPUtils.contains(ProductPropertyActivity.this,"accountName")) {
                    //是否有添加属性
                    if (sku_id != null) {//属性已经选好
                        buyNow();
                    }
                } else {
                    ToastUtil.showToast(ProductPropertyActivity.this, getResources().getString(R.string.no_login_hint_text));
                }
                break;
        }
    }

    /**
     * 立即购买
     */
    private void buyNow() {
        if (NetWorkUtil.noHaveNet(ProductPropertyActivity.this)){
            return;
        }
        LoadDialog.show(ProductPropertyActivity.this);
        CartController addCartController = new CartController(ProductPropertyActivity.this);
        addCartController.getCartCodeData(ProductPropertyActivity.this,sku_id, String.valueOf(number_addsub_view.getValue()), mIs_prompt);
    }

    /**
     * 加入购物车
     */
    private void addCart() {
        if (NetWorkUtil.noHaveNet(ProductPropertyActivity.this)){
            return;
        }
        LoadDialog.show(ProductPropertyActivity.this);
        CartController addCartController = new CartController(ProductPropertyActivity.this);
        addCartController.getCartCodeData(ProductPropertyActivity.this,sku_id, String.valueOf(number_addsub_view.getValue()), mIs_prompt);
        finish();
    }

    /**
     * 点击确定按钮s
     */
    private void onClickOk() {
        if (colorAtt.aliasName.size() == 0) {
            colorStr = "";
        } else {
            if (defaultColor == -1) {
                ToastUtil.showToast(ProductPropertyActivity.this, "请选择属性");
                return;
            }
        }
        if (sizeAtt.aliasName.size() == 0) {
            sizeStr = "";
        } else {
            if (defaultSize == -1) {
                ToastUtil.showToast(ProductPropertyActivity.this, "请选择属性");
                return;
            }
        }
        if (mIs_prompt==0){
            //加入购物车
            addCart();
        }else{
            buyNow();
        }
    }

    @Override
    public void onBackPressed() {
        if (isFromProperty) {
            backForFrontActivity();
        } else {
            //如果不是点击属性进入就直接返回
            finish();
        }
    }

    /**
     *  如果是从点击属性进入,就返回数据
     */
    private void backForFrontActivity() {
        //返回前一个页面要的数据
        if (colorAtt.aliasName.size() == 0) {
            colorStr = "";
        } else {
            if (defaultColor == -1) {
                finish();
            }
        }
        if (sizeAtt.aliasName.size() == 0) {
            sizeStr = "";
        } else {
            if (defaultSize == -1) {
                finish();
            }
        }
        //要返回的数据
        backData();
    }

    private void backData() {
        if (!TextUtils.isEmpty(colorStr) && !TextUtils.isEmpty(sizeStr)) {

            sku = "\"" + colorStr + "\"," + "\"" + sizeStr + "\"," + "\"" + number_addsub_view.getValue() + "件\"";
        }
        if (!TextUtils.isEmpty(colorStr) && TextUtils.isEmpty(sizeStr)) {
            sku = "\"" + colorStr + "\"," + "\"" + number_addsub_view.getValue() + "件\"";
        }
        if (TextUtils.isEmpty(colorStr) && !TextUtils.isEmpty(sizeStr)) {
            sku = "\"" + sizeStr + "\"," + "\"" + number_addsub_view.getValue() + "件\"";
        }
        if (TextUtils.isEmpty(colorStr) && TextUtils.isEmpty(sizeStr)) {
            sku = "\"" + number_addsub_view.getValue() + "件\"";
        }
        size = defaultSize;
        color = defaultColor;
        //返回sku
        Intent intent = new Intent();
        intent.putExtra("sku", sku);
        intent.putExtra("sku_id", sku_id);
        intent.putExtra("count", number_addsub_view.getValue());
        setResult(ProductActivity.PROPERTY_REQUEST_CODE, intent);
        finish();
    }

    //对数据进行解析
    public void select(String skuStr) {
        sku = skuStr;
        if (sku != null) {
            String[] split = sku.split(":");
            String splitsColor = split[0];
            String splitsSize = split[1];
            colorAtt.FailureAliasName.clear();
            for (int i = 0; i < mColorDate.size(); i++) {
                if (splitsColor.equals(mColorDate.get(i))) {
                    defaultColor = i;
                    color = defaultColor;
                }
                //拿到颜色属性不可选的属性集合
                String sku = mColorDate.get(i) + ":" + splitsSize;
                for (int j = 0; j < mFailureSkuDate.size(); j++) {
                    if (sku.equals(mFailureSkuDate.get(j))) {
                        colorAtt.FailureAliasName.add(mColorDate.get(i));
                    }
                }
            }
            sizeAtt.FailureAliasName.clear();
            for (int i = 0; i < mSizeDate.size(); i++) {
                if (splitsSize.equals(mSizeDate.get(i))) {
                    defaultSize = i;
                    size = defaultSize;
                }
                //拿到大小属性不可选的属性集合
                String sku = splitsColor + ":" + mSizeDate.get(i);
                for (int j = 0; j < mFailureSkuDate.size(); j++) {
                    if (sku.equals(mFailureSkuDate.get(j))) {
                        sizeAtt.FailureAliasName.add(mSizeDate.get(i));
                    }
                }
            }
        }
    }

    private void initView() {
        iv_product_list_icon = (ImageView) findViewById(R.id.iv_product_list_icon);
        tv_product_list_name = (TextView) findViewById(R.id.tv_product_list_name);
        tv_product_list_price2 = (TextView) findViewById(R.id.tv_product_list_price2);
        product_cash_coupon_used = (TextView) findViewById(R.id.product_cash_coupon_used); //可使用现金券数

        ll_property_color = (LinearLayout) findViewById(R.id.ll_product_property_color);
        ll_property_size = (LinearLayout) findViewById(R.id.ll_product_property_size);
        tf_color = (TagFlowLayout) findViewById(R.id.tf_color);
        tf_size = (TagFlowLayout) findViewById(R.id.tf_size);
        number_addsub_view = (NumberAddSubView) findViewById(R.id.number_addsub_view);
        number_addsub_view.setMinValue(1);
        popupwind_ok = (TextView) findViewById(R.id.popupwind_ok);

        ll_popupwind_bootom_btn = (LinearLayout) findViewById(R.id.ll_popupwind_bootom_btn);
        popupwind_add_cart = (TextView) findViewById(R.id.popupwind_add_cart);
        popupwind_just_pay = (TextView) findViewById(R.id.popupwind_just_pay);
        if (isFromProperty) {
            ll_popupwind_bootom_btn.setVisibility(View.VISIBLE);
            popupwind_ok.setVisibility(View.GONE);
        } else {
            ll_popupwind_bootom_btn.setVisibility(View.GONE);
            popupwind_ok.setVisibility(View.VISIBLE);
        }
    }

    public void data() {
        //显示商品的名称和价格
        showProductInfo();
        CommonUtil.printI("传递过来的skuBean数据", skuBean.toString());
//        List<Data> datas = null;

        product_cash_coupon_used.setText(String.format(getResources().getString(R.string.product_cash_coupon_tv), product.getItem_sku().get(1).getData().getM_sku_cash_coupon_used()));
        //大小属性
        mSizeDate = new ArrayList<>();
        if (skuBean != null && skuBean.item != null) {
            datas = skuBean.item.data;
        }
        if (datas != null) {
            for (int i = 0; i < datas.size(); i++) {
                String size = datas.get(i).value.get(3);

                if (size!=null && size.length() != 0 && !(mSizeDate.contains(size))) {//如果尺寸不存在就添加进去

                    mSizeDate.add(size);
                }
            }
            sizeAtt.aliasName.addAll(mSizeDate);
        }
        //颜色属性
        mColorDate = new ArrayList<>();
        if (datas != null) {

            for (int i = 0; i < datas.size(); i++) {
                String colorName = datas.get(i).value.get(7);
                if (colorName!=null && colorName.length() != 0 && !(mColorDate.contains(colorName))) {//如果颜色不存在就添加进去
                    mColorDate.add(colorName);
                }
            }

            colorAtt.aliasName.addAll(mColorDate);
        }
        //所有的Sku
        mAllSkuDate = new ArrayList<>();
        if (mColorDate.size() == 0) {
            for (int j = 0; j < mSizeDate.size(); j++) {
                String s = mSizeDate.get(j);
                mAllSkuDate.add(" " + ":" + s);
            }
        } else {
            for (int i = 0; i < mColorDate.size(); i++) {
                String Colorstr = mColorDate.get(i);
                if (mSizeDate.size() == 0) {
                    mAllSkuDate.add(Colorstr + ":" + " ");
                } else {
                    for (int j = 0; j < mSizeDate.size(); j++) {
                        String s = mSizeDate.get(j);
                        mAllSkuDate.add(Colorstr + ":" + s);
                    }
                }
            }
        }


        //无库存的组合Sku
        mFailureSkuDate = new ArrayList<>();
        //先找出有库存的sku
//        if (skuBean != null && skuBean.item != null) {
//            datas = skuBean.item.data;
        if (datas!=null) {
            for (int i = 0; i < datas.size(); i++) {
                String skuDate = datas.get(i).value.get(7) + ":" + datas.get(i).value.get(3);
                if (!(mAllSkuDate.contains(skuDate))) {
                    mFailureSkuDate.add(skuDate);
                }
            }
        }
//        }
        //显示数据到控件上
        CommonUtil.printI("colorAtt.aliasName数据:=", colorAtt.aliasName.toString());
        CommonUtil.printI("sizeAtt.aliasName数据:=", sizeAtt.aliasName.toString());
        showData();
    }

    private void showProductInfo() {

        //显示商品数据到控件上
        //图片的URL
        String iconUrl = product.getItem_images().get(0).getData().getUrl();
        //商品的名称
        String name = product.getName();
        //商品价格
        String regular = product.getRegular_price();
        GlideUtil.getInstance().loadImageCustomSize(ProductPropertyActivity.this, iconUrl, R.drawable.electric_theme_loading_bg,
                R.drawable.electric_theme_error_bg, R.dimen.product_list_icon, R.dimen.product_list_icon, iv_product_list_icon);
        tv_product_list_name.setText(name);
        if(product.getSpecial_price().length()==0){
            tv_product_list_price2.setText(regular);
        }else{
            if (product.getSpecial_price().equals(product.getRegular_price())) {
                tv_product_list_price2.setText(product.getSpecial_price());
            } else {
                tv_product_list_price2.setText(product.getSpecial_price()+"-"+product.getRegular_price());
            }
        }

    }

    @Override
    public void showAddCartCode(AddCartCodeBean addCartCodeBean) {
        //'status'=>'success','text'=>'放入购物车成功。' ,’code’ => 0
        //  'status'=>'error','text'=>'放入购物车失败。’,’code’=>-1
        //'status'=>'error','text'=>'该规格的商品已经下架。’,’code’=>-2
        LoadDialog.dismiss(ProductPropertyActivity.this);
        int code = addCartCodeBean.message.code;
        if (code == RequestCodeConstant.CART_REQUEST_SUCCESS_CODE) {
                if (mIs_prompt == 0) {
                    //加入购物车
                    ToastUtil.showToast(ProductPropertyActivity.this, addCartCodeBean.message.text);
                } else {
                    //立即购买,跳转到确定订单页面
                    Intent payIntent = new Intent(ProductPropertyActivity.this, PayActivity.class);
                    payIntent.putExtra("sku_ids", String.valueOf(sku_id));
                    payIntent.putExtra("amount", String.valueOf(number_addsub_view.getValue()));
                    payIntent.putExtra("is_prompt", mIs_prompt);//代表的是不是立即购买
                    startActivity(payIntent);
                }

        } else if (code == RequestCodeConstant.CART_REQUEST_ERROR_CODE) {
            //返回失败了就什么都不做
            return;
        } else if (code == RequestCodeConstant.CART_REQUEST_ERROR_SOLD_OUT_CODE) {
            ToastUtil.showToast(ProductPropertyActivity.this, addCartCodeBean.message.text);
            finish();
        }

    }

    private void showData() {
        select(sku);
        if (colorAtt.aliasName.size() == 0) {
            ll_property_color.setVisibility(View.GONE);
        } else {
            ll_property_color.setVisibility(View.VISIBLE);
            mColorAdapter = new mTagAdapter(colorAtt);
            tf_color.setAdapter(mColorAdapter);
            mColorAdapter.setSelectedList(color);
            colorStr = mColorDate.get(color);
            //颜色属性标签点击事件
            tf_color.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                boolean is;
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {

                    is = false;
                    //从Base类中拿到不可点击的属性名称进行比较
                    List<String> st = colorAtt.getFailureAliasName();
                    for (int i = 0; i < st.size(); i++) {
                        if (st.get(i).equals(mColorDate.get(position))) {
                            is = true;
                        }
                    }
                    //如是不可点击就进接return 这样就形成了不可点击的假像，达到了我们的目的
                    if (is) {
                        return true;
                    }

                    if (position == defaultColor) {
                        defaultColor = -1;
                        colorStr = "";
                        sizeAtt.FailureAliasName.clear();
                        if (mSizeAdapter != null) {
                            TagAdapNotify(mSizeAdapter, defaultSize);
                        } else {
                            setProductCount();
                        }
                        return true;
                    } else {
                        defaultColor = position;
                        colorStr = mColorDate.get(position);
                    }
                    sizeAtt.FailureAliasName.clear();
                    for (int i = 0; i < mSizeDate.size(); i++) {
                        String sku = colorStr + ":" + mSizeDate.get(i);
                        for (int j = 0; j < mFailureSkuDate.size(); j++) {
                            if (sku.equals(mFailureSkuDate.get(j))) {
                                sizeAtt.FailureAliasName.add(mSizeDate.get(i));
                            }
                        }
                    }
                    if (mSizeAdapter != null) {
                        TagAdapNotify(mSizeAdapter, defaultSize);
                    } else {
                        setProductCount();
                    }
                    return true;
                }
            });
        }
        if (sizeAtt.aliasName.size() == 0) {
            ll_property_size.setVisibility(View.GONE);
        } else {
            ll_property_size.setVisibility(View.VISIBLE);

            mSizeAdapter = new mTagAdapter(sizeAtt);
            tf_size.setAdapter(mSizeAdapter);
            mSizeAdapter.setSelectedList(size);
            sizeStr = mSizeDate.get(size);
            //大小属性标签点击事件
            tf_size.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                boolean is;
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {

                    is = false;
                    List<String> st = sizeAtt.getFailureAliasName();
                    for (int i = 0; i < st.size(); i++) {
                        if (st.get(i).equals(mSizeDate.get(position))) {
                            is = true;
                        }
                    }
                    if (is) {
                        return true;
                    }

                    if (position == defaultSize) {
                        defaultSize = -1;
                        sizeStr = "";
                        colorAtt.FailureAliasName.clear();
                        if (mColorAdapter != null) {
                            TagAdapNotify(mColorAdapter, defaultColor);
                        }
                        setProductCount();
                        return true;
                    } else {
                        defaultSize = position;
                        sizeStr = mSizeDate.get(position);
                    }

                    colorAtt.FailureAliasName.clear();
                    for (int i = 0; i < mColorDate.size(); i++) {
                        String sku = mColorDate.get(i) + ":" + sizeStr;
                        for (int j = 0; j < mFailureSkuDate.size(); j++) {
                            if (sku.equals(mFailureSkuDate.get(j))) {
                                colorAtt.FailureAliasName.add(mColorDate.get(i));
                            }
                        }
                    }
                    if (mColorAdapter != null) {
                        TagAdapNotify(mColorAdapter, defaultColor);
                    }
                    setProductCount();
                    return true;
                }
            });
        }
        setProductCount();
    }

    public void TagAdapNotify(mTagAdapter Adapter, int CcInt) {
        Adapter.getPreCheckedList().clear();
        if (CcInt != -1) {
            Adapter.setSelectedList(CcInt);
        }
        Adapter.notifyDataChanged();
//        //设置商品数量
        setProductCount();
    }

    private void setProductCount() {
        //当调换了属性时,sku id要变,商品的数量也要变
//        List<Data> datas = null;
//        if (skuBean != null && skuBean.item != null) {
//            datas = skuBean.item.data;
//        }
        if (colorAtt.aliasName.size() != 0 && sizeAtt.aliasName.size() != 0) {
            //说明属性都有,设置购买的数量和拿到sku id
            if (colorStr.length() != 0 && sizeStr.length() != 0) {
                if (number_addsub_view.getValue() != 1) {
                    number_addsub_view.setValue(1);
                }
                if (datas!=null) {
                    //都已经选择,可以设置数量
                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).value.get(7).equals(colorStr) && datas.get(i).value.get(3).equals(sizeStr)) {
                            number_addsub_view.setMaxValue(Integer.valueOf(datas.get(i).value.get(6)));
                            //sku id
                            sku_id = datas.get(i).value.get(0);
                            tv_product_list_price2.setText(datas.get(i).value.get(4));
                            product_cash_coupon_used.setText(String.format(getResources().getString(R.string.product_cash_coupon_tv), datas.get(i).value.get(5)));

                        }
                    }
                }
            } else {
                //商品属性没有设置完,就复原
                number_addsub_view.setMaxValue(1);
                number_addsub_view.setValue(1);
                sku_id = null;
                product_cash_coupon_used.setText(String.format(getResources().getString(R.string.product_cash_coupon_tv), "0.00"));
                if(product.getSpecial_price().length()==0){
                    tv_product_list_price2.setText(product.getRegular_price());
                }else{
                    if (product.getSpecial_price().equals(product.getRegular_price())) {
                        tv_product_list_price2.setText(product.getSpecial_price());
                    } else {
                        tv_product_list_price2.setText(product.getSpecial_price()+"-"+product.getRegular_price());
                    }
                }
            }
        }
        if (colorAtt.aliasName.size() == 0 && sizeAtt.aliasName.size() != 0) {
            //没有颜色属性,有尺寸属性
            if (sizeStr.length() != 0) {
                if (number_addsub_view.getValue() != 1) {
                    number_addsub_view.setValue(1);
                }
                if (datas!=null) {
                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).value.get(3).equals(sizeStr)) {
                            number_addsub_view.setMaxValue(Integer.valueOf(datas.get(i).value.get(6)));
                            //sku id
                            sku_id = datas.get(i).value.get(0);
                            tv_product_list_price2.setText(datas.get(i).value.get(4));
                            product_cash_coupon_used.setText(String.format(getResources().getString(R.string.product_cash_coupon_tv), datas.get(i).value.get(5)));
                        }
                    }
                }
            } else {
                //没有设置属性
                number_addsub_view.setMaxValue(1);
                number_addsub_view.setValue(1);
                sku_id = null;
                product_cash_coupon_used.setText(String.format(getResources().getString(R.string.product_cash_coupon_tv), "0.00"));
                if(product.getSpecial_price().length()==0){
                    tv_product_list_price2.setText(product.getRegular_price());
                }else{
                    if (product.getSpecial_price().equals(product.getRegular_price())) {
                        tv_product_list_price2.setText(product.getSpecial_price());
                    } else {
                        if (product.getSpecial_price().equals(product.getRegular_price())) {
                            tv_product_list_price2.setText(product.getSpecial_price());
                        } else {
                            tv_product_list_price2.setText(product.getSpecial_price()+"-"+product.getRegular_price());
                        }
                    }
                }
            }
        }
        if (sizeAtt.aliasName.size() == 0 && colorAtt.aliasName.size() != 0 ) {
            //有颜色属性,没有尺寸属性
            if (colorStr.length() != 0) {
                if (number_addsub_view.getValue() != 1) {
                    number_addsub_view.setValue(1);
                }
                if (datas!=null) {
                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).value.get(7).equals(colorStr)) {
                            number_addsub_view.setMaxValue(Integer.valueOf(datas.get(i).value.get(6)));
                            //sku id
                            sku_id = datas.get(i).value.get(0);
                            tv_product_list_price2.setText(datas.get(i).value.get(4));
                            product_cash_coupon_used.setText(String.format(getResources().getString(R.string.product_cash_coupon_tv), datas.get(i).value.get(5)));

                        }
                    }
                }
            } else {
                //没有设置属性
                number_addsub_view.setMaxValue(1);
                number_addsub_view.setValue(1);
                sku_id = null;
                product_cash_coupon_used.setText(String.format(getResources().getString(R.string.product_cash_coupon_tv), "0.00"));
                if(product.getSpecial_price().length()==0){
                    tv_product_list_price2.setText(product.getRegular_price());
                }else{
                    if (product.getSpecial_price().equals(product.getRegular_price())) { //如果 特别价和正规价相同 显示一种价格
                        tv_product_list_price2.setText(product.getSpecial_price());
                    } else {
                        tv_product_list_price2.setText(product.getSpecial_price()+"-"+product.getRegular_price());
                    }
                }
            }
        }
        if (colorAtt.aliasName.size() == 0 && sizeAtt.aliasName.size() == 0) {
            if (datas!=null) {
                for (int i = 0; i < datas.size(); i++) {
                    number_addsub_view.setMaxValue(Integer.valueOf(datas.get(i).value.get(6)));
                    //sku id
                    sku_id = datas.get(i).value.get(0);
                    if(product.getSpecial_price().length()==0){
                        tv_product_list_price2.setText(product.getRegular_price());
                    }else{
                        if (product.getSpecial_price().equals(product.getRegular_price())) { //如果 特别价和正规价相同 显示一种价格
                            tv_product_list_price2.setText(product.getSpecial_price());
                        } else {
                            tv_product_list_price2.setText(product.getSpecial_price()+"-"+product.getRegular_price());
                        }
                    }
                }
            }
        }
    }
    /**
     * 尺寸的流式布局Adatper
     */
    class mTagAdapter extends TagAdapter<String> {

        private TextView tv;

        public mTagAdapter(Attribute ab) {
            super(ab);
        }

        @Override
        public View getView(FlowLayout parent, int position, Attribute t) {
            boolean is = false;
            //两个布局,一个是可点击的布局，一个是不可点击的布局
            List<String> st = t.FailureAliasName;
            if (st != null) {
                for (int i = 0; i < st.size(); i++) {
                    if (st.get(i).equals(t.aliasName.get(position))) {
                        is = true;
                    }
                }
            }
            if (!is) {
                tv = (TextView) mInflater.inflate(R.layout.popupwindow_tv, parent, false);
                tv.setText(t.aliasName.get(position));
            } else {
                tv = (TextView) mInflater.inflate(R.layout.popupwindow_tv1, parent, false);
                tv.setText(t.aliasName.get(position));
            }
            return tv;
        }
    }

    @Override
    protected void onDestroy() {
           LoadDialog.dismiss(ProductPropertyActivity.this);
        super.onDestroy();
    }
}
